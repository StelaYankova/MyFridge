<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
<link href="<c:url value="css/addFridge.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="navBar.jsp"%>
			<div class = "container">
	
	<c:if test="${not empty exists}">
		<script>
			alert("Sorry, but fridge with this id already exists in your page")
		</script>
	</c:if>
	<table id="form" frame="box">
		<form:form method="post" action="newFridge" commandName="fridge"
			class="form-horizontal">
			<tr>
				<td colspan = "2"><h3>New fridge</h3></td>
			<tr>
				<td><label class="control-label col-sm-2">Name:</label></td>
				<td><form:input path="name" class="form-control" maxlength="20" /><font
					color="red"><form:errors path="name" /></font></td>
			</tr>
			<tr>
				<td><label class="control-label col-sm-2">Code:</label></td>
				<td><form:input path="fridgeCode" class="form-control"
						maxlength="20" /><font color="red"><form:errors
							path="fridgeCode" /></font></td>
			</tr>
			<td><input type="submit" class="btn btn-primary glyphicon"
				value=" Add fridge" /></td>
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