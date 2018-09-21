package com.ktds.member.biz;

import com.ktds.member.vo.MemberVO;

public interface MemberBiz {

	public int updatePoint(MemberVO memberVO, int point); 
	
	public int updateLoginFailCount(MemberVO memberVO, int loginFailCount);
	
}
