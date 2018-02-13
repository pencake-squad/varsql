package com.varsql.app.guest;

import org.springframework.stereotype.Repository;

import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.dao.BaseDAO;

@Repository
public class GuestDAO extends BaseDAO{
	
	public int selectQnaTotalCnt(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectQnaTotalCnt", paramMap);
	}
	
	public Object selectQna(DataCommonVO paramMap) {
		return getSqlSession().selectList("userMapper.selectQna", paramMap);
	}
	
	public Object selectDetailQna(DataCommonVO paramMap) {
		return getSqlSession().selectOne("userMapper.selectDetailQna", paramMap);
	}

	public String selectQnaMaxVal() {
		return getSqlSession().selectOne("userMapper.selectQnaMaxVal");
	}
	
	public int insertQnaInfo(DataCommonVO paramMap){
		return getSqlSession().insert("userMapper.insertQnaInfo", paramMap );
	}
	
	public int deleteQnaInfo(DataCommonVO paramMap){
		return getSqlSession().delete("userMapper.deleteQnaInfo", paramMap );
	}
	
	public int updateQnaInfo(DataCommonVO paramMap){
		return getSqlSession().update("userMapper.updateQnaInfo", paramMap );
	}
}
