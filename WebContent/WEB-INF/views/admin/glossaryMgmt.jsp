<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<!-- Page Heading -->
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="admin.menu.glossary" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="row display-off" id="varsqlViewArea">
	<div class="col-xs-6">
		<div class="panel panel-default">
			<!-- /.panel-heading -->
			<div class="panel-body">
				
				<div class="row">
					<div class="col-xs-12">
						
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="dataTables_filter">
							<label style="float:left; margin-right: 5px;"><select v-model="list_count" @change="search()" class="form-control input-sm"><option
									value="10">10</option>
								<option value="25">25</option>
								<option value="50">50</option>
								<option value="100">100</option></select>
							</label>
							<div class="input-group floatright">
								<input type="text" v-model="searchVal"	class="form-control">
								<span class="input-group-btn">
									<button class="btn btn-default" @click="search()" type="button">
										<span class="glyphicon glyphicon-search"></span>
									</button>
								</span>
							</div>
							
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<div id="dataTables-example_wrapper"
						class="dataTables_wrapper form-inline" role="grid">
						<table
							class="table table-striped table-bordered table-hover dataTable no-footer"
							id="dataTables-example">
							<thead>
								<tr role="row">
									<th style="width: 195px;">
										<spring:message	code="admin.detail.word" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="admin.detail.word_en" />
									</th>
									<th style="width: 150px;">
										<spring:message	code="admin.detail.word_abbr" />
									</th>
									<th style="width: 179px;">
										<spring:message	code="view_dt" />
									</th>
								</tr>
							</thead>
							<tbody class="dataTableContent">
								<tr v-for="(item,index) in gridData" class="gradeA" :class="(index%2==0?'add':'even')">
									<td><a href="javascript:;" @click="viewItem(item)"> {{item.WORD}} </a></td>
									<td class="center">{{item.WORD_EN}}</td>
									<td class="center">{{item.WORD_ABBR}}</td>
									<td class="center">{{item.REG_DT}}</td>
								</tr>
								<tr v-if="gridData.length === 0">
									<td colspan="10"><div class="text-center"><spring:message code="msg.nodata"/></div></td>
								</tr>
							</tbody>
						</table>
						
						<page-navigation :page-info="pageInfo" callback="goPage"></page-navigation>
					</div>
				</div>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<div class="col-xs-6">
		<div class="panel panel-default">
			<div class="panel-heading"><spring:message code="detail.view" /></div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form id="addForm" name="addForm" class="form-horizontal" onsubmit="return false;">
					<div class="form-group">
						<div class="col-sm-12">
							<div class="form-control text required">{{detailItem.WORD}}</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<textarea class="form-control text required" rows="10" readonly="readonly">{{detailItem.MEMO_CONT}}</textarea>
						</div>
					</div>
					
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<script>

VarsqlAPP.vueServiceBean( {
	el: '#varsqlViewArea'
	,data: {
		list_count :10
		,searchVal : ''
		,pageInfo : {}
		,gridData :  []
		,detailItem :{}
	}
	,methods:{
		deleteMsg : function(){
			var _self = this; 
			var selectItem = _self.selectItem;
			
			if(VARSQL.isDataEmpty(selectItem)){
				VARSQL.alert('<spring:message code="msg.data.select" />');
				return ; 
			}
			
			if(!confirm('삭제 하시겠습니까?')){
				return ; 
			}
			
			var param = {
				message_type : _self.message_type
				,selectItem:selectItem.join(',')
			};
			
			this.$ajax({
				data:param
				,url:{gubun:VARSQL.uri.user, url:'/preferences/deleteMsg'}
				,success:function (response){
					_self.search();
				}
			});
		}
		,viewItem : function (item){
			this.detailItem = item;
		}
		,search : function(no){
			var _self = this; 
			
			var param = {
				page:no?no:1
				,message_type : _self.message_type
				,rows: _self.list_count
				,'searchVal':_self.searchVal
			};
			
			this.$ajax({
				url:{gubun:VARSQL.uri.user, url:'/preferences/listMsg'}
				,data : param
				,success: function(resData) {
					_self.gridData = resData.items;
					_self.pageInfo = resData.page;
				}
			})
		}
	}
});
</script>
