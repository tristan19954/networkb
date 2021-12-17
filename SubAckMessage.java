public class SubAckMessage {
    int byteIdentifier;
    Integer returnCode;
    byte[] message = new byte[5];
    public SubAckMessage(int byteIdentifier, int returnCode) {
        this.byteIdentifier = byteIdentifier;
        this.returnCode = returnCode;
        generateMessage();
    }
    private static byte[] intToByteArray(int packetId) {
        return new byte[] {
                (byte)(packetId>> 8),
                (byte) packetId};
    }

    private void generateMessage(){
        // Always the same fixed header
        Integer firstByte = 64;
        Integer secondByte = 2;
        Integer lastByte = 0;
        message[0] = firstByte.byteValue();
        message[1] = secondByte.byteValue();

        byte[] byteIdentifierByte = intToByteArray(byteIdentifier);
        message[2] = byteIdentifierByte[0];
        message[3] = byteIdentifierByte[1];
        message[4] = returnCode.byteValue();
    }

    public byte[] getMessage() {
        return message;
    }
}
