package cn.jishiyu11.xjsjd.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Statement;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Description: String工具类
 */
public class StringUtil {

  private static final String HT = "\t";
  private static final String CRLF = "\r\n";

  // This class cannot be instantiated
  private StringUtil() {
  }

  /**
   * Split the string into an array of strings using one of the separator
   * in 'sep'.
   *
   * @param s the string to tokenize
   * @param sep a list of separator to use
   * @return the array of tokens (an array of size 1 with the original
   * string if no separator found)
   */
  public static String[] split(String s, String sep) {
    // Create the list of separators
    String sepArray[] = new String[sep.length()];
    for (int i = 0; i < sep.length(); ++i) {
      sepArray[i] = "" + sep.charAt(i);
    }
    return split(s, sepArray);
  }

  /**
   * Split the string into an array of strings using one of the separator
   * in 'sep'.
   *
   * @param s the string to tokenize
   * @param sepArray a list of separators (strings, not chars) to use
   * @return the array of tokens (an array of size 1 with the original
   * string if no separator found)
   */
  public static String[] split(String s, String[] sepArray) {
    // convert a String s to an Array, the elements
    // are delimited by sep
    Vector tokenIndex = new Vector(10);
    Vector tokenSep = new Vector(10);
    int len = s.length();
    int i;

    //    Log.debug("Splitting in progress for: " + s);

    // Find all characters in string matching one of the separators in 'sep'
    for (i = 0; i < sepArray.length; ++i) {
      String sep = sepArray[i];
      int index = s.indexOf(sep);
      while (index >= 0) {
        tokenIndex.addElement(index);
        tokenSep.addElement(sep);
        index = s.indexOf(sep, index + sep.length());
      }
    }
    int size = tokenIndex.size();
    // tokenIndex is expected to be ordered
    for (i = 0; i < size; ++i) {
      int tokenI = ((Integer) tokenIndex.elementAt(i)).intValue();
      for (int j = i + 1; j < size; ++j) {
        int tokenJ = ((Integer) tokenIndex.elementAt(j)).intValue();
        if (tokenJ < tokenI) {
          // Swap
          Object temp = tokenIndex.elementAt(i);
          tokenIndex.setElementAt(tokenIndex.elementAt(j), i);
          tokenIndex.setElementAt(temp, j);
        }
      }
    }
    // Finally split
    String[] elements = new String[size + 1];

    // No separators: return the string as the first element
    if (size == 0) {
      elements[0] = s;
    } else {
      // Init indexes
      int start = 0;
      int end = ((Integer) tokenIndex.elementAt(0)).intValue();

      // Get the first token
      elements[0] = s.substring(start, end);
      String sep;

      // Get the mid tokens
      for (i = 1; i < size; i++) {
        // update indexes
        sep = (String) tokenSep.elementAt(i - 1);
        start = ((Integer) tokenIndex.elementAt(i - 1)).intValue() + sep.length();
        end = ((Integer) tokenIndex.elementAt(i)).intValue();
        elements[i] = s.substring(start, end);
      }
      // Get last token
      sep = (String) tokenSep.elementAt(i - 1);
      start = ((Integer) tokenIndex.elementAt(i - 1)).intValue() + sep.length();
      elements[i] = (start < s.length()) ? s.substring(start) : "";
    }

    return elements;
  }

  /**
   * Join the given strings into a single string using sep as separator for
   * individual values.
   *
   * @param list the string array to join
   * @param sep the separator to use
   * @return the joined string
   */
  public static String join(String[] list, String sep) {
    StringBuffer buffer = new StringBuffer(list[0]);
    int len = list.length;

    for (int i = 1; i < len; i++) {
      buffer.append(sep).append(list[i]);
    }
    return buffer.toString();
  }

  /**
   * Returns the string array
   *
   * @param stringVec the Vecrot of tring to convert
   * @return String []
   */
  public static String[] getStringArray(Vector stringVec) {

    if (stringVec == null) {
      return null;
    }

    String[] stringArray = new String[stringVec.size()];
    for (int i = 0; i < stringVec.size(); i++) {
      stringArray[i] = (String) stringVec.elementAt(i);
    }
    return stringArray;
  }

  /**
   * create a vector filled with the elements of the given array
   *
   * @return a vector with the elements from the given array
   */
  public static Vector getVectorFromArray(Object[] array) {

    Vector v = new Vector(array.length);

    for (Object anArray : array) {
      v.addElement(anArray);
    }

    return v;
  }

  /**
   * Find two consecutive newlines in a string.
   *
   * @param s - The string to search
   * @return int: the position of the empty line
   */
  public static int findEmptyLine(String s) {
    int ret = 0;

    // Find a newline
    while ((ret = s.indexOf("\n", ret)) != -1) {
      // Skip carriage returns, if any
      while (s.charAt(ret) == '\r') {
        ret++;
      }
      if (s.charAt(ret) == '\n') {
        // Okay, it was empty
        ret++;
        break;
      }
    }
    return ret;
  }

  /**
   * Removes unwanted blank characters
   *
   * @return String
   */
  public static String removeBlanks(String content) {
    return removeChar(content, ' ');
  }

  /**
   * Removes unwanted backslashes characters
   *
   * @param content The string containing the backslashes to be removed
   * @return the content without backslashes
   */
  public static String removeBackslashes(String content) {
    return removeChar(content, '\\');
  }

  /**
   * Removes unwanted characters
   *
   * @param content The string containing the backslashes to be removed
   * @param ch the character to be removed
   * @return the content without backslashes
   */
  public static String removeChar(String content, char ch) {

    if (content == null) {
      return null;
    }

    StringBuffer buff = new StringBuffer();
    buff.append(content);

    int len = buff.length();
    for (int i = len - 1; i >= 0; i--) {
      if (ch == buff.charAt(i)) {
        buff.deleteCharAt(i);
      }
    }
    return buff.toString();
  }

  /**
   * Builds a list of the recipients email addresses each on a different line,
   * starting just from the second line with an HT ("\t") separator at the
   * head of the line. This is an implementation of the 'folding' concept from
   * the RFC 2822 (par. 2.2.3)
   *
   * @param recipients A string containing all recipients comma-separated
   * @return A string containing the email list of the recipients spread over
   * more lines, ended by CRLF and beginning from the second with the
   * WSP defined in the RFC 2822
   */
  public static String fold(String recipients) {
    String[] list = StringUtil.split(recipients, ",");

    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < list.length; i++) {
      String address = list[i] + (i != list.length - 1 ? "," : "");
      buffer.append(i == 0 ? address + CRLF : HT + address + CRLF);
    }

    return buffer.toString();
  }

  /**
   * This method is missing in CLDC 1.0 String implementation
   */
  public static boolean equalsIgnoreCase(String string1, String string2) {
    // Strings are both null, return true
    if (string1 == null && string2 == null) {
      return true;
    }
    // One of the two is null, return false
    if (string1 == null || string2 == null) {
      return false;
    }
    // Both are not null, compare the lowercase strings
    return (string1.toLowerCase()).equals(string2.toLowerCase());
  }

  /**
   * Util method for retrieve a boolean primitive type from a String.
   * Implemented because Boolean class doesn't provide
   * parseBoolean() method.
   * Returns true if the input string is equal to "true" (case
   * insensitive) false otherwise
   */
  public static boolean getBooleanValue(String string) {
    if ((string == null) || string.equals("")) {
      return false;
    } else {
      return StringUtil.equalsIgnoreCase(string, "true");
    }
  }

  /**
   * Removes characters 'c' from the beginning and the end of the string
   */
  public static String trim(String s, char c) {
    if (s == null) {
      return null;
    }
    if (s.length() == 0) {
      return "";
    }

    int start = 0;
    int end = s.length() - 1;

    while (s.charAt(start) == c) {
      if (++start >= end) {
        // The string is made by c only
        return "";
      }
    }

    while (s.charAt(end) == c) {
      if (--end <= start) {
        return "";
      }
    }

    return s.substring(start, end + 1);
  }

  /**
   * Returns true if the given string is null or empty.
   */
  public static boolean isNullOrEmpty(String str) {
    if (str == null) {
      return true;
    }
    return str.trim().equals("");
  }

  /**
   * This method extracts from address the protocol port and resources
   *
   * @param url eg. http://127.0.0.1:8080/sync
   * @param protocol http
   * @return 127.0.0.1
   */
  public static String extractAddressFromUrl(String url, String protocol) {
    String prefix = protocol + "://";
    if (url.startsWith(prefix)) {
      url = url.substring(prefix.length(), url.length());
    }
    // If we have port number in the address we strip everything
    // after the port number
    int pos = url.indexOf(':');
    if (pos >= 0) {
      url = url.substring(0, pos);
    }

    // If we have resource location in the address then we strip
    // everything after the '/'
    pos = url.indexOf('/');
    if (pos >= 0) {
      url = url.substring(0, pos);
    }
    return url;
  }

  /**
   * This method extracts from address the resources
   *
   * @param url eg. http://127.0.0.1:8080/sync
   * @return http://127.0.0.1:8080
   */
  public static String extractAddressFromUrl(String url) {
    int start = 0;
    if (url.startsWith("https://")) {
      start = 8;
    } else if (url.startsWith("http://")) {
      start = 7;
    }
    // If we have resource location in the address then we strip
    // everything after the '/'
    int pos = (url.substring(start)).indexOf('/');
    if (pos >= 0) {
      url = url.substring(0, pos + start);
    }
    return url;
  }

  public static String removePortFromUrl(String url, String protocol) {
    String prefix = protocol + "://";
    int beginning = 0;
    if (url.startsWith(prefix)) {
      beginning = protocol.length() + 3;
    }

    int pos = url.indexOf(':', beginning);
    if (pos >= 0) {
      int slash = url.indexOf('/', pos);
      url = url.substring(0, pos) + url.substring(slash);
    }
    return url;
  }

  /**
   * This method retrieves the protocol used in the given url.
   *
   * @param url eg. http://127.0.0.1:8080/sync
   * @return the url protocol (e.g. http). Return null if the protocol is
   * not found.
   */
  public static String getProtocolFromUrl(String url) {
    int protocolEndIndex = url.indexOf("://");
    if (protocolEndIndex > 0) {
      return url.substring(0, protocolEndIndex);
    }
    return null;
  }

  /**
   * This method check if the protocol used in the given url is valid.
   *
   * @param url eg. http://127.0.0.1:8080/sync
   * @return the url protocol (e.g. http). Return null if the protocol is
   * not found.
   */
  public static boolean isValidProtocol(String url) {
    String protocol = getProtocolFromUrl(url);
    if (protocol == null) {
      return false;
    } else return protocol.equals("http") || protocol.equals("https");
  }

  /**
   * This method check if a string ends with the specified suffix
   * ignoring the case.
   *
   * @param str - string to check
   * suffix - the suffix to find
   * @return true if the character sequence represented by the argument is a suffix of the
   * character sequence represented by this object; false otherwise.
   */

  public static boolean endsWithIgnoreCase(String str, String suffix) {

    return (str.toLowerCase()).endsWith(suffix.toLowerCase());
  }

  /**
   * Replace all characters c1 with c2
   *
   * @param s the String to be manipulated
   * @param c1 the char to be replaced
   * @param c2 the char to put in place of c1
   * @return the new string
   */
  public static String replaceAll(String s, char c1, char c2) {
    StringBuffer sb = new StringBuffer(s);
    for (int i = 0; i < sb.length(); i++) {
      char c = sb.charAt(i);
      if (c == c1) {
        sb.setCharAt(i, c2);
      }
    }
    return sb.toString();
  }

  /**
   * Replace any occurrence of one string with another one
   *
   * @param s the String to be manipulated
   * @param src the string to be replaced
   * @param tgt the replacement string
   * @return the replaced string (newly allocated). If src is not found the
   * method returns a string identical to the original one
   */
  public static String replaceAll(String s, String src, String tgt) {
    StringBuffer sb = new StringBuffer();
    int pos = s.indexOf(src);

    int start = 0;
    while (pos != -1) {

      String portion = s.substring(start, pos);
      sb.append(portion);
      sb.append(tgt);

      if (pos + src.length() < s.length()) {
        // Get ready for another round
        start = pos + src.length();
        pos = s.indexOf(src, pos + src.length());
      } else {
        pos = -1;
        start = s.length();
      }
    }

    // Append the last chunk
    if (start < s.length()) {
      sb.append(s.substring(start));
    }

    return sb.toString();
  }

  //验证手机号
  public static boolean isValidPhone(String phone) throws PatternSyntaxException {
    return isMobileNO(phone) || isHKPhoneLegal(phone);
   }

  /**
   * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
   */
  public static boolean isMobileNO(String mobiles) throws PatternSyntaxException {
    String telRegex = "[1][23456789]\\d{9}";
    return mobiles.matches(telRegex);
  }

  /**
   * 香港手机号码8位数，5|6|8|9开头+7位任意数
   */
  public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
    String regExp = "^(5|6|8|9)\\d{7}$";
    Pattern p = Pattern.compile(regExp);
    Matcher m = p.matcher(str);
    return m.matches();
  }

  public static String getMD5String(String string){
    byte[] hash;
    try {
      hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }

    StringBuilder hex = new StringBuilder(hash.length * 2);
    for (byte b : hash) {
      if ((b & 0xFF) < 0x10)
        hex.append("0");
      hex.append(Integer.toHexString(b & 0xFF));
    }

    return hex.toString();
  }
}


