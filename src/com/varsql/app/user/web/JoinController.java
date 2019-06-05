package com.varsql.app.user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.user.beans.JoinForm;
import com.varsql.app.user.service.JoinServiceImpl;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.encryption.EncryptDecryptException;



/**
 * The Class OutsideController.
 */
@Controller
@RequestMapping("/join")
public class JoinController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(JoinController.class);
	
	@Autowired
	JoinServiceImpl joinServiceImpl;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("/join/joinForm");
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public @ResponseBody ResponseResult insertUserInfo(@Valid JoinForm	joinForm, BindingResult result, ModelAndView mav, HttpServletRequest req) throws EncryptDecryptException {
		ResponseResult resultObject = new ResponseResult();
		if(result.hasErrors()){
			
			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  saveVirtualPortal validation check {}",errorVal.toString());
			}
			resultObject.setResultCode(ResultConst.CODE.DATA_NOT_VALID.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.VALID.toString());
			resultObject.setItemList(result.getAllErrors());
		}
		
		int idCheck = joinServiceImpl.selectIdCheck(joinForm.getUid()).getItem(); 
		
		if(idCheck > 0){
			resultObject.setResultCode(ResultConst.CODE.DUPLICATES.toInt());
			resultObject.setMessageCode(ResultConst.ERROR_MESSAGE.CONFLICT.toString());
		}
		
		resultObject.setItemOne(joinServiceImpl.insertUserInfo(joinForm));
		
		return resultObject; 
	}
	
	@RequestMapping(value = "/idCheck")
	public @ResponseBody ResponseResult idCheck(@RequestParam(value = "uid")  String uid) {
		return joinServiceImpl.selectIdCheck(uid);
	}
}
