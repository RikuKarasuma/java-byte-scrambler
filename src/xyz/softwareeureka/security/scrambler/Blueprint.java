package xyz.softwareeureka.security.scrambler;

import java.util.ArrayList;

/**
 * A Cipher Map. This Blueprint will enable Inheritors to create
 * variations of Byte Scrambler's which will allow for complex
 * Ciphers to be Masked and Unmasked with ease!  
 *  
 * @author Owen McMonagle.
 * @since 05/11/2017 Updated 19/11/2017
 * @version 0.4
 */
public abstract class Blueprint
{
	
	/**
	 * Each manipulation {@link Type}.
	 */
	private ArrayList<Type> manipulationTypes = new ArrayList<Type>(); 
	
	/**
	 * Indexes of each {@link Type} Position. Used to determine where
	 * the {@link Type} will take place from within the Byte.
	 */
	private byte[] indexes = new byte[0]; 
	
	/**
	 * Default Constructor.
	 */
	public Blueprint() {}
	
	/**
	 * Constructor for {@link Blueprint} re-construction via byte arrays.
	 * 
	 * @param types - Each byte is an ordinal value of {@link Type}.
	 * @param bit_indexes - Each byte is a bit index.
	 */
	public Blueprint(final byte[] types, final byte[] bit_indexes) 
	{
		if(types != null && bit_indexes != null)
		{
			indexes = new byte[bit_indexes.length];
			for(int i = 0; i < types.length; i ++)
			{
				this.manipulationTypes.add(Type.values()[types[i]]);
				this.indexes[i] = bit_indexes[i];
			}
		}
	}
	
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
	 * Retrieves the {@link Type} at the given parameter 'index'.
	 * @param index - Index of the {@link Type} to return.
	 * @return Type at index.
	 */
	public final Type getType(final int index)
	{
		return manipulationTypes.get(index);
	}
	
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
			System.out.println("Byte: " + byte_ + ", Index: " + indexes[byte_]
					+ ", Type: " + manipulationTypes.get(byte_).toString());
	}
	
	/**
	 * A bypass to ArrayLists addAll. Allows children to populate
	 * the manipulation {@link Type} list.
	 * @param list - List of {@link Type}s to populate this blueprint with.
	 */
	public final void addAll(ArrayList<Type> list)
	{
		if(list != null)
			manipulationTypes.addAll(list);
	}
	
	/**
	 * Prevents this object from being cloned.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException 
	{
		throw new CloneNotSupportedException();
	}
}
