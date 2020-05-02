package com.varsql.web.common.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.vartech.common.app.beans.ParamMap;

/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: CommonDAO.java
* @DESC		: 공통 dao
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2018. 12. 18. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Repository
public class CommonDAO extends BaseDAO{

	public Object selectDetailObject(Map paramMap) {
		return getSqlSession().selectOne("commonMapper.selectDetailObject", paramMap);
	}

	/**
	 *
	 * @Method Name  : selectManagerCheck
	 * @Method 설명 : 매니저 권한 체크.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 21.
	 * @변경이력  :
	 * @param exceptionLogVO
	 * @return
	 */
	public int selectManagerCheck(ParamMap param) {
		return getSqlSession().selectOne("commonMapper.selectManagerCheck", param);
	}

}
