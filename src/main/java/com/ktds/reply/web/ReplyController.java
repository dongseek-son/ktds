package com.ktds.reply.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktds.common.session.Session;
import com.ktds.member.vo.MemberVO;
import com.ktds.reply.service.ReplyService;
import com.ktds.reply.vo.ReplyVO;
import com.nhncorp.lucy.security.xss.XssFilter;

@Controller
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@PostMapping("/reply/write")
	public String doReplyWriteAction( 
			@ModelAttribute ReplyVO replyVO
			, @SessionAttribute(Session.USER) MemberVO memberVO
			, HttpSession session) {
		
		String sessionToken = (String)session.getAttribute(Session.TOKEN);
		if ( !replyVO.getToken().equals(sessionToken) ) {
			throw new RuntimeException("잘못된 접근 입니다.");
		}
		
		replyVO.setEmail(memberVO.getEmail());
		XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml");
		replyVO.setReply( filter.doFilter(replyVO.getReply()) );
		boolean isSuccess = this.replyService.createOneReply(replyVO);
		
		return "redirect:/board/detail/" + replyVO.getBoardId();
	}
	
}
