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
	<title>OSMarelmon - Help</title>
	<link rel="shortcut icon" href="osm-favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="osmarelmon.css"/>
</head>
<body>
	<h1 class="content-header">OSMarelmon - Help</h1>
	<div class="content-body">
	OSMarelmon lets you monitor OpenStreetMap relations. Therefore you have to 
	specify the relations you want to monitor. This can be done by typing 
	key-value pairs into the textfields. For example if you want to monitor the 
	public transport of Passau, you have to enter the key "operator" and the 
	value "Stadtwerke Passau". This query would return all relations which are
	maintained by Stadtwerke Passau. <br/>
	To complete your query you have to enter a name for it and press the "Add
	relation" button. This name must not be used before (you can check the used 
	names by checking the "Already monitored relations").<br/>
	Once your query has been accepted you can access an RSS feed which informs 
	you about updates at your query.<br/>
	The "OSMarelmon" feed informs you when new relations are added. <br/>
	<br/>

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