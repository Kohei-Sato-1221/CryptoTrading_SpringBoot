package jp.nyatla.nyansat.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 下記フォーマットのJSONを出力する。
 * {
 * 	version:FIXED_TEXT,
 * 	name:NAME,
 * 	index:[INDEX,INDEX,...],
 * 	value:[
 * 		[V1,V2...],
 * 		[V1,V2...]
 * 	]
 * }
 * @author nyatla
 *
 */
public abstract class BasicJsonExporter
{
	public static final String VERSION="NynSatTableDump/0.1";
	private String _name;
	public BasicJsonExporter(String i_name) throws NyanSatRuntimeException
	{
		this._name=i_name;
	}
	/**
	 * JSONを出力する。
	 * @param s
	 * @throws NyanSatRuntimeException 
	 */
	public String convert2Json(ResultSet i_rs) throws NyanSatRuntimeException
	{
		StringBuffer buf=new StringBuffer(1024);
		buf.append("{\n");
		buf.append("\"version\":\""+VERSION+"\",\n");
		buf.append("\"name\":\""+this._name+"\",\n");
		{
			buf.append("\"index\":");
			this.appendInedexElement(i_rs,buf);
			buf.append(",\n");
		}
		{
			buf.append("\"data\":[\n");
			this.appendDataRows(i_rs,buf);
			buf.append("]}\n");
		}
		return buf.toString();
	}
	/**
	 * ResultSetから[n,n,n],[n,n,n]で構成されるデータを出力する関数。
	 * 継承クラスで実装する。
	 * @param s
	 * @return
	 */
	protected abstract void appendDataRows(ResultSet s,StringBuffer i_buf) throws NyanSatRuntimeException;
	/**
	 * ResultSetからindexを構成して出力する。
	 * @param rs
	 * @return
	 * @throws NyanSatRuntimeException
	 */
	private int appendInedexElement(ResultSet rs,StringBuffer i_buf) throws NyanSatRuntimeException
	{
		try{
			ResultSetMetaData rsm = rs.getMetaData(); //メタデータ取得
			int l=rsm.getColumnCount();
			i_buf.append("{");
			for (int i = 1 ;;i++){
				i_buf.append("\""+rsm.getColumnName(i)+"\":"+(i-1));
				if(i<l){
					i_buf.append(",");
					continue;
				}
				break;
			}
			i_buf.append("}");
			return l-1;
		}catch(SQLException e){
			throw new NyanSatRuntimeException(e);
		}
	}
}
