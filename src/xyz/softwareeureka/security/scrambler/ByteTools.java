package xyz.softwareeureka.security.scrambler;

import java.nio.ByteBuffer;

import xyz.softwareeureka.security.scrambler.BitException.ExceptionType;

/**
 * A Static Byte Binary Tools Class related to scrambling and 
 * unscrambling data. Some of these Tools may depend on the Twos
 * Complement System and have not been tested outside of such.
 *  
 * @author Owen McMonagle.
 * @version 0.4
 * @since 05/11/2017 Updated 08/11/2017
 * 
 * @see Blueprint
 * @see Type
 */
public final class ByteTools 
{

	/**
	 * Represents the Byte Bit Length.
	 * @since 0.4
	 */
	public static final byte BYTE_LENGTH = 8;
	
	/**
	 * Represents the Bit One as Integer.
	 * @since 0.4
	 */
	public static final byte BIT_ONE_ = 1;
	
	/**
	 * Represents the Bit Zero as Integer.
	 * @since 0.4
	 */
	public static final byte BIT_ZERO_ = 0;
	
	/**
	 * Binary String Byte Delimiter. Essentially is used to separate
	 * Bytes within Binary Strings used within this library.
	 * @since 0.1
	 */
	public static final String BYTE_DELIMITER = "~";
	
	/**
	 * Represents the Bit One as Char.
	 * @since 0.1
	 */
	public static final char BIT_ONE = '1';
	
	/**
	 * Represents the Bit Zero as Char.
	 * @since 0.1
	 */
	public static final char BIT_ZERO = '0';
	
	/**
	 * Private Constructor as this is a Static Class.
	 * @since 0.1
	 */
	private ByteTools(){}
	
	/**
	 * Runs both basic and advanced scrambling tests. If both
	 * were successful then True is returned. 
	 * 
	 * @return True if Tests are successful, false otherwise.
	 * @since 0.1
	 */
	public static boolean performTests()
	{
		return basicBinaryStrTests() && advancedBinaryStrTests();
	}

	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works.
	 * 
	 * @return True if Scrambler and Unscrambler works.
	 * @since 0.1
	 */
	public static boolean basicBinaryStrTests()
	{
		System.out.println("Beginning Basic Tests...");
		String text = "1234567890abcdefdh";
		StringBuilder unscrambled = stringtoBinary(text);
		StringBuilder scrambled = basicScramble(unscrambled, false);
		unscrambled = basicScramble(scrambled, true);
		
		byte[] decoded = array(unscrambled.toString());
		byte[] encoded = array(scrambled.toString());
		
		final String decoded_text = new String(decoded);
		final boolean match = decoded.equals(encoded), 
				success = decoded_text.equals(text) && !match;
		
		System.out.println("Encoded: "+new String(encoded));
		System.out.println("Decoded: "+new String(decoded));
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		return success;
	}
	
	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works.
	 * 
	 * @return True if Scrambler and Unscrambler works.
	 * @see Smartprint
	 * @see Type
	 * @since 0.3
	 */
	public static boolean advancedBinaryStrTests()
	{
		System.out.println("Beginning Advanced Tests...");
		String text = "Hello World. I hope this is a Cipher worthy of the Matrix.";
		String binary = stringtoBinary(text).toString();
		final Smartprint blueprint = new Smartprint(binary);
		binary = scramble(binary, blueprint);
		final byte[] scrambled = array(binary);
		binary = scramble(binary, blueprint);
		final byte[] unscrambled = array(binary);
		
		final String translated = new String(unscrambled);
		final boolean match = unscrambled.equals(scrambled), 
				success = translated.equals(text) && !match;
		
		System.out.println("Encoded: "+new String(scrambled));
		System.out.println("Decoded: "+new String(unscrambled));
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		return success;
	}
	
	
	/**
	 * Takes a String, converts it to a Binary String and scrambles each 
	 * Byte within it. The first Bit of every Byte will be inverted to 
	 * either a '1' or '0' depending on the 'reverse' flag. The rebuilt 
	 * Binary String is then returned in Byte Array form.
	 * @param to_scramble - Binary String to scramble.
	 * @param reverse - False to scramble to '1', True to unscramble to '0'.
	 * @return Scrambled Binary String.
	 * @since 0.2
	 */
	public static byte[] convertAndScramble(final boolean reverse, final String to_scramble)
	{
		return array(basicScramble(stringtoBinary(to_scramble), reverse).toString());
	}
	
	/**
	 * Takes a Binary String and scrambles each Byte within it. The first
	 * Bit of every Byte will be inverted to either a '1' or '0' depending
	 * on the 'reverse' flag. The rebuilt Binary String is then returned in
	 * Byte Array form. 
	 * @param to_scramble - Binary String to scramble.
	 * @param reverse - False to scramble to '1', True to unscramble to '0'.
	 * @return Scrambled Binary String. 
	 * @since 0.1
	 */
	public static byte[] basicScramble(final boolean reverse, final StringBuilder to_scramble)
	{
		return array(basicScramble(to_scramble, reverse).toString());
	}
	
	/**
	 * Takes a Binary String and scrambles each Byte within it. The first
	 * Bit of every Byte will be inverted to either a '1' or '0' depending
	 * on the 'reverse' flag. The rebuilt Binary String is then returned. 
	 * Presumes the First Bit of the Byte always starts with '1'. Won't 
	 * work when reversed otherwise. 
	 * 
	 * @param to_scramble - Binary String to scramble.
	 * @param reverse - False to scramble to '1', True to unscramble to '0'.
	 * @return Scrambled Binary String. 
	 * @since 0.1
	 */
	public static StringBuilder basicScramble(final StringBuilder to_scramble, final boolean reverse)
	{
		final StringBuilder builder = new StringBuilder();
		String[] bytes = to_scramble.toString().split(BYTE_DELIMITER);
		builder.append(scramble(bytes[0], reverse));
		for( int i = 1; i < bytes.length; i ++)
			builder.append(BYTE_DELIMITER + scramble(bytes[i], reverse));
		
		return builder;
	}
	
	/**
	 * A basic Bit Scrambler. It finds the first Bit valued '1', then turns
	 * it into a '0'. If the 'reverse' Parameter is 'true', then the first
	 * Bit '0' becomes a '1'. Presumes the First Bit of the Byte always 
	 * starts with '1'. Won't work when reversed otherwise. 
	 *    
	 * @param byte_ - A Byte in Binary String form.
	 * @param reverse - True to scramble, False to unscramble.
	 * @return A scrambled Byte in Binary String form.
	 * @since 0.1
	 */
	private static String scramble(final String byte_, final boolean reverse)
	{
		StringBuilder builder = new StringBuilder(byte_);
		for( int i = 0; i < byte_.length(); i ++)
			if(byte_.charAt(i) == ( !reverse ? BIT_ONE : BIT_ZERO))
			{
				builder.setCharAt(i, ( !reverse ? BIT_ZERO : BIT_ONE));
				return builder.toString();
			}
		return null;
	}

	/**
	 * Takes a Binary String and a {@link Blueprint} filled with Cipher
	 * instructions. Then either scrambles or unscrambles the String based
	 * upon the {@link Blueprint}. The scrambled/unscrambled String is then 
	 * returned.
	 * 
	 * @param binary - Binary or Scrambled Binary.
	 * @param cipher - {@link Blueprint} to Cipher/Decipher.
	 * @return Scrambled or Unscrambled Binary String.
	 * @since 0.3
	 */
	public static String scramble(final String binary, final Blueprint cipher)
	{
		StringBuilder builder = new StringBuilder();
		String[] split_binary = binary.split(BYTE_DELIMITER);
		builder.append(scramble(split_binary[0], cipher.getIndex(0), cipher.get(0)));
		for( int i = 1; i < cipher.size(); i ++)
			builder.append(BYTE_DELIMITER + scramble(split_binary[i], cipher.getIndex(i), cipher.get(i)));
		return builder.toString();
	}
	
	/**
	 * Scrambles a single Byte in Binary String form to the given specified 
	 * {@link Type}. This modified Binary String is then returned.
	 * @param byte_ - Binary String to Scramble with {@link Type}. 
	 * @param index - Index within Byte to modify.
	 * @param scramble_type - {@link Type} of Bit modification.
	 * @return Modified Binary String.
	 * @since 0.3
	 */
	public static String scramble(final String byte_, final byte index, final Type scramble_type)
	{
		final StringBuilder builder = new StringBuilder(byte_);
		switch(scramble_type)
		{
			case INVERT:
				invertBitAt(index, byte_, builder);
				break;
			case LEFT:
				swapLeft(index, byte_, builder);
				break;
			case RIGHT:
				swapRight(index, byte_, builder);
				break;
		}
		return builder.toString();
	}
	
	/**
	 * Takes a Byte Array and a {@link Blueprint} filled with Cipher
	 * instructions. Then either scrambles or unscrambles the Array based
	 * upon the {@link Blueprint}. The scrambled/unscrambled Array is then 
	 * returned.
	 * 
	 * @param bytes - Scrambled or Unscrambled Bytes.
	 * @param cipher - {@link Blueprint} to Cipher/Decipher.
	 * @return Scrambled or Unscrambled Byte Array.
	 * @since 0.4
	 */
	public static byte[] scramble(final byte[] bytes, final Blueprint cipher)
	{
		for(int i = 0; i < cipher.size(); i ++)
			try 
			{
				bytes[i] = scramble(bytes[i], cipher.getIndex(i), cipher.get(i));
			} 
			catch (BitException e) 
			{
				e.printStackTrace();
			} 
		
		return bytes;
	}
	
	/**
	 * Scrambles a single Byte to the given specified {@link Type}. 
	 * This Byte is then returned.
	 * @param byte_ - Byte to Scramble with {@link Type}. 
	 * @param index - Index within Byte to modify.
	 * @param scramble_type - {@link Type} of Bit modification.
	 * @return Modified Byte.
	 * @since 0.4
	 */
	public static byte scramble(final byte byte_, final byte index, final Type scramble_type) throws BitException
	{
		switch(scramble_type)
		{
			case INVERT:
				return invertBitAt(byte_, index, false);
			case LEFT:
				return swapLeft(byte_, index);
			case RIGHT:
				return swapRight(byte_, index, false);
		}
		
		return byte_;
	}
	
	/**
	 * Swaps the bit at the specified Index to the Left. With the Left
	 * Bit taking the previous Bits place. 
	 * @param index - Index of Bit to swap.
	 * @param byte_ - String Binary of Byte to swap.
	 * @param builder - StringBuilder of Binary to make changes within.
	 * @since 0.3
	 */
	private static void swapLeft(final byte index, final String byte_, final StringBuilder builder)
	{
		char swapped_char = byte_.charAt(index - 1);
		builder.setCharAt(index - 1, builder.charAt(index));
		builder.setCharAt(index, swapped_char);
	}
	
	/**
	 * Swaps the bit at the specified Index to the Right. With the Right
	 * Bit taking the previous Bits place. 
	 * @param index - Index of Bit to swap.
	 * @param byte_ - String Binary of Byte to swap.
	 * @param builder - StringBuilder of Binary to make changes within.
	 * @since 0.3
	 */
	private static void swapRight(final byte index, final String byte_, final StringBuilder builder)
	{
		char swapped_char = byte_.charAt(index + 1);
		builder.setCharAt(index + 1, builder.charAt(index));
		builder.setCharAt(index, swapped_char);
	}
	
	/**
	 * Inverts the Bit at the specified Index. The change is reflected 
	 * within the parameter StringBuilder.
	 * @param index - Index of Bit to invert.
	 * @param byte_ - String Binary of Byte to invert.
	 * @param builder - StringBuilder of Binary to make changes within.
	 * @since 0.3
	 */
	private static void invertBitAt(final int index, final String byte_, final StringBuilder builder)
	{
		if(byte_.charAt(index) == BIT_ONE)
			builder.setCharAt(index, BIT_ZERO);
		else if(byte_.charAt(index) == BIT_ZERO)
			builder.setCharAt(index, BIT_ONE);
	}
	
	/**
	 * Inverts the Bit at the specified Byte Index, this modified Byte
	 * is then returned.
	 * <b>Caution: Inverting a Zero at the MSB will cause truncation 
	 * to occur during re-casting. Therefore an Error will be thrown.
	 * This can be disabled with the 'skip_recast_warning'.</b>
	 * @param byte_ - Byte to invert the Bit of.
	 * @param index - Index of the Bit to Invert.
	 * @param skip_recast_warning - Bypass MSB Invert check.
	 * @return Modified Byte with inverted Bit.
	 * @throws BitException - Thrown for Invalid Input.
	 * @since 0.4
	 */
	private static byte invertBitAt(byte byte_, final int index, final boolean skip_recast_warning) throws BitException
	{
		final byte byte_length = (byte) Integer.toBinaryString(byte_).length();
		if(index >= byte_length)
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
		else if( (index == (byte_length-BIT_ONE_)) && !skip_recast_warning)
			throw new BitException(ExceptionType.RECAST_WARNING);
		else if (index < BIT_ZERO_)
			throw new BitException(ExceptionType.NEGATIVE_INDEX);
		
		final byte bit = ((byte) ((byte_ >> index) & BIT_ONE_));
		return (bit == BIT_ZERO_) ? (byte) (byte_ |= (BIT_ONE_ << index) ) 
				: (byte) (byte_ &= ~(BIT_ONE_ << index) );
	}
	
	/**
	 * Checks that the specified Bit Position within the 'array' 
	 * Byte Array is Value '1'. 
	 *  
	 * @param array - Byte Array to check within for Bit.
	 * @param bit_index - Index of Bit to verify.
	 * @throws BitException - Thrown for Invalid Input.
	 * @return True if Bit is '1'.
	 * 
	 * @since 0.4
	 */
	public static boolean isBitSet(final byte[] array, final int bit_index) throws BitException
	{
		final byte byte_index_start = 0;
		if(array == null)	
			throw new BitException(ExceptionType.NULL_OR_INVALID);
		else if(bit_index < byte_index_start)
			throw new BitException(ExceptionType.NEGATIVE_INDEX);
			
		final int final_bit_size = array.length * BYTE_LENGTH;
		final int byte_index = bit_index / BYTE_LENGTH;  
		
		if(bit_index >= final_bit_size)	
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
		
		// Helps to move Bit at 'byte_index' into Bit position 0.
		final int real_bit_index = bit_index % BYTE_LENGTH;
		
		// If we Shift our specified Bit then &(AND) and Compare for '1'.
		// We can determine that the Bit at the given Index is One or Zero.
		return (array[byte_index] >> real_bit_index & BIT_ONE_) == BIT_ONE_;
	}
	
	/**
	 * Swaps Two Bits to the Right at the given 'bit_index'
	 * within the Parameter 'byte_'. 
	 * <b>Caution: Swapping a Zero outside(or replacing) the MSB 
	 * will cause truncation to occur during re-casting. Therefore 
	 * an Error will be thrown.</b>
	 * 
	 * @param byte_ - Byte to swap Bits within.
	 * @param bit_index - Index to swap to the Right.
	 * @param skip_recast_warning - Bypass MSB Swap check.
	 * @return New Byte with swapped Bits.
	 * @throws BitException - Thrown for Invalid Input.
	 * @since 0.4
	 */
	public static byte swapRight(final byte byte_, final byte bit_index, final boolean skip_recast_warning) throws BitException
	{
		final byte right_offset = 1, msb_offset = 2;
		
		if(bit_index == BIT_ZERO_)
			throw new BitException(ExceptionType.SWAP_END);
		else if ( (bit_index == (BYTE_LENGTH - msb_offset)) && !skip_recast_warning)
			throw new BitException(ExceptionType.RECAST_WARNING);
		
		return swap(byte_, bit_index, (byte) (bit_index - right_offset));
	}
	
	/**
	 * Swaps Two Bits to the Left at the given 'bit_index'
	 * within the Parameter 'byte_'.
	 * 
	 * @param byte_ - Byte to swap Bits within.
	 * @param bit_index - Index to swap to the Left.
	 * @return New Byte with swapped Bits.
	 * @throws BitException - Thrown for Invalid Input.
	 * @since 0.4
	 */
	public static byte swapLeft(final byte byte_, final byte bit_index) throws BitException
	{
		final byte left_offset = 1;
		
		if(bit_index == (BYTE_LENGTH - left_offset))
			throw new BitException(ExceptionType.SWAP_END);
		
		return swap(byte_, bit_index, (byte) (bit_index + left_offset));
	}
	
	/**
	 * Swaps Two Bits within the Parameter 'byte_'. Use in reverse
	 * Index. With Index Zero being the far Right Bit.
	 * 
	 * @param byte_ - Byte to swap Bits within.
	 * @param bit_position_0 - First Bit Index to swap.
	 * @param bit_position_1 - Second Bit Index to swap.
	 * @return New Byte with swapped Bits.
	 * @throws BitException - Thrown for Invalid Input.
	 * @since 0.4
	 */
	public static byte swap(final byte byte_, final byte bit_position_0, final byte bit_position_1) throws BitException
	{
		final byte byte_length = (byte) Integer.toBinaryString(byte_).length();
		final byte byte_index_length = 7, byte_index_start = 0;
	
		if ((bit_position_0 > byte_index_length) || (bit_position_1 > byte_index_length) || (bit_position_0 >= byte_length) || (bit_position_1 >= byte_length))
		{
			System.out.println("Byte length: " + byte_length + "\n pos 0: " + bit_position_0 + "\n pos 1: " + bit_position_1);
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
		}
		else if ((bit_position_0 < byte_index_start) || (bit_position_1 < byte_index_start))
			throw new BitException(ExceptionType.NEGATIVE_INDEX);

		final byte bit_0 = (byte) ((byte_ >> bit_position_0) & BIT_ONE_),
				bit_1 = (byte) (((byte_ >> bit_position_1) & BIT_ONE_));

		// Check if Bits are the same...
		if (bit_0 == bit_1)
			return byte_; 
		
		// Create Mask 
		final byte mask = (byte) ((BIT_ONE_ << bit_position_0) | (BIT_ONE_ << bit_position_1));
		
		// XOR (^)
		return (byte) (byte_ ^ mask);// TADAM!!!
	}
	
	/**
	 * Checks that the specified Bit Position within the 'byte_' 
	 * is Value '1'. 
	 * 
	 * @param byte_ - Byte to check Bit inside of.
	 * @param bit_position - Index within the Bit to Verify. 
	 * @throws BitException - Thrown for Invalid Input.
	 * @return True if Bit at given Index is '1'.
	 * @since 0.4
	 */
	public static boolean isBitSet(final byte byte_, final int bit_position) throws BitException 
	{	 
		final byte byte_index_length = 7, byte_index_start = 0;
		
		if(bit_position > byte_index_length)	
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
		else if (bit_position < byte_index_start)
			throw new BitException(ExceptionType.NEGATIVE_INDEX);
		
		return (byte_ >> (bit_position % BYTE_LENGTH) & BIT_ONE_) == BIT_ONE_;
	}

	/**
	 * Converts each Character within the Parameter 'text' to its
	 * char Binary representation, then stores it within a StringBuilder.
	 * Which is then returned. Each Byte is separated with a '~' delimiter.
	 * @param text - Text to convert into Binary. 
	 * @return Binary String of converted Text.
	 * @since 0.1
	 */
	public static StringBuilder stringtoBinary(final String text)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(Long.toBinaryString(text.charAt(0)));
		for(int i = 1; i < text.length(); i ++)
			builder.append(BYTE_DELIMITER+Long.toBinaryString(text.charAt(i)));
		return builder;
	}
	
	/**
	 * Takes each Byte within the Parameter 'bytes' and creates
	 * a Binary String representation. With each Byte delimited 
	 * with '~' Character.
	 * @param bytes - Bytes to convert into Binary String.
	 * @return String of Binary delimited by '~'. 
	 * @since 0.3
	 */
	public static String arrayToBinaryString(final byte[] bytes)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(Long.toBinaryString((char)bytes[0]));
		for(int i = 1; i < bytes.length; i ++)
			builder.append(BYTE_DELIMITER+Long.toBinaryString((char)bytes[i]));
		return builder.toString();
	}
	
	/**
	 * Takes a Binary String Representation, parses it up into Bytes
	 * then returns said Bytes within an Array. Useful for returning
	 * manipulated Binary Strings back to Byte Array form. Expects
	 * each Byte to be delimited with the Character '~'.
	 * 
	 * @param binary_str - Binary String to convert.
	 * @return Byte Array of Bits within the Binary String. 
	 * @since 0.1
	 */
	public static byte[] array(final String binary_str)
	{
		final String[] split_binary = binary_str.split(BYTE_DELIMITER);
		final ByteBuffer decoded_binary_str  = ByteBuffer.allocate(split_binary.length);
		final byte base = 2;
		for(int byte_index = 0 ; byte_index < split_binary.length; byte_index ++)
			decoded_binary_str.put((byte)Long.parseLong(split_binary[byte_index], base));
		
		return decoded_binary_str.array();
	}
	
	/**
	 * Splits up a Binary String Representation into a single 8 Bit Byte 
	 * String at the given Byte Index. Presuming we're dealing with 8 Bit
	 * Bytes, or else this won't work. Use padBinaryString to pad.
	 * @param to_split - Binary String to split up.
	 * @param byte_index - Byte Index. Multiples of Eight.
	 * @return Binary String at specified Index.
	 * @since 0.1
	 */
	public static String splitBinaryRep(final String to_split, final int byte_index)
	{
		final int byte_size = 8, starting_index = byte_size * byte_index;
		int ending_index = (byte_index == 0) ? byte_size : (byte_size * byte_index) + byte_size;
		return to_split.substring(starting_index, ending_index) ;
	}
	
	/**
	 * Pads the passed String with leading Zeroes. Intended for use with
	 * String Binary operations.
	 * @param binary_str - Binary String to be padded.
	 * @return Padded String with leading Zeroes.
	 * @since 0.1
	 */
	public static String padBinaryString(final String binary_str)
	{
		final char to_replace = ' ', padding = '0';
		final int padding_length = ((binary_str.length()/8) + 1) * 8;
		final String format_regex = "%" + padding_length + "s";
		return String.format(format_regex, binary_str).replace(to_replace, padding);
	}
	
}
