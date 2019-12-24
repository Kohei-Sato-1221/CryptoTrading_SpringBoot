package jp.nyatla.nyansat.utils;

import java.util.Date;

public class ArgHelper
{
	private String _args[];
	public ArgHelper(String s[])
	{
		this._args=s;
	}
	/**
	 * i_index番目の引数を取得します。
	 * @param i_index
	 * @param i_default
	 * @return
	 */
	public String getString(int i_index,String i_default)
	{
		if(this._args.length<=i_index) {
			return i_default;
		}
		return i_default;
	}	
	public String getString(String i_key,String i_default)
	{
		for(int i=0;i<this._args.length-1;i++){
			if(i_key.compareTo(this._args[i])==0){
				return this._args[i+1];
			}
		}
		return i_default;
	}
	/**
	 * i_keyのパラメタがi_match_stringに一致するかを検査する。
	 * @param i_key
	 * @param i_match_string
	 * @return
	 */
	public boolean isMatch(String i_key,String i_match_string)
	{
		for(int i=0;i<this._args.length-1;i++){
			if(i_key.compareTo(this._args[i])==0){
				return this._args[i+1].compareToIgnoreCase(i_match_string)==0;
			}
		}
		return false;
	}

	public int getInt(String i_key,int i_default)
	{
		return Integer.parseInt(this.getString(i_key,Integer.toString(i_default)));
	}
	public long getLong(String i_key,long i_default)
	{
		return Long.parseLong(this.getString(i_key,Long.toString(i_default)));
	}
	/**
	 * 2013-10-28-10-30みたいな日付フォーマットを解析
	 * @param i_key
	 * @param i_default
	 * @return
	 */
	public Date getDate(String i_key,Date i_default)
	{
		String s=this.getString(i_key,null);
		if(s==null){
			return i_default;
		}
		return StringUtils.strToDate(s);
	}
	public double getDouble(String i_key, double i_default) {
		return Double.parseDouble(this.getString(i_key,Double.toString(i_default)));
	}
	
}