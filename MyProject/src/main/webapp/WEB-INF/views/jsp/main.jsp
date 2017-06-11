
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<c:url value="css/main.css" />" rel="stylesheet">
</head>
<body>
	<c:if test="${not empty sessionScope.fridge}">
		<c:remove var="fridge" scope="session" />
	</c:if>
	<%@ include file="navBar.jsp"%>
	<div>
		<div class="container">

			<div id="mainPart">

				<br> <br>
				<table id="myTable" border="12px" class="table-striped ">
					<caption align="top">
						<b>Some of our recipes...</b>
					</caption>
					<tr>
						<th>Title</th>
						<th></th>
						<th>Ready in minutes</th>
						<th></th>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
<script>
	$(document)
			.ready(
					function() {
						$
								.ajax({
									url : 'randomRecipes',
									method : 'GET',

									contentType : 'application/json; charset=utf-8',
									success : function(response) {
										var jsonData = response;
										var table = document
												.getElementById("myTable");
										var cnt = 1;
										for ( var jsonObject in jsonData) {
											var row = table.insertRow(cnt);
											var cell1 = row.insertCell(0);
											var cell2 = row.insertCell(1);
											var cell3 = row.insertCell(2);
											var cell4 = row.insertCell(3);
											cell1.innerHTML = jsonData[jsonObject].title;
											cell2.innerHTML = '<img src = '
													+ jsonData[jsonObject].image
													+ '  style ="height:auto; width:50%">';
											cell3.innerHTML = jsonData[jsonObject].readyInMinutes;
											cell4.innerHTML = '<form action="seeRecipe" method="GET">\
												<input type="hidden" name="id" value='+jsonData[jsonObject].id+'> \
												<button type="submit" class="btn btn-primary btn-md glyphicon glyphicon-zoom-in" />Details</button>\
\
															</form>';
											cnt++;

										}
									}
								});
					});
	window.onpageshow = function(event) {
		if (event.persisted) {
			window.location.reload()
		}
	};
</script>
</html>