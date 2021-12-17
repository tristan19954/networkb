import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Broker {
    public static void main(String[] args){
        BlockingQueue<String> globalQueue = new LinkedBlockingDeque<String>();
        BlockingQueue<byte[]> storageQueue = new LinkedBlockingDeque<byte[]>();
        try {

            ServerSocket ss = new ServerSocket(2627);
            ExecutorService threadPool = Executors.newFixedThreadPool(100);
            Storage storage = new Storage(storageQueue);

            while(true) {
                Socket s = ss.accept();
                // read

                BlockingQueue<byte[]> localQueue = new LinkedBlockingDeque<byte[]>(100);
                BlockingQueue<String> subscribeQueue = new LinkedBlockingDeque<String>(300);
                ServerProcessor processor = new ServerProcessor(s, localQueue,subscribeQueue, globalQueue, storage);

                threadPool.submit(new ServerReader(s, processor));
                // write
                threadPool.submit(new ServerWriter(s,localQueue, processor, storageQueue));
            }
        } catch(IOException e) {
            System.out.println("Couldn't reach localhost.");
        } catch(IllegalArgumentException e) {
            System.out.println(e + "Wrong port specified!");
        }
    }
}