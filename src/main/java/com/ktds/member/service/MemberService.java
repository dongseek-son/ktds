package com.ktds.member.service;

import com.ktds.member.vo.MemberVO;

public interface MemberService {

	public boolean createMember( MemberVO memberVO );
	
	public MemberVO authUserInfo( MemberVO memberVO );
	
	public MemberVO loginMember( MemberVO memberVO );
	
	public boolean isBlockUser( String email );
	
	public int readLoginFailCountByEmail( String email );
}
