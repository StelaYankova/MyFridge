<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<link href="<c:url value="css/yourFridges.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="navBar.jsp"%>
			<div class = "container">
	
	<div id="addNewFridge">
		<form action="newFridge" method="GET">
			<button type="submit"
				class="btn btn-primary btn-xs glyphicon glyphicon-plus">Add
				fridge</button>
		</form>
	</div>
	<p id = "heading">
		<br>Your fridges:
	</p>
	<br>
	<br>
	<br>
	<br>

	<c:if test="${not empty sessionScope.fridge}">
		<c:remove var="fridge" scope="session" />
	</c:if>
	<div id="mainPart">

		<table id="myTable" 
			class="table table-striped table-bordered table-hover">
			<thead>
				<th>Fridge</th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
			</thead>
			<tfoot>
				<th>Fridge</th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
			</tfoot>
			<tbody>
				<c:if test="${not empty sessionScope.user.fridges}">
					<c:forEach items="${sessionScope.user.fridges}" var="i">
						<tr>
							<td class="wrapword"><c:out value="${i.name}"></c:out></td>
							<td><form action="SeeProducts" method="GET">
									<input type="hidden" name="fridgeCode" value="${i.fridgeCode}">
									<button type="submit"
										class="btn btn-primary btn-md glyphicon glyphicon-th-list">
										See products in fridge
										<!--<c:set var="fridgeCode" scope="session" value="${i.fridgeCode}"/>-->
									</button>
								</form></td>
							<td>
								<form action="addProductsPage">
									<div id="fridgeCode"></div>
									<input type="hidden" name="fridgeCode" value="${i.fridgeCode}">

									<button type="submit"
										class="btn btn-primary btn-md glyphicon glyphicon-plus">
										Add products to fridge</button>
								</form>
							</td>
							<td>
								<form action="recipeByProducts" method="GET">
									<input type="hidden" name="fridgeCode" value="${i.fridgeCode}">

									<button type="submit"
										class="btn btn-primary btn-md glyphicon glyphicon-list-alt">
										Get recipes by products of this fridge</button>
								</form>
							</td>
							<td><div>
									<form action="removeFridge" method="POST" id="remove1">
										<input type="hidden" name="fridgeCode" value="${i.fridgeCode}">
										<button onclick="return confirm('Are you sure you want to delete this fridge?');"
											class="btn btn-primary btn-md glyphicon glyphicon-trash">
											Remove fridge</button>
									</form>
								</div></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty sessionScope.user.fridges}">
					<tr>
						<td colspan = "5"><c:out value="You don't have fridges yet."></c:out></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div></div>
</body>
<script>
window.onpageshow = function(event) {
	if (event.persisted) {
		window.location.reload()
	}
};

function areYouSure1(evt) {
	evt.preventDefault();
	if (confirm("Are you sure, that you want this fridge to be removed?") == true) {
		document.getElementById('remove1').submit();
		
	}
}
	$(document).ready(function() {
		$("#myTable").DataTable({
			 "aoColumnDefs": [
			                  { 'bSortable': false, 'aTargets': [ 1,2,3,4] }
			               ],
			"lengthMenu" : [ 5, 10, 15, 20 ]

		});
	});
	
</script>
</html>