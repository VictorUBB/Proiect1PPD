package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Server {

    private LocalTime lastCheck = LocalTime.MIN;

    private List<Pair> clasament;

    private Integer deltaT;

    private NodeSyncList finalList;
    public static long calculateTimeDifferenceInMilliseconds(LocalTime time1, LocalTime time2) {
        Instant instant1 = time1.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = time2.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();

        Duration duration = Duration.between(instant1, instant2);
        return Math.abs(duration.toMillis());
    }
    public CompletableFuture<List<Pair>> calculateClasament(int nrTari){
       if (calculateTimeDifferenceInMilliseconds(LocalTime.now(),this.lastCheck) > deltaT){
           return CompletableFuture.supplyAsync(()->{
               List<Pair> clasament = new ArrayList<>();
                synchronized (finalList) {
                    for (int i = 0; i < nrTari; i++) {
                        clasament.add(new Pair(-1, 0, i));
                    }
                    Node curr = finalList.start;

                    while (curr != finalList.last && curr != null) {
                        clasament.get(curr.getTara()).setPunctaj(clasament.get(curr.getTara()).getPunctaj() + curr.getPunctaj());
                        curr = curr.next;
                        //System.out.println(curr);
                    }
                }
               clasament.sort((a,b) -> {
                   return b.Punctaj - a.Punctaj;
               });
               System.out.println("Sort clasamanet " + LocalTime.now());
               this.clasament =clasament;
               this.lastCheck = LocalTime.now();
               return clasament;
           });
       }else{
           return CompletableFuture.supplyAsync(()->{
               return this.clasament;
           });

       }


    }

    public void run(String[] args) throws IOException, IOException, InterruptedException {
        int nrThreadsWorkers = 2;
        int nrThreadReaders = 4;
        int nrTari =10;
        int nrProbleme =10;
        int nrParticipanti =20;
        this.deltaT = 4;
        this.lastCheck = LocalTime.now();
        ServerSocket serverSocket = new ServerSocket(5001);
        System.out.println("Server Listening on port 5001...");
        ExecutorService executorService = Executors.newFixedThreadPool(nrThreadReaders);
        NodeBlockingQueue queue = new NodeBlockingQueue(1000);
        List<Node> banned = Collections.synchronizedList(new LinkedList<Node>());
        NodeSyncList finalLst= new NodeSyncList();
        this.finalList = finalLst;
        CountDownLatch latch =  new CountDownLatch(nrTari*nrProbleme);
        Thread[] workers = new PharalelWorker[nrThreadsWorkers];
        for(int idWorker = 0 ;idWorker < nrThreadsWorkers;idWorker ++){
            workers[idWorker] =  new PharalelWorker(nrTari,nrParticipanti,nrProbleme,queue,finalLst,banned,idWorker,latch);
            workers[idWorker].start();
        }
        int nrConn =0 ;
        while (nrConn < nrTari) {


            Socket connectionSocket = serverSocket.accept();
            nrConn++;
            Runnable reader = new PharalelReader(nrTari,nrParticipanti,nrProbleme,queue,connectionSocket,latch,this);

            executorService.execute(reader);

        }




        try {
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        executorService.shutdownNow();
        //executorService.awaitTermination();

        for(int idWorker = 0 ;idWorker < nrThreadsWorkers;idWorker ++){
            workers[idWorker].join();
        }

        Sequential sequential = new Sequential(nrTari,nrParticipanti,nrProbleme);
        List<Pair> seqFinalList = new ArrayList<>();
        seqFinalList=sequential.run();
        System.out.println("Not sorted");
        finalLst.sort();
        Node curr= finalLst.start;
        int i=0;
        while(curr!=finalLst.last){
            if(curr.getPunctaj()!=seqFinalList.get(i).getPunctaj() &&
                    curr.getId()!=seqFinalList.get(i).getId()){
                System.out.println("ERROR at "+curr.getId());
            }
            System.out.println(curr);
            i++;

            curr=curr.next;
            //System.out.println(curr);
        }
        //System.out.println(finalLst.last+" last");

    }
}
