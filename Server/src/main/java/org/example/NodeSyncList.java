package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NodeSyncList {
    public Node start;

    public Node last;

    private List<Node> inUseIds = new ArrayList<>();
    private int size;

    private final Lock lock;
    public NodeSyncList() {
        this.start = null;
        this.last = null;
        lock = new ReentrantLock();
    }

    public boolean inUse(Node id){
        lock.lock();
//        List<Integer> lst = new ArrayList<>();
//        lst.add(id);
//        lst.add(tara)
        boolean res = inUseIds.contains(id);
        lock.unlock();
        return res;
    }

    public void addtoUse(Node id){
        lock.lock();
        inUseIds.add(id);
        lock.unlock();
    }

    public void delFromUse(Node id){
        lock.lock();
        inUseIds.remove(id);
        lock.unlock();
    }

    public void insert(Node newNode) {
        //newNode.lock();
       // System.out.println(newNode.next+"insert");
            Node poz = get(newNode);
            if(poz!=null){
                poz.lock();
                poz.setPunctaj(poz.getPunctaj()+newNode.getPunctaj());
                poz.unlock();
          //      newNode.unlock();
                return;
            }
            if (start == null) {
                start = newNode;
                last = newNode;
            } else {
                if(this.last != null){
                    Node lastNode = this.last;
                    lastNode.lock();
                    lastNode.next = newNode;
                    newNode.previous = this.last;
                    lastNode.unlock();
                    this.last= newNode;
                }
                else{
                    this.last= newNode;
                }
//                    if(this.start!=null){
//                        Node startNode = this.start;
//                        startNode.lock();
//                        startNode.previous = newNode;
//                        newNode.next = this.start;
//                        startNode.unlock();
//                        this.start = newNode;
//                    }

                    newNode.next=null;



            }
        size++;

        //newNode.unlock();
    }

    public Node get(Node node){


//            if(this.last==null){
//                return null;
//            }
        if(size>0){
            Node current= this.start;
            while(current!=last.next && current!=null){
                current.lock();
                if(current.getId()==node.getId() && current.getTara() == node.getTara()){
                    current.unlock();

                    return current;
                }
                current.unlock();
                current=current.next;

            }
        }



            return null;


    }



    public void delete(Node nodeToDelete) {


        nodeToDelete.lock();
        if (nodeToDelete == start) {
            start = start.next;
        }
        if (nodeToDelete == last) {
            last = last.previous;

        }
        Node previousNode =  nodeToDelete.previous;
        Node nextNode = nodeToDelete.next;


        if (nodeToDelete.previous != null) {

            previousNode.lock();
            previousNode.next = nodeToDelete.next;
            previousNode.unlock();
        }
        if (nodeToDelete.next != null) {

            nextNode.lock();
            nextNode.previous = nodeToDelete.previous;
            nextNode.unlock();
        }


        nodeToDelete.unlock();
    }
    public void sort() {
        boolean swapped;
        Node current;
        Node lastNode = null;

        if (start == null)
            return;

        do {
            swapped = false;
            current = start;

            while (current.next != lastNode) {
                if (current.getPunctaj() < current.next.getPunctaj()) {
                    // Swap data of current and next nodes
                    int tempId = current.getId();
                    int tempPunctaj = current.getPunctaj();
                    int tempTare = current.getTara();
                    current.setId(current.next.getId());
                    current.setPunctaj(current.next.getPunctaj());
                    current.setTara(current.next.getTara());
                    current.next.setId(tempId);
                    current.next.setPunctaj(tempPunctaj);
                    current.next.setTara(tempTare);
                    swapped = true;
                }
                current = current.next;
            }
            lastNode = current; // Mark the last swapped node
        } while (swapped);
    }

}

//import java.util.List;
//import java.util.concurrent.locks.Lock;

//public class NodeSyncList {
//    public Node start;
//    public Node head;
//    public Node end;
//
//    public NodeSyncList() {
//        end = new Node(-1,-1,null, null);
//        start = new Node(-1,-1,null, end);
//        head = end;
//    }
//
//    public Node getHead() {
//        return head;
//    }
//
//    // return the node previous to the one that you're interested in
//    Node get(Node element) {
//        Node prev = start;
//        prev.lock();
//        Node curr = head;
//        curr.lock();
//        while (curr.getNext() != null) {
//            //System.out.println(curr.getData() + " " + element);
//            if (curr.getId()== element.getId()) {
//                return prev;
//            }
//            Node pp = prev;
//            prev = curr;
//            pp.unlock();
//            curr = curr.getNext();
//            curr.lock();
//        }
//        curr.unlock();
//        prev.unlock();
//        return null;
//    }
//
//    void insert(Node element) {
//        start.lock();
//        head.lock();
//
//        start.setNext(element);
//        Node aux = head;
//        head = element;
//
//        aux.unlock();
//        start.unlock();
//    }
//
//    void update(Node prev, Node element) {
//        Node found = prev.getNext();
//
//        found.setPunctaj(element.getPunctaj()+found.getPunctaj());
//
//        found.unlock();
//        prev.unlock();
//    }
//
//    void delete(Node prev) {
//        Node found = prev.getNext();
//
//        if (found == head) {
//            head = found.getNext();
//        }
//        prev.setNext(found.getNext());
//
//
//        found.unlock();
//        prev.unlock();
//    }

//    public void sort()
//    {
//        Node current = head, index = null;
//        Node temp;
//        while (current.getNext().getNext() != null) {
//            index = current.getNext();
//            while (index.getNext() != null) {
//                if (current.getPunctaj() > index.getPunctaj()) {
//                    temp = current;
//                    current.setData(index.getData());
//                    index.setData(temp);
//                }
//                index = index.getNext();
//            }
//            current = current.getNext();
//        }
//    }
//}


/*

         L    L
    0 -> 1 -> 2 -> 3 -> 4 -> 0
              N
         P    C
    Node search(T element) {
        Node prev = start;
        lock(head);
        Node current = head;
        lock(current.getNext());
        Node next = current.getNext();
        while (next != Null) {
            if (current.getData().equals(element))
                return Node;
            prev = current;
            current = next;
            next = current.getNext();
            if (next != Null)
                lock(next);
        }
        unlock(end);
    }

    void add(T element) {
        if (exits(elements))
            ...
        else
        lock(start);
        lock(head);
        Node node = new Node(element, head);
        unlock(head);
        start.setNext(node);
        unlock(start);
    }

    void delete(T element) {

    }

 */