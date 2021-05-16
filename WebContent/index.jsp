<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<!--  

	see the video first to configure access to google drive 
	and download your credentials, otherwise you will 
	not be able to do any tests 
	
-->
	  
	<form action="uploadImage" enctype="multipart/form-data" method="post">
		<input type="file" name="imagen">
		<input type="submit" value="send">
	</form>
</body>
</html>