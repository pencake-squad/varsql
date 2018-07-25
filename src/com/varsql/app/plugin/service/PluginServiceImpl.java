package com.varsql.app.plugin.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.plugin.dao.PluginDAO;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;

/**
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PluginServiceImpl.java
* @DESC		: 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2018. 7. 24. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class PluginServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);
	
	@Autowired
	private PluginDAO pluginDAO; 
	
	/**
	 * 
	 * @Method Name  : glossarySearch
	 * @Method 설명 : 용어 검색.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 24. 
	 * @변경이력  :
	 * @param param
	 * @return
	 */
	public ResponseResult glossarySearch(ParamMap param) {
		ResponseResult result = new ResponseResult();
		
		param.put("keyword", param.getString("keyword").toUpperCase());
		result.setItemList(pluginDAO.selectGlossarySearch(param));
		return result;
	}
}