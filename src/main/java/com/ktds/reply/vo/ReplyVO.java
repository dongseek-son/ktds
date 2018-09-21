package com.ktds.reply.vo;

import com.ktds.board.vo.BoardVO;
import com.ktds.member.vo.MemberVO;

public class ReplyVO {

	private int level;
	private String replyId;
	private int boardId;
	private String email;
	private String reply;
	private String crtDt;
	private String parentReplyId;

	private BoardVO boardVO;
	private MemberVO memberVO;
	
	private String token;
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BoardVO getBoardVO() {
		return boardVO;
	}

	public void setBoardVO(BoardVO boardVO) {
		this.boardVO = boardVO;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getCrtDt() {
		return crtDt;
	}

	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}

	public String getParentReplyId() {
		return parentReplyId;
	}

	public void setParentReplyId(String parentReplyId) {
		this.parentReplyId = parentReplyId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
