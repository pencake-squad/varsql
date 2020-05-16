package com.varsql.web.app.database.service;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.type.ResultType;
import com.varsql.core.common.util.DataExportUtil;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.connection.ConnectionFactory;
import com.varsql.core.db.DBType;
import com.varsql.core.db.valueobject.DatabaseInfo;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.sql.beans.GridColumnInfo;
import com.varsql.core.sql.builder.SqlSource;
import com.varsql.core.sql.builder.SqlSourceBuilder;
import com.varsql.core.sql.builder.SqlSourceResultVO;
import com.varsql.core.sql.builder.VarsqlStatementType;
import com.varsql.core.sql.format.VarsqlFormatterUtil;
import com.varsql.core.sql.util.SQLUtil;
import com.varsql.web.app.database.beans.SqlGridDownloadInfo;
import com.varsql.web.app.database.beans.SqlLogInfo;
import com.varsql.web.app.database.beans.SqlParamInfo;
import com.varsql.web.app.database.beans.SqlUserHistoryInfo;
import com.varsql.web.app.database.dao.SQLDAO;
import com.varsql.web.common.beans.DataCommonVO;
import com.varsql.web.constants.SqlDataConstants;
import com.varsql.web.exception.VarsqlResultConvertException;
import com.varsql.web.util.SqlResultUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.DateUtils;
import com.vartech.common.utils.StringUtil;
import com.vartech.common.utils.StringUtil.EscapeType;
import com.vartech.common.utils.VartechUtils;

/**
 *
 * @FileName  : SQLServiceImpl.java
 * @Date      : 2014. 8. 18.
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class SQLServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(SQLServiceImpl.class);

	@Autowired
	private SQLDAO sqlDAO ;

	/**
	 *
	 * @Method Name  : sqlFormat
	 * @Method 설명 : sql 포멧 맞추기
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 6.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlFormat(SqlParamInfo sqlParamInfo) throws Exception {
		ResponseResult result =new ResponseResult();

		String dbParserPrefix = DBType.getDbParser(sqlParamInfo.getDbType());

		if("varsql".equals(sqlParamInfo.getCustom().get("formatType"))){
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(),dbParserPrefix , VarsqlFormatterUtil.FORMAT_TYPE.VARSQL)+"\n");
		}else{
			result.setItemOne(VarsqlFormatterUtil.format(sqlParamInfo.getSql(),dbParserPrefix )+"\n");
		}

		return result;

	}

	/**
	 *
	 * @Method Name  : sqlData
	 * @Method 설명 : 쿼리 데이타 보기.
	 * @작성자   : ytkim
	 * @작성일   : 2015. 4. 9.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public ResponseResult sqlData(SqlParamInfo sqlParamInfo, HttpServletRequest req) throws Exception {

		Map sqlParamMap = VartechUtils.stringToObject(sqlParamInfo.getSqlParam(), HashMap.class);

		DatabaseInfo dbinfo = SecurityUtil.userDBInfo(sqlParamInfo.getConuid());

		ResponseResult parseInfo=SqlSourceBuilder.parseResponseResult(sqlParamInfo.getSql(),sqlParamMap, DBType.getDbParser(sqlParamInfo.getDbType()));

		List<SqlSource> sqlList = parseInfo.getItems();

		int limit = sqlParamInfo.getLimit();

		if(!SecurityUtil.isAdmin()) {
			sqlParamInfo.setLimit(limit > dbinfo.getMaxSelectCount() ? dbinfo.getMaxSelectCount() : limit);
		}

		ArrayList<SqlSourceResultVO> reLst = new ArrayList<SqlSourceResultVO>();

		ResponseResult result = new ResponseResult();

		Connection conn = null;
		SqlSourceResultVO ssrv =null;

		long stddt = System.currentTimeMillis();
		String[] mmddHH = DateUtils.dateformat("MM-dd-HH", stddt).split("-");

		SqlLogInfo sqlLogInfo= new SqlLogInfo();
		sqlLogInfo.setVconnid(sqlParamInfo.getVconnid());
		sqlLogInfo.setViewid(sqlParamInfo.getViewid());
		sqlLogInfo.setStartTime(stddt);

		sqlLogInfo.setSMm(Integer.valueOf(mmddHH[0]));
		sqlLogInfo.setSDd(Integer.valueOf(mmddHH[1]));
		sqlLogInfo.setSHh(Integer.valueOf(mmddHH[2]));

		sqlLogInfo.setLogSql(sqlParamInfo.getSql());
		sqlLogInfo.setUsrIp(VarsqlUtils.getClientIP(req));

		SqlSource tmpSqlSource =null;
		int sqldx =0,sqlSize = sqlList.size();

		String errorMsg = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlParamInfo.getVconnid());
			conn.setAutoCommit(false);
			for (sqldx =0;sqldx <sqlSize; sqldx++) {
				tmpSqlSource = sqlList.get(sqldx);

				ssrv = new SqlSourceResultVO();
				reLst.add(ssrv);
				tmpSqlSource.setResult(ssrv);
				ssrv.setStarttime(System.currentTimeMillis());

				getRequestSqlData(sqlParamInfo,conn,tmpSqlSource, dbinfo);

				ssrv.setEndtime(System.currentTimeMillis());
				ssrv.setDelay((ssrv.getEndtime()- ssrv.getStarttime())/1000);
				ssrv.setResultMessage((ssrv.getDelay())+" SECOND : "+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));

				sqlLogInfo.setStartTime(ssrv.getStarttime());
				sqlLogInfo.setLogSql(tmpSqlSource.getQuery());
				sqlLogInfo.setCommandType(tmpSqlSource.getCommandType());
				sqlLogInfo.setEndTime(System.currentTimeMillis());

				sqlLogInsert(sqlLogInfo);

				if(SqlDataConstants.VIEWTYPE.GRID.val().equals(ssrv.getViewType())) {
					break;
				}
			}

			result.setItemList(reLst);
			conn.commit();
		} catch (Throwable e ) {
			if(conn != null) conn.rollback();

			errorMsg = e.getMessage();

			if(e instanceof VarsqlResultConvertException){
				result.setResultCode(SqlDataConstants.ERROR.RESULT_CONVERT.intVal());
				ssrv= ((VarsqlResultConvertException)e).getSsrv();
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
			}else {
				boolean ssrvNullFlag = false;
				if(ssrv==null) {
					ssrvNullFlag = true;
					ssrv = new SqlSourceResultVO();
				}

				ssrv.setEndtime(System.currentTimeMillis());
				String tmpMsg = parseInfo.getMessage();
				tmpMsg = (tmpMsg  == null || "".equals(tmpMsg) ?"" :StringUtil.escape(parseInfo.getMessage(), EscapeType.html)+"<br/>");

				if(e instanceof ConnectionFactoryException) {
					result.setResultCode(SqlDataConstants.ERROR.CONNECTION.intVal());
				}else {
					result.setResultCode(SqlDataConstants.ERROR.SQL.intVal());
				}

				result.setMessage(tmpMsg+StringUtil.escape(ssrv.getResultMessage(), EscapeType.html));

				if(ssrvNullFlag) {
					result.setMessage(errorMsg);
				}
			}

			result.addCustoms("errorLine", sqldx);
			result.setItemOne(tmpSqlSource);

			if(VarsqlUtils.isRuntimelocal()) {
				logger.error(getClass().getName()+" sqlData : ", e);
			}
		}finally{
			if(conn !=null){
				conn.setAutoCommit(true);
				SQLUtil.close(conn);
			}
		}

		long enddt = System.currentTimeMillis();

		SqlUserHistoryInfo  sqlUserHistoryInfo= new SqlUserHistoryInfo();

		sqlUserHistoryInfo.setVconnid(sqlLogInfo.getVconnid());
		sqlUserHistoryInfo.setViewid(sqlLogInfo.getViewid());
		sqlUserHistoryInfo.setHistoryId(VartechUtils.generateUUID());
		sqlUserHistoryInfo.setStartTime(VarsqlUtils.getCurrentTimestamp(stddt));
		sqlUserHistoryInfo.setEndTime(VarsqlUtils.getCurrentTimestamp(enddt));
		sqlUserHistoryInfo.setDelayTime((int) ((enddt- stddt)/1000));
		sqlUserHistoryInfo.setLogSql(sqlParamInfo.getSql());
		sqlUserHistoryInfo.setUsrIp(sqlLogInfo.getUsrIp());
		sqlUserHistoryInfo.setErrorLog(errorMsg);

		sqlDAO.insertUserHistory(sqlUserHistoryInfo);

		return  result;
	}

	/**
	 *
	 * @Method Name  : getStatement
	 * @Method 설명 : 실행할 statement 얻기
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param maxRow
	 * @return
	 * @throws SQLException
	 */
	private Statement getStatement(Connection conn, SqlSource tmpSqlSource, int maxRow) throws SQLException {

		Statement stmt = null;
		if(VarsqlStatementType.STATEMENT.equals(tmpSqlSource.getStatementType())){
			stmt = conn.createStatement();
			setMaxRow(stmt, maxRow);
			stmt.execute(tmpSqlSource.getQuery());
		}else if(VarsqlStatementType.CALLABLE.equals(tmpSqlSource.getStatementType())){
			CallableStatement callStatement = conn.prepareCall(tmpSqlSource.getQuery());
			//setMaxRow(callStatement, maxRow);
			callStatement.execute();
			stmt = callStatement;
		}else{
			PreparedStatement pstmt = conn.prepareStatement(tmpSqlSource.getQuery());

			List param= tmpSqlSource.getParam();

			if(param != null){
				for(int i =0 ;i < param.size() ;i++){
					pstmt.setObject(i+1, param.get(i));
				}
			}

			setMaxRow(pstmt, maxRow);
			pstmt.execute();
			stmt = pstmt;
		}

		return stmt;
	}

	/**
	 *
	 * @Method Name  : setMaxRow
	 * @Method 설명 : row 갯수 셋팅
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param stmt
	 * @param maxRow
	 * @throws SQLException
	 */
	private void setMaxRow(Statement stmt, int maxRow) throws SQLException {
		stmt.setMaxRows(maxRow);
	}

	/**
	 *
	 * @param sqlParamInfo
	 * @Method Name  : getResultData
	 * @Method 설명 : 데이타 얻기
	 * @작성일   : 2015. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param conn
	 * @param tmpSqlSource
	 * @param dbinfo
	 * @param vconnid
	 * @param maxRow
	 * @return
	 * @throws SQLException
	 */
	protected void getRequestSqlData(SqlParamInfo sqlParamInfo, Connection conn, SqlSource tmpSqlSource, DatabaseInfo dbInfo) throws SQLException {
		Statement stmt = null;
		ResultSet rs  = null;
		SqlSourceResultVO ssrv = tmpSqlSource.getResult();

		int maxRow = sqlParamInfo.getLimit();

	    try{
			stmt  = getStatement(conn, tmpSqlSource, maxRow);

			rs = stmt.getResultSet();

			if(rs != null){
				SqlResultUtils.resultSetHandler(rs, ssrv, sqlParamInfo, maxRow);
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.GRID.val());
				ssrv.setResultMessage("success result count : "+ssrv.getResultCnt());
			}else{
				ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
				ssrv.setResultCnt(stmt.getUpdateCount());
				ssrv.setResultMessage("success update count : "+ ssrv.getResultCnt());
			}

			ssrv.setResultType(ResultType.SUCCESS.name());

	    }catch(SQLException e){
	    	ssrv.setViewType(SqlDataConstants.VIEWTYPE.MSG.val());
	    	ssrv.setResultType(ResultType.FAIL.name());
	    	ssrv.setResultMessage(String.format("errorcode :%s ; sql state : %s ; message : %s",e.getErrorCode() ,e.getSQLState() , e.getMessage()));
	    	logger.error(getClass().getName()+" sqlData", e);
	    	throw new SQLException(e);
		} finally {
	    	SQLUtil.close(stmt, rs);
	    }
	}

	/**
	 *
	 * @Method Name  : sqlLogInsert
	 * @Method 설명 : 사용자 sql 로그 저장
	 * @작성일   : 2015. 5. 6.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @param tmpSqlSource
	 * @param ssrv
	 */
	private void sqlLogInsert(SqlLogInfo logInfo) {
		try{
		    sqlDAO.insertSqlUserLog(logInfo);
	    }catch(Exception e){
	    	logger.error(getClass().getName()+" sqlLogInsert {}", VartechUtils.reflectionToString(logInfo));
	    	logger.error(getClass().getName()+" sqlLogInsert ", e);
	    }
	}

	/**
	 * 데이타 내보내기.
	 * @param paramMap
	 */
	public void dataExport(ParamMap paramMap, SqlParamInfo sqlParamInfo, HttpServletResponse res) throws Exception {

		String exportType = sqlParamInfo.getExportType();

		String tmpName = sqlParamInfo.getObjectName();

		if(!sqlParamInfo.getBaseSchema().equals(sqlParamInfo.getSchema())) {
			tmpName = sqlParamInfo.getSchema()+"."+tmpName;
		}

		String reqSql = "select "+ sqlParamInfo.getColumnInfo() + " from "+tmpName;
		SqlSource sqlSource = SqlSourceBuilder.getSqlSource(reqSql);

		Connection conn = null;
		SqlSourceResultVO result = null;
		sqlSource.setResult(new SqlSourceResultVO());

		DatabaseInfo dbinfo = SecurityUtil.userDBInfo(sqlParamInfo.getConuid());

		try {
			conn = ConnectionFactory.getInstance().getConnection(sqlParamInfo.getVconnid());
			getRequestSqlData(sqlParamInfo,conn,sqlSource, dbinfo);
			result = sqlSource.getResult();
		} catch (SQLException e) {
			logger.error(getClass().getName()+" dataExport ", e);
		}finally{
			SQLUtil.close(conn);
		}

		String exportFileName = paramMap.getString("fileName", tmpName);

		OutputStream os = res.getOutputStream();

		try {
			if("csv".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".csv",VarsqlConstants.CHAR_SET));
				DataExportUtil.toCSVWrite(result.getData(), result.getColumn(), os);
			}else if("json".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".json",VarsqlConstants.CHAR_SET));
				new ObjectMapper().writeValue(os, result.getData());
			}else if("insert".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".sql",VarsqlConstants.CHAR_SET));
				DataExportUtil.toInsertQueryWrite(result.getData(), sqlSource.getResult().getNumberTypeFlag(), tmpName, os);
			}else if("xml".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".xml",VarsqlConstants.CHAR_SET));
				DataExportUtil.toXmlWrite(result.getData(), result.getColumn() , os);
			}else if("excel".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(exportFileName + ".xlsx",VarsqlConstants.CHAR_SET));
				DataExportUtil.toExcelWrite(result.getData(),result.getColumn() , os);
			}

			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error(getClass().getName()+" dataExport {}", e.getMessage());
	    	logger.error(getClass().getName()+" dataExport ", e);
		}finally {
			IOUtils.closeQuietly(os);
		}
	}

	public void columnInfoExport(DataCommonVO paramMap,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	/**
	 * 쿼리 저장.
	 * @param sqlParamInfo
	 */
	@Transactional(rollbackFor=Exception.class)
	public ResponseResult saveQuery(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();

		if("".equals(sqlParamInfo.getSqlId())){
			sqlDAO.updateSqlFileTabDisable(sqlParamInfo);   // 이전 활성 view mode  N으로 변경.
			sqlParamInfo.setSqlId(VarsqlUtils.generateUUID());
		    sqlDAO.saveQueryInfo(sqlParamInfo);
		}else{
			String mode = String.valueOf(sqlParamInfo.getCustom().get("mode"));

			if("addTab".equals(mode)){
				sqlDAO.updateSqlFileTabDisable(sqlParamInfo);
				sqlDAO.insertSqlFileTabInfo(sqlParamInfo); // 이전 활성 view mode  N으로 변경.
			}else if("delTab".equals(mode)){
				deleteSqlFileTabInfo(sqlParamInfo);
			}else if("viewTab".equals(mode)){
				sqlDAO.updateSqlFileTabDisable(sqlParamInfo);
				sqlDAO.updateSqlFileTabEnable(sqlParamInfo);
			}else{
				sqlDAO.updateQueryInfo(sqlParamInfo);

				if("query_del".equals(mode)){
					deleteSqlFileTabInfo(sqlParamInfo);
				}
			}
		}

		result.setItemOne(sqlParamInfo);

		return result;
	}

	private void deleteSqlFileTabInfo(SqlParamInfo sqlParamInfo) {
		try{
			int tabLen = -1;
			try{
				tabLen = Integer.parseInt(String.valueOf(sqlParamInfo.getCustom().get("len")));
			}catch(Exception e){
				tabLen = -1;
			}

			if(tabLen ==0){
				sqlDAO.deleteAllSqlFileTabInfo(sqlParamInfo);
			}else{
				sqlDAO.deleteSqlFileTabInfo(sqlParamInfo);
			}
		}catch(Exception e){
			throw e;
		}

	}

	/**
	 *
	 * @Method Name  : saveAllQuery
	 * @Method 설명 : sql 파일 모두저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult saveAllQuery(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		Map<String,Object> customParam = (Map<String,Object>)sqlParamInfo.getCustom();

		String sqlIdStr = String.valueOf(customParam.get("sqlIdArr"));

		String[] sqlIdArr= sqlIdStr.split(";");
		Map<String , Integer> reval = new HashMap<String , Integer>();
		for (int i = 0; i < sqlIdArr.length; i++) {
			String sqlId = sqlIdArr[i];

			sqlParamInfo.setSqlId(sqlId);
			sqlParamInfo.setSqlParam(String.valueOf(customParam.get(sqlId+"_param")));
			sqlParamInfo.setSql(String.valueOf(customParam.get(sqlId)));

			reval.put(sqlId, sqlDAO.updateQueryInfo(sqlParamInfo));
		}

		result.setItemOne(reval);

		return result;
	}

	/**
	 * 사용자 sql 목록 보기.
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult selectSqlFileList(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemList(sqlDAO.selectSqlFileList(sqlParamInfo));
		return result;
	}

	/**
	 * sql 저장 정보 삭제 .
	 * @param sqlParamInfo
	 * @return
	 */
	@Transactional(rollbackFor=Throwable.class)
	public ResponseResult deleteSqlSaveInfo(SqlParamInfo sqlParamInfo) {

		ResponseResult result = new ResponseResult();
		result.setItemOne( sqlDAO.deleteSqlSaveInfo(sqlParamInfo));
		deleteSqlFileTabInfo(sqlParamInfo);
		return result;
	}

	/**
	 *
	 * @Method Name  : gridDownload
	 * @Method 설명 : 그리드 데이터 다운로드.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 9.
	 * @변경이력  :
	 * @param sqlGridDownloadInfo
	 * @param res
	 * @throws IOException
	 */
	public void gridDownload(SqlGridDownloadInfo sqlGridDownloadInfo, HttpServletResponse res) throws IOException {
		String exportType = sqlGridDownloadInfo.getExportType();

		List<GridColumnInfo> columnInfo = Arrays.asList(VartechUtils.stringToObject(sqlGridDownloadInfo.getHeaderInfo(), GridColumnInfo[].class , true));

		//List<Map> downloadData = VartechUtils.stringToObject(sqlGridDownloadInfo.getGridData(), ArrayList.class);

		logger.info("grid download : {} " , sqlGridDownloadInfo);


		String downloadName = "grid-data-download";

		OutputStream os = res.getOutputStream();

		try {
			String jsonString = sqlGridDownloadInfo.getGridData();
			if("csv".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".csv",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToCsv(jsonString, columnInfo, os);
			}else if("json".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".json",VarsqlConstants.CHAR_SET));
				DataExportUtil.toTextWrite(jsonString, os);
			}else if("xml".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".xml",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToXml(jsonString, columnInfo , os);
			}else if("excel".equals(exportType)){
				VarsqlUtils.setResponseDownAttr(res, java.net.URLEncoder.encode(downloadName + ".xlsx",VarsqlConstants.CHAR_SET));
				DataExportUtil.jsonStringToExcel(jsonString, columnInfo , os);
			}

			if(os !=null) os.close();
		}catch(Exception e) {
			logger.error(" param {} ", sqlGridDownloadInfo);
			logger.error(" gridDownload {}", e.getMessage());
	    	logger.error(" gridDownload ", e);
		}finally {
			IOUtils.closeQuietly(os);
		}



	}

	/**
	 *
	 * @Method Name  : selectSqlFileTabList
	 * @Method 설명 : sql file tab list
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 7.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult selectSqlFileTabList(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemList(sqlDAO.selectSqlFileTabList(sqlParamInfo));
		return result;
	}

	/**
	 *
	 * @Method Name  : sqlFileDetailInfo
	 * @Method 설명 : sql file 상세보기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 11. 26.
	 * @변경이력  :
	 * @param sqlParamInfo
	 * @return
	 */
	public ResponseResult sqlFileDetailInfo(SqlParamInfo sqlParamInfo) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(sqlDAO.selectSqlFileDetailInfo(sqlParamInfo));
		return result;
	}
}