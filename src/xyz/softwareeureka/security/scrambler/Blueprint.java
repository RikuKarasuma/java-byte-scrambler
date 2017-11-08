package xyz.softwareeureka.security.scrambler;

import java.util.ArrayList;

/**
 * A Cipher Map. This Blueprint will enable Inheritors to create
 * variations of Byte Scrambler's which will allow for complex
 * Ciphers to be Masked and Unmasked with ease!  
 *  
 * @author Owen McMonagle.
 * @since 05/11/2017 Updated 06/11/2017
 * @version 0.3
 */
public abstract class Blueprint extends ArrayList<Type> 
{

	private static final long serialVersionUID = -7645608110916359771L;
	
	/**
	 * Indexes of each {@link Type} Position. Used to determine where
	 * the {@link Type} will take place from within the Byte.
	 */
	private byte[] indexes = new byte[0]; 
	
	/**
	 * List that populates the {@link Blueprint} with Types. How it does 
	 * this is up to the Inheritor.
	 * 
	 * @return List of {@link Type}s.
	 */
	public abstract ArrayList<Type> populate();
	
	/**
	 * Method which will populate the Byte Indexes. These Indexes are used
	 * to determine the Index of the {@link Type} action. Functionality is
	 * up to the Inheritor.
	 */
	public abstract void populateIndexes();
	
	/**
	 * Sets a new Index Value at the specified Index.
	 * @param index - Index to set Value in Array.
	 * @param index_value - Value to set.
	 */
	public final void setIndex(final int index, final byte index_value)
	{
		indexes[index] = index_value;
	}
	
	/**
	 * Sets a new Index Array.
	 * @param array_of_indexes - Index Array to set.
	 */
	public final void setIndex(final byte[] array_of_indexes)
	{
		indexes = array_of_indexes;
	}
	
	/**
	 * Returns the Index positions as a Byte Array.
	 * @return Byte Array of Index Positions. 
	 */
	public final byte[] getIndexes()
	{
		return indexes;
	}
	
	/**
	 * Returns the Index position as a Byte.
	 * @param index - Position to retrieve.
	 * @return Position where {@link Type} will be used within Byte.
	 */
	public final byte getIndex(final int index)
	{
		return indexes[index];
	}
	
	/**
	 * Prints the Byte position, Index position and {@link Type} within
	 * this {@link Blueprint}.
	 */
	public final void print()
	{
		for(int byte_ = 0; byte_ < indexes.length; byte_ ++)
			System.out.println("Byte: " + byte_ + "\nIndex: " + indexes[byte_]
					+ "\nType: " + get(byte_).toString());
	}

}
