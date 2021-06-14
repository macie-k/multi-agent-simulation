package utils.argsparser;
/**
 * 
 * Class responsible for creating argument.
 * @author MACIEJ KAMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public class Argument {
	private final ArgAction action;
	private final String longName;
	private final String shortName;
	private final String help;
	private final String message;
	private final ArgType type;
	
	public Argument(String longName, String help, ArgType type, ArgAction action) {
		this(longName, "", help, type, action, null);
	}
	
	public Argument(String longName, String help, ArgAction action) {
		this(longName, "", help, ArgType.TOGGLE, action, null);
	}
	
	public Argument(String longName, String help, ArgType type, ArgAction action, String message) {
		this(longName, "", help, type, action, message);
	}
	
	public Argument(String longName, String help, ArgAction action, String message) {
		this(longName, "", help, ArgType.TOGGLE, action, message);
	}
	
	public Argument(String longName, String shortName, String help, ArgAction action, String message) {
		this(longName, shortName, help, ArgType.TOGGLE, action, message);
	}
	
	public Argument(String longName, String shortName, String help, ArgAction action) {
		this(longName, shortName, help, ArgType.TOGGLE, action, null);
	}
	
	public Argument(String longName, String shortName, String help, ArgType type, ArgAction action) {
		this(longName, shortName, help, type, action, null);
	}
	
	public Argument(String longName, String shortName, String help, ArgType type, ArgAction action, String message) {
		this.longName = sanitizeName(longName);
		this.shortName = sanitizeName(shortName);
		this.help = help;
		this.action = action;
		this.message = message;
		this.type = type;
	}
	
	public static String sanitizeName(String name) {
		String[] split = name.split("-");
		if(split.length > 3) {
			return name.split("--")[1];
		} else {
			return split[split.length-1];
		}
	}
	
	public ArgType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
		
	public void run() {
		action.run();
	}
	
	public String getHelp() {
		return help;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public String[] getNames() {
		return new String[] {longName, shortName};
	}
}
