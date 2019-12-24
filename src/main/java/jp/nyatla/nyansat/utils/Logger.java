package jp.nyatla.nyansat.utils;

import java.util.ArrayList;


public class Logger
{
	private static boolean need_ln=false;
	private static ArrayList<String> stack=new ArrayList<String>(); 
	public static void start_section(String i_section)
	{
		logln("<"+i_section+">");
		stack.add(i_section);
		need_ln=false;
	}
	public static void start_section()
	{
		StackTraceElement el=Thread.currentThread().getStackTrace()[2];
		String n=el.getClassName()+"#"+el.getMethodName();
		start_section(n);
	}
	public static void end_section()
	{
		if(need_ln){
			logln("");
		}
		if(!stack.isEmpty()){
			logln("</"+(stack.get(stack.size()-1))+">");
			stack.remove(stack.size()-1);
		}
	}
	public static void logln()
	{
		logln("");
	}
	public static void logln(String s)
	{
		System.out.println(s);
		need_ln=false;
		llen=0;
	}
	public static int llen=0;
	public static void log(String s)
	{
		if(s.charAt(s.length()-1)!='\n'){
			need_ln=true;
			llen+=s.length();
			if(llen>80){
				need_ln=false;
				llen=0;
				System.out.println();
			}
		}
		System.out.print(s);
	}


}
