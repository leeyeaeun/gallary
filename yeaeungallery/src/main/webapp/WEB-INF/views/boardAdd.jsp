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
					$('#error').text('������ �����ϼ���');
				}
			});
			/* if(file)  ������ ���õȰ� ������ ���� ���ù�ư�� �þ���ʰ�& ��ȿ���˻�*/
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
				$('#error').text('������ �Է��ϼ���');
				$('#boardTitle').focus();
			}else if($('#boardContent').val()==''){
				$('#error').text('������ �Է��ϼ���');
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

<!-- form���� �Է¹��� ���� ������ db���̺��� �����Ƿ� 
BoardRequest.java ������Ÿ�� Ŭ������ ���� (input tag�̸��� dto�ʵ��� �����ϸ� �ڵ����� ��ü������)
db���� �ΰ��� ���̺� �Է°��� ���� �������̾�  -->

<div class="container">	
	<h1>boardAdd</h1>
	<form id="boardAddForm" action="/boardAdd" method="post" enctype="multipart/form-data">
	
	<!--
	enctype="multipart/form-data"�� �߰��Ͽ� form���� ������ �ѱ���ִ�.
	servlet-context.xml�� MultipartResolver�� ������ ���� ���Ͽ� ����س��ұ⶧���� 
	Multipart�������� �����Ͱ� ���۵� ���, �ش� �����͸� mvc���� ����� �� �ֵ��� ��ȯ���� 
	
	boardController���� ��ȿ�� �˻��� ���н� view�� forwarding�Ǵµ� ������ �Է��� �����Ͱ� �ߵ���, 
	Model�� ������ ���� �޼����� �ߵ��� �Ͽ��� -
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
						<!-- jquery�� ����ġ�� �߰��Ǵ��ڵ�
						<div><input type="file" name="boardImg" multiple="multiple" class="boardImg"/></div> -->
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" ><input type="button" id ="boardAdd" value="Ŭ��"/>
				<span id="error">${errorMsg}</span>
				</td>
			</tr>
		</table>
	</form> 
</div>

</body>
</html>