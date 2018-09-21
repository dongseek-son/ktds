package com.ktds.member.biz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;

@Component
public class MemberBizImpl implements MemberBiz {

	@Autowired
	@Qualifier("memberDaoImplMyBatis")
	private MemberDao memberDao;
	
	@Override
	public int updatePoint(MemberVO memberVO, int point) {
		memberVO.setPoint(memberVO.getPoint()+point);
		
		Map<String, Object> param = new HashMap<>();
		param.put("email", memberVO.getEmail());
		param.put("point", point);
		return memberDao.updatePoint(param);
	}

	@Override
	public int updateLoginFailCount(MemberVO memberVO, int loginFailCount) {
		memberVO.setLoginFailCount(loginFailCount);
		
		Map<String, Object> param = new HashMap<>();
		param.put("email", memberVO.getEmail());
		param.put("loginFailCount", loginFailCount);
		return memberDao.updateLoginFailCount(param);
	}

}
