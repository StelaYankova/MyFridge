<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
<link href="<c:url value="css/possibleRecipes.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="navBar.jsp"%>
			<div class = "container">
	
	<c:if test="${not empty sessionScope.success}">
		<script>
			alert("The products number has been updated successfully!");
		</script>
		<c:remove var="success" scope="session" />
	</c:if>
	<c:if test="${not empty invalid}">
		<font size="6" color="red" id="invalid">Unknown fridge!</font>
	</c:if>
	<c:if test="${not empty sessionScope.productsToRemove}">
		<c:remove var="productsToRemove" scope="session" />
	</c:if>
	<c:if test="${empty invalid}">
		<p id = "heading">
			<br>The recipes, which you could prepare with the products in
			your fridge:
		</p>
		<br>
		<br>
		<br>
		<br>
		<c:if test="${not empty sessionScope.recipe}">
			<c:remove var="recipe" scope="session" />
		</c:if>
		<div id="mainPart">
			<table class="table table-striped table-bordered table-hover"
				id="myData">
				<thead>
					<tr>
						<th>title</th>
						<th>image</th>
						<th>used ingredients</th>
						<th>missed ingredients</th>
						<th></th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>title</th>
						<th>image</th>
						<th>used ingredients</th>
						<th>missed ingredients</th>
						<th></th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${possibleRecipes}" var="i">
						<c:set var="myContent" value="${i.title}" />
						<c:set var="search" value="'" />
						<c:set var="replace" value="%20" />
						<c:set var="titleReplaced"
							value="${fn:replace(myContent,search, replace)}" />
						<tr>
							<td class="wrapword">${i.title}</td>
							<td><img src=${i.image } height="auto" width=60%>
								<form action="seeRecipe" method="GET">
									<input type="hidden" name="id" value="${i.id}">
									<button type="submit"
										class="btn btn-primary btn-sm glyphicon glyphicon-zoom-in">
										Details</button>

								</form></td>
							<td>${i.usedIngredientCount}</td>
							<td>${i.missedIngredientCount}</td>

							<td align="center"><c:if
									test="${sessionScope.user.favouriteRecipes.containsKey(i.id)}">
									<button type="submit" class="glyphicon glyphicon-heart red"
										id="${i.id}"
										onclick='fav("${i.id}","${titleReplaced}","${i.image}", this)'>

									</button>
								</c:if> <c:if
									test="${!sessionScope.user.favouriteRecipes.containsKey(i.id)}">
									<button type="submit"
										class="glyphicon glyphicon-heart-empty red" id="${i.id}"
										onclick='fav("${i.id}","${titleReplaced}","${i.image}", this)'
										value="Like"></button>
								</c:if></td>


						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if></div>

</body>
<script>
	$(document).ready(function() {
		$("#myData").DataTable({
			"aoColumnDefs" : [ {
				'bSortable' : false,
				'aTargets' : [ 1, 4 ]
			}],
			"lengthMenu" : [ 5, 10, 15, 20 ],
            "aoColumns": [
            { sWidth: '30%' },
            { sWidth: '12%' },
            { sWidth: '12%' },
            { sWidth: '12%' },{ sWidth: '9%' } ]
		});
		
	});
	
	function fav(e, title, image, isLiked) {
		var currClass = isLiked;
		var name = title;
		var photo = image;
		var value = e;
		var testClass = "glyphicon-heart-empty";

		var id = "#"+e;
		$.ajax({
			url : 'addRemoveFav',
			type : 'POST',
			data : {
				"id" : value,
				"recName" : name,
				"image" : photo
			},
			success : function() {

				if (!currClass.classList.contains(testClass)) {
					$(id).removeClass("glyphicon glyphicon-heart red")
							.addClass("glyphicon glyphicon-heart-empty red");
					console.log("unlike")
				} else {
					$(id).removeClass(
							"glyphicon glyphicon-heart-empty red").addClass(
							"glyphicon glyphicon-heart red");
					console.log("like")

				}
			}
		});

	}
	function addFav(e, title, image) {
		console.log(title)
		var name = title;
		var photo = image;
		var value = e;
		var id = '#' + e + 'notLiked';
		var idNew = '#' + e + 'liked';
		$.ajax({
			url : 'addToFav',
			type : 'POST',
			data : {
				"id" : value,
				"recName" : name,
				"image" : photo
			},
			success : function() {
				$(id).removeClass("visible").addClass("invisible");
				$(idNew).removeClass("invisible").addClass("visible");
			}
		});

	}

	function removeFav(e, title) {
		var name = title;
		var value = e;
		var id = '#' + e + 'liked';
		var idNew = '#' + e + 'notLiked';
		$.ajax({
			url : 'removeFav',
			data : {
				"id" : value
			},
			type : 'POST',
			success : function() {
				$(id).removeClass("visible").addClass("invisible");
				$(idNew).removeClass("invisible").addClass("visible");
			}
		});
	}
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>
</html>
