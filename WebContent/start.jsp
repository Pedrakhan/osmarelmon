<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
					  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
 
<%-- Direktiven fuer Page-Optionen und verwendete Tag-Libs --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="util.Constants"%>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="monRels" class="java.util.LinkedList" scope="request"/>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>OSMarelmon</title>
	<link rel="shortcut icon" href="osm-favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="osmarelmon.css"/>
</head>
<body>
	<h1 class="content-header">OSMarelmon - The OSM Relation Monitor</h1>
	<div class="content-body">

		<div class="hint-text">
			OSMarelmon monitors relations of OpenStreetMap. Monitored relations
			are periodically checked and the changes are distributed via RSS 
			feeds.<br/>
			To add a new relation to the monitor fill the form below. 
			To build a valid query first name your query.
			Then fill the key value fields. Make sure that you fill for each key 
			field the corresponding value field and vice versa. If you do not 
			need all rows leave them untouched. <a href="Controller?action=help">Help</a>
		<br/>
		</div>

		<form action="Controller?action=add" method="post" enctype="utf-8">
		
			<div class="formField">
				<label>Name your query</label>
				<input type="text" name="queryName"/>
			</div>
			<div class="formField">
				<input type="text" value="key" name="key1"/>
				<input type="text" value="value" name="value1"/>
			</div>
			<div class="formField">
				<input type="text" value="key" name="key2"/>
				<input type="text" value="value" name="value2"/>
			</div>
			<div class="formField">
				<input type="text" value="key" name="key3"/>
				<input type="text" value="value" name="value3"/>
			</div>
			<div class="formField">
				<input type="text" value="key" name="key4"/>
				<input type="text" value="value" name="value4"/>
			</div>
			<div class="formField"><button type="submit" style=" width : 189px;">
					Add relation to monitor
				</button>
			</div>
		</form>
		To subscribe to an RSS feed of any of the queries below, click the name of the query.
		To be informed when new relations are added to OSMarelmon, follow this link: <a href="Feed?action=OSMarelmon">OSMarelmon Feed <img src="rss.png"></img></a>
		<br/>
		<fieldset>
          <legend style="font-size: 18px; font-weight: bold;"> 
          Already monitored relations</legend>
		<div style="overflow: auto; height: 200px; width: 600px;">
          <br/>
          <ul style="list-style-position:inside">
		<c:forEach var="element" items="${monRels}">
					    <li><a href="Feed?action=${element}">${element} <img src="rss.png"></img></a><br/></li>
				</c:forEach>
		</ul>
	</div>
		</fieldset>
	</div>	
	All geographic data by <a href="http://www.openstreetmap.org/">OpenStreetMap</a>, available under <a href="http://opendatacommons.org/licenses/odbl/">ODbL</a>.
	
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