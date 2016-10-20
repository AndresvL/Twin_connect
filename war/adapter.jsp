<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="OAuth.do">
<label>Consumer token</label>
<input type="text" name="token" value ="818784741B7543C7AE95CE5BFB783DF2" required autofocus >
<label>Consumer secret</label>
<input type="text" name="secret" value ="F441FB65B6AA42C995F9FAF3662E8A10" required >
<label>link</label>
<input type="text" name="link" value="https://login.twinfield.com/oauth/initiate.aspx" required >
<input type="submit" value="Get Access"/>
</form>
</body>
</html>