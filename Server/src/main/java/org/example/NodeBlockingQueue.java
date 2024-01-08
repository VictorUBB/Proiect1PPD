package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NodeBlockingQueue {

    private int size;

    private int currentSize;

    public synchronized int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public NodeBlockingQueue(int size) {
        this.size = size;
        this.start = null;
        this.last = null;

    }
    

    private Node start;

    private Node last;


    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

    public void add(Pair pair) throws InterruptedException {
        lock.lock();
        try {
            Node node = new Node(pair.getId(),pair.getPunctaj(),null,null,pair.getTara());
            while (currentSize == size) {
                notFull.await();
            }


            if (start == null) {
                start = node;
                last = node;
            } else {
                last.next = node;
                last = node;
            }
            currentSize++;

            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Node pop() throws InterruptedException {
        lock.lock();
        try {
            while (currentSize == 0) {
                notEmpty.await();
            }

            Node removedData = start;
            start = start.next;
            currentSize--;

            if (currentSize == 0) {
                last = null;
            }

            notFull.signal();
            removedData.next = null;
            return removedData;
        } finally {
            lock.unlock();
        }
    }
    private boolean status =false;
    private int readers;
    private int doneReader=0;
    public synchronized boolean isDone(){
        return status;
    }

    public synchronized void setDone(){
        doneReader++;
        System.out.println("Readers:"+doneReader);
//        if(doneReader == readers){
//            this.status = true;
//        }

    }
}
