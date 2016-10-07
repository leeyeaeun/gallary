<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<h1>Board Detail</h1>
	<a href = '/boardUpdate?boardArticleNo=${map["boardArticle"].boardArticleNo}' class="btn btn-success">수정</a>
	<table class ="table">
		<thead>
			<tr>
				<td>board title</td>
				<td>${map["boardArticle"].boardArticleTitle}</td>
			</tr>
		</thead>
		
		<tbody>
			<tr>
				<td>Content</td>
				<td>${map["boardArticle"].boardArticleContent}</td>
			</tr>
			<c:if test='${map["boardFile"].size()>0}'>
				<tr><td colspan="2" >Image</td><tr>
					<c:forEach var="bf" items='${map["boardFile"]}'>
					<tr>
						<td colspan="2" align="center"><img src="/upload/${bf.boardFileName}.${bf.boardFileExt}"/></td>
					</c:forEach>
				</tr>
			</c:if>
		</tbody>
	</table>
</div>
</body>
</html>