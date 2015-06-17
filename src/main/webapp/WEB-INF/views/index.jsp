<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>

<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script
	src="https://raw.githubusercontent.com/SamWM/jQuery-Plugins/master/numeric/jquery.numeric.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
#formDiv {
	margin: 50px auto 0px auto;
	width: 40%;
}
</style>

<title>Web-client</title>
</head>
<body>



	<div id="formDiv">
		<c:choose>
			<c:when test="${not empty previousCommandType}">
				<c:choose>
					<c:when test="${status == 'OK'}">
						<c:if test="${not empty users}">
							<table class="table table-bordered table-hover table-striped">
								<thead>
									<tr>
										<th>Id</th>
										<th>Name</th>
										<th>Surname</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${users}" var="user">
										<tr>
											<td><c:out value="${user.id}" /></td>
											<td><c:out value="${user.name}" /></td>
											<td><c:out value="${user.surname}" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
						<c:if test="${empty users}">
							<div class="alert alert-success">
								<c:out value="${message}" />
							</div>
						</c:if>
					</c:when>
					<c:when test="${status == 'FAIL'}">
						<div class="alert alert-danger">
							<c:out value="${message}" />
						</div>
					</c:when>
					<c:when test="${status == 'WARN'}">
						<div class="alert alert-warning">
							<c:out value="${message}" />
						</div>
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>


		<form action="index" method="post">
			<div class="form-group" id="commandTypeDiv">
				<label for="commandType">Choose command:</label> <select
					name="commandType" id="commandType">
					<option selected="selected">none</option>
					<option>add</option>
					<option>get</option>
					<option>remove</option>
				</select>
			</div>
			<div class="form-group" id="userIdDiv">
				<label for="userId">User Id</label> <input type="text"
					class="form-control" id="userId" name="userId">
			</div>
			<div class="form-group" id="userNameDiv">
				<label for="userName">Name:</label> <input type="text"
					class="form-control" name="userName" id="userName">
			</div>
			<div class="form-group" id="surnameDiv">
				<label for="surname">Surname:</label> <input type="text"
					class="form-control" name="surname" id="surname">
			</div>
			<button type="submit" id="submitButton" class="btn btn-default">Execute</button>
		</form>
	</div>



	<script type="text/javascript">
		$(document).ready(function() {

			$("#userId").numeric();
			
			$('#userName').bind('keyup blur', function() {
			    $(this).val($(this).val().replace(/[^A-Za-z]/g, ''))
			});
			
			$('#surname').bind('keyup blur', function() {
			    $(this).val($(this).val().replace(/[^A-Za-z]/g, ''))
			});

			var clearFields = function() {
				$('#userId').val("");
				$('#userName').val("");
				$('#surname').val("");
			}

			$('#commandType').change(function() {
				if ($(this).val() === 'none') {
					$('#userIdDiv').show("slow");
					$('#userNameDiv').show("slow");
					$('#surnameDiv').show("slow");
					clearFields();
				} else if ($(this).val() === 'remove') {
					$('#userIdDiv').show("slow");
					$('#userNameDiv').hide("slow");
					$('#surnameDiv').hide("slow");
					clearFields();
				} else if ($(this).val() === 'add') {
					$('#userIdDiv').hide("slow");
					$('#userNameDiv').show("slow");
					$('#surnameDiv').show("slow");
					clearFields();
				} else if ($(this).val() === 'get') {
					$('#userIdDiv').show("slow");
					$('#userNameDiv').show("slow");
					$('#surnameDiv').show("slow");
					clearFields();
				}
			});
		});
	</script>




	<!-- <c:choose>
			<c:when test="${not empty previousCommandType}">
				<c:if test="${status == 'OK'}">
					<c:if test="${previousCommandType == 'get'}">
						<c:choose>
							<c:when test="${not empty users}">
								<table class="table table-bordered table-hover table-striped">
									<thead>
										<tr>
											<th>Id</th>
											<th>Name</th>
											<th>Surname</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${users}" var="user">
											<tr>
												<td><c:out value="${user.id}" /></td>
												<td><c:out value="${user.name}" /></td>
												<td><c:out value="${user.surname}" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:when test="${empty users}">
								<div class="alert alert-warning" ><c:out value="${message}" /></div>
							</c:when>
							<c:otherwise>
								<div class="alert alert-warning" ><c:out value="${message}" /></div>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if
						test="${previousCommandType == 'add' or previousCommandType == 'remove'}">
						<div class="alert alert-success" ><c:out value="${message}" /></div>
					</c:if>
				</c:if>
				<c:if test="${status == 'FAIL'}">
					<div class="alert alert-danger" ><c:out value="${message}" /></div>
				</c:if>

			</c:when>
		</c:choose> -->



</body>
</html>