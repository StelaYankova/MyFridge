<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<c:url value="css/index.css" />" rel="stylesheet">
</head>
<body>
	
	<div id = "welcome"><h1>Welcome to My Fridge!</h1></div>
	<div id="login-box">
		<h2 class="align-text">Login</h2>
		<form action="login" method='POST'>
			<c:if test="${not empty wrong}">
				<p id = "wrongData">Wrong username or password</p>
			</c:if>
			<p class="align-text">
				Username:<input type='text' class="control-label col-sm-16"
					name='username'  maxlength="20"><br> Password:<input type='password'
					class="control-label col-sm-16"  maxlength="20" name='password' /><br> <br>
				<button type="submit"
					class="btn btn-primary glyphicon glyphicon-log-in"> Login</button>
			</p>
		</form>
		<form action="register" method='GET'>
			<button type="submit" class="btn btn-primary btn-xs glyphicon" >
				Register</button>
		</form>
	</div>
</body>
<script>
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>
</html>