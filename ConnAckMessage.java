public class ConnAckMessage{
    boolean sp;
    Integer returnCode;
    byte[] message = new byte[4];
    public ConnAckMessage(boolean sp, int returnCode) {
        this.sp = sp;
        this.returnCode = returnCode;
        generateMessage();
    }
    
    private void generateMessage(){
        // Always the same fixed header
        Integer firstByte = 64;
        Integer secondByte = 2;
        message[0] = firstByte.byteValue();
        message[1] = secondByte.byteValue();

        Integer spValue = 0;
        if (sp){
            spValue = 1;
        }
        message[2] = spValue.byteValue();

        // can be 0 to 5
        message[3] = returnCode.byteValue();
    }

    public byte[] getMessage() {
        return message;
    }
}