import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class ServerWriter implements Runnable{
    
    private Socket s;
    private final BlockingQueue<byte []> localQueue;
    private final BlockingQueue<byte[]> storageQueue;
    private ServerProcessor processor;
    private volatile boolean running = true;

    public ServerWriter(Socket s, BlockingQueue<byte[]> localQueue, ServerProcessor Servproc, BlockingQueue<byte[]> storageQueue) {
        this.s = s;
        this.localQueue = localQueue;
        this.processor = Servproc;
        this.processor.setWriter(this);
        this.storageQueue = storageQueue;
    }
    private synchronized void analyzeStorage(OutputStream out, String client_id){
        byte[] message = storageQueue.peek();
        byte[] idLength = Arrays.copyOfRange(message, 0, 2);
        int length = ((idLength[0] & 0xFF) << 8) | (idLength[1] & 0xFF);
        byte[] identifierByte = Arrays.copyOfRange(message, 2, 2 + length);
        String client_identifier = "";
        try{
            client_identifier = new String(identifierByte ,"UTF8");
        }catch(UnsupportedEncodingException e){
            System.out.println("Something went wrong");
        }
        if(client_identifier.equals(client_id)){
            System.out.println("VICTORYYYYYYYYYYYYYYYYYYYYY");
            byte[] a = Arrays.copyOfRange(message, 2 + length, message.length);
           // for(int i=0; i<a.length;i++){
            //    System.out.println(String.format("%8s", Integer.toBinaryString(a[i] & 0xFF)).replace(' ', '0'));
           // }
            try {
                out.write(a);
                out.flush();
            } catch(IOException e){
                System.out.println("Something was interrupted");
            }
            storageQueue.remove();
        }
    }

    @Override
    public void run(){
        String client_id = processor.getClientId();
        while(running) {
            try{
                OutputStream out = s.getOutputStream();
    
                // do something
                // test
                while(true){
                    if(!localQueue.isEmpty()){
                        try{
                            byte[] a = localQueue.take();
                            out.write(a);
                            out.flush();
                        } catch(InterruptedException e){
                            System.out.println("Interrupted");
                        }
                    }
                    if(!storageQueue.isEmpty()){
                        client_id = processor.getClientId();
                        analyzeStorage(out, client_id);
                    }
                }
    
               // s.close();
            } catch (IOException a){
                System.out.println("ServerWorker died");
            }
        }   
    }

    /**
     * Stops the thread
     */
    public void terminate() {
        running = false;
    }
}
