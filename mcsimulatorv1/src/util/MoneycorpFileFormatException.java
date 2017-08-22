package util;

/**
 * Indicates prepared file format does not conform to that expected
 * @author andyc
 */
public class MoneycorpFileFormatException extends Exception {

   public MoneycorpFileFormatException() { super(); }
   public MoneycorpFileFormatException(String message) { super(message); }
   public MoneycorpFileFormatException(String message, Throwable cause) { super(message, cause); }
   public MoneycorpFileFormatException(Throwable cause) { super(cause); }

}
