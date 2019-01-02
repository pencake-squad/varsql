<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/tagLib.jspf"%>
<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1);
%>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header"><spring:message code="user.edit.pasword" /></h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<div class="page-cont row">
	<div class="col-xs-12 fill">
		<div class="panel panel-default">
			<div class="panel-body">
				<div class="row">	
					<div class="col-sm-12">
						<div class="pull-right margin-bottom5">
							<button type="button" class="btn btn-default save-btn">
								<i class="fa fa-save"></i><spring:message code="btn.save"/>
							</button>
						</div>
					</div>
				</div>
				
				<div>
					<form id="passwordResetForm" class="form-horizontal required-validate" method="post" onsubmit="return false;">
						<div class="form-group">
							<label class="col-lg-2 control-label"><spring:message code="user.form.password.current"/></label>
							<div class="col-lg-10">
								<input type="password"  id="currPw" name="currPw" value="" class="form-control text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><spring:message code="user.form.password.new" /></label>
							<div class="col-lg-10">
								 <input type="password" id="upw" name="upw" value="" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><spring:message code="user.form.password.confirm"/></label>

				            <div class="col-sm-10">
				                <input type="password" id="confirmUpw" name="confirmUpw" value="" class="form-control"/>
				            </div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<script>

$(document).ready(function (){
	passwordMain.init();
});

var passwordMain = {
	init:function(){
		var _self = this;
		_self.initEvt();
	}
	,initEvt : function (){
		var _self = this;
		
		$('.save-btn').on('click',function (){
			$('#passwordResetForm').submit();
		});
		
		$('#passwordResetForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				currPw: {
	                validators: {
	                    notEmpty: { message: '필수 입력사항입니다.'}
	                }
	            }
				,upw : {
	                validators: {
	                	 notEmpty: { message: '필수 입력사항입니다.'}
                    	,stringLength: {min: 4, max: 500, message: '최소 4글자 이상 이여야 합니다.' }
	                    ,identical: {
	                        field: 'confirmUpw',
	                        message: '비밀번호가 같아야합니다.'
	                    }
	                }
	            }
	            ,confirmUpw : {
	                validators: {
	                    notEmpty: {
	                        message: '필수 입력사항입니다.'
	                    }
	                    ,identical: {
	                        field: 'upw',
	                        message: '비밀번호가 같아야합니다.'
	                    }
	                }
	            }
			}
		}).on('success.form.bv', function(e) {
			// Prevent form submission
			e.preventDefault();
			
			_self.saveInfo();
		});
	}
	// 정보 저장.
	,saveInfo : function (){
		var _self = this;
		
		var params  =$('#passwordResetForm').serializeJSON();
		
		VARSQL.req.ajax({
			url: {type:VARSQL.uri.user, url:'/preferences/passwordSave'},
			cache: false,
			type:"post",
			data:params,
			dataType: "json",
			success: function(resData) {
				if(resData.messageCode=='valid'){
					var items = resData.items;
					objLen = items.length;
					if(objLen >0){
						var item;
						for(var i=0; i <objLen; i++){
							item = items[i];
							alert(item.field + "\n"+ item.defaultMessage)
							return ; 
						}
					}
				}
				
				if(resData.resultCode == 403){
					alert('현재 비밀번호가 맞지 않습니다');
					return ; 
				}
				
				alert('비밀번호가 변경되었습니다.');
				
				location.href= location.href;
			}
		});
	}
}


</script>