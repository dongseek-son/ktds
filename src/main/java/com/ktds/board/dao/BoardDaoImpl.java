package com.ktds.board.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MemberVO;

@Repository
public class BoardDaoImpl implements BoardDao{

	// Inner Class (Interface)
	private interface Query {
		int INSERT = 0;
		int SELECT_ONE = 1;
		int DELETE_ONE = 2;
		int SELECT_ALL = 3;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="boardQueries")
	private List<String> boardQueries;
	
	@Override
	public int insertBoard( BoardVO boardVO ) {
		return this.jdbcTemplate.update(
				this.boardQueries.get(Query.INSERT)
				, boardVO.getSubject()
				, boardVO.getContent()
				, boardVO.getEmail()
				, boardVO.getFileName()
				, boardVO.getOriginFileName());
	}

	@Override
	public int updateBoard( BoardVO boardVO ) {
		System.out.println("Call updateBoard();");
		return 0;
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return this.jdbcTemplate.queryForObject(this.boardQueries.get(Query.SELECT_ONE)
						, new Object[] {id}
						, new RowMapper<BoardVO>() {

							@Override
							public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
								MemberVO memberVO = BindData.bindData(rs, new MemberVO());
								BoardVO boardVO = BindData.bindData(rs, new BoardVO());
								boardVO.setMemberVO(memberVO);
								return boardVO;
							}
							
						});
	}

	@Override
	public int deleteOneBoard( int id ) {
		return this.jdbcTemplate.update(this.boardQueries.get(Query.DELETE_ONE), id);
	}

	@Override
	public List<BoardVO> selectAllBoards( BoardSearchVO boardSearchVO ) {
		return this.jdbcTemplate.query(this.boardQueries.get(Query.SELECT_ALL), new RowMapper<BoardVO>() {
			@Override
			public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				MemberVO memberVO = BindData.bindData(rs, new MemberVO());
				BoardVO boardVO = BindData.bindData(rs, new BoardVO());
				boardVO.setMemberVO(memberVO);
				return boardVO;
			}
		});
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}
