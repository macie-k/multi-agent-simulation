package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
/**
 * Class containing logs logic.
 * 
 * @author MACIEJ KAZMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public class Log {
		
	/* color logging if not in IDE */
	public static Boolean IDE = false;
	private static String SUCCESS = Ansi.colorize(" [OK] ", new AnsiFormat(Attribute.GREEN_TEXT()));
	private static String ERROR = Ansi.colorize(" [ERROR] ", new AnsiFormat(Attribute.RED_TEXT()));
	private static String WARNING = Ansi.colorize(" [!] ", new AnsiFormat(Attribute.YELLOW_TEXT()));
	public static void success(String s) {
		System.out.println(getTime() + (IDE ? " [OK] " : SUCCESS) + s);
	}
	
	public static void success(String s, boolean nocolors) {
		System.out.println(getTime() + " [OK] " + s);
	}
	
	public static void error(String e) {
		System.err.println(getTime() + (IDE ? " [ERROR] " : ERROR) + getCallerLog(e));
	}
	
	public static void error(String e, boolean nocolors) {
		System.err.println(getTime() + " [ERROR] " + getCallerLog(e));
	}
	
	public static void warning(String w) {
		System.out.println(getTime() + (IDE ? " [!] " : WARNING) + w);
	}
	
	public static void warning(String w, boolean nocolors) {
		System.out.println(getTime() + " [!] " + w);
	}
		
	/**
	 * Returns the class and method that threw the error
	 * @param log
	 * @return
	 */
	private static String getCallerLog(String log) {	   
		String[] callerClassSplit = Thread.currentThread().getStackTrace()[3].getClassName().split("\\.");
		String callerClass = callerClassSplit[callerClassSplit.length-1];
		String callerMethod = Thread.currentThread().getStackTrace()[3].getMethodName();

		return String.format("@%s.%s(): %s", callerClass, callerMethod, log);
	}
	
	/**
	 * Returns current time, the ? symbol (\u2022) may not be displayed properly in windows cmd 
	 * @return
	 */
	private static String getTime() {
		return String.format("[%s] \u2022", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
	}
}

