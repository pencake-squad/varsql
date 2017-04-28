package com.varsql.web.app.manager.qna;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;
import com.vartech.common.app.beans.SearchParameter;


@Repository
public class QnaDAO extends BaseDAO{
	
	public int selectQnaMgmtTotalCnt(SearchParameter searchParameter) {
		return getSqlSession().selectOne("manageMapper.selectQnaMgmtTotalCnt", searchParameter);
	}
	
	public List<Object> selectQnaMgmtList(SearchParameter searchParameter) {
		return getSqlSession().selectList("manageMapper.selectQnaMgmtList", searchParameter);
	}

	public int updateQnaAnswerContent(DataCommonVO paramMap){
		return getSqlSession().update("manageMapper.updateQnaAnswerContent", paramMap);
	}

}
