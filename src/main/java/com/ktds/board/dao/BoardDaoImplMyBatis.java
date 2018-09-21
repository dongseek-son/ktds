package com.ktds.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;

@Repository
public class BoardDaoImplMyBatis extends SqlSessionDaoSupport implements BoardDao {

	@Autowired
	@Override
	public void setSqlSessionTemplate( SqlSessionTemplate sqlSessionTemplate ) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int insertBoard(BoardVO boardVO) {
		return this.getSqlSession().insert("BoardDao.insertBoard", boardVO);
	}

	@Override
	public int updateBoard(BoardVO boardVO) {
		return this.getSqlSession().update("BoardDao.updateBoard", boardVO);
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return this.getSqlSession().selectOne("BoardDao.selectOneBoard", id);
	}

	@Override
	public int deleteOneBoard(int id) {
		return this.getSqlSession().delete("BoardDao.deleteOneBoard", id);
	}

	@Override
	public List<BoardVO> selectAllBoards( BoardSearchVO boardSearchVO ) {
		return this.getSqlSession().selectList("BoardDao.selectAllBoards", boardSearchVO);
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {
		return this.getSqlSession().selectOne("BoardDao.selectAllBoardsCount", boardSearchVO);
	}

}
