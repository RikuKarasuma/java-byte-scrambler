package xyz.softwareeureka.security.scrambler;

import java.util.ArrayList;
import java.util.Random;

/**
 * A special {@link Blueprint} which molds itself to a specific Binary
 * String. This is done by taking the Binary String which this is to 
 * generate a Cipher {@link Type} Map for. Then generating a Random
 * {@link Type} and modified Index based upon each Byte within. This
 * ensures a completely new Cipher each time.
 *   
 * @author Owen McMonagle.
 * @version 0.4
 * @since 05/11/2017 Updated 08/11/2017
 * 
 * @see ByteTools
 * @see Blueprint
 * @see Type
 *
 */
public final class Smartprint extends Blueprint 
{
	private static final long serialVersionUID = -1363449840374640942L;
	
	/**
	 * Each Binary String in Array form. Used for the population
	 * of {@link Type}s and Indexes within 'populate' and 
	 * 'populateIndexes'. Null after use within Constructor.
	 */
	private String[] binary = null;
	
	/**
	 * Alternative Storage, for non Binary Strings. Null after use 
	 * within Constructor.
	 */
	private byte[] data = null;

	/**
	 * Takes in a Binary String where each Byte is Delimited with
	 * a '~' Character. Splits each Byte into an String Array. 
	 * After which, we populate our {@link Blueprint} with {@link Type}s,
	 * generate our Bit Indexes and finally dispose of our Binary
	 * String Array.
	 *   
	 * @param binary_str - Binary String where each Byte is delimited
	 * with a '~' Character.
	 */
	public Smartprint(final String binary_str) 
	{
		binary = binary_str.split(ByteTools.BYTE_DELIMITER);
		addAll(populate());
		populateIndexes();
		// Nullify as we no longer need it.
		binary = null;
	}
	
	/**
	 * Takes in a Binary Array String where each Byte is an Element in the
	 * Array. After which, we populate our {@link Blueprint} with {@link Type}s,
	 * generate our Bit Indexes and finally dispose of our Binary
	 * String Array.
	 *   
	 * @param binary_strs - Binary Strings where each Byte is an Element in the
	 * Array.
	 */
	public Smartprint(final String[] binary_strs) 
	{
		binary = binary_strs;
		addAll(populate());
		populateIndexes();
		// Nullify as we no longer need it.
		binary = null;
	}
	
	public Smartprint(final byte[] byte_array) 
	{
		data = byte_array;
		addAll(populate());
		populateIndexes();
		// Nullify as we no longer need it.
		data = null;
	}

	/**
	 * Generates a List of {@link Type}s the Length of the 'binary' or 
	 * 'data' Arrays.
	 * Each {@link Type} is randomly generated. Once completed the List is
	 * returned.
	 * @return ArrayList of randomly generated {@link Type}s. 
	 */
	@Override
	public ArrayList<Type> populate() 
	{
		final ArrayList<Type> types = new ArrayList<>();
		final int length = ((binary != null) ? binary.length : data.length),
				random_bound = Type.values().length;
		final Random random = new Random();
		for(int i = 0; i < length; i++)
			types.add(Type.values()[random.nextInt(random_bound)]);
		return types;
	}

	/**
	 * Generates an Array of Indexes and sets them within {@link Blueprint}. 
	 * Where each Index is a Bit Position within a Byte on the 'binary' or
	 * the 'data' Arrays.
	 * The Bit Position itself is random but requires Two Bits space on each
	 * Side of the Byte. This is so we can accommodate swapping Bits Left 
	 * and Right.
	 */
	@Override
	public void populateIndexes() 
	{
		final int length = (binary != null) ? binary.length : data.length, offset = 2;
		final Random random = new Random();
		setIndex(new byte[length]);
		for(int i = 0; i < length; i ++)
		{
			final byte boundary = (byte) ((binary != null) ? binary[i].length():
				(Integer.toBinaryString(data[i]).length()));
			byte next_index = (byte) random.nextInt(boundary - offset);
			// If Zero or One, correct in case of Swap.
			if(next_index == 0 || next_index == 1)
				next_index = 2;
			setIndex(i, next_index);
		}
	}

}
