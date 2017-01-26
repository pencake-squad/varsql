<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<html>
<head>
<%@ include file="/WEB-INF/include/initvariable.jspf"%>
<title><spring:message code="database.preferences" /></title>
<%@ include file="/WEB-INF/include/head-preferences.jsp"%>

</head>

<BODY class="pub-main-body">
	<tiles:insertAttribute name="body" />
</BODY>
</html>

<script>
$('.pub-main-body').on('selectstart', function (e){
	return false; 	
})
</script>


