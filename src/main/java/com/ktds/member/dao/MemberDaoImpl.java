package com.ktds.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MemberVO;

@Repository
public class MemberDaoImpl implements MemberDao {

	private interface Query {
		int INSERT = 0;
		int LOGIN = 1;
		int UPDATE_POINT = 2;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="memberQueries")
	private List<String> memberQueries;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		return this.jdbcTemplate.update(
				this.memberQueries.get(Query.INSERT)
				, memberVO.getEmail()
				, memberVO.getName()
				, memberVO.getPassword() );
	}

	@Override
	public MemberVO selectOneMember(MemberVO memberVO) {
		return jdbcTemplate.queryForObject(
				this.memberQueries.get(Query.LOGIN)
				, new Object[] {memberVO.getEmail(),  memberVO.getPassword()}
				, new RowMapper<MemberVO>() {

					@Override
					public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						return BindData.bindData(rs, new MemberVO());
					}
				});
	}

	@Override
	public int updatePoint(Map<String, Object> param) {
		String email = (String)param.get("email");
		int point = (int)param.get("point");
		return this.jdbcTemplate.update(this.memberQueries.get(Query.UPDATE_POINT), point, email);
	}

	@Override
	public int updateLoginFailCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MemberVO selectOneMemberByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlockUser(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String selectSaltByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectLoginFailCountByEmail(String email) {
		// TODO Auto-generated method stub
		return 0;
	}

}
