package com.varsql.core.auth;

/**
 * 
 * @FileName  : DatabaseService.java
 * @프로그램 설명 :
 * @Date      : 2019. 3. 20. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DatabaseService  {
	
	public void getUserDatabaseInfo() throws Exception {
		new AuthDAO().getUserDataBaseInfo();  
	}
}
