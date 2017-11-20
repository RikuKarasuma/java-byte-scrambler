package xyz.softwareeureka.security.scrambler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Encapsulates {@link EncodedMessage} and {@link Blueprint} into a file
 * for storage. Done so in a non serialized manner as to not leak Class
 * information.
 *  
 * @author Owen McMonagle
 * @see EncodedMessage
 * @see Blueprint
 * @see Smartprint
 * @see FileImpl
 * @version 0.3
 * @since 18/11/2017 updated 19/11/2017.
 */
public final class Safe implements FileImpl
{

	/**
	 * Default name for Safe file.
	 */
	private static final String DEFAULT_NAME = "de_cipher.safe";
	
	/**
	 * Encoded message to store in Safe file.
	 */
	private EncodedMessage msg = null;
	
	/**
	 * Key to store in Safe file.
	 */
	private Blueprint key = null;
	
	/**
	 * Path to the Safe file.
	 */
	private String path = "";
	
	/**
	 * Default Constructor.
	 */
	public Safe(){}
	
	/**
	 * Constructor which allows to set file path.
	 * @param file_path - Path of Safe file.
	 */
	public Safe(String file_path)
	{
		path = file_path;
	}
	
	/**
	 * Sets file path, {@link EncodedMessage} and {@link Blueprint}.
	 * @param file_path - Path to the {@link Safe} file.
	 * @param encoded_msg - {@link EncodedMessage} to store.
	 * @param msg_key - Cipher {@link Blueprint} to the {@link EncodedMessage}.
	 */
	public Safe(String file_path, EncodedMessage encoded_msg, Blueprint msg_key)
	{
		path = file_path;
		msg = encoded_msg;
		key = msg_key;
	}
	
	/**
	 * Retrieves {@link Safe} data from the parameter 'safe_file'.
	 * The data is then set to this {@link Safe}. 
	 * @param safe_file - {@link Safe} file.
	 */
	public Safe(File safe_file)
	{
		path = safe_file.getAbsolutePath();
		Safe file_safe = new Safe(path).fromFile();
		this.key = file_safe.key;
		this.msg = file_safe.msg;
	}
	
	/**
	 * Retrieves the {@link EncodedMessage} associated with this 
	 * {@link Safe}.
	 * @return {@link EncodedMessage} stored in this {@link Safe}.
	 */
	public EncodedMessage getMsg()
	{
		return msg;
	}
	
	/**
	 * Retrieves the {@link Blueprint} associated with this 
	 * {@link Safe}s {@link EncodedMessage}.
	 * @return This {@link Safe}s {@link EncodedMessage} {@link Blueprint}.
	 */
	public Blueprint getMap()
	{
		return key;
	}
	
	/**
	 * Sets the path to the {@link Safe} file.
	 * @param file_path - Path to {@link Safe} file.
	 */
	public void setPath(String file_path)
	{
		path = file_path;
	}

	/**
	 * Saves the {@link EncodedMessage} and {@link Blueprint} to a file
	 * at the parameter 'path'. If 'path' is empty, then the default name
	 * 'DEFAULT_NAME' string is used. The saved {@link Safe} file is then 
	 * returned. The format of the {@link Safe} file  is as follows:<br><br>
	 * <ul>
	 * 	<li>(Line 1) Each byte is a {@link Type} ordinal.</li>
	 * 	<li>(Line 2) Each byte is a index value of {@link Type}.</li>
	 * 	<li>(Line 3) Each byte is part of the {@link EncodedMessage}.</li>
	 * </ul>
	 * 
	 * @return Saved {@link Safe} file.
	 */
	@Override
	public File toFile() 
	{
		if(msg == null || key == null)
		{
			System.err.println("Encoded message or Key is null...");
			return null;
		}
		if(path.isEmpty()) path = DEFAULT_NAME;
		File file = null;
		BufferedWriter data_out = null;
		try
		{
			file = new File(path);
			// Init streams...
			FileOutputStream safe_output_str = new FileOutputStream(file);
			data_out = new BufferedWriter(new OutputStreamWriter(safe_output_str));
			final int key_length = key.getIndexes().length;
			// Write Type ordinal's to file...
			for(int i = 0; i < key_length; i ++)
				data_out.write((byte)key.getType(i).ordinal()); 
			data_out.newLine();
			// Write Type indexes to file...
			for(int i = 0; i < key_length; i ++)
				data_out.write(key.getIndex(i)); // Write Bit Index.
			data_out.newLine();
			// Write encoded byte's to file...
			for(int i = 0; i < msg.getEncoded().length; i ++)
				data_out.write(msg.getEncoded()[i]); // Write msg.
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally {
			try {
				if(data_out != null)
					data_out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * Reads the {@link EncodedMessage} and {@link Blueprint} from a file
	 * at the parameter 'path'. If 'path' is empty, then the default name
	 * 'DEFAULT_NAME' string is used. The read {@link Safe} is then returned.
	 * The format of the {@link Safe} file  is as follows:<br><br>
	 * <ul>
	 * 	<li>(Line 1) Each byte is a {@link Type} ordinal.</li>
	 * 	<li>(Line 2) Each byte is a index value of {@link Type}.</li>
	 * 	<li>(Line 3) Each byte is part of the {@link EncodedMessage}.</li>
	 * </ul>
	 * 
	 * @return Read {@link Safe} from file.
	 */
	@Override
	public Safe fromFile() 
	{
		if(path.isEmpty()) path = DEFAULT_NAME;
		Safe loaded_safe = null;
		BufferedReader data_in = null;
		try
		{
			// Init streams..
			FileInputStream safe_output_str = new FileInputStream(new File(path));
			data_in = new BufferedReader(new InputStreamReader(safe_output_str, "UTF-8"));
			
			// Read in data...
			byte[] types = data_in.readLine().getBytes();
			byte[] indexes = data_in.readLine().getBytes();
			byte[] data = data_in.readLine().getBytes();
			
			// Recreate cipher key...
			Blueprint key = new Blueprint(types, indexes) {
				public void populateIndexes(){}
				public ArrayList<Type> populate(){return null;}
			};
			
			// Recreate Safe object.
			loaded_safe = new Safe(path, new EncodedMessage(data), key);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try {
				if(data_in != null)
					data_in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return loaded_safe;
	}
	
	/**
	 * Overridden to provide a little bit of Runtime Security.
	 * Throws new {@link CloneNotSupportedException}.
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException 
	{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works. Within this context
	 * our {@link EncodedMessage} and generated {@link Blueprint}
	 * is saved to file, then recreated and tested for discrepancies. 
	 * 
	 * @return True if {@link EncodedMessage}, {@link Blueprint}
	 * and {@link ByteTools} works as intended with {@link Safe}.
	 */
	public static boolean test()
	{
		System.out.println("Beginning Safe storage Tests...");
		String text_str = "Hello World, I hope there's no more testing to be done here.",
				desktop_path = System.getenv("HOMEDRIVE")+System.getenv("HOMEPATH")+"\\Desktop\\Message_2";
		
		final EncodedMessage msg = new EncodedMessage(text_str);
		
		Safe safe = (Safe) new Safe(desktop_path, msg, msg.getMap());
		safe.toFile();
		
		System.out.println("Encoded: " + new String(msg.getEncoded()));
		
		safe = (Safe) new Safe(desktop_path, null, null).fromFile();
		
		final byte[] decoded = safe.msg.getDecoded(safe.getMap());
		System.out.println("Decoded: " + new String(decoded));
		
		boolean match = text_str.getBytes().equals(safe.msg.getEncoded()),
				success = new String(decoded).equals(text_str) && !match;
		
		
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		
		return success;
	}
}
