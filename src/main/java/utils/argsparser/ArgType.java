package utils.argsparser;
/**
 * 
 * Enum representing possible argument types.
 * @author MACIEJ KAèMIERCZYK
 * @author JANUSZ IGNASZAK
 *
 */
public enum ArgType {
	
	STRING("STRING"),
	INT("INT"),
	FLOAT("FLOAT"),
	BOOL("BOOL"),
	TOGGLE("");

	private final String value;

    private ArgType(String value) {
        this.value = value;
    }
    
    public String getTypeValue() {
    	return value;
    }
}
