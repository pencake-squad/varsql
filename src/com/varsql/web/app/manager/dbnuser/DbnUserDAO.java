package com.varsql.web.app.manager.dbnuser;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class DbnUserDAO extends BaseDAO{
	
	public int selectdbListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectdbListTotalCnt", paramMap);
	}
	
	public List<Object> selectdbList(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectdbList", paramMap);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("updateQnaAnswerContent", paramMap);
	}
	
	public int selectDbUserMappingListTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("selectDbUserMappingListTotalCnt", paramMap);
	}
	
	public List<Object>  selectDbUserMappingList(DataCommonVO paramMap){
		return getSqlSession().selectList("selectDbUserMappingList", paramMap);
	}

	public Object updateDbUser(String[] viewidArr, DataCommonVO paramMap) throws Exception {
        SqlSession batchSqlSession = getBatchSqlSession(getSqlSession());
        
        boolean result = false; 
        try {
        	batchSqlSession.commit(false);
        	
        	batchSqlSession.delete("deleteDbUser",paramMap);
        	
            for(String id: viewidArr){
            	paramMap.put("viewid", id);
            	batchSqlSession.update("updateDbUser", paramMap);
            }
            batchSqlSession.commit();
            result = true;
        }catch(Exception e){
        	batchSqlSession.rollback();
        	throw e;
        }finally{
        	
        	batchSqlSession.close();
        }
		return result;
	}
}
