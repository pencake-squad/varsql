package com.varsql.db.ext.oracle;

import com.varsql.core.db.meta.datatype.DataTypeImpl;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.varsql.core.db.valueobject.VarSqlDbType;

/**
 * 
 * @FileName  : DataTypeDB2.java
 * @프로그램 설명 : db2 data type
 * @Date      : 2015. 6. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class OracleDataType extends DataTypeImpl{
	
	// 버전별 데이타를 체크 하기위해서 버전을 받음. 
	public OracleDataType() {
		// TODO Auto-generated constructor stub
		setDataTypeInfo("-101", new  DataTypeInfo("TIMESTAMP WITH TIME ZONE", false , false, false, VarSqlDbType.OTHER));
		setDataTypeInfo("2005", new  DataTypeInfo("CLOB", false , false , false , VarSqlDbType.BINARY));
		setDataTypeInfo("TIMESTAMP(6)", getDataType("TIMESTAMP"));
		setDataTypeInfo("12", getDataType("VARCHAR2"));
		setDataTypeInfo("VARCHAR", getDataType("VARCHAR2"));
		setDataTypeInfo("NUMBER", new  DataTypeInfo("NUMBER",  true , true, true, VarSqlDbType.NUMBER));
		setDataTypeInfo("3", getDataType("NUMBER"));
		
	}
}
