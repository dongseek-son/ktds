package com.ktds.board.dao;

import java.util.List;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

public interface BoardDao {

	public int insertBoard( BoardVO boardVO );
	public int updateBoard( BoardVO boardVO );
	public BoardVO selectOneBoard( int id );
	public int deleteOneBoard( int id );
	
	public int selectAllBoardsCount( BoardSearchVO boardSearchVO );
	public List<BoardVO> selectAllBoards( BoardSearchVO boardSearchVO );

}
