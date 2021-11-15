/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syncsemexample;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SyncSemExample {
    
    private static String[] names = {"Anton", "Boris", "Vasiliy", "Vladimir", "Grigoriy", 
        "Dmitry", "Evgeniy", "Michail", "Nikolay", "Anyfry", "Pavel", "PETR"};

    //M zaniat li stol
    private static boolean tables[] = new boolean[5];
    
    private static Semaphore sem = new Semaphore(5, true);//5 - kol razresheniy. boolean - v sluchainom poriadke - F, T - po poriadky
    
    
     
    public static void main(String[] args) {
         for (int i =0 ; i<names.length; i++){
             new Thread(new Client(names[i])).start();
             try {
                 Thread.sleep((int)(Math.random()*2000));
             } catch (InterruptedException ex) {
                 Logger.getLogger(SyncSemExample.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
        
        
        
        
    }//MAIN
    
    
    public static class Client implements Runnable {
        
        private String name;
        
        public Client (String name){
            this.name = name;
        }
        
        @Override
        public void run() {
            System.out.println("Klient " + name + " prishel v kafe");
            try {
                //pytaemsia poluchit razreshrnie semafora
                sem.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncSemExample.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int freeTable = -1;
            int chosenTable = -1;
            for (int i =0 ; i<names.length; i++){
                if(!tables[i]){
                    tables[i] = true;
                    System.out.println(name + " zanial stolik " + i);
                    chosenTable = i;
                    break;
                }
            }
            
            try {
                Thread.sleep((int)(1000 + Math.random()*3600));
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncSemExample.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            tables[chosenTable] = false;
            sem.release();//otdaem razreshenie semaforu obratno. on dast ego tomu kto jdet acuire
            System.out.println(name + " osvobodil stolik N " + chosenTable + " Yshel ne zaplativ");
            
        }
    
}
    
    
    
    
    
}//KONEC
