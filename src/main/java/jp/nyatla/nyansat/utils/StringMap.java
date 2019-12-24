package jp.nyatla.nyansat.utils;

/**
 * 文字列-idのペアを定義するクラス。
 *
 */
public class StringMap
{
	private String[] _table;
	public StringMap(String[] i_table)
	{
		this._table=i_table;
	}
	public int getId(String i_st)
	{
		for(int i=0;i<this._table.length;i++){
			if(i_st.compareToIgnoreCase(this._table[i])==0){
				return i;
			}
		}
		throw new IndexOutOfBoundsException();
	}	
	public int getId(String i_st,int i_default)
	{
		for(int i=0;i<this._table.length;i++){
			if(i_st.compareToIgnoreCase(this._table[i])==0){
				return i;
			}
		}
		return i_default;
	}
	public boolean hasId(String i_st)
	{
		for(int i=0;i<this._table.length;i++){
			if(i_st.compareToIgnoreCase(this._table[i])==0){
				return true;
			}
		}
		return false;
	}
	public String getString(int i_idx)
	{
		return this._table[i_idx];
	}
	public int getSize()
	{
		return this._table.length;
	}
}
