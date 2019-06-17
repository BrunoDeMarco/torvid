package src;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class RequestController {

    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

//    public static void main(String[] args){
//        SimpleClient client = new SimpleClient();
//        try {
//            InetAddress address = InetAddress.getLocalHost();
//            client.downloadTorrent("/home/bruno.appolonio/Projects/torvid/server/torrent-in/C5C9B9A20573A95810813823DD5F548021ED6B6A.torrent", "/home/bruno.appolonio/Projects/torvid/server/torrent-out", address);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        client.stop();
//    }

}
