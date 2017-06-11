<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="css/addNewProduct.css" />" rel="stylesheet">
</head>

<body>
	<%@ include file="navBar.jsp"%>
			<div class = "container">
	<c:if test="${not empty invalid}">
		<font size="6" color="red" id="invalid">Unknown fridge!</font>
	</c:if>
	<c:if test="${empty invalid}">
		<div id="seeProducts">
			<form action="SeeProducts" method="GET">
				<button type="submit"
					class="btn btn-primary btn-xs glyphicon glyphicon-th-list">
					See products in fridge
					<!--<c:set var="fridgeCode" scope="session" value="${i.fridgeCode}"/>-->
				</button>
			</form>
		</div>
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
		<p id = "addProductsToFridge">
			<br>Add products to fridge: "${sessionScope.fridge.name}"
		</p>
		<div id="mainPart">

			<br> <br> <br>

			<div id="image">
				<img
					src="http://findicons.com/files/icons/2770/ios_7_icons/128/clear_shopping_cart.png">
			</div>
			<form action="addProduct" method="POST">
				<div id="z"></div>
				<div id="custom-tag"></div>
				<div>
					<label> Product type: </label> <select id="ProductType">
						<option class="control-label col-sm-2">Select product
							type</option>
						<c:forEach var="entry" items="${applicationScope.productTypes}">
							<option class="form-control control-label col-sm-2"
								value="${entry}">${entry}</option>
						</c:forEach>
					</select>
				</div>
				<div id="selected">
					<label> Choose product: </label> <select id="product">
						<option class="control-label col-sm-2" id="s">Select
							Product</option>
					</select>
				</div>
				<div id="ready">
					<input id="chosenProduct" type="hidden" name="product" value="">
					<input type="number" name="quantity" class="control-label col-sm-3"
						min="1" max="20000" required />
					<div>
						<font size="2" id="unit"></font>
					</div>
					<br>
					<button type="submit" class="btn btn-primary glyphicon">Add</button>
					<img id='prodPic' src='' class="img-rounded" height=65px width=65px>

				</div>
			</form>
		</div>
	</c:if></div>
</body>

<script>
	function success() {
		alert("The products number has been updated successfully!");
	}
	$(document).ready(function() {
		$('#ProductType').change(function(event) {
			var products = $("select#ProductType").val()
			$.get('addProductsByType', {
				productsName : products
			}, function(response) {
				var select = $('#product');
				select.find('option').not(':first').remove();
				$.each(response, function(index, value) {
					$('<option>').val(value).text(value).appendTo(select);
				});
			});
		});

		$('#selected').change(function(event) {
			var selected = $('#product').find(":selected").text();
			document.getElementById("chosenProduct").value = selected;
			$.ajax({
				url : 'getProductPic',
				type : 'GET',
				data : {
					product : selected
				},
				dataType : 'text',
				success : function(response) {
					var pic = response;
					console.log(pic);
					$('#prodPic').attr('src', 'images/' + pic);
				}
			});
			$.ajax({
				url : 'getProductUnit',
				type : 'GET',
				data : {
					product : selected
				},
				dataType : 'text',
				success : function(response) {
					var unit = response;
					$('#unit').html('(' + unit + ')');
				}
			});
			var continue_button = document.getElementById('ready');
			continue_button.style.visibility = 'visible';
		});
	});

	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>

</html>