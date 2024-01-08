package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Thread> clients =new ArrayList<>();
        for(int i =0 ;i<4;i++){
            String[] arg = {String.valueOf(i)};
            Thread tr = new Thread(()-> {
                try {
                    Client client = new Client();
                    client.main(arg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            clients.add(tr);
            tr.start();
            Thread.sleep(500);
        }

        for (int i =0 ;i<4;i++){
            clients.get(i).join();
        }
        System.out.println("Hello world!");
    }
}