<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>

<jsp:include page="./otherHead.jsp" flush="true"></jsp:include>
<div class="col-xs-3">
	<jsp:include page="./otherLeft.jsp" flush="true"></jsp:include>
	<jsp:include page="../dbcomm/dbLeftTemplate.jsp" flush="true"></jsp:include>
</div>

<jsp:include page="../dbcomm/dbRightLayoutTemplate.jsp" flush="true"></jsp:include>
	
