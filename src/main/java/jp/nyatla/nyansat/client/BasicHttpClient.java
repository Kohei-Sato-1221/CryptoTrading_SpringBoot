package jp.nyatla.nyansat.client;

import jp.nyatla.nyansat.utils.BaseObject;
import jp.nyatla.nyansat.utils.NyanSatRuntimeException;

import java.io.*;
import java.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;

public class BasicHttpClient extends BaseObject {

	public BasicHttpClient() throws NyanSatRuntimeException
	{
	}
	protected String _ua=null;
	protected String _coookie=null;
	protected final String CHAR_SET="UTF-8";
	public void setSession(String i_user_agent,String i_cookie)
	{
		this._ua=i_user_agent;
		this._coookie=i_cookie;
		
	}
	private Integer _i_last_status_code;
	private String _i_last_body;
	/**
	 * パース済のHTMLドキュメントをgetリクエストで取得します。
	 * @throws IOException 
	 */
	public Document getHtmlDocument(String i_request) throws IOException
	{
		String s=this.getTextContents(i_request);
		if(s==null){
			return null;
		}
		return Jsoup.parse(s,CHAR_SET);
	}
	public Document getHtmlDocument(String i_request,int i_retry) throws IOException
	{
		String s=this.getTextContents(i_request,CHAR_SET,i_retry);
		if(s==null){
			return null;
		}
		return Jsoup.parse(s,CHAR_SET);
	}	
	/**
	 * テキストをGetリクエストで取得します。
	 * @throws IOException 
	 */
	public String getTextContents(String i_request,String i_charset) throws IOException
	{
		return this.getTextContents(i_request,null, i_charset);
	}
	public String getTextContents(String i_request,String[][] i_additional_header,String i_charset) throws IOException
	{
		this._i_last_body=null;
		this._i_last_status_code=null;
		HttpURLConnection con =this.makeConnection(i_request,"GET");
		try {
			con.setDoOutput(false);
			// 送信
			if(i_additional_header!=null){
				for(String[] i:i_additional_header){
					con.addRequestProperty(i[0],i[1]);
				}
			}			
			con.connect();
			this._i_last_status_code=con.getResponseCode();
			switch(this._i_last_status_code){
			case 200:
				break;
			default:
				this._i_last_body=this.getResponse(con, i_charset);
				return null;
			}
			String ret=this.getResponse(con, i_charset);
			return ret;
		}finally{
			con.disconnect();
		}
	}	
	
	
	
	public String getTextContents(String i_request,String i_charset,int i_retry) throws IOException
	{
		for(int i=0;i<i_retry;i++){
			String d=this.getTextContents(i_request,i_charset);
			if(d!=null){
				return d;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				break;
			}
		}
		return null;
	}

	/**
	 * 200,201以外はエラーをエラーです。
	 * 接続エラーの場合は例外が発生します。
	 * @param i_additional_header
	 * [][0]にキー名、[][1]に値
	 * @return 
	 * 成功すればresponsebody
	 */
	public String postTextContents(String i_request,String i_charset,String[][] i_additional_header,String i_body) throws IOException
	{
		this._i_last_body=null;
		this._i_last_status_code=null;		
		HttpURLConnection con =this.makeConnection(i_request,"POST");
		try {
			con.setDoOutput(true);
			// 送信
			if(i_additional_header!=null){
				for(String[] i:i_additional_header){
					con.addRequestProperty(i[0],i[1]);
				}
			}
			con.connect();
			con.getOutputStream().write(i_body.getBytes());
			this._i_last_status_code=con.getResponseCode();
			switch(this._i_last_status_code){
			case 200:
			case 201:
				break;
			default:
				this._i_last_body=this.getResponse(con, i_charset);
				return null;
			}
			String ret=this.getResponse(con, i_charset);
			return ret;
		}finally{
			con.disconnect();
		}
	}	
	
	/**
	 * 接続エラーの場合は例外が発生します。
	 * HTTPエラーの場合はnullを返します。
	 * @throws IOException 
	 */
	public String getTextContents(String i_request) throws IOException
	{
		return this.getTextContents(i_request,CHAR_SET);
	}
	
	
	protected HttpURLConnection makeConnection(String i_url,String i_method)
	{
		try {
			// アドレス設定、ヘッダー情報設定
			URL url = new URL(i_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(30*1000);
			con.setConnectTimeout(15*1000);
			con.setRequestMethod(i_method); // POSTまたはGET
			con.setInstanceFollowRedirects(true);// 勝手にリダイレクトさせない
			con.setRequestProperty("Host", url.getHost()+ (url.getPort() == 80 ? "" : ":"+url.getPort()));
			if(this._ua!=null){
				con.setRequestProperty("User-Agent",this._ua);
			}
			con.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.7");
			con.setRequestProperty("Accept-Language", "ja,en-us;q=0.7,en;q=0.3");
			con.setRequestProperty("Connection", "keep-alive");
			if(this._coookie!=null){
				con.setRequestProperty("Cookie",this._coookie);
			}
			return con;
		} catch (IOException e) {
			throw new NyanSatRuntimeException(e);
		}
	}
	/**
	 * Requestを送信した後にResponseテキストを回収する関数。
	 * contentLengthが0以外の時にコンテンツを取得する。
	 * 
	 * @param i_con
	 * @param i_char_set
	 * @return
	 * 取得したコンテンツ。取得できなければnull
	 * @throws NyanSatRuntimeException
	 * @throws IOException
	 */
	protected String getResponse(HttpURLConnection i_con,String i_char_set) throws IOException
	{
		// body部の文字コード取得
		String contentType = i_con.getHeaderField("Content-Type");
		if(contentType==null){
			return null;
		}
		//0以外の場合(不明-1の場合も含む)はコンテンツを読みだそうとするよ。
		if(i_con.getContentLengthLong()==0) {
			return null;
		}
		for (String elm : contentType.replace(" ", "").split(";")) {
			if (elm.startsWith("charset=")) {
				i_char_set = elm.substring(8);
				break;
			}
		}		
		StringBuffer b = new StringBuffer();
		// body部受信
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(i_con.getInputStream(), i_char_set));
			try {
				char[] cbuf = new char[4096];
				int l = br.read(cbuf);
				while (l > 0) {
					b.append(cbuf, 0, l);
					l = br.read(cbuf);
				}
			}finally {
				br.close();
			}
		} catch (UnsupportedEncodingException e) {
			throw new NyanSatRuntimeException(e);
		}
		return b.toString();
	}



	/**
	 * 最後に実行したHTTPリクエストのステータスコード。
	 * 例外発生時はnull
	 * @return
	 */
	public int getLastStatus() {
		return this._i_last_status_code;
	}
	/**
	 * 最後に実行したHTTPリクエストのBody
	 * 取得できなかった場合はnull
	 * @return
	 */	
	public String getLastErrorBody()
	{
		return this._i_last_body;
	}
}
