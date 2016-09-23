package com.varsql.web.app.manager.stats;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.dao.BaseDAO;


@Repository
public class SqlStatsDAO extends BaseDAO{
	
	public List dbSqlDateStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectSqlDateStat", paramMap);
	}
	
	public List dbSqlDayStats(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectSqlDayStat", paramMap);
	}
	
	public List dbSqlDayUserRank(DataCommonVO paramMap) {
		return getSqlSession().selectList("selectSqlDayUserRank", paramMap);
	}

	
}
