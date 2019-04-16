package com.varsql.app.common.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.app.common.beans.ExceptionLogVO;
import com.varsql.app.common.dao.CommonDAO;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CommonServiceImpl.java
* @DESC		: 공통 서비스 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Service
public class CommonServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	@Autowired
	private CommonDAO commonDAO;
	
	/**
	 * 
	 * @Method Name  : insertExceptionLog
	 * @Method 설명 : error insert
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 16. 
	 * @변경이력  :
	 * @param exceptionType
	 * @param e
	 */
	public void insertExceptionLog(String exceptionType, Throwable e) {
		try{
			ExceptionLogVO  exceptionLogVO = new ExceptionLogVO(exceptionType, e); 
			commonDAO.insertExceptionLog(exceptionLogVO);
		}catch(Throwable e1) {
			logger.error("insertExceptionLog Cause : "+ e1.getMessage() ,e1);
		}
	}
	
}