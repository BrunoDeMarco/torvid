import com.turn.ttorrent.client.SimpleClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SimpleDownloader {
    public void Main(){
        SimpleClient a = new SimpleClient();
        try {
            InetAddress address = InetAddress.getLocalHost();
            a.downloadTorrent("a", "a", address);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
