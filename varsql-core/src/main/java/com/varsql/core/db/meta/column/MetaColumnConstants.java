package com.varsql.core.db.meta.column;

/**
 * 
 * @FileName : Column.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 13.
 * @프로그램설명:
 * @변경이력	:
 */
public interface MetaColumnConstants {
	String TABLE_CAT="TABLE_CAT";
	String KEY_SEQ="KEY_SEQ";
	String PK_NAME="PK_NAME";
	String TABLE_SCHEM="TABLE_SCHEM";
	String TABLE_NAME="TABLE_NAME";
	String COLUMN_NAME="COLUMN_NAME";
	String DATA_TYPE="DATA_TYPE";
	String TYPE_NAME="TYPE_NAME";
	String TYPE_NAME_SIZE="TYPE_NAME_SIZE";
	String COLUMN_SIZE="COLUMN_SIZE";
	String BUFFER_LENGTH="BUFFER_LENGTH";
	String DECIMAL_DIGITS="DECIMAL_DIGITS"; // 소수 자릿수값.
	String NUM_PREC_RADIX="NUM_PREC_RADIX";
	String NULLABLE="NULLABLE";
	String REMARKS="REMARKS";
	String COLUMN_DEF="COLUMN_DEF";
	String SQL_DATA_TYPE="SQL_DATA_TYPE";
	String SQL_DATETIME_SUB="SQL_DATETIME_SUB";
	String CHAR_OCTET_LENGTH="CHAR_OCTET_LENGTH";
	String ORDINAL_POSITION="ORDINAL_POSITION";
	String IS_NULLABLE="IS_NULLABLE";
	String SCOPE_CATLOG="SCOPE_CATLOG";
	String SCOPE_SCHEMA="SCOPE_SCHEMA";
	String SCOPE_TABLE="SCOPE_TABLE";
	String SOURCE_DATA_TYPE="SOURCE_DATA_TYPE";
	String IS_AUTOINCREMENT="IS_AUTOINCREMENT";
	String COMMENT="COMMENT";
	String CONSTRAINTS="CONSTRAINTS";	
	
	
	//procedure
	String PROCEDURE_CAT="PROCEDURE_CAT";
	String PROCEDURE_SCHEM="PROCEDURE_SCHEM";
	String PROCEDURE_NAME="PROCEDURE_NAME";
	String NUM_INPUT_PARAMS="NUM_INPUT_PARAMS";
	String NUM_OUTPUT_PARAMS="NUM_OUTPUT_PARAMS";
	String NUM_RESULT_SETS="NUM_RESULT_SETS";
	String PROCEDURE_TYPE="PROCEDURE_TYPE";
	String SPECIFIC_NAME="SPECIFIC_NAME";
	String PROCEDURE_COL_NULLABLE="NULLABLE";
	String PROCEDURE_COL_LENGTH="LENGTH";
	String PROCEDURE_COL_TYPE="COLUMN_TYPE";
	
	//function
	String FUNCTION_NAME="FUNCTION_NAME";
	String FUNCTION_TYPE="FUNCTION_TYPE";
	
	
	// index 
	String INDEX_TYPE="TYPE";
	String ASC_OR_DESC="ASC_OR_DESC";
	String INDEX_NAME="INDEX_NAME";
	
	String BUFFER_POOL="BUFFER_POOL";
	
	String TABLE_SPACE="TABLE_SPACE";
	
	String STATUS="STATUS";
	
	
	
}
