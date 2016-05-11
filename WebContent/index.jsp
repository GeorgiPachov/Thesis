<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="msvalidate.01" content="9697D823F4B764380ACF65358BC6E097" />
<title>Content analyzer 0.1</title>
</head>
<body
	style="max-width: 600px; margin-left: auto; margin-right: auto; padding-top: 100px">
	<h1>Content Analyser v 0.1</h1>
	<form action="/analyze" method="POST">
		Enter sentence to analyze: <br> <input type="text" name="input" />
		<input type="hidden" name="command" value="test" /> <br> <input
			type="submit" />
	</form>
	<br />
	<hr />
	<br />
	<form action="/analyze" method="POST">
		Enter brand to analyze opinions for:<br> <input type="text"
			name="input" /> <input type="hidden" name="command" value="analyze" />
		<br> <input type="submit" />
	</form>
	<div style="display: block">
		<br />
		<hr />
		<br /> Research project @ FMI, University of Sofia, 2015, Georgi
		Pachov (<a href="mailto:georgi.patchov@gmail.com">georgi.patchov@gmail.com</a>)
	</div>
	<div>
	<br />
	<hr />
	<br />
	<c:if test="${result!=null}">
		${result}
	</c:if>
	</div>
</body>
</html>