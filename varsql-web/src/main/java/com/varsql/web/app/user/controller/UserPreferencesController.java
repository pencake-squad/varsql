package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.web.app.user.beans.PasswordForm;
import com.varsql.web.app.user.beans.QnAInfo;
import com.varsql.web.app.user.beans.UserForm;
import com.varsql.web.app.user.service.UserPreferencesServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.constants.VarsqlParamConstants;
import com.varsql.web.util.CheckUtils;
import com.varsql.web.util.DatabaseUtils;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.HttpUtils;

/**
 *
 *
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: PreferencesController.java
* @DESC		: 사용자 환경설정 컨트롤러.
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 28. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/user/preferences")
public class UserPreferencesController extends AbstractController{

	private static final Logger logger = LoggerFactory.getLogger(UserPreferencesController.class);

	@Autowired
	UserPreferencesServiceImpl userPreferencesServiceImpl;

	/**
	 *
	 * @Method Name  : preferencesMain
	 * @Method 설명 : 사용자 정보 환결 설정.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"","/","/main"})
	public ModelAndView preferencesMain(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();

		setModelDefaultValue(req , model);
		model.addAttribute("detailInfo" , userPreferencesServiceImpl.selectUserDetail(SecurityUtil.loginId(req)));
		model.addAttribute("localeInfo" , LocaleConstants.values());
		return getModelAndView("/general", VIEW_PAGE.USER_PREFERENCES, model);
	}

	private void setModelDefaultValue(HttpServletRequest req, ModelMap model) {
		model.addAttribute("headerview", ("N".equals(req.getParameter("header"))?"N":""));
		model.addAttribute("originalURL", HttpUtils.getOriginatingRequestUri(req));
	}

	/**
	 *
	 * @Method Name  : userInfoSave
	 * @Method 설명 : 사용자 정보 업데이트.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param userForm
	 * @param result
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/userInfoSave"})
	public @ResponseBody ResponseResult userInfoSave(@Valid UserForm userForm, BindingResult result,HttpServletRequest req, HttpServletResponse res) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController userInfoSave check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			userForm.setViewid(SecurityUtil.loginId(req));
			resultObject.setItemOne(userPreferencesServiceImpl.updateUserInfo(userForm,req,res));
		}

		return  resultObject;
	}

	/**
	 *
	 * @Method Name  : preferencesPassword
	 * @Method 설명 : 패스워드 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/password"})
	public ModelAndView preferencesPassword(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/password", VIEW_PAGE.USER_PREFERENCES, model);
	}

	/**
	 *
	 * @Method Name  : passwordSave
	 * @Method 설명 : 비밀번호 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param passwordForm
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/passwordSave"})
	public @ResponseBody ResponseResult passwordSave(@Valid PasswordForm passwordForm, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  UserMainController passwordSave check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			passwordForm.setViewid(SecurityUtil.loginId(req));
			resultObject = userPreferencesServiceImpl.updatePasswordInfo(passwordForm, resultObject);
		}

		return  resultObject;
	}

	/**
	 * @Method Name  : preferencesMessage
	 * @Method 설명 : 메시지 관리
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/message"})
	public ModelAndView preferencesMessage(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/memoMgmt", VIEW_PAGE.USER_PREFERENCES, model);
	}

	/**
	 *
	 * @Method Name  : qna
	 * @Method 설명 : qna 관리
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 4.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/qna"})
	public ModelAndView qna(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);

		return getModelAndView("/qna", VIEW_PAGE.USER_PREFERENCES, model);
	}

	/**
	 * @Method Name  : preferenceslistMsg
	 * @Method 설명 : 메시지 목록보기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/listMsg"})
	public @ResponseBody ResponseResult preferenceslistMsg(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));

		return userPreferencesServiceImpl.selectUserMsg(searchParameter);
	}

	@RequestMapping({"/msgReplyList"})
	public @ResponseBody ResponseResult msgReplyList(HttpServletRequest req) throws Exception {
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		return userPreferencesServiceImpl.selectUserMsgReply(paramMap);
	}

	/**
	 * @Method Name  : preferencesdeleteMsg
	 * @Method 설명 : 메시지 목록보기
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/deleteMsg"})
	public @ResponseBody ResponseResult preferencesdeleteMsg(HttpServletRequest req) throws Exception {
		ParamMap paramMap = HttpUtils.getServletRequestParam(req);
		paramMap.put(VarsqlParamConstants.UID, SecurityUtil.loginId(req));

		return userPreferencesServiceImpl.deleteUserMsg( paramMap);
	}

	/**
	 *
	 * @Method Name  : qnalist
	 * @Method 설명 : qna list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10.
	 * @변경이력  :
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qnaList")
	public @ResponseBody ResponseResult qnalist(HttpServletRequest req) throws Exception {
		SearchParameter searchParameter = HttpUtils.getSearchParameter(req);
		searchParameter.addCustomParam(VarsqlParamConstants.UID, SecurityUtil.loginId(req));

		return userPreferencesServiceImpl.selectQna(searchParameter);
	}

	/**
	 *
	 * @Method Name  : qna
	 * @Method 설명 : qna 정보 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10.
	 * @변경이력  :
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/insQna")
	public @ResponseBody ResponseResult insQna(@Valid QnAInfo qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());

			if(CheckUtils.isEmpty(qnaInfo.getQnaid())) {
				resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, true);
			}else {
				resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
			}
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : qnaDelete
	 * @Method 설명 : qna  삭제
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10.
	 * @변경이력  :
	 * @param qnaid
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delQna")
	public @ResponseBody ResponseResult qnaDelete(@RequestParam(value = "qnaid" , required=true)  String qnaid,HttpServletRequest req) throws Exception {

		QnAInfo qnaInfo = new QnAInfo();
		qnaInfo.setQnaid(qnaid);

		qnaInfo.setUserid(SecurityUtil.loginId());

		return userPreferencesServiceImpl.deleteQnaInfo(qnaInfo);
	}

	/**
	 *
	 * @Method Name  : qnaUpdate
	 * @Method 설명 : qna 수정.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 10.
	 * @변경이력  :
	 * @param qnaInfo
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updQna")
	public @ResponseBody ResponseResult qnaUpdate(@Valid QnAInfo qnaInfo, BindingResult result,HttpServletRequest req) throws Exception {
		ResponseResult resultObject = new ResponseResult();

		if(result.hasErrors()){
			for(ObjectError errorVal : result.getAllErrors()){
				logger.warn("###  GuestController qna check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}else{
			qnaInfo.setUserid(SecurityUtil.loginId());
			resultObject = userPreferencesServiceImpl.saveQnaInfo(qnaInfo, false);
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : sqlfile
	 * @Method 설명 : sql file list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 10. 30.
	 * @변경이력  :
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"/sqlFile"})
	public ModelAndView sqlFile(HttpServletRequest req, HttpServletResponse res,ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		setModelDefaultValue(req , model);
		DatabaseUtils.reloadUserDatabaseInfo();
		model.addAttribute("dblist", SecurityUtil.loginInfo(req).getDatabaseInfo().values());

		return getModelAndView("/sqlFile", VIEW_PAGE.USER_PREFERENCES, model);
	}

}
