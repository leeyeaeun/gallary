<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
$(document).ready(function(){
	$('#add').click(function(){
		var flag= true;
		$('.boardImg').each(function(item,index){
			if($(this).val()==''){
				flag=false;
				$('#error').text('파일을 선택하세요');
			}

		});
		/* if(file)  파일이 선택된게 없으면 파일 선택버튼이 늘어나지않게& 유효성검사*/
		if(flag){
			//$('#error').text('');
		 $('#fileSection').append('<div>	<input type="file" name="boardImg" multiple="multiple" class="boardImg"/> </div>');
		}	
	});
	$('#boardAdd').click(function(){
		$('.boardImg').each(function(item,index){
			if($(this).val()==''){
				$(this).remove();
			}
		});
		
		if($('#boardTitle').val()==''){
			$('#error').text('제목을 입력하세요');
			$('#boardTitle').focus();
		}else if($('#boardContent').val()==''){
			$('#error').text('내용을 입력하세요');
			$('#boardContent').focus();
		}else{
			$('#boardUpdateForm').submit();
		}
	});
});
</script>
</head>
<body>
<div class="container">
	<h1>Board Update</h1>
	<form id="boardUpdateForm"action='/boardUpdate?boardArticleNo=${map["boardArticle"].boardArticleNo}' method="post" enctype="multipart/form-data">
		<table class ="table">
			<thead>
				<tr>
					<td>board title</td>
					<td>
						<input type="text" name ="boardTitle" value ='${map["boardArticle"].boardArticleTitle}'>
					</td>				
				</tr>
			</thead>
			<tbody>			
				<tr>
					<td>Content</td>
					<td>
						<input type="text" name ="boardContent"value ='${map["boardArticle"].boardArticleContent}'>
					</td>					
				</tr>									
					<c:if test='${map["boardFile"].size()>0}'>
						<c:forEach var="bf" items='${map["boardFile"]}'>					
							<tr>
								<td>Image</td>
								<td>
									<img src="/upload/${bf.boardFileName}.${bf.boardFileExt}"/>
								</td>	
							</tr>								
						</c:forEach>
					</c:if>
				<tr>
					<td>
					<input type="button" id ="add" value="fileAdd"/>
						<div id="fileSection"></div>
					</td>
				</tr>	
				<tr>
					<td colspan="2" ><input type="button" id ="boardAdd" value="클릭"/>
					<span id="error">${errorMsg}</span>
					</td>
				</tr>		
			</tbody>
		</table>
	</form>
</div>	
</body>
</html>