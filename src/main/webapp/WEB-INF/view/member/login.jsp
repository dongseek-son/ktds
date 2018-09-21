<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<script type="text/javascript">
		var message = "${param.message}";
		if ( message != "" ) {
			alert(message);
		}
		var message = "${message}";
		if ( message != "" ) {
			alert(message);
		}
	</script>
</head>
<body>
	<h1>로그인</h1>
	<hr>
	<form:form modelAttribute="memberVO" method="post" action="/HelloSpring/memberlogin">
		<div class="errors">
			<ul>
				<li><form:errors path="email" /></li>
				<li><form:errors path="password" /></li>			
			</ul>
		</div>
		<div>
			<input type="email" name="email" placeholder="이메일 주소를 입력하세요." />
		</div>
		<div>
			<input type="password" name="password" placeholder="비밀번호를 입력하세요" />
		</div>
		<div>
			<input type="submit" value="로그인" />
		</div>
	</form:form>
</body>
</html>