<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://bootstrapjsp.org/" prefix="b"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="msvalidate.01" content="9697D823F4B764380ACF65358BC6E097" />
<title>Content analyzer 0.1</title>
</head>
<body>
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
</body>
</html>