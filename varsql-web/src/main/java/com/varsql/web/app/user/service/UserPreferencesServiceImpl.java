package com.varsql.web.app.user.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.common.util.StringUtil;
import com.varsql.core.db.encryption.EncryptionFactory;
import com.varsql.web.app.user.dao.UserPreferencesDAO;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.constants.VarsqlErrorCode;
import com.varsql.web.dto.user.PasswordRequestDTO;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.dto.user.UserReqeustDTO;
import com.varsql.web.dto.user.UserResponseDTO;
import com.varsql.web.model.entity.app.QnAEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.spec.QnASpec;
import com.varsql.web.repository.user.QnAEntityRepository;
import com.varsql.web.repository.user.UserMgmtRepository;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.encryption.EncryptDecryptException;
import com.vartech.common.utils.PagingUtil;

@Service
public class UserPreferencesServiceImpl extends AbstractService{
	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesServiceImpl.class);

	@Autowired
	UserPreferencesDAO userPreferencesDAO;
	
	@Autowired
	private QnAEntityRepository qnaEntityRepository;
	
	@Autowired
	private UserMgmtRepository userMgmtRepository;

	/**
	 *
	 * @Method Name  : selectUserDetail
	 * @Method 설명 : 사용자 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param loginId
	 * @return
	 */
	public UserEntity findUserInfo(String viewid) {
		return userMgmtRepository.findByViewid(viewid);
	}

	/**
	 *
	 * @Method Name  : updateUserInfo
	 * @Method 설명 : 사용자 정보 업데이트
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param userForm
	 * @param req
	 * @param res
	 * @return
	 */
	public boolean updateUserInfo(UserReqeustDTO userForm, HttpServletRequest req, HttpServletResponse res) {
		
		UserEntity userInfo = userMgmtRepository.findByViewid(SecurityUtil.userViewId());
		
		userInfo.setLang(userForm.getLang());
		userInfo.setUname(userForm.getUname());
		userInfo.setOrgNm(userForm.getOrgNm());
		userInfo.setDeptNm(userForm.getDeptNm());
		userInfo.setDescription(userForm.getDescription());
		
		userInfo = userMgmtRepository.save(userInfo);
		
		boolean flag = userInfo != null;

		if(flag) {
			// 언어 변경시 처리.
			Locale userLocale= LocaleConstants.parseLocaleString(userForm.getLang());

			if(userLocale != null  && !userLocale.equals(SecurityUtil.loginInfo().getUserLocale())) {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
				if (localeResolver == null) {
					throw new IllegalStateException("No LocaleResolver found.");
				}

				if(localeResolver.resolveLocale(req) != userLocale) {
					localeResolver.setLocale(req, res, userLocale);
				}

				SecurityUtil.loginInfo().setUserLocale(userLocale);
			}
		}

		return flag;
	}

	/**
	 *
	 * @Method Name  : updatePasswordInfo
	 * @Method 설명 : 비밀번호 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param passwordForm
	 * @param resultObject
	 * @return
	 * @throws EncryptDecryptException
	 */
	public ResponseResult updatePasswordInfo(PasswordRequestDTO passwordForm, ResponseResult resultObject) throws EncryptDecryptException {
		String password = userPreferencesDAO.selectUserPasswordCheck(passwordForm);

		if(passwordForm.getCurrPw().equals(EncryptionFactory.getInstance().decrypt(password))){
			passwordForm.setUpw(EncryptionFactory.getInstance().encrypt(passwordForm.getUpw()));
			resultObject.setItemOne(userPreferencesDAO.updatePasswordInfo(passwordForm)> 0);
		}else{
			resultObject.setResultCode(VarsqlErrorCode.PASSWORD_NOT_VALID.code());
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : selectUserMsg
	 * @Method 설명 : 사용자 메시지 목록 [환경 설정]
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserMsg(SearchParameter searchParameter) {
		ResponseResult result = new ResponseResult();

		int totalcnt = userPreferencesDAO.selectUserMsgTotalcnt(searchParameter);

		if(totalcnt > 0){
			result.setItemList(userPreferencesDAO.selectUserMsg(searchParameter));
		}else{
			result.setItemList(null);
		}
		result.setPage(PagingUtil.getPageObject(totalcnt, searchParameter));

		return result;
	}

	/**
	 *
	 * @Method Name  : selectUserMsgReply
	 * @Method 설명 : 답변 목록 구하기.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 5. 2.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult selectUserMsgReply(ParamMap ParamMap) {
		ResponseResult result = new ResponseResult();
		result.setItemList(userPreferencesDAO.selectUserMsgReply(ParamMap));
		return result;
	}

	/**
	 *
	 * @Method Name  : deleteUserMsg
	 * @Method 설명 : 메시지 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 8. 16.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult deleteUserMsg(ParamMap paramMap) {
		ResponseResult result = new ResponseResult();

		String[] viewidArr = StringUtil.split(paramMap.getString("selectItem"),",");

		result.setItemOne(userPreferencesDAO.deleteUserMsg(viewidArr,paramMap));

		return result;
	}

	/**
	 *
	 * @Method Name  : selectQna
	 * @Method 설명 : qna list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult searchQna(SearchParameter searchParameter) {
		Page<QnAEntity> result = qnaEntityRepository.findAll(
			QnASpec.userQnaSearch(searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);
		
		return VarsqlUtils.getResponseResult(result, searchParameter);
	}
	/**
	 *
	 * @Method Name  : insertQnaInfo
	 * @Method 설명 : qna 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public ResponseResult saveQnaInfo(QnARequesetDTO qnaInfo, boolean insFlag) {
		QnAEntity entity= qnaInfo.toEntity();
		entity = qnaEntityRepository.save(entity);
		return VarsqlUtils.getResponseResultItemOne(entity==null? 0:1);
	}

	/**
	 *
	 * @Method Name  : deleteQnaInfo
	 * @Method 설명 : qna 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3.
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteQnaInfo(String qnaid) {
		
		QnAEntity entity = qnaEntityRepository.findByQnaid(qnaid);
		
		if(entity.getAnswerId() ==null) {
			qnaEntityRepository.delete(entity);
		}
		
		ResponseResult result = new ResponseResult();
		result.setMessageCode("answer.msg.notdelete");
		
		return result;
	}
}