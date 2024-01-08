package org.example;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Message implements Serializable {
    private Type type;
    private List<Pair> data;
    private String infoType;

    private String tara;

    private int size;

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Message(Type type, List<Pair> data, String tara) {
        this.type = type;
        this.data = data;
        this.tara = tara;
    }

    public Message(Type type, String infoType, String tara) {
        this.type = type;
        this.infoType = infoType;
        this.tara = tara;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Pair> getData() {
        return data;
    }

    public void setData(List<Pair> data) {
        this.data = data;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data=" + data +
                ", infoType='" + infoType + '\'' +
                ", tara='" + tara + '\'' +
                '}';
    }
}
