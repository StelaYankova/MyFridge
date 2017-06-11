<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<c:url value="css/seeProducts.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="navBar.jsp"%>
		<div class = "containerPage">

	<c:if test="${not empty sessionScope.success}">
		<script>
			alert("The products number has been updated successfully!");
		</script>
		<c:remove var="success" scope="session" />

	</c:if>
	<c:if test="${not empty sessionScope.overMaxNum}">
		<script>
			alert("The products quantity has to be less than 40 000!");
		</script>
		<c:remove var="overMaxNum" scope="session" />
	</c:if>
	<c:if test="${not empty invalid}">
		<font size="6" color="red" id="invalid">Unknown fridge!</font>
	</c:if>
	<c:if test="${empty invalid}">

		<div id="addProducts">
			<form action="addProductsPage" method="POST">
				<button type="submit"
					class="btn btn-primary btn-xs glyphicon glyphicon-th-list">
					Add products to fridge
					<!--<c:set var="fridgeCode" scope="session" value="${i.fridgeCode}"/>-->
				</button>
			</form>
		</div>
		<c:if test="${not empty success}">
			<div class="alert alert-success">
				<strong>Success!</strong> The products number has been updated
				successfully!
			</div>
		</c:if>
		<p id = "productsInFridge">
			<br>The products in fridge: "${sessionScope.fridge.name}"
		</p>
		<br>
		<br>
		<br>
		<div id="mainPart">
			<div class="container">
				<div class="btn-group" style="position: absolute;">
					<c:forEach var="type" items="${applicationScope.productTypes}">
						<button type="button" class="btn btn-primary" id="${type}"
							onclick="load('${type}')">
							<c:out value="${type}"></c:out>
							<div id="${type}"></div>
						</button>
					</c:forEach>
				</div>
			</div>
			<br> <br> <br>
			<table class="table table-striped table-bordered table-hover"
				id="myTable" border="2">
				<thead id="head" class="invisible">
					<tr>
						<th>image</th>
						<th>name</th>
						<th>quantity</th>
						<th>Remove product</th>
						<th>Remove x from product</th>
						<th>Add x to product</th>
					</tr>
				</thead>
				<tfoot id="foot" class="invisible">
					<tr>
						<th>image</th>
						<th>name</th>
						<th>quantity</th>
						<th>Remove product</th>
						<th>Remove x from product</th>
						<th>Add x to product</th>
					</tr>
				</tfoot>
				<tbody id='tbody'>
				</tbody>
			</table>
		</div>
		<br>
		<br>
		<div id="fridge">
			<img
				src="http://findicons.com/files/icons/2770/ios_7_icons/128/fridge.png">
		</div>
	</c:if></div>
</body>


<script>
	function load(e) {
		setTimeout(
				function() {
					var table = $('#myTable');

					if ($(table).find("#tbody").html().trim().length !== 0) {
						$('#myTable').DataTable().clear().draw();
					}

					$
							.ajax({
								url : 'seeProductFromType',
								type : 'GET',
								data : {
									productsName : e
								},
								contentType : 'application/json; charset=utf-8',
								success : function(response) {
									console.log(response)
									var jsonData = response;
									var cnt = 0;
									var table = $('#myTable').DataTable({
										"aoColumnDefs" : [ {
											'className': "wrapword", "targets": [ 1,2 ],
 											"orderable": "false"
										} ],
										 "aoColumns": [
										              null,
										              { sWidth: '18%' },
										               { sWidth: '18%' },
										               null,null,null ],
										"lengthMenu" : [ 5, 10, 15, 20 ],
										"bDestroy" : true,
										"ordering": false
									});
									$("#fridge").removeClass("invisible")
											.addClass("visible");
									for ( var jsonObject in jsonData) {
										var rowNode = table.row
												.add(
														[
																'<img src = "images/' + jsonData[jsonObject].image + '"  height="42" width="42"/>',
																jsonData[jsonObject].name,
																jsonData[jsonObject].number
																		+ ' ('
																		+ jsonData[jsonObject].unit
																		+ ')',
																'<form action="./removeProducts" method="POST"><input type="hidden" name="prName" value='
																		+ jsonData[jsonObject].name
																				.split(
																						' ')
																				.join(
																						'%20')
																		+ '><button type="submit" class = " btn btn-primary btn-md glyphicon glyphicon-remove"> Remove product</button></form>',
																'<form id = "remove" action="./removeProductQuantity" method="POST"><input type="hidden" name="prName" value='
																		+ jsonData[jsonObject].name
																				.split(
																						' ')
																				.join(
																						'%20')
																		+ '><input type="number" name="pr_quantity" min=0 max = "20000"  maxlength="5" required/><button type="submit" class = " btn btn-primary btn-md glyphicon glyphicon-minus"> Remove product quantity</button</form>',
																'<form id = "add" action="./addProductQuantity" method="POST"><input type="hidden" name="prName" value='
																		+ jsonData[jsonObject].name
																				.split(
																						' ')
																				.join(
																						'%20')
																		+ '><input type="number" name="pr_quantity" min=0  max = "20000"  maxlength="5" required/><button type="submit" class = " btn btn-primary btn-md glyphicon glyphicon-plus"> Add product quantity</button></form>' ])
												.draw().node();

										$("#head").removeClass("invisible")
												.addClass("visible");
										$("#foot").removeClass("invisible")
												.addClass("visible");
										$("#fridge").addClass("invisible");
									}

									$("#head").removeClass("invisible")
											.addClass("visible");
									$("#foot").removeClass("invisible")
											.addClass("visible");

								},
								error : function() {
									alert("Something went wrong...");
								}
							});
				}, 50);

	}

	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}

	};
</script>

</html>
