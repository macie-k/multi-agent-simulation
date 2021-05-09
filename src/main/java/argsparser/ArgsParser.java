package argsparser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import utils.Log;

import static utils.Utils.isNumber;

public class ArgsParser {
	private static ArrayList<Argument> arguments = new ArrayList<>();
	private static HashMap<String, Object> values = new HashMap<>();
	
	public ArgsParser() {
		/* add help argument */
		arguments.add(new Argument("--help", "-h", "show this help message and exit", () -> {
			showHelp();
		}));
	}
	
	/* parse arguments */
	public void parse(String[] args) {		
		if(args.length == 0) return;
		
		/* show help if argument present & exit program */
		for(String arg : args) {
			if(arg.equals("-h") || arg.equals("--help")) {
				showHelp();	
				System.exit(1);
			}
		}
		
		for(String arg : args) {
			boolean exists = false;
									
			String name = Argument.sanitizeName(arg.split("=")[0]);			// extract argument's name
			Object value = arg.contains("=") ? arg.split("=")[1] : null;	// extract argument's value
			ArgType valueType = ArgType.TOGGLE;		// default its type to TOGGLE
			
			/* if value exists check for its type */
			if(value != null) {
				String strVal = (String) value;
				
				value = (strVal).contains("\"") ? (strVal).split("\"")[1] : value;	// remove quotation marks if present
				valueType = ArgType.STRING;		// default the type to STRING
				
				/* check if value is numeric */
				if(isNumber(strVal)) {
					valueType = ArgType.FLOAT;			// default numeric value to FLOAT
					value = Double.parseDouble(strVal);
					
					/* check if is integer and change type */
					if(((double) value) % 1 == 0) {
						valueType = ArgType.INT;
						value =  Integer.parseInt(strVal);
					}
				}
				
				/* check if value is logical */
				if(Boolean.parseBoolean(strVal)) {
					valueType = ArgType.BOOL;
					value = Boolean.parseBoolean(strVal);
				}
			}

			for(Argument a : arguments) {
				/* if provided argument matches any of the created */
				if(a.getLongName().equals(name) || a.getShortName().equals(name)) {
					exists = true;
					values.put(a.getLongName(), value);		// save name and value to map
					
					/* check if type is correct */
					final ArgType type = a.getType();
					if(type != valueType) {
						Log.error("Wrong value type for argument " + name + ", expected: " + type + " provided: " + valueType, true);
						continue;
					}
					a.run();	// run assigned functionality
				}
			}
			
			if(!exists) {
				Log.warning("Unknown argument: " + name);
			}
			
		} showMessages();
		System.out.println();
	}
		
	/* display help if -h or --help is present */
	private void showHelp() {
		arguments.sort(new nameSorter());	// sort arguments
		System.out.println("available arguments: ");
		
		/* calc maximum length for correct indentation */
		int maxLen = 0;
		for(Argument a : arguments) {
			final int len = 2 + (1 + a.getShortName().length()) + 2
					+ (2 + a.getLongName().length())
					+ (1 + a.getType().getTypeValue().length());
			if(len > maxLen) {
				maxLen = len;
			}
		}
		
		/* print argument's names & accepted types */
		for(Argument a : arguments) {
			final String sName = a.getShortName();
			final String lName = a.getLongName();
			final ArgType type = a.getType();
			
			String names = "  ";

			if(!sName.equals("")) {
				names += "-" + sName + ", ";
			}
			
			names += "--" + lName;
			if(type != ArgType.TOGGLE) {
				names += "=" + type.getTypeValue();
			}
			
			System.out.printf("%-" + (maxLen + 4) + "s %s\n", names, a.getHelp());	// indent and print help message
		}
	}
	
	/* method for sorting arguments by longName field */
	private class nameSorter implements Comparator<Argument> {
		@Override
		public int compare(Argument arg0, Argument arg1) {
			return arg0.getLongName().compareToIgnoreCase(arg1.getLongName());
		}
	}
	
	/* print messages only for used arguments */
	private void showMessages() {
		for(Argument a : arguments) {
			if(values.containsKey(a.getLongName())) {
				final String mess = a.getMessage();
				if(mess != null) Log.success(mess);
			}
		}
	}
	
	public Object getValue(String name) {
		return values.get(name);
	}
	
	/* method for adding and checking arguments */
	public void addArguments(Argument ...args) {	
		final ArrayList<String> usedNames = new ArrayList<>();		// list for all -n and --names
		for(Argument a : args) {
			final String lName = a.getLongName();
			final String sName = a.getShortName();
			
			String dup = null;
			
			if(!sName.equals("") && usedNames.contains(sName))
				dup = "-" + sName;
			
			if(usedNames.contains(lName))
				dup = "--" + lName;
			
			if(dup != null)
				Log.error("Duplicated argument name: " + dup, true);
			
			usedNames.add(lName);
			usedNames.add(sName);
			arguments.add(a);
		}
	}
}
