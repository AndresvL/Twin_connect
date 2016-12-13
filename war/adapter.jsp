<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Twinfield Connect</title>

<!-- Bootstrap Core CSS -->
<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Theme CSS -->
<link href="css/freelancer.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="vendor/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Montserrat:400,700"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic"
	rel="stylesheet" type="text/css">

<!-- Sweet Alert -->
<script src="sweetalert2/sweetalert2.min.js"></script>
<link rel="stylesheet" href="sweetalert2/sweetalert2.min.css">


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
		        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		    <![endif]-->
<script>
	function message() {
		swal({
			title : 'Please Wait!',
			text : 'Importing data',
			imageUrl : 'loadingbar.gif',
			imageWidth : 150,
			imageHeight : 150,
			animation : false
		})

		return true;
	}
</script>
</head>

<body id="page-top" class="index">

	<!-- Contact Section -->
	<section id="contact">
		<div class="container">
			<div class="row">
				<div class="col-lg-8 col-lg-offset-2">
					<div class="panel-group">
						<div class="panel panel-success">
							<div class="panel-heading">Access</div>
							<div class="panel-body">
								<form name="getAccess" action="OAuth.do">
									<div class="row control-group">
										<div class="alert alert-success">
											<strong>Success!</strong> Indicates a successful or positive
											action.
										</div>
										<div
											class="form-group col-xs-12 floating-label-form-group controls">
											<label>Software Token</label> <input type="text"
												class="form-control" placeholder="Software token"
												name="token" id="name"
												value="e0aa544680b8cbee18a15b6650600db2" required
												data-validation-required-message="Please enter the software token">
											<p class="help-block text-danger"></p>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="form-group col-xs-12">
											<input type="submit" class="btn btn-success btn-lg"
												value="Access" onclick="return message();" />
										</div>
									</div>

								</form>
							</div>
						</div>
						<div class="panel panel-success">
							<div class="panel-heading">Import</div>
							<div class="panel-body">
								<form action="import.do">
									<div class="row control-group">
										<div class="alert alert-success">
											${error}
										</div>
										<div
											class="form-group col-xs-12 floating-label-form-group controls">
											<label>Office</label> <select name="offices">
												<c:forEach items="${offices}" var="office">
													<option value="${office.code}">${office.name}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<br>
									<div id="success"></div>
									<div class="row">
										<div class="form-group col-xs-12">
											<input type="submit" class="btn btn-success btn-lg"
												value="Import" name="category" onclick="return message();" />
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-success">
							<div class="panel-heading">Export</div>
							<div class="panel-body">
								<form action="import.do">
									<div class="row control-group">
										<div class="alert alert-success">
											${error}
										</div>
										<div
											class="form-group col-xs-12 floating-label-form-group controls">

											<label>FactuurType</label> <select name="factuurType">
												<option value="Alle">Alle</option>
												<option value="Opgehaald">Opgehaald</option>
												<option value="Klaargezet">Klaargezet</option>
												<option value="Compleet">Compleet</option>
												<option value="Afgehandeld">Afgehandeld</option>
											</select>
										</div>
									</div>
									<br>
									<div id="success"></div>
									<div class="row">
										<div class="form-group col-xs-12">
											<input type="submit" class="btn btn-success btn-lg"
												value="Export" name="category" onclick="return message();" />
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>


	<!-- jQuery -->
	<script src="vendor/jquery/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>

	<!-- Plugin JavaScript -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

	<!-- Contact Form JavaScript -->
	<script src="js/jqBootstrapValidation.js"></script>
	<script src="js/contact_me.js"></script>

	<!-- Theme JavaScript -->
	<script src="js/freelancer.min.js"></script>

</body>

</html>

