import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class Storage {
    HashMap<String, ArrayList<String>> subscriptions;
    BlockingQueue<byte[]> storageQueue;
    public Storage(BlockingQueue<byte[]> storageQueue){
        this.subscriptions = new HashMap<String, ArrayList<String>>();
        this.storageQueue = storageQueue;
    }

    private byte[] intToByteArray(int packetId) {
        return new byte[] {
                (byte)(packetId>> 8),
                (byte) packetId};
    }

    public void addSubscribe(String topic, String client_id){
        if (subscriptions.containsKey(topic)){
            ArrayList<String> subscriberIds = subscriptions.get(topic);
            subscriberIds.add(client_id);
            subscriptions.replace(topic,subscriberIds);
        }
        else{
            ArrayList<String> subscriberId = new ArrayList<>();
            subscriberId.add(client_id);
            subscriptions.put(topic, subscriberId);
        }
    }
    public void removeSubscribe(String topic, String client_id){
        if (subscriptions.containsKey(topic)){
            ArrayList<String> subscriberIds = subscriptions.get(topic);
            subscriberIds.remove(client_id);
            subscriptions.replace(topic,subscriberIds);
        }
        else{
            System.out.println("Unsubscribe could not be done as topic doesn't exist");
        }
    }

    public void transmitMessage(String topic, byte[] message){
        if (subscriptions.containsKey(topic)){
            ArrayList<String> subscriberIds = subscriptions.get(topic);
            for(int i =0; i<subscriberIds.size(); i++){
                byte[] ids = new byte[0];
                try{
                    ids = subscriberIds.get(i).getBytes("UTF8");
                }catch(UnsupportedEncodingException e) {
                    System.out.println("Error of encoding");
                }
                int idsLength = ids.length;
                byte[] idsLengthByte = intToByteArray(idsLength);


                byte[] messageTransmitted = new byte[message.length + 2 + idsLength];

                //Generates a byte array with client identifier as the first bytes
                System.arraycopy(idsLengthByte, 0, messageTransmitted,0, 2);
                System.arraycopy(ids, 0, messageTransmitted,2,idsLength);
                System.arraycopy(message, 0, messageTransmitted,2+idsLength, message.length);
                for(int b =0; b<message.length; b++){
                    System.out.println(String.format("%8s", Integer.toBinaryString(message[b] & 0xFF)).replace(' ', '0'));
                }

                storageQueue.add(messageTransmitted);
            }
        }
    }

    public HashMap<String, ArrayList<String>> getSubscriptions() {
        return subscriptions;
    }
}
