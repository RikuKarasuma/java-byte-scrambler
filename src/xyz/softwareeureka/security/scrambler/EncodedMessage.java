package xyz.softwareeureka.security.scrambler;

/**
 * An encoded Message. <br>
 * 
 * Use Cases: <br><br>
 * Takes User specified Text and scrambles it using a Randomly generated 
 * {@link Smartprint}.<br><br>
 * Takes a encoded Byte Array for storage and/or later decryption.<br><br>
 * Takes User specified Binary and {@link Smartprint} for decoding said
 * Binary.<br><br>
 * Takes User specified Text and {@link Smartprint}. Then scrambles said
 * Text with specified {@link Smartprint}<br><br>
 * 
 * Never intended to be Polymorphed, Inherited or Serialized.
 * 
 *  
 * @author Owen McMonagle.
 * @since 06/11/2017
 * @see ByteTools
 * @see Smartprint
 * @see Type
 * @version 0.1
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
	private Smartprint map = null;
	
	/**
	 * For storing encoded Bytes with no Map.
	 * @param encoded_msg - Pre-encoded Bytes.
	 */
	public EncodedMessage(final byte[] encoded_msg)
	{
		msg = encoded_msg;
	}
	
	/**
	 * For encoding Text with a random {@link Smartprint}.
	 * @param text - Text to encode.
	 */
	public EncodedMessage(final String text) 
	{
		String binary = ByteTools.stringtoBinary(text).toString();
		map = new Smartprint(binary);
		binary = ByteTools.scramble(binary, map);
		msg = ByteTools.array(binary);
	}
	
	/**
	 * For encoding Text with specific {@link Smartprint}.
	 * @param encoding_map - {@link Smartprint} to encode with.
	 * @param text - Text to encode.
	 */
	public EncodedMessage(final Smartprint encoding_map, final String text) 
	{
		String binary = ByteTools.stringtoBinary(text).toString();
		map = encoding_map;
		binary = ByteTools.scramble(binary, map);
		msg = ByteTools.array(binary);
	}
	
	/**
	 * For decoding Binary with specific Map. The decoded Bytes are
	 * then stored within the 'msg' Byte Array.
	 * @param binary - Binary to decode. Byte delimited by '~'.
	 * @param decoding_map - Specific {@link Smartprint} to decode with.
	 */
	public EncodedMessage(String binary, final Smartprint decoding_map) 
	{
		map = decoding_map;
		binary = ByteTools.scramble(binary, map);
		msg = ByteTools.array(binary);
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
	 * Returns the {@link Smartprint} used to encode with.
	 * @return Encoding {@link Smartprint}.
	 */
	public Smartprint getMap()
	{
		return map;
	}
	
	/**
	 * Decodes the encoded Bytes with the specified {@link Smartprint}.
	 * The decoded Bytes are then returned in an Array. If no encoded
	 * Bytes or {@link Smartprint} exist, then an empty Byte Array is
	 * returned.
	 * @param encoding_map - Specific {@link Smartprint} to decode with.
	 * @return Decoded Byte Array, or Empty Byte Array if Error occurred.
	 */
	public byte[] getDecoded(final Smartprint encoding_map)
	{
		if(msg != null && encoding_map != null)
			return ByteTools.array(ByteTools.scramble(ByteTools.arrayToBinaryString(msg), encoding_map));
		else
			return new byte[0];
	}
	
	/**
	 * Tests the scrambling and decoding functionality of the 
	 * library. The premise is, if the initial text is scrambled,
	 * and reversed. Will it still match in the end and can it
	 * be reverted? If so, our code works.
	 * 
	 * @return True if {@link EncodedMessage}, {@link Smartprint}
	 * and {@link ByteTools} works as intended.
	 */
	public static boolean test()
	{
		System.out.println("Beginning Encoded Message Tests...");
		String text_str = "Hello World, I hope there's no more Testing to be done here.";
		
		final EncodedMessage msg = new EncodedMessage(text_str);
		System.out.println("Encoded: " + new String(msg.getEncoded()));
		System.out.println("Decoded: " + new String(msg.getDecoded(msg.getMap())));
		
		boolean match = text_str.getBytes().equals(msg.getEncoded()),
				success = new String(msg.getDecoded(msg.getMap())).equals(text_str) && !match;
		
		System.out.println("Match: " + match);
		System.out.println("Success: " + success + "\n");
		
		return success;
	}
}
