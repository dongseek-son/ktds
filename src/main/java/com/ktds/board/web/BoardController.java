package com.ktds.board.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.board.service.BoardService;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.common.session.Session;
import com.ktds.common.web.DownloadUtil;
import com.ktds.member.vo.MemberVO;
import com.nhncorp.lucy.security.xss.XssFilter;

import io.github.seccoding.web.mimetype.ExtFilter;
import io.github.seccoding.web.mimetype.ExtensionFilter;
import io.github.seccoding.web.mimetype.ExtensionFilterFactory;
import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class BoardController {
	
	private Logger statiticsLogger = LoggerFactory.getLogger("list.statistics");
	private Logger paramLogger = LoggerFactory.getLogger(BoardController.class);

	@Value("${upload.path}") // properties 파일의 내용을 읽어옴
	private String uploadPath;
	
	@Autowired
	@Qualifier("boardServiceImpl")
	private BoardService boardService;
	
	@RequestMapping("/board/list/init")
	public String viewBoardListPageForInitiate( HttpSession session ) {
		session.removeAttribute(Session.SEARCH);
		return "redirect:/board/list";
	}
	
	@RequestMapping("board/list")
	public ModelAndView viewBoardListPage( 
			@ModelAttribute BoardSearchVO boardSearchVO 
			, HttpServletRequest request
			, HttpSession session) {
		
		if ( boardSearchVO.getSearchKeyword() == null ) {
			boardSearchVO = (BoardSearchVO)session.getAttribute(Session.SEARCH);
			if ( boardSearchVO == null ) {
				boardSearchVO = new BoardSearchVO();
				boardSearchVO.setPageNo(0);
			}
		}
		
		PageExplorer pageExplorer = this.boardService.readAllBoards(boardSearchVO);
		statiticsLogger.info("URL : /board/list, IP : " + request.getRemoteAddr() 
			+ ", List Size : " + pageExplorer.getList().size());
		
		session.setAttribute(Session.SEARCH, boardSearchVO);
		
		ModelAndView view = new ModelAndView("board/list");
		view.addObject("boardVOList", pageExplorer.getList());
		view.addObject("pagenation", pageExplorer.make());
		view.addObject("boardSearchVO", boardSearchVO);
		return view;
	}
	
	// Spring 4.2 이하에서 사용
	// @RequestMapping(value="/write", method=RequestMethod.GET)
	// Spring 4.3 이상에서 사용
	@GetMapping("board/write")
	public String viewBoardWritePage() {
		return "board/write";
	}
	
	@PostMapping("board/write")
	public ModelAndView doBoardWriteAction( 
			@Valid @ModelAttribute BoardVO boardVO
			, Errors errors
			, @SessionAttribute(name=Session.USER) MemberVO memberVO
			, HttpServletRequest request) {

		ModelAndView view = new ModelAndView("redirect:/board/list/init");
		
		if ( errors.hasErrors() ) {
			view.setViewName("board/write");
			view.addObject("boardVO", boardVO);
			return view;
		}
		
		String sessionToken = (String)request.getSession().getAttribute(Session.TOKEN);
		if ( !boardVO.getToken().equals(sessionToken) ) {
			throw new RuntimeException("잘못된 접근 입니다.");
		}
		
		MultipartFile uploadFile = boardVO.getFile();
		
		if ( !uploadFile.isEmpty() ) {
			String originFileName = uploadFile.getOriginalFilename();
			String fileName = UUID.randomUUID().toString();
			
			File uploadDir = new File(this.uploadPath);
			if ( !uploadDir.exists() ) {
				uploadDir.mkdirs();
			}
			
			File destFile = new File(this.uploadPath, fileName);
			
			try {
				uploadFile.transferTo(destFile);
				boardVO.setOriginFileName(originFileName);
				boardVO.setFileName(fileName);
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if ( destFile.exists() ) {
					ExtensionFilter filter = ExtensionFilterFactory.getFilter(ExtFilter.APACHE_TIKA);
					boolean isImageFile = filter.doFilter(
							destFile.getAbsolutePath()
							,"image/jpg"
							,"image/bmp"
							,"image/jpeg"
							,"image/gif"
							,"image/png" );
					
					if ( !isImageFile ) {
						destFile.delete();
						boardVO.setOriginFileName("");
						boardVO.setFileName("");
					}
				}
			}

		}
		
		boardVO.setMemberVO(memberVO);
		boardVO.setEmail(memberVO.getEmail());
		
//		(condition) ? true : false; <- 삼항연산자(Elvis Operator), 변환 과정이 있어서 if-else에 비해 느림
//		String view = this.boardService.createBoard(boardVO, memberVO) ? "redirect:/board/list" : "redirect:/board/write";
		
		XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml");
		boardVO.setSubject( filter.doFilter(boardVO.getSubject()) );
		boardVO.setContent( filter.doFilter(boardVO.getContent()) );
		
		boolean isSuccess = this.boardService.createBoard(boardVO, memberVO);
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug( String.format(paramFormat
				, request.getRemoteAddr()
				, boardVO.getSubject() + " , "
				+ boardVO.getContent() + " , "
				+ boardVO.getEmail() + " , " 
				+ boardVO.getFileName() + " , "
				+ boardVO.getOriginFileName()
				, view.getViewName() ));
		
		return view;
	}
	
	@RequestMapping("/board/detail/{id}")
	public ModelAndView viewBoardDetailPage( 
			@PathVariable int id 
			, @SessionAttribute(name=Session.USER) MemberVO memberVO 
			, HttpServletRequest request
			) {
		
		BoardVO boardVO = null;
		
		boardVO = this.boardService.readOneBoard(id, memberVO);
/*		if ( boardVO == null ) {
			return new ModelAndView("redirect:/board/list");
		}*/

		ModelAndView view = new ModelAndView("board/detail");
		view.addObject("boardVO", boardVO);
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug( String.format(paramFormat
				, request.getRemoteAddr()
				, id
				, boardVO.getId() + " , "
				+ boardVO.getSubject() + " , "
				+ boardVO.getContent() + " , "
				+ boardVO.getEmail() + " , " 
				+ boardVO.getFileName() + " , "
				+ boardVO.getOriginFileName()
				));
		
		return view;
	}
	
	@RequestMapping("/board/delete/{id}")
	public String doBoardDeleteAction( @PathVariable int id
			, HttpServletRequest request
			, @SessionAttribute(Session.USER) MemberVO memberVO) {
		boolean isSuccess = this.boardService.deleteOneBoard(id);
		
		String paramFormat = "IP:%s, Param:%s, Result:%s";
		paramLogger.debug( String.format(paramFormat
				, request.getRemoteAddr()
				, memberVO.getEmail() + " , "
				+ id + " , " 
				+ isSuccess
				, "redirect:/board/list"
				));
		
		return "redirect:/board/list";
	}
	
	@RequestMapping("/board/download/{id}")
	public void fileDownload(
			@PathVariable int id
			, HttpServletRequest request
			, HttpServletResponse response
			, @SessionAttribute(Session.USER) MemberVO memberVO
			) {
		
		if ( memberVO.getPoint() < 5 ) {
			throw new PolicyViolationException("포인트가 부족합니다.", "/board/detail/" + id );
		}
		
		BoardVO boardVO = this.boardService.readOneBoard(id);
		String originFileName = boardVO.getOriginFileName();
		String fileName = boardVO.getFileName();
		
		// File.separator : windows - '\' , Unix/Linux/macos - '/'
		try {
			new DownloadUtil(this.uploadPath + File.separator + fileName).download(request, response, originFileName);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
