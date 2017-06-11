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
<link rel="stylesheet" type="text/css"
	href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.css">
<script type="text/javascript" charset="utf8"
	src="//cdn.datatables.net/1.10.12/js/jquery.dataTables.js"></script>
<link href="<c:url value="css/navBar.css" />" rel="stylesheet">
</head>
<body>

	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href = "index" class="navbar-brand">My Fridge</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="homePage"><span
					class="glyphicon glyphicon-home"></span>Home</a></li>
			<li><a href="seeFavouriteRecipes"><span
					class="glyphicon glyphicon-list-alt"></span> Your favourite recipes</a></li>
			<li><a href="yourFridges"><span
					class="glyphicon glyphicon-th-large"></span> You fridges </a></li>
			<li><a href="Logout"><span
					class="glyphicon glyphicon-log-out"></span> Logout</a></li>
		</ul>
	</div>
	</nav>
	<p id="welcome">
		Welcome,
		<c:out value="${sessionScope.user.username}" />
		!
	</p>
	<br>
	<img src="profilePics/${sessionScope.user.photo}" id="pic"
		class="img-circle" width="140" height="100">
	<form action="changeProfile" method='GET'>
		<button type="submit" id="changeProfile"
			class="btn btn-primary btn-xs glyphicon glyphicon-user">
			Change your profile</button>
	</form>
	<br>
	<br>
	<br>
	<br>
	<br>
</body>

</html>