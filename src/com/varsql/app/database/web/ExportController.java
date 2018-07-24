package com.varsql.app.database.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.app.database.beans.PreferencesInfo;
import com.varsql.app.database.service.ExportServiceImpl;
import com.varsql.core.db.beans.DatabaseParamInfo;

/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ExportController.java
* @DESC		: export 처리. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2017. 11. 21. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@Controller
@RequestMapping("/database/tools/export")
public class ExportController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExportController.class);
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;

	@RequestMapping("/main")
	public ModelAndView exportMain(DatabaseParamInfo databaseParamInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		return  new ModelAndView("/database/tools/exportMain",model);
	}
	
	@RequestMapping("/table")
	public ModelAndView table(PreferencesInfo preferencesInfo, ModelAndView mav, HttpServletRequest req) throws Exception {
		ModelMap model = mav.getModelMap();
		exportServiceImpl.selectTableExportConfigInfo(preferencesInfo, model);
		return  new ModelAndView("/database/tools/export/table",model);
	}
	
	@RequestMapping("/tableExport")
	public void tableExport(PreferencesInfo preferencesInfo, HttpServletRequest req,  HttpServletResponse res) throws Exception {
		exportServiceImpl.tableExport(preferencesInfo, res);
	}
}
