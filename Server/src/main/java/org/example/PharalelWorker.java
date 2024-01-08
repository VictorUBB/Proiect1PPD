package org.example;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PharalelWorker extends Thread{

    private int nrTari;
    private int nrConcurenti;
    private int nrProbleme;

    private NodeBlockingQueue queue;



    private NodeSyncList finalList;

    private CountDownLatch latch;


    private List<Node> bannedList;

private int id;
    public PharalelWorker(int nrTari, int nrConcurenti, int nrProbleme, NodeBlockingQueue queue, NodeSyncList finalLst, List<Node> bannedList, int id, CountDownLatch latchWorker) {
        this.nrTari = nrTari;
        this.nrConcurenti = nrConcurenti;
        this.nrProbleme = nrProbleme;
        this.queue=queue;
        this.finalList=finalLst;
        this.bannedList = bannedList;
        this.id=id;
        this.latch=latchWorker;
    }
    public PharalelWorker(int nrTari, int nrConcurenti, int nrProbleme, NodeBlockingQueue queue) {
        this.nrTari = nrTari;
        this.nrConcurenti = nrConcurenti;
        this.nrProbleme = nrProbleme;
        this.queue=queue;

    }
    @Override
    public void run(){

        try {
           // System.out.println("Thred stareted" + id);
            while (latch.getCount() > 0 ||queue.getCurrentSize()>0) {

                Node node = queue.pop();
                while (finalList.inUse(node)){
                    Thread.sleep(5);
                }
                finalList.addtoUse(node);
                //latch.countDown();
                synchronized (bannedList){
                    if(bannedList.contains(node)){
                        finalList.delFromUse(node);
                        continue;
                    }
                    if(node.getPunctaj() == 0){
                        finalList.delFromUse(node);
                        continue;

                    }
                    if(node.getPunctaj() == -1){
                        bannedList.add(node);
                        Node poz= finalList.get(node);

                        if(poz !=null){
                            //poz.lock();
                            finalList.delete(poz);
                            //poz.unlock();
                        }
                        finalList.delFromUse(node);
                        continue;
                    }
                }

                //System.out.println(node + " node");

                    Node poz = finalList.get(node);

                    if (poz != null) {
                        poz.lock();
                        poz.setPunctaj(node.getPunctaj() + poz.getPunctaj());
                        //System.out.println(poz);
                        poz.unlock();

                    } else {
                        finalList.insert(node);
                       // System.out.println(node + " insert");
                    }
//
                finalList.delFromUse(node);
                }
//            if(queue.getCurrentSize()==0){
//                System.out.println("Id :" + id);
//            }

            System.out.println("Thread finished execution." + id);                //System.out.println(problem.toString()+"/"+id);
            } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }

}
