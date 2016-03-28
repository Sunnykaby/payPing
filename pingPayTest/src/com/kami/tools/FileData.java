package com.kami.tools;

import java.io.*;
import java.util.*;
public class FileData
{
	final int EOF=-1;
	private double[][] source;//数据源矩阵
	File f;
	public FileData(String data_txt)
	{
		f=new File(data_txt);
	}
	public double[][] get_source() throws IOException
	{
		String a=new String();
		String b=new String();
		Vector v=new Vector();
		int row=0;//数据源矩阵的行
		int col=0;//列
		FileReader in=new FileReader(f);
		BufferedReader inTxt=new BufferedReader(in);
		inTxt.mark((int)f.length()+1);//在文档开头设mark
		/////////////////////////求二维数据的行和列//////////////////////////
		while(inTxt.read()!=EOF)
		{
			row++;
			inTxt.readLine();//readLine()是一个很有用的方法
		}
		inTxt.reset();//回到文档开头mark
		a=inTxt.readLine();
		StringTokenizer wordString=new StringTokenizer(a);
		while(wordString.hasMoreTokens())
		{
			v.add(wordString.nextToken());//StringTokenizer类的nextToken（）方法可以读入单个字符串，是个很有用
			//的方法
		}
		col=v.size()-1;//去掉#
		////////////////////////将文本文件中的数组读入存于double数组source///////////////////////
		source=new double[row][col];//定义读入数据存放的数组
		inTxt.reset();//回到文档开头mark
		int i,j;
		for(i=0;inTxt.read()!=EOF;i++)
		{
			a=inTxt.readLine();
			StringTokenizer wordSS=new StringTokenizer(a);
			for(j=0;wordSS.hasMoreTokens();j++)
			{
				b=wordSS.nextToken();
				source[i][j]=Double.valueOf(b).doubleValue();//该方法将String类型的小数b转化double型（很关键）
				//另一个可以用于该功能的是方法new Float(b).doubleValue()
			}
		}
		in.close();
		return source;
	}
}
