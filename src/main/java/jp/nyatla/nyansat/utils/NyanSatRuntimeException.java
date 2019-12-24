package jp.nyatla.nyansat.utils;

public class NyanSatRuntimeException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NyanSatRuntimeException(String s)
	{
		super(s);
	}
	public NyanSatRuntimeException()
	{
		super();
	}
	public NyanSatRuntimeException(Throwable e)
	{
		super(e);
	}
}
