package com.ktds.member.dao;

import java.util.Map;

import com.ktds.member.vo.MemberVO;

public interface MemberDao {

	public int insertMember( MemberVO memberVO );
	
	public MemberVO selectOneMember( MemberVO memberVO );
	
	public int updatePoint( Map<String, Object> param );
	
	public int updateLoginFailCount( Map<String, Object> param );
	
	public MemberVO selectOneMemberByEmail( String email );
	
	public boolean isBlockUser( String email );
	
	public String selectSaltByEmail( String email );
	
	public int selectLoginFailCountByEmail( String email );

}
