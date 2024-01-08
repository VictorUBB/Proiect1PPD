package org.example;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client {
    private static Socket socket;
    private static ObjectInputStream dataIn;
    private static ObjectOutputStream dataOut;

    public static void createFiles(int tara, int nrConcurenti, int nrProbleme) throws IOException {
        Random rand = new Random();


            for(int idProb=0;idProb<nrProbleme;idProb++){
                String filename="files/Rezultate"+tara+"_"+idProb+".txt";
                FileWriter myWriter = new FileWriter(filename);
                for(int conc = 0 ;conc<nrConcurenti;conc++){
                    int points =rand.nextInt(101)-1;
                    String id= String.valueOf(tara)+String.valueOf(conc);

                        myWriter.write(id+";"+points+"\n");


                }
                myWriter.close();
            }
    }
    public static List<Pair> read(int nrP,String tara){

        BufferedReader reader = null;
        List<Pair> pairs = new ArrayList<>();
       for(int nrProb =0; nrProb<nrP;nrProb++){
           String filename="files/Rezultate"+tara+"_"+nrProb+".txt";
           try {
               reader = new BufferedReader(new FileReader(filename));
           } catch (FileNotFoundException e) {
               throw new RuntimeException(e);
           }
           for(String line : reader.lines().toList()){
               String[] splitLine= line.split(";");
               int id=Integer.valueOf(splitLine[0]);
               int points=Integer.valueOf(splitLine[1]);

               Pair problem= new Pair(id,points,Integer.valueOf(tara));
               try {
                   // System.out.println(problem);
                   pairs.add(problem);

               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
           }


           try {
               reader.close();

           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }

        return pairs;
    }
    public static void main(String [] args) throws IOException, InterruptedException, ClassNotFoundException {
        // Extract the arguments from command line arguments
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(System.in));
//        System.out.println("Enter the country:");
//        String tara = reader.readLine();
        String tara =args[0];

        // Create a new socket and attempt to connect to the server
        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 5001), 1000);
            System.out.println("Connection Successful!");
            dataOut = new ObjectOutputStream(socket.getOutputStream());
            dataIn = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }


        // Initialize input and output streams for communication with the server


        // Send calculation details to the server
        createFiles(Integer.valueOf(tara),20,10);
        List<Pair> pairs=read(10,tara);
//        Message startReading =  new Message(Type.START,"starte reading",tara);
//        startReading.setSize(pairs.size());
        //dataOut.writeObject(startReading);
        int current = 0;
        while (current < pairs.size()){
            List<Pair> pairsToSend = new ArrayList<>(pairs.subList(current,current + 20));
            Message message = new Message(Type.DATA,pairsToSend,tara);
            current+=20;
            dataOut.writeObject(message);
            Message info = new Message(Type.LEADER,new ArrayList<>(),tara);
            dataOut.writeObject(info);
            Message response = (Message) dataIn.readObject();
            if(response.getType() == Type.LEADER){
                System.out.println("LEADERBOARD");
                for(Pair p : response.getData()){
                    System.out.println(p);
                }
            }
            Thread.sleep(1000);
        }
        Message stop = new Message(Type.STOP,"ended",tara);
        dataOut.writeObject(stop);

            // Receive and print the server's response
            //String serverMessage = dataIn.readUTF();
            //System.out.println("Result: " + serverMessage);

        }


}
