<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<div>
	${sessionScope._USER_.name } ( ${sessionScope._USER_.email } )님
	
	Point : ${sessionScope._USER_.point }
	<s:authorize access="hasRole('ROLE_ADMIN')">
		<span> - 관리자 계정 </span>
	</s:authorize>
</div>
<div>
	<a href="/HelloSpring/member/logout">Logout</a>
</div>