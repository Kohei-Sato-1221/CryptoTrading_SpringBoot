package jp.nyatla.nyansat.utils;

public class BaseObject
{
	protected void info(String i_info)
	{
		System.out.println("[info]"+i_info);
	}
	protected void info(Object i_obj,String i_info)
	{
		this.info(i_obj.getClass().getSimpleName()+":"+i_info);
	}
}
