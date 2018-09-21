package com.ktds.reply.dao;

import java.util.List;

import com.ktds.reply.vo.ReplyVO;

public interface ReplyDao {

	public int insertOneReply( ReplyVO replyVO );
	
	public List<ReplyVO> selectAllReplies( int boardId );
	
}
