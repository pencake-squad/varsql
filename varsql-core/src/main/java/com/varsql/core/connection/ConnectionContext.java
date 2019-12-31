package com.varsql.core.connection;

import java.sql.SQLException;
import com.varsql.core.exception.ConnectionFactoryException;

/**
 * 
 * @FileName : ConnectionContext.java
 * @작성자 	 : ytkim
 * @Date	 : 2014. 2. 21.
 * @프로그램설명:
 * @변경이력	:
 */
public interface ConnectionContext {
	final public String DEFAULT_CONN_ID = "varsql";
	final public String DEFAULT_ALIAS = "varsql";
	
	public java.sql.Connection getConnection() throws  ConnectionFactoryException;
	public java.sql.Connection getConnection(String dbalias) throws  ConnectionFactoryException;
}