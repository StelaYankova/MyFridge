<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<c:url value="css/favouriteRecipes.css" />" rel="stylesheet">
</head>

<body>
	<%@ include file="navBar.jsp"%>
		<div class = "container">
	
	<c:if test="${not empty sessionScope.fridge}">
		<c:remove var="fridge" scope="session" />
	</c:if>
	<p id = "yourFavouriteRecipes">
		<br>Your favourite recipes:
	</p>

	<br>
	<br>
	<br>
	<br>
	<div id="mainPart">
		<table border="2" id="myTable"
			class="table table-striped table-bordered table-hover">

			<thead>
				<tr>
					<th>Title</th>
					<th></th>
					<td></td>
					<td></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<th>Title</th>
					<th></th>
					<td></td>
					<td></td>
				</tr>
			</tfoot>

			<tbody>
				<c:forEach var="entry" items="${sessionScope.user.favouriteRecipes}">
					<tr>
						<td class="wrapword"><c:out value="${entry.value.name}"></c:out></td>
						<td><img class="pic" src="${entry.value.image}" height=auto
							width=30%></td>
						<td>
							<form action="seeRecipe" method="GET">
								<input type="hidden" value="${entry.key}" name="id" />
								<button type="submit"
									class="btn btn-primary btn-md glyphicon glyphicon-zoom-in">Details</button>
							</form>
						</td>
						<td>
							<form id = "removeFromFavourites" action="removeFavValue" method="POST">
								<input type="hidden" value="${entry.key}" name="id" />
								<button type="submit" class="glyphicon glyphicon-heart red"></button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div></div>
	<script>
		$(document).ready(function() {
			$("#myTable").DataTable({
				 "aoColumnDefs": [
				                  { 'bSortable': false, 'aTargets': [ 1,2,3] }
				               ],
				"lengthMenu" : [ 5, 10, 15, 20 ],
				
			});
		});
		window.onpageshow = function(event) {
			if (event.persisted) {
				window.location.reload()
			}
		};
	</script>
</body>
</html>