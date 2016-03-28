package com.kami.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

import com.kami.tools.FileTools;
import com.opensymphony.xwork2.ActionSupport;
//忽视该action，这是我自己写的玩的
public class FileUploadAction extends ActionSupport {
	public File uploadFile;
	public String uploadFileFileName;
	public String username ;

	public String doUpload() {
		try {
			//System.out.println(username);
			System.out.println(uploadFileFileName);
//			File destFile = new File("E:\\Exp_data\\upLoadTest\\", uploadFileFileName);
//			SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//			String dateString = currentDateFormat.format(Calendar.getInstance().getTime());
//			dateString.replace(" ", "-");
			String basePath = "/users/user/GitDir/master_data/ExpData/OriginData/";
//			String basePath = "E:\\Master_Code\\ActivityDetect\\GitDir\\master_data\\ExpData\\OriginData\\";
			File fileDir = new File(basePath+FileTools.getDateAndType(uploadFileFileName));
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			File destFile = new File(basePath+FileTools.getDateAndType(uploadFileFileName), uploadFileFileName);
			FileUtils.copyFile(uploadFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
