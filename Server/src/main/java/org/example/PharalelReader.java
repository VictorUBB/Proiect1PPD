package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class PharalelReader implements Runnable{
    private int nrTari;
    private int nrConcurenti;
    private int nrProbleme;

    private NodeBlockingQueue queue;

    private int start;


    private int end;

    private int id;


    private String filename;

    private CountDownLatch latch;

    private Socket socket;

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public PharalelReader(int nrTari, int nrConcurenti, int nrProbleme, NodeBlockingQueue queue, Socket socket, CountDownLatch latch,Server server) {
        this.nrTari = nrTari;
        this.nrConcurenti = nrConcurenti;
        this.nrProbleme = nrProbleme;
        this.queue = queue;
       this.socket = socket;
       this.latch = latch;
       this.server = server;
    }

    @Override
    public synchronized void run(){


        try {
            ObjectInputStream dataIn = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream dataOut = new ObjectOutputStream(socket.getOutputStream());
            Message message = (Message) dataIn.readObject();
            while (message.getType() != Type.STOP){
                System.out.println(message);
                if(message.getType() == Type.LEADER){
                    server.calculateClasament(nrTari)
                            .thenAccept(clasament -> {
                                try {
                                    //ObjectOutputStream dataOut = new ObjectOutputStream(socket.getOutputStream());
                                    Message response = new Message(Type.LEADER, clasament, "-1");
                                    dataOut.writeObject(response);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
                else{
                    latch.countDown();
                    for(Pair problem : message.getData()){

                        try {
                            // System.out.println(problem);
                            queue.add(problem);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                message= (Message) dataIn.readObject();

            }
            System.out.println(message);
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }


//        queue.setDone();
//        if(id==0){
//            while (!synchrBlockList.isDone()){
//                FileWriter outWriter = null;
//                try {
//                    outWriter = new FileWriter("files/Clasament1.txt");
//                    synchronized (clasamentList){
//                        for (Pair pair:clasamentList){
//                            outWriter.write(pair.toString()+"\n");
//                        }
//                    }
//
//                    outWriter.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }
    }
}
