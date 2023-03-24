package com.mannash.simcardvalidation;

import java.util.ArrayList;
import java.util.List;

public class ThreadController {
    private static List<Thread> threadList = new ArrayList<Thread>();

    public static void addThread(Thread thread){
        threadList.add(thread);
    }
    public static void interruptThread(){

            for (Thread thread : threadList) {
                try {
                    System.out.println("Thread status before interrupting : " + thread.getName().toString() + " status : " + thread.isAlive() );
                System.out.println("Interrupting thread : " + thread.getName().toString());
                thread.interrupt();
                thread.stop();

                    System.out.println("Thread status after interrupting : " + thread.getName().toString() + " status : " + thread.isAlive() );

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

    }
    public  static void clear(){
        threadList.clear();
    }

}
