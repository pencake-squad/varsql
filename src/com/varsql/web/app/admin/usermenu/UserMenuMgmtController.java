package com.varsql.web.app.admin.usermenu;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.common.constants.DBMenuConstants;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;
import com.varsql.web.util.VarsqlUtil;


/**
 * 
 * @FileName  : UserMenuMgmtController.java
 * @프로그램 설명 :
 * @Date      : 2015. 4. 16. 
 * @작성자      : ytkim
 * @변경이력 :
 */
@Controller
@RequestMapping("/admin/userMenu")
public class UserMenuMgmtController{

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(UserMenuMgmtController.class);
	
	@Autowired
	UserMenuMgmtService userMenuMgmtService; 
	
	@RequestMapping({"/listDbMenu"})
	public @ResponseBody String listDbMenu(
			@RequestParam(value = DBMenuConstants.DB_TYPE ,required = true) String db_type 
			,@RequestParam(value = VarsqlParamConstants.SEARCHVAL, required = false, defaultValue = "" )  String searchval
			,@RequestParam(value = VarsqlParamConstants.SEARCH_NO, required = false, defaultValue = "1" )  int pageNo
			,@RequestParam(value = VarsqlParamConstants.SEARCH_ROW, required = false, defaultValue = "10" )  int rows
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(DBMenuConstants.DB_TYPE, db_type);
		paramMap.put(VarsqlParamConstants.SEARCHVAL, searchval);
		paramMap.put(VarsqlParamConstants.SEARCH_NO, pageNo);
		paramMap.put(VarsqlParamConstants.SEARCH_ROW, rows);
		
		return userMenuMgmtService.listDbMenu(paramMap);
	}
	
	@RequestMapping({"/moodifyDbMenu"})
	public @ResponseBody String moodifyDbMenu(@RequestParam(value = DBMenuConstants.DB_TYPE ,required = true) String db_type 
			,@RequestParam(value = DBMenuConstants.MENU_ID ,required = true) String menu_id 
			,@RequestParam(value = DBMenuConstants.UNIQUE_ID ) String unique_id 
			,@RequestParam(value = DBMenuConstants.MENU_NM ) String menu_nm 
			,@RequestParam(value = DBMenuConstants.MENU_URL ) String menu_url 
			,@RequestParam(value = DBMenuConstants.MENU_PARAM ) String menu_param 
			,@RequestParam(value = DBMenuConstants.POPUP_YN ) String popup_yn 
			,@RequestParam(value = DBMenuConstants.VIEW_OPTION ) String view_option 
			,@RequestParam(value = DBMenuConstants.SORT_ORDER ) String sort_order 
			,@RequestParam(value = DBMenuConstants.DEPTH) String depth
			,@RequestParam(value = DBMenuConstants.USE_YN) String use_yn
			) throws Exception {
		
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(DBMenuConstants.DB_TYPE ,db_type );
		paramMap.put(DBMenuConstants.MENU_ID ,menu_id );
		paramMap.put(DBMenuConstants.UNIQUE_ID ,unique_id );
		paramMap.put(DBMenuConstants.MENU_NM ,menu_nm );
		paramMap.put(DBMenuConstants.MENU_URL ,menu_url );
		paramMap.put(DBMenuConstants.MENU_PARAM ,menu_param );
		paramMap.put(DBMenuConstants.POPUP_YN ,popup_yn );
		paramMap.put(DBMenuConstants.VIEW_OPTION ,view_option );
		paramMap.put(DBMenuConstants.SORT_ORDER ,sort_order );
		paramMap.put(DBMenuConstants.DEPTH,depth);
		paramMap.put(DBMenuConstants.USE_YN,use_yn);
		
		Map json = new HashMap();
		
		json.put("result", userMenuMgmtService.moodifyDbMenu(paramMap));
		
		return VarsqlUtil.objectToString(json);
	}
	
	@RequestMapping({"/addDbMenu"})
	public @ResponseBody String addDbMenu(@RequestParam(value = DBMenuConstants.DB_TYPE ,required = true) String db_type 
			,@RequestParam(value = DBMenuConstants.P_MENU_ID ,required = true) String p_menu_id 
			,@RequestParam(value = DBMenuConstants.UNIQUE_ID ) String unique_id 
			,@RequestParam(value = DBMenuConstants.MENU_NM ) String menu_nm 
			,@RequestParam(value = DBMenuConstants.MENU_URL ) String menu_url 
			,@RequestParam(value = DBMenuConstants.MENU_PARAM ) String menu_param 
			,@RequestParam(value = DBMenuConstants.POPUP_YN ) String popup_yn 
			,@RequestParam(value = DBMenuConstants.VIEW_OPTION ) String view_option 
			,@RequestParam(value = DBMenuConstants.SORT_ORDER ) String sort_order 
			,@RequestParam(value = DBMenuConstants.DEPTH) String depth
			,@RequestParam(value = DBMenuConstants.USE_YN) String use_yn
			) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(DBMenuConstants.DB_TYPE ,db_type );
		paramMap.put(DBMenuConstants.P_MENU_ID ,p_menu_id );
		paramMap.put(DBMenuConstants.UNIQUE_ID ,unique_id );
		paramMap.put(DBMenuConstants.MENU_NM ,menu_nm );
		paramMap.put(DBMenuConstants.MENU_URL ,menu_url );
		paramMap.put(DBMenuConstants.MENU_PARAM ,menu_param );
		paramMap.put(DBMenuConstants.POPUP_YN ,popup_yn );
		paramMap.put(DBMenuConstants.VIEW_OPTION ,view_option );
		paramMap.put(DBMenuConstants.SORT_ORDER ,sort_order );
		paramMap.put(DBMenuConstants.DEPTH,depth);
		paramMap.put(DBMenuConstants.USE_YN,use_yn);

		Map json = new HashMap();
		
		json.put("result", userMenuMgmtService.addDbMenu(paramMap));
		
		return VarsqlUtil.objectToString(json);
	}
	
	@RequestMapping({"/deleteDbMenu"})
	public @ResponseBody String deleteDbMenu(@RequestParam(value = DBMenuConstants.DB_TYPE ,required = true) String db_type 
			,@RequestParam(value = DBMenuConstants.MENU_ID, required = true)  String menuid) throws Exception {
		DataCommonVO paramMap = new DataCommonVO();
		
		paramMap.put(DBMenuConstants.DB_TYPE, db_type);
		paramMap.put(DBMenuConstants.MENU_ID, menuid);
		
		Map json = new HashMap();
		
		json.put("result", userMenuMgmtService.deleteDbMenu(paramMap));
		
		return VarsqlUtil.objectToString(json);
	}
}
