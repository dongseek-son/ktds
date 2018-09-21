package com.ktds.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ktds.member.biz.MemberBiz;
import com.ktds.member.service.MemberService;
import com.ktds.member.vo.MemberVO;

public class UserService implements AuthenticationProvider{
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberBiz memberBiz;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		MemberVO memberVO = new MemberVO();
		String email = authentication.getPrincipal().toString();
		memberVO.setEmail(email);
		memberVO.setPassword(authentication.getCredentials().toString());
		
		this.memberBiz.updateLoginFailCount(memberVO, this.memberService.readLoginFailCountByEmail(email)+1);
		memberVO = this.memberService.authUserInfo(memberVO);

		UsernamePasswordAuthenticationToken result = null;
		
		// Token 값 생성 및 등록 코드 작성
		if ( memberVO != null ) {
			String grade = "ROLE_USER";
			if ( memberVO.isAdmin() ) {
				grade = "ROLE_ADMIN";
			}
			
			List<GrantedAuthority> roles = new ArrayList<>();
			roles.add(new SimpleGrantedAuthority(grade));
			if ( grade.equals("ROLE_ADMIN") ) {
				roles.add(new SimpleGrantedAuthority("ROLE_USER") );
			}
			
			result = new UsernamePasswordAuthenticationToken(email, memberVO.getPassword(), roles);
			
			User user = new User(email, memberVO.getPassword(), grade, this.memberService.isBlockUser(email));
			result.setDetails(user);
		}
		else {
			throw new BadCredentialsException("잘못된 인증");
		}
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
