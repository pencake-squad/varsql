<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<style>
.left-panel {
	margin-bottom: 5px;
}

.left-panel.padding2{
	padding :2px 2px;
}

</style>

<div class="panel panel-default left-panel" style="min-height:600px;">
	<div class="panel-default left-panel padding2">
		<div id="dbSchemaList">
		</div>
	</div>

	<div class="ui-tabs ui-widget">
		<ul id="varsqlDbServiceMenu" style="margin-right: 2px;">
			<li>db를 선택하시오.</li>
		</ul>
		<div id="dbServiceMenuContent" style="height:180px;"></div>
		<div id="metadata_content_area_wrap"><div id="metadata_content_area"></div></div>
	</div>
</div>

