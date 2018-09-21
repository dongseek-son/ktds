package com.ktds.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.common.web.SHA256Util;
import com.ktds.member.biz.MemberBiz;
import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	@Qualifier("memberDaoImplMyBatis")
	private MemberDao memberDao;
	
	@Autowired
	private MemberBiz memberBiz;
	
	@Override
	public boolean createMember( MemberVO memberVO ) {
		String salt = SHA256Util.generateSalt();
		memberVO.setPassword( SHA256Util.getEncrypt(memberVO.getPassword(), salt) );
		memberVO.setSalt(salt);
		return this.memberDao.insertMember( memberVO ) >0;
	}

	@Override
	public MemberVO loginMember(MemberVO memberVO) {
		String salt = this.memberDao.selectSaltByEmail(memberVO.getEmail());
		if ( salt != null ) {
			memberVO.setPassword( SHA256Util.getEncrypt(memberVO.getPassword(), salt) );
		}

		MemberVO loginMemberVO = this.memberDao.selectOneMember(memberVO);
			
		if ( loginMemberVO != null ) {
			this.memberBiz.updatePoint(memberVO, +2);
			this.memberBiz.updateLoginFailCount(memberVO, 0);
			return loginMemberVO;
		}
		
		return null;
	}

	@Override
	public boolean isBlockUser(String email) {
		return this.memberDao.isBlockUser(email);
	}

	@Override
	public MemberVO authUserInfo(MemberVO memberVO) {
		String salt = this.memberDao.selectSaltByEmail(memberVO.getEmail());
		if ( salt != null ) {
			memberVO.setPassword( SHA256Util.getEncrypt(memberVO.getPassword(), salt) );
		}
		memberVO = this.memberDao.selectOneMember(memberVO);
		return memberVO;
	}

	@Override
	public int readLoginFailCountByEmail(String email) {
		return this.memberDao.selectLoginFailCountByEmail(email);
	}
	
}
