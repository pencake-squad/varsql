package com.varsql.app.database.beans;

import com.varsql.app.common.constants.SqlDataConstants;
import com.varsql.core.db.beans.DatabaseParamInfo;

/**
 * 
 * @FileName : DatabaseParamInfo.java
 * @Author   : ytkim
 * @Program desc : database parameter vo
 * @Hisotry :
 */
public class SqlParamInfo extends DatabaseParamInfo{
	
	// sql_id
	private String sqlId;
	
	// sql_id
	private String sqlTitle;
	
	// sql
	private String sql;
	
	// limit count
	private int limit;
	
	// sql parameer
	private String sqlParam;
	
	private String exportType;
	
	private String columnInfo;
	
	public SqlParamInfo(){
		super();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if(limit > -1) {
			this.limit =limit;
		}else {
			this.limit = SqlDataConstants.DEFAULT_LIMIT_ROW_COUNT; 
		}
		
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getColumnInfo() {
		return columnInfo;
	}

	public void setColumnInfo(String columnInfo) {
		this.columnInfo = columnInfo;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getSqlTitle() {
		return sqlTitle;
	}

	public void setSqlTitle(String sqlTitle) {
		this.sqlTitle = sqlTitle;
	}
}
