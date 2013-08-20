<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
					  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
					  
<%-- Direktiven fuer Page-Optionen und verwendete Tag-Libs --%>
<%@page import="types.ThreadStates"%>
<%@page import="controller.ThreadMonitor"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<jsp:useBean id="queryName" class="java.lang.String" scope="session"/>

    
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>OSMarelmon - Scheduled</title>
	<link rel="shortcut icon" href="osm-favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="osmarelmon.css"/>
</head>
<body>

	<h1 class="content-header">Query scheduled at OSMarelmon.</h1>
	<div class="content-body">
		
		<div class="hint-text">
		This page will update itself after your query has been executed and 
		downloaded from OverpassAPI.
		If you do not want to wait for the result and add a new query click here
		 <a href="Controller?action=start">here</a> to return 
		to the main page. <br/>
		I am sorry to say that this feature doesn't work properly yet, so you will 
		NOT be informed if your query has successfully been added. I am still  
		trying to figure out what causes this problem. Please return manually to
		the <a href="Controller?action=start">main page</a> and update that page
		so you can see if your query has been added.
				<%
					ThreadStates state = ThreadMonitor.getThreadState(queryName);
				%>
				<%if(state==ThreadStates.SUCCEEDED){%>
				<meta http-equiv="refresh" content="0; URL=Controller?action=result"/> 
				<%} else if(state==ThreadStates.FAILED){%>
				<meta http-equiv="refresh" content="0; URL=Controller?action=error"/> 
				<%}else{ %> 
				<meta http-equiv="refresh" content="5; URL=Controller?action=scheduled"/> 
				<%}%>			
		</div>

	</div>


	<!-- W3C symbols for standard co<noscript><meta http-equiv="refresh" content="0; url=news-nojs.php"></noscript>mpliance -->
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