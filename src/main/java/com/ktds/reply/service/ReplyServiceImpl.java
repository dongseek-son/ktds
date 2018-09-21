package com.ktds.reply.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktds.reply.dao.ReplyDao;
import com.ktds.reply.vo.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao replyDao;

	@Override
	public boolean createOneReply(ReplyVO replyVO) {
		return this.replyDao.insertOneReply(replyVO) > 0;
	}

}
