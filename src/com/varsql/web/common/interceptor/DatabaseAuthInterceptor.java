package com.varsql.web.common.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.beans.DatabaseInfo;
import com.varsql.web.common.constants.VarsqlParamConstants;

/**
 * 
 * @FileName  : DatabaseAuthInterceptor.java
 * @프로그램 설명 : database check 인터 셉터.
 * @Date      : 2015. 6. 22. 
 * @작성자      : ytkim
 * @변경이력 :
 */
public class DatabaseAuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws ServletException, IOException {
		String connid =req.getParameter(VarsqlParamConstants.CONN_UUID);
		if (!authCheck(req, connid)) {
			req.getRequestDispatcher("/invalidDatabase").forward(req, res);
		    return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @Method Name  : authCheck
	 * @Method 설명 : database 관련 권한 체크. 
	 * @작성일   : 2015. 6. 22. 
	 * @작성자   : ytkim
	 * @변경이력  :  
	 * 			/database/base 밑이면 connid만 체크 
	 * 			각 데이터베이스 벤더의 data를 콜하는 경우면 database type까지 체크. 
	 * @param req
	 * @param connid
	 * @return
	 */
	private boolean authCheck(HttpServletRequest req, String connid) {
		Map<String, DatabaseInfo> dataBaseInfo = SecurityUtil.loginInfo(req).getDatabaseInfo();
		
		if(!dataBaseInfo.containsKey(connid)){
			return false;
		}
		
		req.setAttribute(VarsqlParamConstants.VCONNID, dataBaseInfo.get(connid).getVconnid());
		return true; 
	}
}
