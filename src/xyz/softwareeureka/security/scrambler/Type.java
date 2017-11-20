package xyz.softwareeureka.security.scrambler;

/**
 * Scrambler instructions used within a Cipher Blueprint.
 * <br>
 * <ul>
 * <li>Invert Bit</li>
 * <li>Swap Bit Left</li>
 * <li>Swap Bit Right</li>
 * <li>Swap Bit Left, invert both Bits</li>
 * <li>Swap Bit Right, invert both Bits</li>
 * <li>Invert all Bits after the first index</li>
 * <li>Invert Bits, 0, 2, 4 and 6.</li>
 * </ul>
 * 
 * @author Owen McMonagle.
 * @version 0.2
 * @since 05/11/2017 updated 09/11/2017
 */
public enum Type 
{
	/**
	 * Change Bit from 0 to 1 or vice versa.
	 */
	INVERT,
	/**
	 * Swap with the Bit to the Left.
	 */
	LEFT,
	/**
	 * Swap with the Bit to the Right.
	 */
	RIGHT,
	/**
	 * Swap with the Bit to the Left. Then invert both Bits.
	 */
	INVERSE_LEFT,
	/**
	 * Swap with the Bit to the Right. Then invert both Bits.
	 */
	INVERSE_RIGHT,
	/**
	 * Invert all the Bits within this Byte after the first
	 * index.
	 */
	INVERSE_ALL,
	/**
	 * Inverts every Second Bit or so.
	 * Shreds at Indexes: 0, 2, 4 and 6.
	 */
	SHREDDED;
}
