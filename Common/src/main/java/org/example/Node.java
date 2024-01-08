package org.example;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private int id;
    private int punctaj;
    public Node next;
    public Node previous;

    private int tara;
    private final Lock lock;

    public Node(int id, int punctaj, Node next, Node previous,int tara) {
        this.id = id;
        this.punctaj = punctaj;
        this.next = next;
        this.previous = previous;
        this.tara = tara;
        this.lock = new ReentrantLock();
    }

    public int getTara() {
        return tara;
    }

    public void setTara(int tara) {
        this.tara = tara;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id && tara == node.tara;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tara);
    }

    @Override
    public String toString() {
        return  tara+";"+id +
                ";" + punctaj
                ;
    }
}
