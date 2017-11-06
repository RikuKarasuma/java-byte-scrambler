package xyz.softwareeureka.security.scrambler;

import java.nio.ByteBuffer;

/**
 * A Static Byte Binary Tools Class related to scrambling and 
 * unscrambling data. Some of these Tools depend on the Twos
 * Complement System of Signed Variables. 
 *  
 * @author Owen McMonagle.
 * @version 0.3
 * @since 05/11/2017 Updated 06/11/2017
 * 
 * @see Blueprint
 * @see Type
 */
public final class ByteTools 
{

	/**
	 * Binary String Byte Delimiter. Essentially is used to separate
	 * Bytes within Binary Strings used within this library.
	 */
	public static final String BYTE_DELIMITER = "~";
	
	/**
	 * Represents the Bit One.
	 */
	public static final char BIT_ONE = '1';
	
	/**
	 * Represents the Bit Zero.
	 */
	public static final char BIT_ZERO = '0';
	
	/**
	 * Private Constructor as this is a Static Class.
	 */
	private ByteTools(){}
	
	/**
	 * Runs both basic and advanced scrambling tests. If both
	 * were successful then True is returned. 
	 * 
	 * @return True if Tests are successful, false otherwise.
	 */
	public static boolean performTests()
	{
		return basicTests() && advancedTests();
	}

	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works.
	 * 
	 * @return True if Scrambler and Unscrambler works.
	 */
	public static boolean basicTests()
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
	 */
	public static boolean advancedTests()
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
	 */
	public static byte[] convertAndScramble(final boolean reverse, final String to_scramble)
	{
		return array(basicScramble(stringtoBinary(to_scramble), reverse).toString());
	}
	
	/**
	 * Takes a Binary String and scrambles each Byte within it. The first
	 * Bit of every Byte will be inverted to either a '1' or '0' depending
	 * on the 'reverse' flag. The rebuilt Binary String is then returned in
	 * Byte Array form. Depends on Twos Complement.
	 * @param to_scramble - Binary String to scramble.
	 * @param reverse - False to scramble to '1', True to unscramble to '0'.
	 * @return Scrambled Binary String. 
	 */
	public static byte[] basicScramble(final boolean reverse, final StringBuilder to_scramble)
	{
		return array(basicScramble(to_scramble, reverse).toString());
	}
	
	/**
	 * Takes a Binary String and scrambles each Byte within it. The first
	 * Bit of every Byte will be inverted to either a '1' or '0' depending
	 * on the 'reverse' flag. The rebuilt Binary String is then returned. 
	 * Depends on Twos Complement.
	 * @param to_scramble - Binary String to scramble.
	 * @param reverse - False to scramble to '1', True to unscramble to '0'.
	 * @return Scrambled Binary String. 
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
	 * Bit '0' becomes a '1'. Depends on Twos Complement.
	 *    
	 * @param byte_ - A Byte in Binary String form.
	 * @param reverse - True to scramble, False to unscramble.
	 * @return A scrambled Byte in Binary String form.
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
	 * Swaps the bit at the specified Index to the Left. With the Left
	 * Bit taking the previous Bits place. 
	 * @param index - Index of Bit to swap.
	 * @param byte_ - String Binary of Byte to swap.
	 * @param builder - StringBuilder of Binary to make changes within.
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
	 */
	private static void invertBitAt(final int index, final String byte_, final StringBuilder builder)
	{
		if(byte_.charAt(index) == BIT_ONE)
			builder.setCharAt(index, BIT_ZERO);
		else if(byte_.charAt(index) == BIT_ZERO)
			builder.setCharAt(index, BIT_ONE);
	}

	/**
	 * Converts each Character within the Parameter 'text' to its
	 * char Binary representation, then stores it within a StringBuilder.
	 * Which is then returned. Each Byte is separated with a '~' delimiter.
	 * @param text - Text to convert into Binary. 
	 * @return Binary String of converted Text.
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
	 * Bytes, or else this won't work.
	 * @param to_split - Binary String to split up.
	 * @param byte_index - Byte Index. Multiples of Eight.
	 * @return Binary String at specified Index.
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
	 */
	public static String padBinaryString(final String binary_str)
	{
		final char to_replace = ' ', padding = '0';
		final int padding_length = ((binary_str.length()/8) + 1) * 8;
		final String format_regex = "%" + padding_length + "s";
		return String.format(format_regex, binary_str).replace(to_replace, padding);
	}
	
}
