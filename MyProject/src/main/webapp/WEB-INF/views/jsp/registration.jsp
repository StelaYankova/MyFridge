<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="css/registration.css" />" rel="stylesheet">
<title>Registration Form</title>
</head>
<body>
		<div class = "container">

	<table id="form" frame="box">
		<form:form method="POST" action="register" commandName="user"
			enctype="multipart/form-data">
			<tr><td colspan="2"><h4>Registration form</h4></td></tr>
			<tr>
				<td>Username</td>
				<td><form:input type="text" class="control-label col-sm-12"
						path="username" maxlength="20" /><font color="red"><form:errors
							path="username" /></font></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><form:input type="password" class="control-label col-sm-12"
						path="password" maxlength="20" /><font color="red"><form:errors
							path="password" /></font></td>
			</tr>
			<tr>
				<td>Repeat password</td>
				<td><form:input type="password" class="control-label col-sm-12"
						path="confirmPassword" maxlength="20" /><font color="red"><form:errors
							path="confirmPassword" /></font></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><form:input type="text" class="control-label col-sm-12"
						path="email" maxlength="20" /><font color="red"><form:errors
							path="email" /></font></td>
			</tr>
			<tr>
				<td>Photo</td>
				<td><form:input type="file" class="control-label col-sm-12"
						accept="image/*" path="photo" name="photo" /><font color="red"><form:errors
							path="photo" /></font></td>
				<td>
			</tr>
			<td><button type="submit"
					class="btn btn-primary btn-md control-label col-sm-12">Register</button></td>
		</form:form>
	</table></div>
</body>
<script>
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>
</html>