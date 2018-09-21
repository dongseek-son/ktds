<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="/WEB-INF/view/common/header.jsp" />
	<form:form  modelAttribute="boardVO" method="post" action="/HelloSpring/board/write" enctype="multipart/form-data">
		<input type="hidden" name="token" value="${sessionScope._TOKEN_ }">
		<div class="errors">
			<ul>
				<li><form:errors path="subject" /></li>
				<li><form:errors path="content" /></li>			
			</ul>
		</div>
		<div>
			<input type="text" name="subject" placeholder="제목을 입력하세요." value="${boardVO.subject }" />
		</div>
		<div>
			<textarea name="content" placeholder="내용을 입력하세요." >${boardVO.content }</textarea>
		</div>
		<div>
			<input type="file" name="file" placeholder="파일을 선택하세요." />
		</div>
<%-- 		<div>
			<input type="email" name="email" placeholder="이메일을 입력하세요" value="${sessionScope._USER_.email }"/>
		</div> --%>
		<div>
			<input type="submit" value="등록" />
			<a href="/HelloSpring/board/list">목록</a>
		</div>
	</form:form>
</body>
</html>