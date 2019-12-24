package jp.nyatla.nyansat.utils;

/**
 * Copied from http://d.hatena.ne.jp/nattou_curry_2/20100101/1262358761
 *
 */
public class Base64 {
	/**
	 * 文字列をBASE64エンコードします。
	 * @param text エンコード対象の文字列
	 * @return エンコード後の文字列
	 */
	public static String base64Encode( String text ) {

		// 文字列をASCIIのバイト配列に変換します。
		byte[] bytes;
		try {
			bytes = text.getBytes( "ASCII" );
		} catch ( java.io.UnsupportedEncodingException e ) {
			// ASCIIへの変換に失敗:
			throw new NyanSatRuntimeException( e );
		}

		return base64Encode( bytes );
	}

	/**
	 * バイト配列をBASE64エンコードします。
	 * @param bytes エンコード対象のバイト配列
	 * @return エンコード後の文字列
	 */
	public static String base64Encode( byte[] bytes ) {

		// バイト配列をビットパターンに変換します。
		StringBuffer bitPattern = new StringBuffer();
		for ( int i = 0; i < bytes.length; ++i ) {
			int b = bytes[i];
			if ( b < 0 ) {
				b += 256;
			}
			String tmp = Integer.toBinaryString( b );
			while ( tmp.length() < 8 ) {
				tmp = "0" + tmp;
			}
			bitPattern.append( tmp );
		}

		// ビットパターンのビット数が6の倍数にするため、末尾に0を追加します。
		while ( bitPattern.length() % 6 != 0 ) {
			bitPattern.append( "0" );
		}

		// 変換表
		final String[] table = {
			 "A", "B", "C", "D", "E", "F", "G", "H",
			 "I", "J", "K", "L", "M", "N", "O", "P",
			 "Q", "R", "S", "T", "U", "V", "W", "X",
			 "Y", "Z", "a", "b", "c", "d", "e", "f",
			 "g", "h", "i", "j", "k", "l", "m", "n",
			 "o", "p", "q", "r", "s", "t", "u", "v",
			 "w", "x", "y", "z", "0", "1", "2", "3",
			 "4", "5", "6", "7", "8", "9", "+", "/"
		};

		// 変換表を利用して、ビットパターンを4ビットずつ文字に変換します。
		StringBuffer encoded = new StringBuffer();
		for ( int i = 0; i < bitPattern.length(); i += 6 ) {
			String tmp = bitPattern.substring( i, i + 6 );
			int index = Integer.parseInt( tmp, 2 );
			encoded.append( table[index] );
		}

		// 変換後の文字数を4の倍数にするため、末尾に=を追加します。
		while ( encoded.length() % 4 != 0 ) {
			encoded.append( "=" );
		}

		return encoded.toString();
	}
}
