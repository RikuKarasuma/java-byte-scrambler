package xyz.softwareeureka.security.scrambler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implements file save and read functions for various objects. 
 * 
 * @see Blueprint
 * @see EncodedMessage
 * 
 * @author Owen McMonagle.
 * @since 18/11/2017
 * @version 0.3
 */
public interface FileImpl 
{
	
	abstract File toFile();
	static File toFile(String path, Object to_save)  
	{
		File file = null;
		if(path != null && !path.isEmpty() && to_save != null)
		{
			FileOutputStream file_output = null;
			ObjectOutputStream object_output_stream = null;
			try
			{
				file = new File(path);
				file_output = new FileOutputStream(file);
				object_output_stream = new ObjectOutputStream(file_output);
				object_output_stream.writeObject(to_save);
				object_output_stream.flush();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally 
			{
				try {
					object_output_stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	abstract Object fromFile();
	static Object fromFile(String path)
	{
		Object read_in = null;
		if(path != null && !path.isEmpty())
		{
			ObjectInputStream object_input_stream = null;
			FileInputStream file_input;
			try
			{
				file_input = new FileInputStream(new File(path));
				object_input_stream = new ObjectInputStream(file_input);
				read_in = object_input_stream.readObject();
			}
			catch(ClassNotFoundException | IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(object_input_stream != null)
					try {
						object_input_stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return read_in;
	}
}
