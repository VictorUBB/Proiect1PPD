package org.example;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Pair implements Serializable {
    public int Id;
    public int Punctaj;

    public int tara;

    public Pair(int id, int punctaj, int tara) {
        Id = id;
        Punctaj = punctaj;
        this.tara = tara;
    }

    public int getTara() {
        return tara;
    }

    public void setTara(int tara) {
        this.tara = tara;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPunctaj() {
        return Punctaj;
    }

    public void setPunctaj(int punctaj) {
        Punctaj = punctaj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Id == pair.Id && tara == pair.tara;
    }

    @Override
    public String toString() {
        return tara+";"+Id+";"+Punctaj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id,tara);
    }

}

