<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
					  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%-- Direktiven fuer Page-Optionen und verwendete Tag-Libs --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>OSMarelmon - Error</title>
	<link rel="shortcut icon" href="osm-favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="osmarelmon.css"/>
</head>
<body>
	<h1 class="content-header">OSMarelmon - Fehler</h1>
	<div class="content-body">
		
		<div class="error">
			An error occured<br/>
			${requestScope.errorMsg}<br/>
		</div>
		
		<%--
		 Einkommentieren, um Fehlermeldung detailierter zu erhalten.
		 Message: ${requestScope.cause.message} <br/>
		 Stacktrace:<br/>
		 <% 
		 	Exception cause = (Exception)request.getAttribute(model.actions.ErrorAction.ATTR_CAUSE);
		 	for (int i=0; i<cause.getStackTrace().length; i++) {
		 		out.write(cause.getStackTrace()[i] + "<br/>");
		 	}
		 %>
 		<br/>
 		<br/>
 		 --%>

		<a href="Controller?action=start">Back to main page</a>
	
	</div>
	
	
	<!-- W3C symbols for standard compliance -->
	<div class="validation_icons">
		<a href="http://validator.w3.org/check?uri=referer">
			<img class="w3icon" src="valid-xhtml11.png" 
			     alt="Valides XHTML 1.1"/>
		</a>
		<a href="http://jigsaw.w3.org/css-validator/">
			<img class="w3icon" src="valid-css.png"
	             alt="CSS ist valide!"/>
		</a>
	</div>
	
	
</body>
</html>