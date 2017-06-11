<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
div {
	position: relative;
	top: 350px;
	left: 230px;
	border-radius: 25px;
	background: #ffffff;
	padding: 20px;
	width: 200px;
	height: 100px;
}

form {
	
}

body {
	background:
		url(https://d13yacurqjgara.cloudfront.net/users/1078347/screenshots/2799566/oops.png)
		no-repeat center center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="border-radius"
		style="border: 1px solid black; text-align: center">
		<form action="http://localhost:8080/MyProject/homePage" method="GET"
			id="home">
			Something went wrong...<br>

			<button class="btn btn-primary glyphicon glyphicon-home">Home
				page</button>
		</form>
	</div>
</body>

</html>
<!-- https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSKmTpZpAg_yvvosrutOOKjOeNWmn4PuUm2FEHymA479qkdG4pSvw -->