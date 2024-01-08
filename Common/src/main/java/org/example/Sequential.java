package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Sequential {
    private int nrTari;
    private int nrConcurenti;
    private int nrProbleme;


    public Sequential(int nrTari, int nrConcurenti, int nrProbleme) throws IOException {
        this.nrTari = nrTari;
        this.nrConcurenti = nrConcurenti;
        this.nrProbleme = nrProbleme;

    }

    public List<Pair> run() throws IOException {
        List<Pair> queue = new LinkedList<>();
        List<Pair> banned =  new ArrayList<>();
        for(int i=0;i<nrTari;i++){

            for(int idProb=0;idProb<nrProbleme;idProb++){
                String filename="files/Rezultate"+i+"_"+idProb+".txt";
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                for(String line : reader.lines().toList()){
                    String[] splitLine= line.split(";");
                    int id=Integer.valueOf(splitLine[0]);
                    int points=Integer.valueOf(splitLine[1]);
                    int tara = i;
                    Pair problem= new Pair(id,points,tara);
                    if(banned.contains(problem)){
                        continue;
                    }
                    if(points == 0){
                        continue;
                    }
                    if(points == -1){
                        banned.add(problem);
                        queue.remove(problem);
                        continue;
                    }

                    int poz= queue.indexOf(problem);
                    if(poz != -1 ){
                        Pair pair = queue.get(poz);
                        pair.Punctaj += problem.getPunctaj();
                    }
                    else {
                        queue.add(problem);
                    }
                    queue.sort((a,b) -> {
                        return b.Punctaj - a.Punctaj;
                    });
                }


            reader.close();

            }

        }
        FileWriter outWriter = new FileWriter("files/Clasament.txt");
        for(int i=0;i<queue.size();i++){
            outWriter.write(queue.get(i).toString()+"\n");

        }
        outWriter.close();
        return queue;
    }

}
