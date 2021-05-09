package argsparser;

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
