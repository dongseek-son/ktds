package com.ktds.common;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ktds.common.session.Session;
import com.ktds.member.vo.MemberVO;

public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(
			HttpServletRequest request
			, HttpServletResponse response
			, Object handler) // handler ==> Controller
			throws Exception {

		HttpSession session = request.getSession();
		MemberVO member = (MemberVO)session.getAttribute(Session.USER);
		
		if ( member == null ) {
			response.sendRedirect("/HelloSpring/member/login?message=" + URLEncoder.encode("로그인이 필요합니다.", "UTF-8"));
			return false;
		}
		
		return true;
	}
	
}
