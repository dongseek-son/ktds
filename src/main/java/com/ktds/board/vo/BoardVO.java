package com.ktds.board.vo;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.ktds.common.dao.support.Types;
import com.ktds.member.vo.MemberVO;
import com.ktds.reply.vo.ReplyVO;

public class BoardVO {

	@Types
	private int id;
	@Types
	@NotEmpty(message="제목은 필수 입력 값입니다.")
	private String subject;
	@Types
	@NotEmpty(message="내용은 필수 입력 값입니다.")
	private String content;
	@Types(alias="B_EMAIL")
	private String email;
	@Types
	private String crtDt;
	@Types
	private String mdfyDt;
	@Types
	private String fileName;
	@Types
	private String originFileName;
	
	private String token;

	private MultipartFile file;
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	private MemberVO memberVO;
	
	private List<ReplyVO> replyList;
	
	public List<ReplyVO> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyVO> replyList) {
		this.replyList = replyList;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCrtDt() {
		return crtDt;
	}

	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}

	public String getMdfyDt() {
		return mdfyDt;
	}

	public void setMdfyDt(String mdfyDt) {
		this.mdfyDt = mdfyDt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginFileName() {
		return originFileName;
	}

	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	
	@Override
	public String toString() {
		String format = "BoardVO [Id : %d, Subject: %s, Content: %s, Email: %s, "
				+ "CrtDt: %s, MdfyDt: %s, FileName: %s, OriginFileName: %s, MemberVO: %s]";
		return String.format(format
				, this.id
				, this.subject
				, this.content
				, this.email
				, this.crtDt
				, this.mdfyDt
				, this.fileName
				, this.originFileName
				, this.memberVO.toString());
	}
	
	public BoardVO() {
		this.fileName="";
		this.originFileName="";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
