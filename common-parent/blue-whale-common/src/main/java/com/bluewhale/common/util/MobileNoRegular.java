package com.bluewhale.common.util;


/**
 * Created by Glenn on 2017/5/11 0011.
 */
public class MobileNoRegular {
	private final static String DEFAULT_PREFIX = "+86";

	private final static String MOBILE_REGULAR="^1\\d{10}$";

	private String _prefix;

	private String _value;

	private boolean _isValidMobileNo = false;

	public String getPrefix()
	{
		return _prefix;
	}

	public boolean isValidMobileNo()
	{
		return _isValidMobileNo;
	}

	public String getValue()
	{
		return _value;
	}

	public MobileNoRegular(String mobileno) {
		this(mobileno, null);
	}

	public String generateDefaultNickName(){
		return this._value.substring(_value.length()-5);
	}

	public MobileNoRegular(String mobileno, String prefix)
	{
		try
		{
			if (null == mobileno || "".equals(mobileno.trim()))
				return;
			int startidx = 0;
			char[] chr = mobileno.toCharArray();

			if (mobileno.startsWith("00"))
			{
				_prefix = "+" + String.valueOf(chr[2]) + String.valueOf(chr[3]);
				startidx = 4;
			}
			// else if (chr[0] == '+')
			else if (mobileno.startsWith("+"))
			{
				_prefix = mobileno.substring(0, 3);
				startidx = 3;
			}
			else
			{
				_prefix = prefix == null ? DEFAULT_PREFIX : prefix;
			}
			_value = mobileno.substring(startidx, mobileno.length());

			_isValidMobileNo = CommonUtil.checkRegular(MOBILE_REGULAR,_value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("The Paremters is mobileno: " + mobileno + " prefix is: " + prefix);
			_isValidMobileNo = false;
			_value = null;
			_prefix = null;
		}
	}

	@Override
	public String toString()
	{
		if (!_isValidMobileNo)
			return StringUtil.EMPTYSTRING;

		StringBuffer sb = new StringBuffer();
		if (!_prefix.equals(StringUtil.EMPTYSTRING))
			sb.append(_prefix);
		else
			sb.append(DEFAULT_PREFIX);
		sb.append(_value);
		return sb.toString();
	}
}