<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Case</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="<c:url value="css/navBarForChangeProfile.css" />" rel="stylesheet">
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
						class="glyphicon glyphicon-list-alt"></span> Your favourite
						recipes</a></li>
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

</body>

</html>