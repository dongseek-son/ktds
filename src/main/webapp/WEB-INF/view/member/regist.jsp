<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/HelloSpring/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {
		$("#email").keyup(function () {
			// Ajax요청
			// $.post("URL", 요청 파라미터, function(response) {})
			$.post("/HelloSpring/member/check/duplicate"
					, {
						"email": $(this).val()
					}
					, function(response) {
						if ( response.duplicated ) {
							$("#email-error").slideDown(100);	
						}
						else {
							$("#email-error").slideUp(100);
						}
					})
		})
		
		$("#password").keyup(function () {
			$.post("/HelloSpring/member/check/password"
					, {
						"password": $(this).val()
					}
					, function(response) {
						if ( response.available ) {
							$("#password-check").text("사용가능한 패스워드 입니다.");
						}
						else {
							$("#password-check").text("패스워드는 영문,숫자,특수문자 조합으로 8자이상으로 만들어주세요.");
						}
					})
		})
	})
</script>
</head>
<body>
	<h1>회원가입</h1>
	<hr>
	<form:form modelAttribute="memberVO" method="post" action="/HelloSpring/member/regist" autocomplete="off">
		<div class="errors">
			<ul>
				<li><form:errors path="email" /></li>
				<li><form:errors path="name" /></li>
				<li><form:errors path="password" /></li>				
			</ul>
		</div>
		<div>
			<input type="email" name="email" id="email" placeholder="이메일 주소를 입력하세요." value="${memberVO.email }" />
			<div id="email-error" style="display:none">
				이 이메일은 사용할 수 없습니다.
			</div>
		</div>
		<div>
			<input type="text" name="name" placeholder="이름을 입력하세요." value="${memberVO.name }" />
		</div>
		<div>
			<input type="password" name="password" id="password" placeholder="비밀번호를 입력하세요" />
		</div>
		<div>
			<span id="password-check">
				패스워드는 영문,숫자,특수문자 조합으로 8자이상으로 만들어주세요.
			</span>
		</div>
		<div>
			<input type="submit" value="등록" />
		</div>
	</form:form>
</body>
</html>