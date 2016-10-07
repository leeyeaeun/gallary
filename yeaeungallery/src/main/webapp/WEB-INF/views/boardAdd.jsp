<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
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
			 $('#fileSection').append('<div> <input type="file" name="boardImg" multiple="multiple" class="boardImg"/> </div>');
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
			$('#boardAddForm').submit();
			}
		});
	});
</script>
<title>Insert title here</title>
</head>
<body>

<!-- form에서 입력받은 값과 동일한 db테이블이 없으므로 
BoardRequest.java 도메인타입 클래스를 만들어서 (input tag이름이 dto필드명과 동일하면 자동으로 객체생성됨)
db내의 두개의 테이블에 입력값을 각각 넣을것이얌  -->

<div class="container">	
	<h1>boardAdd</h1>
	<form id="boardAddForm" action="/boardAdd" method="post" enctype="multipart/form-data">
	
	<!--
	enctype="multipart/form-data"을 추가하여 form에서 파일을 넘길수있다.
	servlet-context.xml에 MultipartResolver를 스프링 설정 파일에 등록해놓았기때문에 
	Multipart형식으로 데이터가 전송된 경우, 해당 데이터를 mvc에서 사용할 수 있도록 변환해줌 
	
	boardController에서 유효성 검사후 실패시 view로 forwarding되는데 기존의 입력한 데이터가 뜨도록, 
	Model에 저장한 에러 메세지가 뜨도록 하였음 -
	-->
		<table border="1" class="table">
			<tr>
				<td>boardTitle</td>
				<td><input id="boardTitle" type="text" name="boardTitle" value="${boardRequest.boardTitle }"/></td>
			</tr>
			<tr>
				<td>content</td>
				<td><input id="boardContent" type="text" name="boardContent"value="${boardRequest.boardContent}"/></td>
			</tr>
			<tr>
				<td>image</td>
				<td>
					<input type="button" id ="add" value="fileAdd"/>
					<div id="fileSection">
						<!-- jquery로 이위치에 추가되는코드
						<div><input type="file" name="boardImg" multiple="multiple" class="boardImg"/></div> -->
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" ><input type="button" id ="boardAdd" value="클릭"/>
				<span id="error">${errorMsg}</span>
				</td>
			</tr>
		</table>
	</form> 
</div>

</body>
</html>