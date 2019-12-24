package jp.nyatla.nyansat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils
{
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * YYYY-MM-DD形式の文字列に変換します。
	 * @param i_s
	 * @return
	 */
	public static synchronized String toDateString(Date i_s){
		return sdf.format(i_s);
	}	
	/**
	 * y-m-d,又はy-m-d-h-s表記の文字列をDate値に変換します。
	 * @param i_string
	 * @param i_default
	 * @return
	 */
	public static Date strToDate(String i_string)
	{
		String[] sl=i_string.split("-");
		Calendar ca=Calendar.getInstance();
		ca.setTimeZone(TimeZone.getDefault());
		ca.set(Calendar.MILLISECOND,0);
		if(sl.length==3){			
			//y-m-d
			ca.set(Integer.parseInt(sl[0]),Integer.parseInt(sl[1])-1,Integer.parseInt(sl[2]),0,0,0);
		}
		if(sl.length==5){
			//y-m-d-h-m
			ca.set(Integer.parseInt(sl[0]),Integer.parseInt(sl[1])-1,Integer.parseInt(sl[2]),Integer.parseInt(sl[3]),Integer.parseInt(sl[4]),0);
		}
		return ca.getTime();
	}
	/**
	 * hh:mm[:ss]形式を、0:00:00からの秒単位の経過時間に変換します。
	 * @param i_str_format_time
	 * @return
	 */
	public static int str2IntTime(String i_str_format_time)
	{
		String[] s=i_str_format_time.split(":");
		if(s.length==2){
			return (Integer.parseInt(s[0])*60+Integer.parseInt(s[1]))*60;
		}
		if(s.length==3){
			return (Integer.parseInt(s[0])*60+Integer.parseInt(s[1]))*60+Integer.parseInt(s[2]);
		}
		return -1;
	}
	public static String intTime2Str(int i_time)
	{
		int h=i_time/60/60;
		int m=(i_time-(h*60*60))/60;
		int s=i_time-(h*60*60)-(m*60);
		return String.format("%02d:%02d:%02d",h,m,s);
	}
	
	/**
	 * 文字列からハッシュ値を得ます。
	 * @param i_str
	 * @param i_hash_name
	 * @return
	 */
    public static String toHashString(String i_str,String i_hash_name){
        byte[] hash = null;
        MessageDigest md;
            try {
                md = MessageDigest.getInstance(i_hash_name);
                md.update(i_str.getBytes());  
                hash = md.digest();  
            } catch (NoSuchAlgorithmException e) {
                new NyanSatRuntimeException(e);
            }
        return hashByte2MD5(hash);
    }
    private static String hashByte2MD5(byte []hash) {  
        StringBuffer hexString = new StringBuffer();  
        for (int i = 0; i < hash.length; i++) {  
            if ((0xff & hash[i]) < 0x10) {  
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));  
            } else {  
                hexString.append(Integer.toHexString(0xFF & hash[i]));  
            }  
        }          
        return hexString.toString();  
    }
}
