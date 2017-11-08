package xyz.softwareeureka.security.scrambler;

/**
 * 
 * @author Owen McMonagle.
 * 
 * @since 08/11/2017
 *
 */
public final class BitException extends Exception 
{
	private static final long serialVersionUID = 8010099422995872304L;
	private static final String MESSAGE = "Invalid Bit Calculation Data: ";
	private ExceptionType type = null;
	
	public BitException(ExceptionType exception_type) 
	{
		type = exception_type;
	}
	
	@Override
	public String getMessage() 
	{
		return MESSAGE + type.getMessage();
	}
	
	public enum ExceptionType
	{
		RECAST_WARNING
		{
			@Override
			public String getMessage() 
			{
				return "Can't Swap here. After the Recast the Byte will no longer be recognizable.";
			}
		},
		SWAP_END
		{
			@Override
			public String getMessage() 
			{
				return "Can't Swap at End of Index.";
			}
		},
		NULL_OR_INVALID
		{
			@Override
			public String getMessage() 
			{
				return "Null or Invalid Data.";
			}
		},
		NEGATIVE_INDEX
		{
			@Override
			public String getMessage() 
			{
				return "Nagative Index Value.";
			}
		},
		GREATER_THAN_LENGTH
		{
			@Override
			public String getMessage() 
			{
				return "Bit Index greater than Array Bit Length.";
			}
		};
		
		public abstract String getMessage();
	}

}
