package com.ktds.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.common.session.Session;
import com.ktds.member.vo.MemberVO;

public class PreventSessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute(Session.USER);
		
		if( memberVO != null ) {
			response.sendRedirect("/HelloSpring/board/list");
			return false;
		}
		return true;
	}
	
}
