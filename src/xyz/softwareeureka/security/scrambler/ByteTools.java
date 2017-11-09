package xyz.softwareeureka.security.scrambler;

import java.nio.ByteBuffer;

import xyz.softwareeureka.security.scrambler.BitException.ExceptionType;

/**
 * A Static Byte Binary Tools Class related to scrambling and 
 * unscrambling data. Some of these Tools may depend on the Twos
 * Complement System and have not been tested outside of such.
 *  
 * @author Owen McMonagle.
 * @version 0.5
 * @since 05/11/2017 Updated 09/11/2017
 * 
 * @see Blueprint
 * @see Type
 */
public final class ByteTools 
{
	
	/**
	 * Test String.
	 * @since 0.4
	 */
	public static final String TEST_STR = "Hello World. Use these Cipher Tools wisely!";

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
		return basicBinaryStrTests() && advancedBinaryStrTests() && basicBinaryTests() && advancedBinaryTests();
	}

	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works. <br><br>
	 * 
	 * Tests Binary String Operations.
	 * 
	 * @return True if Scrambler and Unscrambler works.
	 * @since 0.1
	 */
	public static boolean basicBinaryStrTests()
	{
		System.out.println("Beginning Basic Binary String Tests...");
		StringBuilder unscrambled = stringtoBinary(TEST_STR);
		StringBuilder scrambled = basicScramble(unscrambled, false);
		unscrambled = basicScramble(scrambled, true);
		
		byte[] decoded = array(unscrambled.toString());
		byte[] encoded = array(scrambled.toString());
		
		final String decoded_text = new String(decoded);
		final boolean match = decoded.equals(encoded), 
				success = decoded_text.equals(TEST_STR) && !match;
		
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
	 * be reverted? If so, our code works. <br><br>
	 * 
	 * Tests Binary String Operations.
	 * 
	 * @return True if Scrambler and Unscrambler works.
	 * @see Smartprint
	 * @see Type
	 * @since 0.3
	 */
	public static boolean advancedBinaryStrTests()
	{
		System.out.println("Beginning Advanced Binary String Tests...");
		String binary = stringtoBinary(TEST_STR).toString();
		final Smartprint blueprint = new Smartprint(binary);
		binary = scramble(binary, blueprint);
		final byte[] scrambled = array(binary);
		binary = scramble(binary, blueprint);
		final byte[] unscrambled = array(binary);
		
		final String translated = new String(unscrambled);
		final boolean match = unscrambled.equals(scrambled), 
				success = translated.equals(TEST_STR) && !match;
		
		System.out.println("Encoded: "+new String(scrambled));
		System.out.println("Decoded: "+new String(unscrambled));
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		return success;
	}
	
	/**
	 * Tests the isBitSet Functions by creating a Binary String of
	 * Two Bytes. Then attempting to rebuild those Bytes by using
	 * the isBitSet Functions. If the Two Strings match in the End.
	 * Then our isBitSet Functions work. <br><br>
	 * 
	 * Tests Binary isBitSet Operations.
	 * 
	 * @return True if all Tests Pass.
	 * 
	 * @since 0.4
	 */
	public static boolean basicBinaryTests()
	{
		System.out.println("Beginning Basic Binary Tests...");
		System.out.println("isBitSet[Array]...");
		final byte[] original_bytes = { 3, 54 };
		final String binary_str_rep = Integer.toBinaryString(3) + Integer.toBinaryString(54);
		String binary_str_rebuilt = "";
		for(int i = 0; i < original_bytes.length; i ++)
		{
			final byte byte_len = (byte) (Integer.toBinaryString(original_bytes[i]).length() - BIT_ONE_);
			for( int a = byte_len; a > -1; a --)
				try
				{
					binary_str_rebuilt += isBitSet(original_bytes, i, a) ? BIT_ONE : BIT_ZERO;
				}
				catch (BitException e)
				{
					e.printStackTrace();
				}
		}
			
		final boolean success_0 = binary_str_rebuilt.equals(binary_str_rep);
		
		System.out.println("Binary String[0]: "+binary_str_rep);
		System.out.println("Rebuilt Binary[0]: "+binary_str_rebuilt);
		System.out.println("Success[0]: " + success_0 + "\n");

		System.out.println("isBitSet[Byte]...");
		
		binary_str_rebuilt = "";
		for(int i = 0; i < original_bytes.length; i ++)
		{
			final byte byte_len = (byte) (Integer.toBinaryString(original_bytes[i]).length() - BIT_ONE_);
			for( int a = byte_len; a > -1; a --)
				try
				{
					binary_str_rebuilt += isBitSet(original_bytes[i], a) ? BIT_ONE : BIT_ZERO;
				}
				catch (BitException e)
				{
					e.printStackTrace();
				}
		}
			
		final boolean success_1 = binary_str_rebuilt.equals(binary_str_rep);
		
		System.out.println("Binary String[1]: "+binary_str_rep);
		System.out.println("Rebuilt Binary[1]: "+binary_str_rebuilt);
		System.out.println("Success[1]: " + success_1 + "\n");
		
		return success_0 && success_1;
	}
	
	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works. <br><br>
	 * 
	 * Tests Binary Scrambling Operations, specifically with
	 * Bytes.
	 * 
	 * @since 0.4
	 * @return True if Tests Pass.
	 */
	public static boolean advancedBinaryTests()
	{
		System.out.println("Beginning Advanced Binary Tests...");
		final byte[] original_bytes = TEST_STR.getBytes();
		final Smartprint cipher = new Smartprint(original_bytes);
		final byte[] scrambled = scramble(original_bytes, cipher),
				unscrambled = scramble(scrambled, cipher);
		
		final String translated = new String(unscrambled);
		final boolean match = unscrambled.equals(scrambled), 
				success = translated.equals(TEST_STR) && !match;
		
		System.out.println("Encoded: "+new String(scrambled));
		System.out.println("Decoded: "+translated);
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
	 * {@link Type}. This modified Binary String is then returned. Does not
	 * have access to all Binary Manipulation {@link Type}s.
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
			default:
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
	 * @return New Scrambled or Unscrambled Byte Array.
	 * @since 0.4
	 */
	public static byte[] scramble(final byte[] bytes, final Blueprint cipher)
	{
		final byte[] new_address_space = new byte[bytes.length];
		for(int i = 0; i < cipher.size(); i ++)
			try 
			{
				new_address_space[i] = scramble(bytes[i], cipher.getIndex(i), cipher.get(i));
			} 
			catch (BitException e) 
			{
				e.printStackTrace();
			} 
		
		return new_address_space;
	}
	
	/**
	 * Scrambles a single Byte to the given specified {@link Type}. 
	 * This Byte is then returned. All scrambling Function Types are
	 * detailed here and within {@link Type}.
	 * @param byte_ - Byte to Scramble with {@link Type}. 
	 * @param index - Index within Byte to modify.
	 * @param scramble_type - {@link Type} of Bit modification.
	 * @return New Modified Byte.
	 * @since 0.4
	 */
	public static byte scramble(final byte byte_, final byte index, final Type scramble_type) throws BitException
	{
		final byte new_address_space = byte_;
		switch(scramble_type)
		{
			case INVERT:
				return invertBitAt(new_address_space, index, false, false);
			case LEFT:
				return swapLeft(new_address_space, index);
			case RIGHT:
				return swapRight(new_address_space, index, false);
			case INVERSE_ALL:
				return inverse(new_address_space);
			case INVERSE_LEFT:
				return leftInverse(new_address_space, index);
			case INVERSE_RIGHT:
				return rightInverse(new_address_space, index);
			case SHREDDED:
				return shred(new_address_space);
		}
		return new_address_space;
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
	 * @param ignore_value_length - Bypass Value Length check.
	 * @return Modified Byte with inverted Bit.
	 * @throws BitException - Thrown for Invalid Input.
	 * @since 0.4
	 */
	private static byte invertBitAt(byte byte_, final int index, final boolean skip_recast_warning, final boolean ignore_value_length) throws BitException
	{
		final byte byte_length = (byte) Integer.toBinaryString(byte_).length();
		if(index >= byte_length && !ignore_value_length)
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
	 * Byte Array is Value '1'. Use in reverse Index. With Index 
	 * Zero being the far Right Bit.
	 *  
	 * @param array - Byte Array to check within for Bit.
	 * @param byte_index - Index of the Byte to check.
	 * @param bit_position - Index of Bit to verify.
	 * @throws BitException - Thrown for Invalid Input.
	 * @return True if Bit is '1'.
	 * 
	 * @since 0.4
	 */
	public static boolean isBitSet(final byte[] array, final int byte_index, final int bit_position) throws BitException
	{
		final byte byte_index_start = 0;
		if(array == null)	
			throw new BitException(ExceptionType.NULL_OR_INVALID);
		else if(bit_position < byte_index_start)
			throw new BitException(ExceptionType.NEGATIVE_INDEX);
		
		final int total_bit_len = array.length * BYTE_LENGTH;
		
		if(bit_position >= total_bit_len)	
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
		
		// Helps to move Bit at 'byte_index' into Bit position 0.
		final int real_bit_index = bit_position % BYTE_LENGTH;
		
		// If we Shift our specified Bit then &(AND) and Compare for '1'.
		// We can determine that the Bit at the given Index is One or Zero.
		return (array[byte_index] >> real_bit_index & BIT_ONE_) == BIT_ONE_;
	}
	
	/**
	 * Checks that the specified Bit Position within the 'byte_' 
	 * is Value '1'. Use in reverse Index. With Index Zero being 
	 * the far Right Bit.
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
			throw new BitException(ExceptionType.GREATER_THAN_LENGTH);
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
		return (byte) (byte_ ^ mask);
	}
	
	/**
	 * Swaps the Bit at the given Index to the Left. Then Inverts both
	 * that Bit and the swapped Bit.
	 * @param byte_ - Byte to swap and invert. 
	 * @param index - Index to swap and invert at.
	 * @return New Modified Byte.
	 * @since 0.5
	 */
	public static byte leftInverse(final byte byte_, final byte index)
	{
		try 
		{
			final boolean ignore_safety_checks = false;
			byte new_addr = swapLeft(byte_, index);
			new_addr = invertBitAt(new_addr, index, ignore_safety_checks, ignore_safety_checks);
			new_addr = invertBitAt(new_addr, index+1, ignore_safety_checks, ignore_safety_checks);
			return new_addr;
		}
		catch (BitException e) 
		{
			e.printStackTrace();
		}
		return byte_;
	}
	
	/**
	 * Swaps the Bit at the given Index to the Right. Then Inverts both
	 * that Bit and the swapped Bit.
	 * @param byte_ - Byte to swap and invert. 
	 * @param index - Index to swap and invert at.
	 * @return New Modified Byte.
	 * @since 0.5
	 */
	public static byte rightInverse(final byte byte_, final byte index)
	{
		try 
		{
			final boolean ignore_safety_checks = false;
			byte new_addr = swapRight(byte_, index, ignore_safety_checks);
			new_addr = invertBitAt(new_addr, index, ignore_safety_checks, ignore_safety_checks);
			new_addr = invertBitAt(new_addr, index-1, ignore_safety_checks, ignore_safety_checks);
			return new_addr;
		}
		catch (BitException e) 
		{
			e.printStackTrace();
		}
		return byte_;
	}
	
	/**
	 * Shreds a Byte of Data. Shredding is the Inversion of certain Bits
	 * within the Parameter 'byte_'. To be used via a {@link Type}. Shreds
	 * at Indexes: 0, 2, 4 and 6.
	 * @param byte_ - Byte of Data to Shred.
	 * @return Shredded Byte.
	 * @since 0.5
	 */
	public static byte shred(final byte byte_)
	{	
		final boolean safety_bypasses = true;
		try 
		{
			byte new_addr = invertBitAt(byte_, 0, safety_bypasses, safety_bypasses);
				new_addr = invertBitAt(new_addr, 2, safety_bypasses, safety_bypasses);
			new_addr = invertBitAt(new_addr, 4, safety_bypasses, safety_bypasses);
			new_addr = invertBitAt(new_addr, 6, safety_bypasses, safety_bypasses);
			
			return new_addr;
		} 
		catch (BitException e) 
		{
			e.printStackTrace();
		}
		return byte_;
	}
	
	/**
	 * Inverts all of the Bits within the Parameter 'byte_'. To be used
	 * via {@link Type}.
	 * @param byte_ - Byte of Data to invert.
	 * @return New inverted Byte.
	 * @since 0.5
	 */
	public static byte inverse(final byte byte_)
	{
		return (byte) ~byte_;
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
