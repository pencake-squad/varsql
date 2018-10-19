/**
 * pubTab v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/

;(function ($, window, document) {
	"use strict";
    var pluginName = "pubTab"
		,_datastore = {}
		,defaults = {
			speed : 150
			,width:'auto'
			,overItemViewMode : 'drop'
			,dropItemHeight :'auto'		//drop item height
			,moveZIndex : 9				// move 영역 z- index
			,filter: function ($obj) {
				// Modify $obj, Do not return
			}
			,icon :{
				prev :'pubTab-left-arrow'
				,next : 'pubTab-right-arrow'
			}
			,activeIcon :{
				position : 'prev'		//  활성시 html 추가 위치
				,html : ''				// 활성시 추가할 html
				,click: false			// 클릭 이벤트.
			}
			,addClass : 'service_menu_tab'	// tab li 추가 클래스
			,items:[]							// tab item
			,click :function (item){			// tab click 옵션
				
			}
			,itemKey :{							// item key mapping
				title :'name'
			}
		};
        
    function Plugin(element, options) {
        this.selector = (typeof element=='object') ? element.selector : element;
		this.contextId = 'pubTab-'+new Date().getTime();
		this.element = $(element);

        options.width= isNaN(options.width) ?  this.element.width() : options.width;
        this.options = $.extend({}, defaults, options);
		
		this.init();
	
		return this; 
    }

	$(document).on('mousedown.pubtab', 'html', function (e) {
		if(e.which !==2 && $(e.target).closest('.pubTab-drop-item-wrapper').length < 1){
			$('.pubTab-move-area.pubTab-open').removeClass('pubTab-open');
		}
	});

	Plugin.prototype ={
		init :function(){
			var _this =this; 

			_this.config = {tabWidth :[]};
			_this.draw();

			_this.initEvt();

		}
		,initEvt : function (){
			var _this = this; 

			_this.element.on('click', '.pubTab-item-title',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item-cont');

				_this.element.find('.pubTab-item-cont.active').removeClass('active');
				itemEle.addClass('active')

				if($.isFunction(_this.options.click)){
					var tabIdx = itemEle.attr('data-tab-idx');

					_this.options.click.call(itemEle,_this.options.items[tabIdx])
				}
			})

			_this.element.on('click', '.pubTab-icon-area',function (e){
				var sEle = $(this)
					,itemEle = sEle.closest('.pubTab-item-cont');

				if($.isFunction(_this.options.activeIcon.click)){
					var tabIdx = itemEle.attr('data-tab-idx');

					_this.options.activeIcon.click.call(itemEle,_this.options.items[tabIdx])
				}
			})
			
			var prevElement = _this.element.find('.pubTab-prev')
				, nextElement = this.element.find('.pubTab-next');
			
			var prevTimerObj = null;
			prevElement.on( "mouseenter", function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				function movePrev(){
					_this.config.tabScrollElement.scrollLeft(scrollLeft-10);
					prevTimerObj = setTimeout(function(){
						scrollLeft = _this.config.tabScrollElement.scrollLeft();
						movePrev()
					}, _this.options.speed);
				}

				movePrev();	
			}).on( "mouseleave", function (e){
				clearTimeout(prevTimerObj);
			});
			
			var nextTimerObj = null;
			nextElement.on( "mouseenter", function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				function moveNext(){
					_this.config.tabScrollElement.scrollLeft(scrollLeft+10);
					nextTimerObj = setTimeout(function(){
						scrollLeft = _this.config.tabScrollElement.scrollLeft();
						moveNext()
					}, _this.options.speed);
				}
				
				prevElement.show();
				
				moveNext();	
			}).on( "mouseleave", function (e){
				clearTimeout(nextTimerObj);
			});


			if(_this.options.overItemViewMode =='drop'){

				_this.element.find('.pubTab-drop-open-btn').on('click', function (e){
					e.preventDefault();
					e.stopPropagation();

					var sEle = $(this)
						,tabArea=sEle.closest('.pubTab-move-area')
					
					if(tabArea.hasClass('pubTab-open')){
						tabArea.removeClass('pubTab-open');
					}else{
						tabArea.addClass('pubTab-open');
					}
				});

				_this.config.dropItemAreaElement.on('click', '.pubTab-drop-item',function (e){
					e.preventDefault();
					e.stopPropagation();

					var sEle = $(this)
						,dataIdx = sEle.data('tab-idx')
						,selItem =_this.config.tabWidth[dataIdx]; 

					_this.element.find('.pubTab-item-cont.active').removeClass('active');
					_this.element.find('.pubTab-item-cont[data-tab-idx="'+dataIdx+'"]').addClass('active');
					var itemEndPoint = selItem.leftLast+_this.config.moveAreaWidth+2; 
					
					var leftVal =0; 
					if(itemEndPoint > _this.config.width){
						leftVal = itemEndPoint - _this.config.width; 
					}else{

						var schLeft = _this.config.tabScrollElement.scrollLeft();
						if(schLeft > schLeft.leftFront){
							leftVal = schLeft.leftFront;
						}
					}
					_this.config.tabScrollElement.scrollLeft(leftVal);

					$(_this.element.find('.pubTab-move-area')).removeClass('pubTab-open');

					if($.isFunction(_this.options.click)){
						_this.options.click.call(this,_this.options.items[dataIdx])
					}		
				})
			}

			/*
			prevElement.on('click', function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				
				_this.config.tabScrollElement.scrollLeft(scrollLeft-10);

				if(scrollLeft-20 < 0){
					prevElement.hide();
				}

			})

			nextElement.on('click', function (e){
				var scrollLeft = _this.config.tabScrollElement.scrollLeft();
				_this.config.tabScrollElement.scrollLeft(scrollLeft+10);

				if(scrollLeft > 0){
					prevElement.show();
				}
			})
			*/
		}
		,itemClick : function (idx, addAttr){
			idx = isNaN(idx) ? 0 :idx; 
			addAttr = addAttr || {};
			var clickEle = $(this.element.find('.pubTab-item-cont').get(idx));
			
			for(var key in addAttr){
				clickEle.attr(key , addAttr[key]);
			}
			clickEle.find('.pubTab-item-title').trigger('click')
		}
		,refresh : function (){
			var _this = this; 
			var eleW = _this.element.width();

			_this.config.width = eleW;

			if(_this.config.totalWidth > eleW){
				$('#'+_this.contextId+'pubTab-move-space').show();
				_this.element.find('.pubTab-move-area').show();
			}else{
				_this.element.find('.pubTab-item-cont').removeClass('pubTab-hide');
				$('#'+_this.contextId+'pubTab-move-space').hide();
				_this.element.find('.pubTab-move-area').hide();
				_this.config.tabContainerElement.css('left', '0px');
			}

			return this; 
		}
		/**
		* set drop item height 
		*/
		,setDropHeight : function (h){
			if(isNaN(h)){
				return this
			}
			this.config.dropItemAreaElement.css('max-height',h+'px');
			return this; 
		}
		/**
		* set tab width 
		*/
		,setWidth : function (val){
			this.refresh();
			return this; 
		}
		,getSelectItem : function(){
			var sEle = this.element.find('.pubTab-item-cont.active'); 
			return this.options.items[sEle.data('tab-idx')];
		}
		,destroy:function (){
			
		}
		,draw : function (){
			var _this = this
				,_opts = _this.options
				,items = _opts.items
				,itemLen = items.length; 
			
			function tabItemHtml (){
				var tabHtm = [];
				var activeIcon =_opts.activeIcon; 
				var prevFlag = false;
				var addHtml = '';
				if(activeIcon && activeIcon.html != '') {
					prevFlag = activeIcon.position =='prev' ?true :false; 
					addHtml = '<span class="pubTab-icon-area">'+activeIcon.html+'</span>';
				}

				var titleKey = _opts.itemKey.title;
				var item;
				var itemHtm;
				for(var i = 0 ;i < itemLen ;i++){
					item = items[i];
					
					if(prevFlag){
						itemHtm = addHtml + '<span class="pubTab-item-title">'+item[titleKey]+'</span>';
					}else{
						itemHtm = '<span class="pubTab-item-title">'+item[titleKey]+'</span>'+addHtml;
					}
					
					tabHtm.push('<li class="pubTab-item '+(i+1==itemLen ? 'last':'')+'"><div class="pubTab-item-cont '+_opts.addClass+'" data-tab-idx="'+i+'">'+itemHtm+'</div></li>');
				}
				return tabHtm.join('');
			}

			function dropItemHtml (){
				var tabHtm = [];
				var titleKey = _opts.itemKey.title;
				for(var i = itemLen-1 ;i >= 0  ;i--){
					var item = items[i];
					tabHtm.push('<li class="pubTab-drop-item" data-tab-idx="'+i+'">'+item[titleKey]+'</li>');
				}
				return tabHtm.join('');
			}

			var strHtm = [];
			strHtm.push('<div class="pubTab-wrapper" style="'+(!isNaN(_opts.height) ? 'height:'+_opts.height+'px' :'')+'">');
			strHtm.push('	<div id="'+_this.contextId+'pubTab" class="pubTab">');
			strHtm.push('		<div id="'+_this.contextId+'pubTab-scroll" class="pubTab-scroll">');
			strHtm.push('			<ul id="'+_this.contextId+'pubTab-container" class="pubTab-container">');
			strHtm.push(tabItemHtml());
			strHtm.push('			<li><div id="'+_this.contextId+'pubTab-move-space"  style="display:none;">&nbsp;</div></li>');
			strHtm.push('			</ul>');
			strHtm.push('		</div> ');
			strHtm.push('		<div class="pubTab-move-area" style="z-index:'+_opts.moveZIndex+';">');

			
			strHtm.push('		<span class="pubTab-drop-open-btn">');
			strHtm.push('			<div class="pubTab-move-dim"></div>');
			strHtm.push('			<i class="pubTab-prev '+_opts.icon.prev+'"></i>');
			strHtm.push('			<i class="pubTab-next '+_opts.icon.next+'"></i>');
			if(_opts.overItemViewMode =='drop'){
				strHtm.push('		<div id="'+_this.contextId+'DropItem" class="pubTab-drop-item-wrapper"><ul class="pubTab-drop-item-area">'+dropItemHtml()+'</ul></div>');
			}

			strHtm.push('</span>');
			strHtm.push('		</div>');
			strHtm.push('	</div>');
			strHtm.push('</div>');

			_this.element.empty().html(strHtm.join(''));
					
			_this.config.tabContainerElement =  $('#'+_this.contextId+'pubTab-container');
			_this.config.tabScrollElement = $('#'+_this.contextId+'pubTab-scroll');
			_this.config.dropItemAreaElement = $('#'+_this.contextId+'DropItem');;
			_this.config.moveAreaWidth  = this.element.find('.pubTab-move-area').width();
			$('#'+_this.contextId+'pubTab-move-space').css('width',_this.config.moveAreaWidth);

			_this.calcItemWidth();
			_this.setWidth(_opts.width);
			_this.setDropHeight(_opts.dropItemHeight)
		}
		,calcItemWidth :function (){
			var _this =this;
			var containerW = 0; 
			_this.config.tabContainerElement.find('.pubTab-item').each(function(i , item){
				var itemW =$(item).outerWidth();
				containerW +=itemW;
				_this.config.tabWidth[i] = {
					itemW : itemW
					,leftFront : (containerW-itemW)
					,leftLast : containerW
				}
			});

			_this.config.totalWidth = containerW;
		}
		,setScrollInfo : function (){
			this.config.scroll = {
				width : this.config.tabContainerElement.width() - _this.config.tabScrollElement.width()	
			} 
		}
	};

    $[ pluginName ] = function (selector,options) {

		if(!selector){
			return ; 
		}

		var _cacheObject = _datastore[selector];

		if(typeof options === 'undefined'){
			return _cacheObject; 
		}
		
		if(!_cacheObject){
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
			return _cacheObject; 
		}else if(typeof options==='object'){
			_cacheObject.destroy();
			_cacheObject = new Plugin(selector, options);
			_datastore[selector] = _cacheObject;
			return _cacheObject; 
		}

		if(typeof options === 'string'){
			var callObj =_cacheObject[options]; 
			if(typeof callObj ==='undefined'){
				return options+' not found';
			}else if(typeof callObj==='function'){
				return _cacheObject[options].apply(_cacheObject,args);
			}else {
				return typeof callObj==='function'; 
			}
		}

		return _cacheObject;	
    };

})(jQuery, window, document);
