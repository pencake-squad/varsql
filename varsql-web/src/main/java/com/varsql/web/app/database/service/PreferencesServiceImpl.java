package com.varsql.web.app.database.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.dto.user.PreferencesRequestDTO;
import com.varsql.web.model.entity.user.UserDBPreferencesEntity;
import com.varsql.web.repository.spec.UserDBPreferencesSpec;
import com.varsql.web.repository.user.UserDBPreferencesEntityRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;

/**
 * 
 * @FileName  : PreferencesServiceImpl.java
 * @Date      : 2014. 8. 18. 
 * @작성자      : ytkim
 * @변경이력 :
 * @프로그램 설명 :
 */
@Service
public class PreferencesServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(PreferencesServiceImpl.class);
	
	@Autowired
	private UserDBPreferencesEntityRepository userDBPreferencesEntityRepository ;
	
	/**
	 * 
	 * @Method Name  : selectPreferencesInfo
	 * @Method 설명 : 설정 정보 가져오기.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 3. 16. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		return selectPreferencesInfo(preferencesInfo, false);
	}
	public String selectPreferencesInfo(PreferencesRequestDTO preferencesInfo ,boolean flag) {
		UserDBPreferencesEntity item = userDBPreferencesEntityRepository.findOne(UserDBPreferencesSpec.findPrefVal(preferencesInfo)).orElse(null);
		return  item==null ? (flag ? "{}" : null) : (item.getPrefVal() ==null? "{}" :item.getPrefVal()) ;
	}
	

	/**
	 * 
	 * @Method Name  : selectPreferencesInfos
	 * @Method 설명 : 설정값 다중으로 가져오기
	 * @작성자   : ytkim
	 * @작성일   : 2020. 8. 15. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public List<UserDBPreferencesEntity> selectPreferencesInfos(PreferencesRequestDTO... preferencesInfo) {
		List<UserDBPreferencesEntity> items = userDBPreferencesEntityRepository.findAll(UserDBPreferencesSpec.findPrefVal(preferencesInfo));
		return items; 
	}
	
	
	/**
	 * 
	 * @Method Name  : savePreferencesInfo
	 * @Method 설명 : 설정 저장.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 3. 16. 
	 * @변경이력  :
	 * @param preferencesInfo
	 * @return
	 */
	public ResponseResult savePreferencesInfo(PreferencesRequestDTO preferencesInfo) {
		UserDBPreferencesEntity userDBPreferencesEntity = userDBPreferencesEntityRepository.findOne(UserDBPreferencesSpec.findPrefVal(preferencesInfo)).orElse(null);
		
		if(userDBPreferencesEntity==null) {
			userDBPreferencesEntity = preferencesInfo.toEntity();
		}else {
			userDBPreferencesEntity.setPrefVal(preferencesInfo.getPrefVal());
		}
		
		userDBPreferencesEntityRepository.save(userDBPreferencesEntity);
		
		return VarsqlUtils.getResponseResultItemOne(1); 
	}
}