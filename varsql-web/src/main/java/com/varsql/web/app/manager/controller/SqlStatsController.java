package com.varsql.web.app.manager.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.app.manager.service.SqlStatsServiceImpl;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VarsqlParamConstants;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.utils.HttpUtils;



/**
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: SqlStatsController.java
* @DESC		: sql 통계
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 8. 20. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/manager/stats")
public class SqlStatsController extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(SqlStatsController.class);

	@Autowired
	SqlStatsServiceImpl sqlStatsServiceImpl;

	@Autowired
	ManagerCommonServiceImpl dbnUserServiceImpl;

	/**
	 *
	 * @Method Name  : dbSqlDateStats
	 * @Method 설명 : 날짜별 통
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconid
	 * @param s_date
	 * @param e_date
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbSqlDateStats"})
	public @ResponseBody ResponseResult dbSqlDateStats(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true) String vconid
			,@RequestParam(value = VarsqlParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = VarsqlParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			) throws Exception {
		
		return sqlStatsServiceImpl.dbSqlDateStats(vconid, s_date, e_date);
	}

	/**
	 *
	 * @Method Name  : dbSqlDayStats
	 * @Method 설명 : 일별  sql command count
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconid
	 * @param s_date
	 * @param e_date
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbSqlDayStats"})
	public @ResponseBody ResponseResult dbSqlDayStats(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true) String vconid
			,@RequestParam(value = VarsqlParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = VarsqlParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			) throws Exception {

		return sqlStatsServiceImpl.dbSqlDayStats(vconid, s_date, e_date);
	}

	/**
	 *
	 * @Method Name  : dbSqlDayUserRank
	 * @Method 설명 : 일 별 사용자  rank
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconid
	 * @param s_date
	 * @param e_date
	 * @param command_type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/dbSqlDayUserRank"})
	public @ResponseBody ResponseResult dbSqlDayUserRank(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true) String vconid
			,@RequestParam(value = VarsqlParamConstants.SEARCH_START_DATE, required = true, defaultValue = "" )  String s_date
			,@RequestParam(value = VarsqlParamConstants.SEARCH_END_DATE, required = true, defaultValue = "" )  String e_date
			,@RequestParam(value = "command_type", required = false,defaultValue = "" )  String command_type
			) throws Exception {

		return sqlStatsServiceImpl.dbSqlDayUserRank(vconid, s_date, e_date ,command_type);
	}
	/**
	 * @Method Name  : logList
	 * @Method 설명 :
	 * @작성자   : ytkim
	 * @작성일   : 2018. 7. 26.
	 * @변경이력  :
	 * @param vconid
	 * @param s_date
	 * @param e_date
	 * @param command_type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/logList"})
	public @ResponseBody ResponseResult logList(HttpServletRequest req) throws Exception {

		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);

		return sqlStatsServiceImpl.selectLogSearch(searchParameter);
	}

}
