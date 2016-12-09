/*
**
*ytkim
*varsql ui js
 */
;(function($, window, document, VARSQL) {
"use strict";

var _ui=VARSQL.ui||{};

_ui.base ={
	dto:$.extend({}, VARSQL.datatype , {})
	,constants:$.extend({}, VARSQL.constants , {})
};

_ui.options={
	dbtype:''
	,urlPrefix:''
	,param:{}
	,hiddenArea : '#dbHiddenArea'
	,downloadForm : '#downloadForm'
	,getUriPrefix:function (uri){
		return '/'+this.urlPrefix+(uri?uri:'');
	}
};

_ui.create = function (_opts){
	var _self = this; 
	
	_self.headerMenu.init(_opts);
	_self.leftDbObject.create(_opts);
	_self.layout.init(_opts);
}

// header 메뉴 처리.
_ui.headerMenu ={
	init : function(){
		var _self = this;
		
		_self.initEvt();
	}
	,initEvt : function (){
		
		$('body').on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				if (evt.shiftKey) {
					switch (evt.keyCode) {
						case 83: // keyCode 83 is s
							$('.sql-save-btn').trigger('click');
							break;
						default:
							break;
					}
				}
			}
		});
		
		$('.db-header-menu-wrapper .header-menu-item').on('click', function (e){
			var dataMenuItem = $(this).attr('data-menu-item');
			var menuArr = dataMenuItem.split('_');
			
			var depth1 =menuArr[0]
				,menu_mode =menuArr[1]; 
			
			switch (depth1) {
				case 'file': {
					
					switch (menu_mode) {
						case 'save':
							$('.sql-save-btn').trigger('click');
							break;
						case 'close':
							var isInIFrame = (window.location != window.parent.location);
							if(isInIFrame==true){
								parent.userMain.activeClose();
							}else {
								if(confirm('창을 닫으시겠습니까?')){
									window.close();
								}
							}
							
							break;
						default:
							break;
					}
					
					break;
				}case 'edit':{
					alert('['+menu_mode+'] 준비중입니다.');
					return ; 
					switch (menu_mode) {
						case 'compare':
							break;
						default:
							break;
					}
					break;
				}case 'tool':{
					alert('['+menu_mode+'] 준비중입니다.');
					return ; 
					switch (menu_mode) {
						case 'import':
							break;
						case 'export':
							break;
						case 'setting':
							break;
						default:
							break;
					}
					break;
				} default:
					break;
			}
			
		})
	}
}

// main layout 처리.
_ui.layout = {
	layoutObj :{
		mainLayout :false
		, leftLayout : false
		, rightLayout : false
	}
	,init : function(_opts){
		var _self = this; 
		_self.setLayout();
		_self.initEvt();
	}	
	,initEvt : function (){
		
	}
	,setLayout: function (){
		var _self = this; 
		_self.layoutObj.mainLayout = $('body').layout({
			center__paneSelector:	".ui-layout-center-area"
			, west__paneSelector:	".ui-layout-left-area"
			, north__paneSelector: ".ui-layout-header-area"
			, south__paneSelector : ".ui-layout-footer-area"
			, west__size:	300 
			, spacing_open:			5 // ALL panes
			, spacing_closed:   8 // ALL panes
			, north__spacing_open: 0
			, south__spacing_open: 0
			, resizerDblClickToggle: false
			, center__onresize: function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				_self.layoutObj.rightLayout.resizeAll(obj1, obj2 ,obj3 ,obj4 ,obj5);
			} 
			, west__onresize: function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				_self.layoutObj.leftLayout.resizeAll(obj1, obj2 ,obj3 ,obj4 ,obj5);
			}
		});
		
		_self.layoutObj.leftLayout = $('div.ui-layout-left-area').layout({
			north__paneSelector: ".ui-layout-left-top-area"
			, center__paneSelector: ".ui-layout-left-middle-area"
			, south__paneSelector: ".ui-layout-left-bottom-area"
			, north__size:    38
			, north__resizable: false
			, north__spacing_open: 0
			, south__size:    150
			, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
			, spacing_closed:   8  // ALL panes
			, resizerDblClickToggle: false
			, center__onresize_end:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					if($('.db-metadata-area.show-display').length > 0){
						$.pubGrid('#left_service_menu_content>#'+$('.db-metadata-area.show-display').attr('id')).resizeDraw({width:obj3.layoutWidth,height:obj3.layoutHeight-24});
					}
				}catch(e){
					console.log(arguments)
				}
			}
			,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					if($('.varsql-meta-cont-ele.on').length > 0){
						$.pubGrid('#'+$('.varsql-meta-cont-ele.on').attr('id')).resizeDraw({width:obj3.resizerLength,height:obj3.layoutHeight});
					}
				}catch(e){
					console.log(arguments)
				}
			}
		});

		_self.layoutObj.rightLayout = $('div.ui-layout-center-area').layout({
			north__paneSelector: ".inner-layout-toolbar-area"
			, center__paneSelector: ".inner-layout-sql-editor-area"
			, south__paneSelector: ".inner-layout-result-area"
			, north__size:    65
			, north__resizable: false
			, south__size:    100 
			, spacing_open:   5  // ALL panes  //0 일경우 버튼 사라짐.
			, spacing_closed:   8  // ALL panes
			, north__spacing_open: 0
			, resizerDblClickToggle: false
			, center__onresize:  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				$('.CodeMirror.cm-s-default').css('height' ,obj3.layoutHeight);
			}
			,south__onresize_end :  function (obj1, obj2 ,obj3 ,obj4 ,obj5){
				try{
					if($('#dataGridArea .pubGrid-body-container').length > 0){
						$.pubGrid('#dataGridArea').resizeDraw({width:obj3.resizerLength,height:obj3.css.height-25});
					}
				}catch(e){
					console.log(e)
				}
			}
		});
		
		$('.CodeMirror.cm-s-default').css('height' ,$('#editorAreaTable').height());
		VARSQL.ui.SQL.sqlTextAreaObj.refresh();
	}
}

// 왼쪽 영역 처리.
_ui.leftDbObject ={
	options :{
		selector:'#leftDbObjectWrap'
		,active: null
		,db_object_list:[]
		,param:{}
	}
	,create:function (_opts){
		var _self = this;
		
		if(!_opts.dbtype) {
			alert('dbtype empty');
			return ;
		}
		
		$.extend(true,_ui.options, _opts);
		$.extend(true,_self.options, _opts);
		
		_self._grid();
		_ui.SQL.init({dbtype:_opts.dbtype});
		
		_self.initEvt();
		
		_self._userSettingInfo();
	}
	// init left event 
	,initEvt : function (){
		var _self = this;
		
		// schema refresh button 
		$('.refresh-schema-btn').on('click', function (e){
			if(_self.options.active){
				_self._click(_self.options.active.attr('obj_nm'));
			}
		})
		
	}
	// db schema 그리기
	,_grid:function (){
		var _self = this;
		
		var data = _self.options.db_object_list;
		var len = data.length; 
	
		if(len < 1) return ; 
	
		var strHtm = [];
		var item; 
		for (var i=0; i<len ; i++ ){
			item = data[i];
			strHtm.push('<li><a href=\"javascript:;\" class=\"db-list-group-item\" obj_nm='+item+'>'+item+'</a></li>');
		}
									
		$(_self.options.selector).html(strHtm.join(''));
		
		if(len > 1){
			$('.db-schema-list-btn').show();
		}
		
		$(_self.options.selector+' .db-list-group-item').on('click', function (){
			if(_self.options.active) _self.options.active.removeClass('active');
			_self.options.active =$(this);
			_self.options.active.addClass('active');
			_ui.options.param.schema =_self.options.active.attr('obj_nm');
			$('#varsql_schema_name').html(_ui.options.param.schema);
			_self._click(this);
		});
		
		$(_self.options.selector+' .db-list-group-item[obj_nm="'+_ui.options.connInfo.schema+'"]').trigger('click');
		
	}
	// 사용자 셋팅 정보 가져오기.
	,_userSettingInfo : function (){
		var _self = this;
		var params = _ui.options.param;
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#db-page-wrapper'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/userSettingInfo.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (res){
		    	if(res.sqlInfo){
		    		var sqlInfo = res.sqlInfo;
		    		$('#sql_id').val(sqlInfo.SQL_ID);
		    		$('#saveSqlTitle').val(sqlInfo.GUERY_TITLE);
		    		_ui.SQL.getTextAreaObj().setValue(sqlInfo.QUERY_CONT);
		    	}else{
		    		$('#saveSqlTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyymmdd')+'query');
		    	}
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
		});  
	}
	// 스키마 클릭. 
	,_click:function (obj){
		var _self = this;
		var tmpParam = _self.options.param;
		tmpParam.schema = $(obj).attr('obj_nm');
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#left_service_menu_content'
		    ,url:{gubun:VARSQL.uri.database, url:'/serviceMenu.varsql'}
		    ,dataType:'json'
		    ,data:tmpParam
		    ,success:function (resData){
		    	_ui.leftDbObjectServiceMenu.create(
		    		$.extend({},{param:tmpParam} , resData)
		    	);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
};

/*
 * 왼족 메뉴 셋팅
 * 테이블 , 스키마 , view 등등 
 */
_ui.leftDbObjectServiceMenu ={
	metadataCache:false
	,metaGridHeight :150
	,options :{
		selector:'#leftServiceMenu'
		,menuData:[]
		,param:{}
		,contentAreaId:'#left_service_menu_content'
		,metadata_content_area_wrapId:'#metadata_content_area_wrap'
		,metadata_content_area_wrapEle:null
		,metadata_content_areaId:'#metadata_content_area'
		,metadata_content_areaIdEle:null
	}
	,_metaCacheGubun : ''
	// 왼쪽 메뉴 생성 . 
	,create: function (options){
		var _self = this; 
		
		$.extend(true,_self.options, options);
		
		_self._initCacheObject();
		_self.initElement();
	
		_self._tabs();
	}
	,_initCacheObject : function (){
		this.metadataCache = {
			'table':{}
			,'view':{}
			,'procedure':{}
			,'function':{}
		}
	}
	,initElement :function (){
		var _self = this;
		_self.options.metadata_content_area_wrapEle = $(_self.options.metadata_content_area_wrapId);
	}
	,getMetaContentWrapEle:function (){
		return this.options.metadata_content_area_wrapEle; 
	}
	// 왼쪽 상단 텝 메뉴 그리기
	,_tabs : function (){
		var _self = this; 
	
		var data = _self.options.menuData;
		var len = data.length; 
	
		if(len < 1) return ; 
		var item; 
	
		var htmStr = new Array();
		for (var i=0; i<len ; i++ ){
			item = data[i];
			htmStr.push('<li class="service_menu_tab ui-state-default ui-corner-top" contentid="'+item.contentid+'" contenturl="'+item.contentid+'"><a href="#tabs-3" class="left-menu-ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-3">'+item.name+'</a></li>');
		}
		
		var beforeSelectContentId = _self.options.active?_self.options.active.attr('contentid'):'';
		$(_self.options.contentAreaId).empty();
		$(_self.options.selector).empty();
		$(_self.options.selector).html(htmStr.join(''));
		beforeSelectContentId=beforeSelectContentId?beforeSelectContentId:$($('.service_menu_tab')[0]).attr('contentid');
		
		$(_self.options.selector+' .service_menu_tab').on('click', function (){
			var sObj = $(this);
			_self._off();
			_self.options.active =sObj;
			_self._on();
			var refresh = sObj.attr('refresh')=='Y'?true:false; 
			sObj.attr('refresh','N');
			
			_self._dbObjectList(sObj, refresh);
		});
		
		$($('.service_menu_tab[contentid='+beforeSelectContentId+']')).trigger('click');
		
		_self._serviceMenuContextMenu();
	}
	,_serviceMenuContextMenu : function (){
		var _self = this; 
		$.pubContextMenu('.service_menu_tab', {
			items:[
			       {key:'refresh' ,name: '새로고침'},
    		]
			,callback:function (key){
	    		var sObj = this.element;
	    		
	        	if(key=='refresh'){
	        		_self._removeMetaCache();
	        		sObj.attr('refresh','Y').trigger('click');
	        	}
	    	}
		});
	}
	// 텝 메뉴 활성 지우기
	,_off : function (){
		var _self = this; 
		if(_self.options.active) _self.options.active.removeClass('ui-state-active');
	}
	// 텝메뉴 활성 시키기
	,_on : function (){
		var _self = this; 
		if(_self.options.active) _self.options.active.addClass('ui-state-active');
	}
	// 메타 데이타 케쉬된값 꺼내기
	,_getMetaCache:function (gubun, key){
		var t =this.metadataCache[gubun][key]; 
		return t?t:null;
	}
	// 메타 데이타 셋팅하기.
	,_setMetaCache:function (gubun, key ,data){
		this.metadataCache[gubun][key]= data;  
	}
	,_removeMetaCache:function (gubun, key){
		
		if(typeof gubun !='undefined' && typeof key != 'undefined'){
			delete this.metadataCache[gubun][key];  
		}else if(typeof gubun !='undefined'){
			delete this.metadataCache[gubun];
		}else{
			this._initCacheObject();
		}
	}
	// 클릭시 테메뉴에 해당하는 메뉴 그리기
	,_dbObjectList:function(selObj,refresh){
		var _self = this;
		var $contentId = selObj.attr('contentid');
		
		var activeObj = $(_self.options.contentAreaId+'>#'+$contentId);
		
		$(_self.options.contentAreaId+'>'+' .show-display').removeClass('show-display');
		
		
		if(activeObj.length > 0){
			activeObj.addClass('show-display');
			if(refresh){
				activeObj.empty();
			}else{
				return ; 
			}
		}else{
			$(_self.options.contentAreaId).append('<div id="'+$contentId+'" class="db-metadata-area show-display"></div>');
		}
		
		VARSQL.req.ajax({      
		    type:"POST"  
		    ,url:{gubun:VARSQL.uri.database, url:'/dbObjectList.varsql'}
		    ,dataType:'json'
		    ,data:$.extend(true,_self.options.param,{'gubun':$contentId}) 
		    ,success:function (resData){
		    	_self['_'+$contentId].call(_self,resData);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 클릭시 텝메뉴에 해당하는 메뉴 그리기
	,_dbObjectMetadataList:function(param,callback,refresh){
		var _self = this;
		
		if(!refresh){
			var cacheData = _self._getMetaCache(param.gubun,param.name);
		
			if(cacheData){
				_self[callback].call(_self,cacheData, param);
				return ; 
			}
		}
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,url:{gubun:VARSQL.uri.database, url:'/dbObjectMetadataList.varsql'}
		    ,dataType:'json'
		    ,async:false
		    ,data:param
		    ,success:function (resData){
		    	_self._setMetaCache(param.gubun,param.name, resData); // data cache
		    	_self[callback].call(_self,resData, param);
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 컨텍스트 메뉴 sql 생성 부분 처리 .
	,_createScriptSql :function (scriptObj){
		_ui.SQL.addCreateScriptSql(scriptObj);
	}
	/**
	 * @method _createDDL
	 * @param name 
	 * @param val 
	 * @param options 
	 * @description create ddl
	 */	
	,_createDDL :function (sObj){
		var _self = this; 
		
		var param =$.extend({},_self.options.param,{'gubun':'table','name':sObj.objName})
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,url:{gubun:VARSQL.uri.database, url:'/createDDL.varsql'}
		    ,dataType:'json'
		    ,data:param
		    ,success:function (resData){
		    	if(sObj.scriptType=='ddl_copy'){
		    		_ui.text.copy(resData.result);
		    	}else{
		    		_ui.SQL.addSqlEditContent(resData.result);
		    	}
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});
	}
	// 데이타 내보내기
	,_dataExport : function (exportObj){
		_ui.SQL.exportDataDownload(exportObj);
	}
	// 테이블 정보보기.
	,_tables:function (resData, reqParam){
		var _self = this;
		try{
    		var len = resData.result?resData.result.length:0;
    		var strHtm = [];
    		
			var itemArr = resData.result;
			var item;
			
			var tableHint = {};
			$.each(itemArr , function (_idx, _item){
				tableHint[_item.TABLE_NAME] = {
					colums:[]
					,text :_item.TABLE_NAME
				};
			})
			
			// 테이블 hint;
//			CodeMirror.commands.autocomplete = function(cm) {
//			    CodeMirror.showHint(cm, CodeMirror.hint.sql, { 
//			        tables: tableHint
//			    } );
//			}
			
			// 테이블 hint;
			VARSQLHints.setTableInfo( tableHint);
			
			$.pubGrid(_self.options.contentAreaId+'>#tables',{
				height:'auto'
				,autoResize :false
				,tColItem : [
					{key :'TABLE_NAME', label:'Table', width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowClick : function (idx, item){
					var sObj = $(this);
					
	    			var refresh = sObj.attr('refresh')=='Y'?true:false; 
	    			sObj.attr('refresh','N');
	    			
	    			$('.table-list-item.active').removeClass('active');
	    			sObj.addClass('active');
	    			
	    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'table','name':item.TABLE_NAME}), '_tableMetadata', refresh);
				}
				,rowContextMenu :{
					beforeSelect :function (){
						$(this).trigger('click');
					}
					,callback: function(key,sObj) {
						var ele = this.element, sItem = this.gridItem;
						var gubun='table'
							,tmpName = sItem.TABLE_NAME;
						
						if(key=='refresh'){
							_self._removeMetaCache(gubun,tmpName);
							ele.attr('refresh','Y');
							ele.trigger('click.pubgridrow');
							return ; 
						}
						
						var cacheData = _self._getMetaCache(gubun,tmpName);
						
						if(key=='ddl_copy' || key=='ddl_paste'){
							_self._createDDL({
								scriptType : key
								,gubun : 'table'
								,objName :  tmpName 
								,item : cacheData
							});
							return ;
						}
						
						if(key=='export_data'||key=='export_column'){
							_self._dataExport({
								gubun:gubun
								,downloadType:key
								,objName :  tmpName 
								,item : cacheData
							});
							return ;
						}
						
						key = sObj.mode;
						
						_self._createScriptSql({
							scriptType : key
							,gubun : 'table'
							,objName :  tmpName 
							,item : cacheData
							,param_yn: sObj.param_yn
						});
					},
					items: [
						{key : "refresh" , "name": "새로고침"}
						,{key : "sql_create", "name": "sql생성" 
							,subMenu: [
								{ key : "selectStar","name": "select *" , mode: "selectStar"}
								,{ key : "select","name": "select column" ,mode:"select"}
								,{ key : "insert","name": "insert" , mode:"insert"}
								,{ key : "update","name": "update" ,mode:"update"}
								,{ key : "delete","name": "delete" ,mode:"delete"}
								,{ key : "drop","name": "drop" , mode:"drop"}
							]
						}
						,{key : "create_ddl_top","name": "DDL 보기" 
							,subMenu:[
								{key : "ddl_copy","name": "복사하기"}
								,{key : "ddl_paste","name": "edit 영역에보기"}
							]
						}
						,{key : "mybatis-sql_create","name": "mybatis Sql생성" 
							,subMenu : [
								{ key : "mybatis_insert","name": "insert" ,mode:"insert" ,param_yn:'Y'}
								,{ key : "mybatis_update","name": "update" ,mode:"update" ,param_yn:'Y'}
								,{ key : "mybatis_delete","name": "delete" ,mode:"delete",param_yn:'Y'}
							]
						}
						,{key :'export', "name": "내보내기" 
							,subMenu:[
								{key : "export_data","name": "데이타 내보내기"}
								,{key : "export_column","name": "컬럼정보 내보내기"}
							]
						}
					]
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//테이블에 대한 메타 정보 보기 .
	,_tableMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
			var colArr = [];
			$.each(colData.result , function (i , item){
				colArr.push(item.COLUMN_NAME);
			});
			
			VARSQLHints.setTableColumns(reqParam.name ,colArr);
			
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', key: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', key: 'TYPE_NAME_SIZE' },
					{ label: '널여부', key: 'IS_NULLABLE',width:45},
					{ label: '키여부', key: 'KEY_SEQ',width:45},
					{ label: '설명', key: 'REMARKS',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'table');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//view 정보 보기.
	,_views:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.result;
			
			$.pubGrid(_self.options.contentAreaId+'>#views',{
				headerView:true
				,height:'auto'
				,autoResize :false
				,tColItem : [
					{key :'TABLE_NAME', label:'View', width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowClick : function (idx, item){
					var sObj = $(this);
	    			$('.view-list-item.active').removeClass('active');
	    			sObj.addClass('active');
	    			
	    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'view','name':item.TABLE_NAME}), '_viewMetadata');
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// view 메타 데이타 보기.
	,_viewMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', key: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', key: 'TYPE_NAME_SIZE' },
					{ label: '널여부', key: 'IS_NULLABLE',width:45},
					{ label: '키여부', key: 'KEY_SEQ',width:45},
					{ label: '설명', key: 'REMARKS',width:45}
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'view');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_procedures:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.result;
			
			$.pubGrid(_self.options.contentAreaId+'>#procedures',{
				headerView:true
				,height:'auto'
				,autoResize :false
				,tColItem : [
					{key :'PROCEDURE_NAME', label:'Procedure',width:200, sort:true}
					,{key :'REMARKS', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowClick : function (idx, item){
					var sObj = $(this);
	    			
	    			$('.procedure-list-item.active').removeClass('active');
	    			sObj.addClass('active');
	    			
	    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'procedure','name':item.PROCEDURE_NAME}), '_procedureMetadata');
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	//테이블에 대한 메타 정보 보기 .
	,_procedureMetadata :function (colData ,reqParam){
		var _self = this;
		
		try{
    		var gridObj = {
    			data:colData.result
    			,column : [
					{ label: '컬럼명', key: 'COLUMN_NAME',width:80 },
					{ label: '데이타타입', key: 'TYPE_NAME_SIZE' },
					{ label: '널여부', key: 'NULLABLE',width:45},
					{ label: '설명', key: 'REMARKS',width:45},
				]
    		};
			
    		_self.setMetadataGrid(gridObj, 'procedure');
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	,_functions:function (resData ,reqParam){
		var _self = this;
		try{
			var itemArr = resData.result;
    				
			$.pubGrid(_self.options.contentAreaId+'>#functions',{
				headerView:true
				,height: _self.metaGridHeight
				,tColItem : [
					{key :'FUNCTION_NAME', label:'Function',width:200, sort:true}
					,{key :'FUNCTION_TYPE', label:'설명'}
				]
				,tbodyItem :itemArr
				,rowClick : function (idx, item){
					var sObj = $(this);
	    			
	    			$('.function-list-item.active').removeClass('active');
	    			sObj.addClass('active');
	    			
	    			_self._dbObjectMetadataList($.extend({},_self.options.param,{'gubun':'function','name':sObj.attr('function_nm')}), '_functionMetadata');
				}
			});
 		}catch(e){
			VARSQL.log.info(e);
		}
	}
	// 메타 데이타 그리기.
	,setMetadataGrid :function (gridObj, type){
		var _self = this;
		//_self.getMetaContentWrapEle().empty();
		//_self.getMetaContentWrapEle().html('<div id="'+_self.options.metadata_content_areaId.replace('#', '')+'"></div>');
		
		var tmpEle = $(_self.options.metadata_content_area_wrapId+type);
		
		if(!tmpEle.hasClass('on')){
			$('.varsql-meta-cont-ele.on').removeClass('on');
			tmpEle.addClass('on');
		}
		
		if(tmpEle.length < 1){
			_self.getMetaContentWrapEle().append('<div id="'+ (_self.options.metadata_content_area_wrapId+type).replace('#', '') +'" class="varsql-meta-cont-ele on"></div>');
		}
		
		$.pubGrid(_self.options.metadata_content_area_wrapId+type, {
			headerOptions : {
				redraw : false
			}
			,height:'auto'
			,autoResize :false
			,tColItem : gridObj.column
			,tbodyItem :gridObj.data
		});
		
		
	}
	//tab 종료 값 얻기
	,_getContentId:function (){
		return this.options.active.attr('contentid');
	}
	//db url call 할때 앞에 uri 뭍이기
	,_getPrefixUri:function (uri){
		return _ui.options.getUriPrefix(uri);
	}
};

/**
 * sql 데이타 그리드
 */
_ui.SQL = {
	sqlTextAreaObj:null
	,resultMsgAreaObj:null
	,dataGridSelectorWrapObj:null
	,memoDialog : null
	,options :{
		selector:'#sqlExecuteArea'
		,dataGridSelector:'#dataGridArea'
		,dataGridSelectorWrap:'#dataGridAreaWrap'
		,resultMsgAreaWrap:'#resultMsgAreaWrap'
		,dataGridResultTabWrap:'#data_grid_result_tab_wrap'
		,preloaderArea :'#sqlEditerPreloaderArea'
		,limitCnt:'#limitRowCnt'
		,vconnidObj:'#vconnid'
		,active: null
		,dbtype:'db2'
		,cancel: "input,textarea,button,select,option"
		,distance: 1
		,delay: 0
	}
	//SQL ui 초기화 
	,init:function (options){
		var _self = this; 
		if(options && !options.dbtype) {
			alert('dbtype empty');
			return ;
		}
		$.extend(true,_self.options, options);
		_self._createHtml();
		_self._initEditor('text/x-sql');
		_self._initEvent();
	}
	,_createHtml :function(){
		var _self = this;
		
		var resultTabHtm = [],resultGridHtm=[];
		
		// data grid result tab
		resultTabHtm.push('<ul id="data_grid_result_tab" class="sql-result-tab">');
		resultTabHtm.push('	<li tab_gubun="result" class="on"><a href="javascript:;">결과</a></li>');
		resultTabHtm.push('	<li tab_gubun="msg"><a href="javascript:;">메시지</a></li>');
		resultTabHtm.push('</ul>');
		
		$(_self.options.dataGridResultTabWrap).html(resultTabHtm.join(''));
		
		// data grid araea
		resultGridHtm.push('<div id="dataGridArea" class="sql-result-area on" tab_gubun="result"></div>');
		resultGridHtm.push('<div id="resultMsgAreaWrap" class="sql-result-area" tab_gubun="msg"></div>');
		$(_self.options.dataGridSelectorWrap).html(resultGridHtm.join(''));
	}
	,_initEditor : function (mime){
		var _self = this;
		
		_self.sqlTextAreaObj = CodeMirror.fromTextArea(document.getElementById(_self.options.selector.replace('#', '')), {
			mode: mime,
			indentWithTabs: true,
			smartIndent: true,
			lineNumbers: true,
			height:'auto',
			 lineWrapping: true,
			matchBrackets : true,
			autofocus: true,
			extraKeys: {"Ctrl-Space": "autocomplete"},
			hintOptions: {tables: {
				users: {name: null, score: null, birthDate: null},
				countries: {name: null, population: null, size: null}
			}}
		});
	}
	//이벤트 초기화 
	,_initEvent :function (){
		var _self = this; 
	
		var textareaObj = $('.CodeMirror.cm-s-default');
		
		textareaObj.on('keydown',function (e) {
			var evt =window.event || e; 
			
			if(evt.ctrlKey){
				if (evt.keyCode == 13) { // keyCode 13 is Enter
					$('.sql-execue-btn').trigger('click');
					return false; // preventing default action
				}
				
				if (evt.altKey) { // keyCode 78 is n
					switch (evt.keyCode) {
						case 78:
							$('.sql-new-file').trigger('click');
							break;
						default:
							break;
					}
				}
				
				if (evt.shiftKey) { // keyCode 70 is f
					switch (evt.keyCode) {
						case 70:
							$('.sql-format-btn').trigger('click');
							break;
						case 83: // keyCode 83 is s
							$('.sql-save-btn').trigger('click');
							break;
						case 88: // toUpperCase
							var sCursor = _self.getTextAreaObj().getCursor(true)
							,eCursor = _self.getTextAreaObj().getCursor(false);
						
							_self.getTextAreaObj().replaceSelection(_self.getSql().toUpperCase());
							_self.getTextAreaObj().setSelection(sCursor, eCursor);
							break;
						case 89: // toLowerCase
							var sCursor = _self.getTextAreaObj().getCursor(true)
							,eCursor = _self.getTextAreaObj().getCursor(false);
						
							_self.getTextAreaObj().replaceSelection(_self.getSql().toLowerCase());
							_self.getTextAreaObj().setSelection(sCursor, eCursor);
							break;
						default:
							break;
					}
					
					return false; 
				}
			}
		});
		
		// sql 실행
		$('.sql-execue-btn').on('click',function (evt){
			_self.sqlData(evt);
		});
		
		// sql 보내기
		$('.sql-send-btn').on('click',function (evt){
			_self.sqlSend(evt);
		});
		
		// sql 포멧 정리.
		$('.sql-format-btn').on('click',function (){
			_self.sqlFormatData();
		});
		
		// sql 정보 저장. 
		$('.sql-save-btn').on('click',function (e){
			_self.saveSql();
		});
		
		$('#sql-save-list-no').keydown(function(e) {
			if (e.keyCode == '13') {
				_self.sqlSaveList($('#sql-save-list-no').val());
			}
		});
		
		$('#saveSqlSearch').keydown(function(e) {
			if (e.keyCode == '13') {
				_self.sqlSaveList(1);
			}
		});
		
//		$('#recvUserSearch').keydown(function(e) {
//			if (e.keyCode == '13') {
//				$('#recvUserSearchBtn').trigger('click');
//			}
//		});
		$('#recvUserSearch').autocomplete({
			source : function( request, response ) {
				var params = { searchVal : request.term };
				
				VARSQL.req.ajax({      
				    type:"POST"
				    ,url:{gubun:VARSQL.uri.user, url:'/searchUserList.varsql'}
				    ,dataType:'json'
				    ,data: params
				    ,success:function (data){
				    	//서버에서 json 데이터 response 후 목록에 뿌려주기 위함 VIEWID,UID,UNAME
				    	
				    	var result = [];
				    	$.each(data.items , function (idx,item){
				    		result.push({
								label: item.UNAME+'('+item.UID+')',
								value: item.VIEWID
							})
				    	})
				    	
						response(result	);
					}
					,error :function (data, status, err){
						VARSQL.log.error(data, status, err);
					}
					,beforeSend: _self.loadBeforeSend
					,complete: _self.loadComplete
				});  
			},
			//조회를 위한 최소글자수
			minLength: 2,
			select: function( event, ui ) {
				var strHtm = [];
				var sItem = ui.item;
				
				if($('.recv_id_item[_recvid="'+sItem.value+'"]').length > 0 ) return false;
				
				strHtm.push('<div class="recv_id_item" _recvid="'+sItem.value+'">'+sItem.label);
				strHtm.push('<a href="javascript:;" class="pull-right">X</a></div>');
				$('#recvIdArr').append(strHtm.join(''));
				
				$('.recv_id_item[_recvid="'+sItem.value+'"] a').on('click', function (){
					$(this).closest('[_recvid]').remove();
				})
				
				this.value ='';
				
				return false; 
			}
		});
		
		// sql 정보 목록 이동. 
		$('.sql-list-move-btn').on('click',function (e){
			var mode = $(this).attr('_mode');
			
			var pageNo = $('#sql-save-list-no').val();
			var lastPage = $('#sql-save-list-pagecnt').html(); 
			
			pageNo = parseInt(pageNo, 10);
			if(mode=='p'){
				pageNo = pageNo -1; 
			}else{
				pageNo = pageNo +1;
			}
			
			if(pageNo > 0 && pageNo <= lastPage){
				_self.sqlSaveList(pageNo);
				return ; 
			}
		});
		
		
		$('.sql-save-list-btn').dropdown();
		$('.sql-save-list-btn').on('click',function (){
			
			if($('.sql-save-list-layer').attr('loadFlag') != 'Y'){
				$('.varsql-dropdown').addClass('on');
				_self.sqlSaveList();
			}
		});
		
		$('.sql-new-file').on('click',function (){
			$('#sql_id').val('');
			$('#saveSqlTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_query');
			_self.getTextAreaObj().setValue('');
		});
		
		$(_self.options.dataGridResultTabWrap+' [tab_gubun]').on('click',function (){
			var sObj = $(this);
			
			$(_self.options.dataGridResultTabWrap+' [tab_gubun]').removeClass('on');
			sObj.addClass('on');
			
			// data grid araea
			$(_self.options.dataGridSelectorWrap +' [tab_gubun]').removeClass('on');
			$(_self.options.dataGridSelectorWrap +' [tab_gubun='+sObj.attr('tab_gubun')+']').addClass('on');
		});
	}
	// save sql
	,saveSql : function (){
		var _self = this; 
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :_self.getTextAreaObj().getValue()
			,'sqlTitle' : $('#saveSqlTitle').val()
			,'sql_id' : $('#sql_id').val()
		});
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/saveQuery.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (res){
		    	$('#sql_id').val(res.sql_id);
		    	_self.sqlSaveList();
		    	//$(_self.options.preloaderArea +' .preloader-msg').html('저장되었습니다.');
		    	
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	// sql 보내기.
	,sqlSend :function (){
		var _self = this;
		var sqlVal = _self.getSql();
		
		sqlVal=$.trim(sqlVal);
		
		$('#memoTitle').val(VARSQL.util.dateFormat(new Date(), 'yyyy-mm-dd HH:MM')+'_제목');
		$('#memoContent').val(sqlVal);
		
		if(_self.memoDialog==null){
			_self.memoDialog = $('#memoTemplate').dialog({
				height: 350
				,width: 640
				,modal: true
				,buttons: {
					"보내기":function (){
						var recvEle = $('.recv_id_item[_recvid]');
						
						if(recvEle.length < 1) {
							alert('보낼 사람을 선택하세요.');
							return ; 
						}
						
						if(!confirm('보내기 시겠습니까?')) return ; 
						
						var recv_id = [];
						$.each(recvEle,function (i , item ){
							recv_id.push($(item).attr('_recvid'));
						});

						var params = {
							'memo_title' : $('#memoTitle').val()
							,'memo_cont' : $('#memoContent').val()
							,'recv_id' : recv_id.join(';;')
						};
						
						VARSQL.req.ajax({      
						    type:"POST" 
						    ,loadSelector : '#editorAreaTable'
						    ,url:{gubun:VARSQL.uri.user, url:'/sendSql.varsql'}
						    ,dataType:'json'
						    ,data:params 
						    ,success:function (resData){
						    	_self.memoDialog.dialog( "close" );
							}
							,error :function (data, status, err){
								VARSQL.log.error(data, status, err);
							}
							,beforeSend: _self.loadBeforeSend
							,complete: _self.loadComplete
						});
					}
					,Cancel: function() {
						_self.memoDialog.dialog( "close" );
					}
				}
				,close: function() {
					_self.memoDialog.dialog( "close" );
				}
			});
		}
		
		_self.memoDialog.dialog("open");
		
		$('#recvIdArr').html('');
		
	}
	// 저장된 sql 목록 보기.
	,sqlSaveList : function (pageNo){
		var _self = this; 
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			searchVal : $('#saveSqlSearch').val()
			,page : pageNo ? pageNo : $('#sql-save-list-no').val()
			,countPerPage : 5
		});
		
		VARSQL.req.ajax({
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlList.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (res){
		    	var items = res.items;
		    	var paging = res.paging;
		    	var strHtm = []
		    		,len = items.length;
		    	
		    	if(items.length > 0){
		    		for(var i =0 ;i <len; i++){
		    			var item = items[i];
		    			strHtm.push('<li _idx="'+i+'"><a href="javascript:;" class="save-list-item" _mode="view">'+item.GUERY_TITLE+'&nbsp;</a>');
		    			strHtm.push('<a href="javascript:;" class="pull-right save-list-item" _mode="del">삭제</a></li>');
		    		}
		    	}else{
		    		strHtm.push('<li>no data</li>')
		    	}
		    	
		    	$('#saveSqlList').empty().html(strHtm.join(''));
		    	
		    	$('.sql-save-list-layer').attr('loadFlag' ,'Y');
		    	$('#sql-save-list-no').val(paging.currPage); 
		    	$('#sql-save-list-pagecnt').html(paging.totalPage); 
		    	$('#sql-save-list-totalcnt').html(paging.totalCount); 
		    	
		    	$('#saveSqlList .save-list-item').on('click', function (e){
		    		var sObj = $(this)
		    			, mode = sObj.attr('_mode')
		    			, idx = sObj.closest('[_idx]').attr('_idx');
		    		
		    		var sItem =items[idx]; 
		    		
		    		if(mode=='view'){
		    			$('#sql_id').val(sItem.SQL_ID);
		    			$('#saveSqlTitle').val(sItem.GUERY_TITLE);
		    			_self.getTextAreaObj().setValue(sItem.QUERY_CONT);
		    			$('.sql-save-list-btn').trigger('click');
		    		}else{
		    			if(!confirm('['+sItem.GUERY_TITLE + '] 삭제하시겠습니까?')){
		    				return ; 
		    			}
		    			params['sql_id'] = sItem.SQL_ID;
		    			VARSQL.req.ajax({
		    			    type:"POST"
		    			    ,loadSelector : '#editorAreaTable'
		    			    ,url:{gubun:VARSQL.uri.sql, url:'/base/delSqlSaveInfo.varsql'}
		    			    ,dataType:'json'
		    			    ,data:params 
		    			    ,success:function (res){
		    			    	_self.sqlSaveList();
		    			    }
		    			});
		    		}
		    	})
		    	
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	//텍스트 박스 object
	,getTextAreaObj:function(){
		return this.sqlTextAreaObj; 
	}
	//data grid area
	,getDataGridWrapObj:function(){
		var _self = this; 
		
		if(_self.dataGridSelectorWrapObj==null){
			_self.dataGridSelectorWrapObj = $(_self.options.dataGridSelectorWrap );
		}
		return _self.dataGridSelectorWrapObj;
	}
	//result message area
	,getResultMsgAreaObj:function(){
		var _self = this; 
		
		if(_self.resultMsgAreaObj==null){
			_self.resultMsgAreaObj = $(_self.options.resultMsgAreaWrap);
		}
		return _self.resultMsgAreaObj; 
	}
	,getSql: function (mode){
		var _self = this;
		var textObj = _self.getTextAreaObj(); 
		
		return textObj.getSelection()
	}
	// sql 데이타 보기 
	,sqlData :function (evt){
		var _self = this;
		var sqlVal = _self.getSql();
		
		sqlVal=$.trim(sqlVal);
		if('' == sqlVal){
			sqlVal  = _self.getTextAreaObj().getValue();
		}
		
		if(''== sqlVal) return ; 
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :sqlVal
			,'limit' : $(_self.options.limitCnt).val()
		});
		
		VARSQL.req.ajax({      
		    type:"POST" 
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlData.varsql'}
		    ,dataType:'json'
		    ,data:params 
		    ,success:function (resData){
		    	try{
		    				    		
		    		var resultLen = resData.length;
		    		
		    		if(resultLen < 1 ){
		    			resData.data = [{result:"데이타가 없습니다."}];
		    			resData.column =[{name:'result',key:'result', align:'center'}];
		    			_self.setGridData(resData);
		    		}else{
		    			var item, msgViewFlag = false;
		    			
		    			for(var i=0; i < resultLen; i++){
		    				item = resData[i];
		    				if(item.viewType=='grid'){
		    					_self.setGridData(item);
		    				}
		    				if(item.resultType=='FAIL' || item.viewType=='msg'){
		    					msgViewFlag = true;
		    				}
		    				_self.getResultMsgAreaObj().prepend('<div class="'+(item.resultType=='FAIL'?'error-log-message':'success-log-message')+'">'+item.resultMessage+'</div>');
		    				
		    				_self.getResultMsgAreaObj().animate({scrollTop: 0},'fast');
		    			}
		    			
		    			if(msgViewFlag){
		    				$(_self.options.dataGridResultTabWrap+" [tab_gubun=msg]").trigger('click');
		    			}else{
		    				$(_self.options.dataGridResultTabWrap+" [tab_gubun=result]").trigger('click');
		    			}
		    		}
		 		}catch(e){
					VARSQL.log.info(e);
				}		             
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	,sqlFormatData :function (){
		var _self = this;
		var sqlVal = _self.getSql('pos');
		var tmpEditor =_self.getTextAreaObj(); 
		sqlVal=$.trim(sqlVal);
		
		var startSelection , endSelection;
		
		if('' == sqlVal){
			startSelection = {line:0,ch:0};
			tmpEditor.setSelection(startSelection, {line:10000,ch:0});
			sqlVal  = tmpEditor.getValue();
		}else{
			startSelection = tmpEditor.listSelections()[0].anchor;
			endSelection = tmpEditor.listSelections()[0].head;
			
			if(startSelection.line > endSelection.line){
				startSelection  = endSelection;
			}else if(startSelection.line == endSelection.line && startSelection.ch > endSelection.ch){
				startSelection  = endSelection;
			}
		}
		
		if(''== sqlVal) return ; 
		
		var params =VARSQL.util.objectMerge (_ui.options.param,{
			'sql' :sqlVal
		});
		
		VARSQL.req.ajax({      
		    type:"POST"
		    ,loadSelector : '#editorAreaTable'
		    ,url:{gubun:VARSQL.uri.sql, url:'/base/sqlFormat.varsql'}
		    ,dataType:'text'
		    ,data:params 
		    ,success:function (res){
		    	var linecnt = VARSQL.matchCount(res,_ui.base.constants.newline);
	    		tmpEditor.replaceSelection(res);
	    		tmpEditor.setSelection(startSelection, {line:startSelection.line+linecnt,ch:0});
			}
			,error :function (data, status, err){
				VARSQL.log.error(data, status, err);
			}
			,beforeSend: _self.loadBeforeSend
			,complete: _self.loadComplete
		});  
	}
	// export data download
	,exportDataDownload : function (exportInfo){
		var key = exportInfo.downloadType
		,gubun = exportInfo.gubun
		,tmpName = exportInfo.objName
		,data = exportInfo.item;
		
		var dataArr = data.result;
		var len = dataArr.length;
		
		var strHtm = [];
		strHtm.push("	<table style=\"100%\" >");
		strHtm.push("		<colgroup>");
		strHtm.push("			<col width=\"310px\" />");
		strHtm.push("			<col width=\"20px\" />");
		strHtm.push("			<col width=\"*\" />");
		strHtm.push("		</colgroup>");
		strHtm.push("		<tr>");
		strHtm.push("			<td>");
		strHtm.push("			  <div style=\"height:225px;overflow-x:hidden;overflow-y:auto;\">");
		strHtm.push("				<table style=\"vertical-align: text-top;\" class=\"table table-striped table-bordered table-hover dataTable no-footer\" id=\"dataTables-example\">");
		strHtm.push("					<thead>");
		strHtm.push("						<tr role=\"row\">");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 20px;\"><input type=\"checkbox\" name=\"columnCheck\" value=\"all\">all</th>");
		strHtm.push("							<th tabindex=\"0\" rowspan=\"1\" colspan=\"1\" style=\"width: 150px;\">Column</th>");
		strHtm.push("						</tr>");
		strHtm.push("					</thead>");
		strHtm.push("					<tbody class=\"dataTableContent1\">");
		var item;
		for(var i=0; i < len; i++){
			item = dataArr[i];
			strHtm.push("						<tr class=\"gradeA add\">	");
			strHtm.push("							<td class=\"\"><input type=\"checkbox\" name=\"columnCheck\" value=\""+item.COLUMN_NAME+"\"></td>	");
			strHtm.push("							<td class=\"\">"+item.COLUMN_NAME+"</td>	");
			strHtm.push("						</tr>");
		}
		strHtm.push("					</tbody>");
		strHtm.push("				</table>");
		strHtm.push("			  </div>");
		strHtm.push("			</td>");
		strHtm.push("			<td></td>");
		strHtm.push("			<td style=\"vertical-align: text-top;\">");
		strHtm.push("				<div>");
		strHtm.push("					<label class=\"control-label\">LIMIT COUNT</label>");
		strHtm.push("					<input class=\"\" id=\"exportCount\" name=\"exportCount\" value=\"1000\">");
		strHtm.push("				</div>");
		strHtm.push("				<ul>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"csv\" checked></span>CSV</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"json\"></span>JSON</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"insert\"></span>INSERT문</li>");
		strHtm.push("					<li><span><input type=\"radio\" name=\"exportType\" value=\"xml\"></span>XML</li>");
		strHtm.push("				</ul>");
		strHtm.push("			</td>");
		strHtm.push("		</tr>");
		strHtm.push("	</table>");
		
		var modalEle = $('#data-export-modal'); 
		if(modalEle.length > 0){
			modalEle.empty();
			modalEle.html(strHtm.join(''));
		}else{
			$(_ui.options.hiddenArea).append('<div id=\"data-export-modal\" title="데이타 내보내기">'+strHtm.join('')+'</div>');
			modalEle = $('#data-export-modal'); 
		}
		
		var checkAllObj = $("input:checkbox[name='columnCheck'][value='all']"); 
		checkAllObj.on('click',function (){
			VARSQL.check.allCheck($(this),"input:checkbox[name='columnCheck']");
		});
		
		checkAllObj.trigger('click');
		
		modalEle.dialog({
			height: 350
			,width: 640
			,modal: true
			,buttons: {
				"내보내기":function (){
					if(!confirm('내보내기 하시겠습니까?')) return ; 

					var params =VARSQL.util.objectMerge (_ui.options.param,{
						exportType : VARSQL.check.radio('input:radio[name="exportType"]')
						,columnInfo : VARSQL.check.getCheckVal("input:checkbox[name='columnCheck']:not([value='all'])").join(',')
						,name: tmpName
						,limit: $('#exportCount').val()
					});

					VARSQL.req.download(_ui.options.downloadForm, {
						type: 'post'
						,url: {gubun:VARSQL.uri.sql, url:'/base/dataExport.varsql'}
						,params:params
					});
				}
				,Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
			,close: function() {
			  $( this ).dialog( "close" );
			}
		});
	}
	// 스크립트 내보내기
	,addCreateScriptSql :function (scriptInfo){
		var _self = this;
		var key = scriptInfo.scriptType
			,gubun = scriptInfo.gubun
			,tmpName = scriptInfo.objName
			,data = scriptInfo.item
			,param_yn  = scriptInfo.param_yn;
		
		param_yn = param_yn?param_yn:'N';
		
		var reval =[];
		
		var dataArr = data.result, tmpval , item;
		
		var len = dataArr.length;
		
		reval.push(_ui.base.constants.newline); // 첫라인 줄바꿈으로 시작.
		// select 모든것.
		if(key=='selectStar'){
			reval.push('select * from '+tmpName);
			
		}
		// select 컬럼 값
		else if(key=='select'){
			reval.push('select ');
			for(var i=0; i < len; i++){
				item = dataArr[i];
				reval.push((i==0?'':',')+item.COLUMN_NAME);
			}
			
			reval.push(' from '+tmpName);

		}
		// insert 문
		else if(key=='insert'){
			reval.push('insert into '+tmpName+' (');
			var valuesStr = [];
			for(var i=0; i < len; i++){
				item = dataArr[i];
				if(i!=0){
					reval.push(',');
					valuesStr.push(',');
				}
				reval.push(item.COLUMN_NAME);
				
				valuesStr.push(queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE));
				
			}
			reval.push(' )'+_ui.base.constants.newline +'values( '+ valuesStr.join('')+' )');
			
		}
		// update 문
		else if(key=='update'){
			reval.push('update '+tmpName+_ui.base.constants.newline+' set ');
			
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				
				tmpval = queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE);
				
				if(item.KEY_SEQ =='YES'){
					keyStr.push(item.COLUMN_NAME+ ' = '+ tmpval);
				}else{
					if(!firstFlag){
						reval.push(',');
					}
					reval.push(item.COLUMN_NAME+ ' = '+ tmpval);
					firstFlag = false; 
				}
			}
			
			if(keyStr.length > 0) reval.push(_ui.base.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// delete 문
		else if(key=='delete'){
			reval.push('delete from '+tmpName);
			
			var item;
			var keyStr = [];
			var firstFlag = true; 
			
			for(var i=0; i < len; i++){
				item = dataArr[i];
				if(!firstFlag){
					reval.push(',');
				}
				
				tmpval = queryParameter(param_yn, item.COLUMN_NAME , item.DATA_TYPE);
				
				if(item.KEY_SEQ =='YES'){
					keyStr.push(item.COLUMN_NAME+ ' = '+ tmpval);
				}
			}
			
			if(keyStr.length > 0) reval.push(_ui.base.constants.newline+'where '+keyStr.join(' and '));
			
		}
		// drop 문
		else if(key=='drop'){
			reval.push('drop table '+tmpName);
		}
		
		_self.addSqlEditContent(reval.join(''));
	}
	// 에디터 영역에 값 넣기.
	,addSqlEditContent :function (cont){
	
		var _self = this
			,insVal = cont +_ui.base.constants.querySuffix;
		
		var editObj =_self.getTextAreaObj()
			,insLine = editObj.lastLine(); 
		editObj.replaceRange(insVal, CodeMirror.Pos(insLine));
		editObj.setSelection({line:insLine+1,ch:0}, {line:editObj.lastLine()+1,ch:0});
		editObj.focus();
		
	}
	/*
	,loadBeforeSend :function (){
		
	}
	,loadComplete :function (){
		
	}
	*/
	// sql data grid
	,setGridData: function (pGridData){
		var _self = this; 
		
		$.pubGrid(_self.options.dataGridSelector,{
			headerView:true
			,height:'auto'
			,autoResize :false
			,resizeGridWidthFixed : true
			,tColItem : pGridData.column
			,tbodyItem :pGridData.data
		});
		
		return ; 
		var labelClick= function (){
			var sObj = $(this);
			sObj.unbind('click');
			
			try{
				$($('.ui-grid-col-header-select').parent()).html($('.ui-grid-col-header-select').val());
			}catch(e){}

			var labelHtm = '<input type="text" class="ui-grid-col-header-select" value="'+sObj.text()+'"/>';
			var labelObj = $(labelHtm);
						
			sObj.html(labelObj);

			labelObj.blur(function (){
				sObj.text(labelObj.val());
				sObj.bind('click',labelClick);
			});

			labelObj.select();
		};
		$('.ui-grid-cols-select').click(labelClick);
	}
};

_ui.progress = {
	start:function (divObj){
		try{
			var obj = $(divObj);
			
			var modalcls = divObj.replace(/^[.#]/, '');
			
			$(divObj).prepend('<div class="'+modalcls+'dialog-modal transbg" style="position:absolute;z-index:100000;text-align:center;border:1px solid;background: #CCC; filter:alpha(opacity=50); -moz-opacity:0.5; opacity: 0.5;display:table-cell;vertical-align:middle"><span><span style="font-weight:bold;background: #fff;">기다리시오....인내심을 가지고..</span></span></div>');
			
			$("."+modalcls +'dialog-modal > span').css('line-height',obj.outerHeight() +'px');
			$("."+modalcls +'dialog-modal').css('width',obj.outerWidth());
			$("."+modalcls +'dialog-modal').css('height',obj.outerHeight());
			$("."+modalcls +'dialog-modal').show();
		}catch(e){
			alert(e);
		}
	},
	end :function (divObj){
		try{
			$('.'+divObj.replace(/^[.#]/, '') +'dialog-modal').hide();
		}catch(e){
			alert(e);
		}
	}
};


/**
 * textCopy 띄우기
 */
_ui.text={
	clipboardObj :false
	,clipBoardEle : false
	,copy :function (copyString){
		var _this = this; 
		
		var strHtm = [];
		
		var modalEle = $('#data-copy-modal'); 
		if(modalEle.length > 0){
			$('#data-copy-area').empty();
		}else{
			$(_ui.options.hiddenArea).append('<div id=\"data-copy-modal\" title="복사"><div><pre id="data-copy-area"></pre></div></div>');
			modalEle = $('#data-copy-modal'); 
		}
		
		$('#data-copy-area').html(copyString);
		
		modalEle.dialog({
			height: 350
			,width: 640
			,modal: true
			,buttons: {
				"복사":function (){
					clipboard.copy(copyString);
					$( this ).dialog( "close" );
				}
				,Cancel: function() {
					$( this ).dialog( "close" );
				}
			}
			,close: function() {
			  $( this ).dialog( "close" );
			}
		});

	}
}

function queryParameter(flag, colName, dataType){
	if(flag=='Y'){
		return _ui.base.constants.queryParameterPrefix+colName+_ui.base.constants.queryParameterSuffix;
	}else{
		return _ui.base.dto[dataType].val; 
	}
}

VARSQL.ui = _ui;
}(jQuery, window, document,VARSQL));