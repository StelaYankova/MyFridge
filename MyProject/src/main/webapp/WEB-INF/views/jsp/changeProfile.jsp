<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<c:url value="css/changeProfile.css" />" rel="stylesheet">
</head>
<body>
	<%@ include file="navBarForChangeProfile.jsp"%>
			<div class = "container">
	
	<div id="removeProfile">
		<form action="removeProfile" method="POST" id="remove">
			<button onclick="areYouSure()"
				class="btn btn-primary btn-xs glyphicon glyphicon-remove">Delete
				you profile</button>
			<br>
		</form>
	</div>
	<div id="mainPart">
	<img src="./ProfilePic" class="img-circle"
			width="220" height="220" /><br>
		<br>

		<table id="form">
			<form:form method="POST" action="updateInfo" commandName="user"
				enctype="multipart/form-data">
				<tr>
					<td>Password</td>
					<td><form:input type="password" class="control-label col-sm-8"
							path="password" maxlength="20"
							value="${sessionScope.user.password}" /><font color="red"><form:errors
								path="password" /></font></td>
				</tr>
				<tr>
					<td>Repeat password</td>
					<td><form:input type="password" class="control-label col-sm-8"
							path="confirmPassword" maxlength="20"
							value="${sessionScope.user.password}" /><font color="red"><form:errors
								path="confirmPassword" /></font></td>
				</tr>
				<tr>
					<td>Email</td>
					<td><form:input type="text" class="control-label col-sm-8"
							path="email" maxlength="20" value="${sessionScope.user.email}" /><font
						color="red"><form:errors path="email" /></font></td>
				</tr>
				<tr>
					<td>Photo</td>
					<td><form:input type="file" class="control-label col-sm-12"
							accept="image/*" path="photo" name="photo"
							value="${sessionScope.user.photo}" /><font color="red"><form:errors
								path="photo" /></font></td>
				</tr>
				<td><input type="submit" class="btn btn-primary glyphicon"
					value="Save changes" /></td>
			</form:form>

		</table>
	</div></div>
</body>
<script>
	function LoadValue() {
		FormName.photo.value = "${sessionScope.user.photo}";
	}

	function areYouSure() {
		if (confirm("Are you sure, that you want your profile to be removed?") == true) {
			document.getElementById('remove').submit();
		} else {
			document.getElementById('remove').onsubmit = function() {
				return false;
			};
		}
	}
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>

</html>