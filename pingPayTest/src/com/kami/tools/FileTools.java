package com.kami.tools;

public class FileTools {
	public static Integer getTypeFromFileName(String fileName) {
		String type = fileName.substring(fileName.lastIndexOf("_")+1,fileName.lastIndexOf("."));
		return Integer.parseInt(type);
	}
	
	public static String getDateFromFileName(String fileName){
		String dateString = fileName.substring(fileName.indexOf("_")+1,fileName.lastIndexOf("_"));
		return dateString;
	}
	public static String getDateAndType(String fileName){
		String dateAndTypeString = fileName.substring(fileName.indexOf("_")+1,fileName.lastIndexOf("."));
		return dateAndTypeString;
	}
	
	public static void main(String[] args){
		System.out.println(getTypeFromFileName("rotation_2015-07-29-12-44-01_1.log"));
		System.out.println(getDateFromFileName("rotation_2015-07-29-12-44-01_1.log"));
	}
}
