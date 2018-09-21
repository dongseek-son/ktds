package com.ktds.member.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.member.vo.MemberVO;

@Repository
public class MemberDaoImplMyBatis extends SqlSessionDaoSupport implements MemberDao {

	private Logger logger = LoggerFactory.getLogger(MemberDaoImplMyBatis.class);
	
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		logger.debug("Initiate MyBatis");
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int insertMember(MemberVO memberVO) {
		return getSqlSession().insert("MemberDao.insertMember", memberVO);
	}

	@Override
	public MemberVO selectOneMember(MemberVO memberVO) {
		return getSqlSession().selectOne("MemberDao.selectOneMember", memberVO);
	}

	@Override
	public int updatePoint( Map<String, Object> param ) {
		return getSqlSession().update("MemberDao.updatePoint", param);
	}

	@Override
	public int updateLoginFailCount(Map<String, Object> param) {
		return getSqlSession().update("MemberDao.updateLoginFailCount", param);
	}

	@Override
	public MemberVO selectOneMemberByEmail(String email) {
		return getSqlSession().selectOne("MemberDao.selectOneMemberByEmail", email);
	}

	@Override
	public boolean isBlockUser(String email) {
		return getSqlSession().selectOne("MemberDao.isBlockUser", email);
	}

	@Override
	public String selectSaltByEmail(String email) {
		return getSqlSession().selectOne("selectSaltByEmail", email);
	}

	@Override
	public int selectLoginFailCountByEmail(String email) {
		return getSqlSession().selectOne("selectLoginFailCountByEmail", email);
	}
	
}
