/**
 * This class implements the message object structure. This will be use as a
 * superclass for all the different messages depending on the packet type.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class Message {
    
    private final FixedHeader fixedHeader;
    private Object variableHeader;
    private Object payload;

	public Message(FixedHeader fixedHeader) {
		this.fixedHeader = fixedHeader;
	}

    public Message(FixedHeader fixedHeader, Object variableHeader) {
        this.fixedHeader = fixedHeader;
        this.variableHeader = variableHeader;
    }

    public Message(FixedHeader fixedHeader, Object variableHeader, Object payload) {
        this.fixedHeader = fixedHeader;
        this.variableHeader = variableHeader;
        this.payload = payload;
    }

	/**
	 * @return the fixedHeader
	 */
	public FixedHeader getFixedHeader() {
		return fixedHeader;
	}

    
    /** 
     * @return Object
     */
    public Object getVariableHeader() {
        return this.variableHeader;
    }

    
    /** 
     * @return Object
     */
    public Object getPayload() {
        return this.payload;
    }

	
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "Message :{" +
            " fixedHeader='" + getFixedHeader() + "'" +
            ", variableHeader='" + getVariableHeader().toString() + "'" +
            ", payload='" + "getPayload().toString()" + "'" +
            "}";
    }

}
