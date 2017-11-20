package xyz.softwareeureka.security.scrambler;


/**
 * Performs Tests on {@link ByteTools} and {@link EncodedMessage}. As well as
 * their interaction with {@link Type} and {@link Blueprint}. If they are not
 * working as intended. An Error Message will print out stating such.
 *  
 * @author Owen McMonagle.
 * @since 06/11/2017
 * @version 0.3
 * 
 * @see ByteTools
 * @see EncodedMessage
 * @see Blueprint
 * @see Smartprint
 * @see Type
 */
public final class TestManager 
{
	
	private boolean failed = false;
	
	TestManager() 
	{
		final boolean byte_tools_tests = ByteTools.performTests(),
				encoded_msg_tests = EncodedMessage.test(),
				storage_tests = Safe.test();
		
		if(byte_tools_tests && encoded_msg_tests && storage_tests)
			System.out.println("All Tests Completed...\nNo Errors found.");
		else
		{
			failed = true;
			System.err.println("All Tests Completed...\nErrors found.");
			System.err.println("Byte tools successful: " + byte_tools_tests);
			System.err.println("Encoding message successful: " + encoded_msg_tests);
			System.err.println("Safe storage successful: " + storage_tests);
		}
	
	}

	public static void main(String[] args) 
	{
		for(int i = 0; i < 10000; i ++)
			if(new TestManager().failed)
				System.exit(1);
	}

}

