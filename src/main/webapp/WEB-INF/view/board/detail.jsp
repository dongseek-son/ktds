<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="/WEB-INF/view/common/header.jsp" />
	<h1>${boardVO.subject } <span style="font-size: 12pt;">${boardVO.crtDt }</span></h1>
	<h3>${boardVO.memberVO.name }</h3>
	<c:if test="${not empty boardVO.originFileName }">
		<p><a href="/HelloSpring/board/download/${boardVO.id }">${boardVO.originFileName }</a></p>
	</c:if>
	<div>
		${boardVO.content }
	</div>
	<div>
		<a href="/HelloSpring/board/delete/${boardVO.id }">삭제</a>
		<a href="/HelloSpring/board/list">목록</a>
	</div>
	<hr />
	<div>
		<c:forEach items="${boardVO.replyList }" var="reply">
			<div style="margin-left: ${(reply.level-1) *30}px">
				<div>${reply.memberVO.name} (${reply.memberVO.email })</div>
				<div>${reply.crtDt }</div>
				<div>${reply.reply }</div>
			</div>
		</c:forEach>
	</div>
	<form action="/HelloSpring/reply/write" method="post" >
		<input type="hidden" name="token" value="${sessionScope._TOKEN_ }" />
		<input type="hidden" name="boardId" value="${boardVO.id}" />
		<input type="hidden" name="parentReplyId" value="0" />
		<textarea name="reply"></textarea>
		<input type="submit" value="등록" />
	</form>
</body>
</html>