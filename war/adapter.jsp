<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Twinfield Connect</title>
</head>
<body>
	<form action="OAuth.do">
		<label>Consumer token</label> <input type="text" name="token"
			value="818784741B7543C7AE95CE5BFB783DF2" required autofocus>
		<label>Consumer secret</label> <input type="text" name="secret"
			value="F441FB65B6AA42C995F9FAF3662E8A10" required> <label>link</label>
		<input type="text" name="link" value="https://login.twinfield.com/oauth/initiate.aspx" required>
		<input type="submit" value="Get Access" />
	</form>
	<div>${session}</div>
	
	<textarea rows="30" cols="70"  style="border:none;"><c:out value="${soap}" /></textarea>
	
	<form action="import.do">
	    <input type="text" placeholder="code" name="code" required>
		<input type="submit" value="getUser" name="category"/>
		<input type="submit" value="getOffice" name="category"/>
		<input type="submit" value="getEmployees" name="category"/>
		<input type="submit" value="getMaterials" name="category"/>
		<input type="submit" value="getProjects" name="category"/>
		<input type="submit" value="getRelations" name="category"/>
		<input type="submit" value="getHourTypes" name="category"/>
	</form>
</body>
</html>