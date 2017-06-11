
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="<c:url value="css/recipe.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="navBar.jsp"%>
		<div class = "container">

	<c:if test="${not empty unknownRecipe}">
		<font size="6" color="red" id="invalid">Unknown Recipe!</font>
	</c:if>
	<c:if test="${ empty unknownRecipe}">
		<div id="missingcookNow">
			<c:forEach var="entry" items="${notEnough}">
				<h6 id = "notEnoughProducts" >
					<c:out value="You dont have enough ${entry};"></c:out>
				</h6>
			</c:forEach>

			<c:if test="${not empty sessionScope.fridge}">
				<form action="cookNow" method="POST">
					<input type="hidden" value="${sessionScope.recipe}"
						name="recipeToCook">
					<button type="submit" class="btn btn-primary btn-md">Cook
						recipe</button>
				</form>
			</c:if>
		</div>
		<div id="mainPart">
			<img src=${sessionScope.recipe.image}> <b><c:out
					value="${sessionScope.recipe.title}"></c:out></b> <br>
			<c:out value="INSTRUCTIONS: "></c:out>
			<br>
			<div id="instructions">
				<c:if test="${not empty sessionScope.recipe.instructions}">
					<c:out value="${sessionScope.recipe.instructions}"></c:out>
				</c:if>
				<c:if test="${empty sessionScope.recipe.instructions}">
					<c:out value="No instructions available"></c:out>
				</c:if>
			</div>
			<br>
			<div id="ingredients">
				<b><c:out value="Ingredients"></c:out></b> <br>
				<c:forEach var="entry" items="${sessionScope.recipe.products}">
					<c:out value="${entry.key} - "></c:out>
					<c:forEach var="p" items="${entry.value}">
						<c:out value="${p.key}"></c:out>
						<c:out value=" "></c:out>
						<c:out value="${p.value}"></c:out>
						<br>
					</c:forEach>
				</c:forEach>
			</div>
		</div>
	</c:if></div>
</body>
<script>
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
	$(document).ready(function() {
		$('#mainPart').insertAfter('#missingcookNow');
		$('#ingredients').insertAfter('#instructions');
	});
</script>
</html>