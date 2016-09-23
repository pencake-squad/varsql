package com.varsql.web.app.database;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.database.db2.Db2Controller;
import com.varsql.web.common.constants.UserConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.SecurityUtil;

/**
 * 
 * @FileName  : SQLController.java
 * @프로그램 설명 : databse관련 공통으로 처리할 controller
 * @Date      : 2015. 6. 22. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Controller
@RequestMapping("/database")
public class SQLController {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Db2Controller.class);
	
	@Autowired
	private SQLService sqlService;
	
	/**
	 * 
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 실행시 처리. 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param schema
	 * @param limit
	 * @param sql
	 * @param dbtype
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/base/sqlData",""})
	public @ResponseBody String sqlData(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true, defaultValue = "" )  String vconnid, 
			@RequestParam(value = VarsqlParamConstants.DB_SCHEMA, required = true, defaultValue = "" )  String schema,
			@RequestParam(value = VarsqlParamConstants.LIMIT, required = true, defaultValue = "" )  String limit,
			@RequestParam(value = VarsqlParamConstants.SQL, required = true, defaultValue = "" )  String sql,
			@RequestParam(value = VarsqlParamConstants.DB_TYPE, required = true, defaultValue = "" )  String dbtype,
			HttpServletRequest req) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		paramMap.put(VarsqlParamConstants.DB_SCHEMA, schema);
		paramMap.put(VarsqlParamConstants.SQL, sql);
		paramMap.put(VarsqlParamConstants.LIMIT, limit);
		
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		return sqlService.sqlData(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : sqlFormat
	 * @Method 설명 : 쿼리 포켓 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param sql
	 * @param dbtype
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/base/sqlFormat",""})
	public @ResponseBody String sqlFormat(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true, defaultValue = "" )  String vconnid, 
			@RequestParam(value = VarsqlParamConstants.SQL, required = true, defaultValue = "" )  String sql,
			@RequestParam(value = VarsqlParamConstants.DB_TYPE, required = true, defaultValue = "" )  String dbtype ) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		paramMap.put(VarsqlParamConstants.SQL, sql);
		paramMap.put(VarsqlParamConstants.DB_TYPE, dbtype);
		
		return sqlService.sqlFormat(paramMap);
	}
	
	/**
	 * 
	 * @Method Name  : dataExport
	 * @Method 설명 : data export
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param vconnid
	 * @param dbtype
	 * @param objectName
	 * @param exportType
	 * @param columnInfo
	 * @param limit
	 * @param req
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"/base/dataExport",""})
	public void dataExport(@RequestParam(value = VarsqlParamConstants.VCONNID, required = true, defaultValue = "" )  String vconnid 
			,@RequestParam(value = VarsqlParamConstants.DB_TYPE, required = true, defaultValue = "" )  String dbtype 
			,@RequestParam(value = VarsqlParamConstants.DB_OBJECT_NAME, required = true, defaultValue = "" )  String objectName 
			,@RequestParam(value = VarsqlParamConstants.EXPORT_TYPE, required = true, defaultValue = "" )  String exportType
			,@RequestParam(value = VarsqlParamConstants.EXPORT_COLUMN_INFO, required = true, defaultValue = "" )  String columnInfo
			,@RequestParam(value = VarsqlParamConstants.LIMIT, required = true, defaultValue = "" )  String limit
			,HttpServletRequest req
			,HttpServletResponse response
	) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(VarsqlParamConstants.VCONNID, vconnid);
		paramMap.put(VarsqlParamConstants.DB_TYPE, dbtype);
		paramMap.put(VarsqlParamConstants.DB_OBJECT_NAME, objectName);
		paramMap.put(VarsqlParamConstants.EXPORT_TYPE, exportType);
		paramMap.put(VarsqlParamConstants.EXPORT_COLUMN_INFO, columnInfo);
		paramMap.put(VarsqlParamConstants.LIMIT, limit);
		paramMap.put(UserConstants.UID, SecurityUtil.loginId(req));
		
		sqlService.dataExport(paramMap, response);
	}
	
}
