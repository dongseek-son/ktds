package com.ktds.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.board.dao.BoardDao;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.member.biz.MemberBiz;
import com.ktds.member.vo.MemberVO;
import com.ktds.reply.dao.ReplyDao;
import com.ktds.reply.vo.ReplyVO;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.PagerFactory;
import io.github.seccoding.web.pager.explorer.ClassicPageExplorer;
import io.github.seccoding.web.pager.explorer.PageExplorer;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	@Qualifier("boardDaoImplMyBatis")
	private BoardDao boardDao;
	
	@Autowired
	private MemberBiz memberBiz;
	
	@Autowired
	private ReplyDao replyDao;
	
//	public void setBoardDao(BoardDao boardDao) {
//		System.out.println("Spring에서 호출함.");
//		System.out.println(this.boardDao);
//		this.boardDao = boardDao;
//	}
	
	@Override
	public boolean createBoard( BoardVO boardVO, MemberVO memberVO ) {
		
		if ( boardVO.getOriginFileName().length() > 0 ) {
			this.memberBiz.updatePoint(memberVO, +20);
		} 
		else {
			this.memberBiz.updatePoint(memberVO, +10);
		}
		
		return this.boardDao.insertBoard( boardVO ) > 0;
	}

	@Override
	public boolean updateBoard( BoardVO boardVO ) {
		return this.boardDao.updateBoard( boardVO ) > 0;
	}

	@Override
	public BoardVO readOneBoard(int id, MemberVO memberVO) {
		BoardVO boardVO = this.readOneBoard(id);
		boardVO.setReplyList(this.replyDao.selectAllReplies(id));
		
		if ( !boardVO.getEmail().equals( memberVO.getEmail() ) ) {
			if ( memberVO.getPoint() < 2 ) {
				throw new PolicyViolationException("포인트가 부족합니다.", "/board/list");
			}
			this.memberBiz.updatePoint(memberVO, -2);
		}
		return boardVO;
	}
	
	@Override
	public BoardVO readOneBoard(int id) {
		return this.boardDao.selectOneBoard(id);
	}

	@Override
	public boolean deleteOneBoard(int id) {
		return this.boardDao.deleteOneBoard(id) > 0;
	}

	@Override
	public PageExplorer readAllBoards( BoardSearchVO boardSearchVO ) {
		
		int totalCount = this.boardDao.selectAllBoardsCount(boardSearchVO);
		
		Pager pager = PagerFactory.getPager( Pager.ORACLE, boardSearchVO.getPageNo()+"" );
	
		pager.setTotalArticleCount(totalCount);
		
		PageExplorer pageExplorer = pager.makePageExplorer(ClassicPageExplorer.class, boardSearchVO);
		
		pageExplorer.setList(this.boardDao.selectAllBoards(boardSearchVO));
		
		return pageExplorer;
	}

}
