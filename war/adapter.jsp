<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Twinfield Connect</title>
</head>
<body>
	<form action="OAuth.do">
	<input type="text" name="token"
			value="e0aa544680b8cbee18a15b6650600db2" >
		<input type="submit" value="Get Access" />
	</form>
	
	<div><input type="text" name="session"
			value="${session}" disabled></div>
			<div>${error}</div>

	<textarea rows="30" cols="70" style="border: none;"><c:out
			value="${soap}" /></textarea>

	<form action="import.do">
		<select name="offices">
			<c:forEach items="${offices}" var="office">
				<option value="${office.code}">${office.name}</option>
			</c:forEach>
		</select> 
		<input type="submit" value="getEmployees" name="category" /> 
		<input type="submit" value="getMaterials" name="category" /> 
		<input type="submit" value="getProjects" name="category" />
		<input type="submit" value="getRelations" name="category" />
		<input type="submit" value="getHourTypes" name="category" />
	</form>
</body>
</html>