package xyz.softwareeureka.security.scrambler;

import java.util.Arrays;

/**
 * An encoded Message. <br>
 * 
 * Use Cases: <br><br>
 * 
 * Takes Byte Array and scrambles its Information.<br><br>
 * 
 * Takes User specified Text and scrambles it using a Randomly generated 
 * {@link Blueprint}.<br><br>
 * 
 * Takes a encoded Byte Array for storage and/or later decryption.<br><br>
 * 
 * Takes User specified Binary and {@link Blueprint} for decoding said
 * Binary.<br><br>
 * 
 * Takes User specified Text and {@link Blueprint}. Then scrambles said
 * Text with specified {@link Blueprint}<br><br>
 * 
 * 
 * Never intended to be polymorphed, inherited or serialized. The scrambled
 * bytes and {@link Blueprint} cipher map, if transported.. are advised to 
 * be done so, separately. This is to limit the risk that the map will be 
 * used to decipher the scrambled bytes. The best way to transport would be
 * using the {@link Safe} object to save to file without the use of 
 * serialization. 
 * 
 *  
 * @author Owen McMonagle.
 * @since 06/11/2017 updated 08/11/2017
 * @see ByteTools
 * @see Blueprint
 * @see Smartprint
 * @see Type
 * @version 0.2
 *
 */
public final class EncodedMessage
{

	/**
	 * Encoded/Decoded Bytes from User passed Data. Depending on the
	 * Constructor used.
	 */
	private byte[] msg = null;
	
	/**
	 * Specific {@link Smartprint} used to encode Bytes.
	 */
	private volatile Blueprint map = null;
	
	/**
	 * For storing Bytes with no Map.
	 * @param bytes - Random Bytes.
	 */
	public EncodedMessage(final byte[] bytes)
	{
		msg = bytes;
	}
	
	/**
	 * For encoding a Byte Array with a specified {@link Blueprint}.
	 * @param bytes - Random Bytes to encode.
	 * @param encoding_map - Cipher Map to encode with.
	 */
	public EncodedMessage(final byte[] bytes, final Blueprint encoding_map)
	{
		map = encoding_map;
		msg = ByteTools.scramble(bytes, map);
	}
	
	/**
	 * For encoding Text with a random {@link Smartprint}.
	 * @param text - Text to encode.
	 */
	public EncodedMessage(final String text) 
	{
		map = new Smartprint(text.getBytes());
		msg = ByteTools.scramble(text.getBytes(), map);
	}
	
	/**
	 * For encoding Text with specific {@link Smartprint}.
	 * @param encoding_map - {@link Smartprint} to encode with.
	 * @param text - Text to encode.
	 */
	public EncodedMessage(final Blueprint encoding_map, final String text) 
	{
		map = encoding_map;
		msg = ByteTools.scramble(text.getBytes(), map);
	}
	
	/**
	 * Returns the encoded Byte Array.
	 * @return Encoded Bytes.
	 */
	public byte[] getEncoded()
	{
		return msg;
	}
	
	/**
	 * Returns the {@link Blueprint} used to encode with.
	 * @return Encoding {@link Blueprint}.
	 */
	public Blueprint getMap()
	{
		return map;
	}
	
	/**
	 * Decodes the encoded Bytes with the specified {@link Blueprint}.
	 * The decoded Bytes are then returned in an Array. If no encoded
	 * Bytes or {@link Blueprint} exist, then an empty Byte Array is
	 * returned.
	 * @param encoding_map - Specific {@link Blueprint} to decode with.
	 * @return Decoded Byte Array, or Empty Byte Array if Error occurred.
	 */
	public byte[] getDecoded(final Blueprint encoding_map)
	{
		if(msg != null && encoding_map != null)
			return ByteTools.scramble(Arrays.copyOf(msg, msg.length), encoding_map);
		else
			return new byte[0];
	}
	
	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works.
	 * 
	 * @return True if {@link EncodedMessage}, {@link Blueprint}
	 * and {@link ByteTools} works as intended.
	 */
	public static boolean test()
	{
		System.out.println("Beginning Encoded Message Tests...");
		String text_str = "Hello World, I hope there's no more Testing to be done here.";
		
		final EncodedMessage msg = new EncodedMessage(text_str);
		final byte[] decoded = msg.getDecoded(msg.getMap());
		System.out.println("Encoded: " + new String(msg.getEncoded()));
		System.out.println("Decoded: " + new String(decoded));
		
		boolean match = text_str.getBytes().equals(msg.getEncoded()),
				success = new String(decoded).equals(text_str) && !match;
		
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		
		return success;
	}
}
