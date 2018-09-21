package com.ktds.member.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import com.ktds.common.dao.support.Types;
import com.ktds.member.validator.MemberValidator.Login;
import com.ktds.member.validator.MemberValidator.Regist;

public class MemberVO {

	@Types(alias="M_EMAIL")
	@NotEmpty( groups={Login.class, Regist.class}, message="이메일은 필수 입력 값입니다.")
	@Email( groups={Login.class, Regist.class}, message="이메일형식으로 작성해주세요.")
	private String email;
	@Types
	@NotEmpty( groups={Regist.class}, message="이름은 필수 입력 값입니다.")
	private String name;
	@Types
	@NotEmpty( groups={Login.class, Regist.class}, message="비밀번호는 필수 입력 값입니다.")
	@Pattern( 
			groups={Regist.class}
			, regexp="((?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,})"
			, message="패스워드는 영문,숫자,특수문자 조합으로 8자이상으로 만들어주세요.")
	private String password;
	@Types
	private int point;
	private String latestLogin;
	private int loginFailCount;
	private String salt;
	private boolean isAdmin;

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		String format = "MemberVO [Email: %s, Name: %s, Password: %s, Point: %d, LatestLogin: %s, LoginFailCount: %d]";
		return String.format(format, this.email, this.name, this.password, this.point, this.latestLogin, this.loginFailCount);
	}

	public String getLatestLogin() {
		return latestLogin;
	}

	public void setLatestLogin(String latestLogin) {
		this.latestLogin = latestLogin;
	}

	public int getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
