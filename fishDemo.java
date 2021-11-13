import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import java.util.*;

public class fishDemo {

    public static void main(String[] args) {

        int numberOfFish = ThreadLocalRandom.current().nextInt(1, 100);
        int males = 0;
        int females = 0;
        FishThread fish;
        int periodOfTime;
        int wholeTime = 0;
        int randomIndexOfFIsh; // initializing info variables

        ExecutorService ex = Executors.newCachedThreadPool();
        Vector<FishThread> newFishes = new Vector<FishThread>();
        Vector<FishThread> allFishes = new Vector<FishThread>(); // creating collections of new and old and dead fish
        Vector<FishThread> deadFishes = new Vector<FishThread>();

        for (int i = 0; i < numberOfFish; i++) { // adding each Fish in the pool
            fish = new FishThread(); // creating thread for each fish
            ex.execute(fish);
            allFishes.add(fish);
            if (fish.getGender() == 'M') {
                males++;
            } else {
                females++;
            } // assigning gender to fish
        }

        // printing info
        System.out.println("In the beggining there are: " + allFishes.size() + " fish in the akvarium, there are "
                + females + " females, and " + males + " males.");

        // going through everyFish to see if they met opposite gender
        for (FishThread eachFish : allFishes) {
            periodOfTime = ThreadLocalRandom.current().nextInt(1, 10); // random period of time in the interval of 10
                                                                       // days
            wholeTime += periodOfTime; // getting whole time

            synchronized (eachFish) {
                try {
                    randomIndexOfFIsh = (int) (Math.random() * allFishes.size()); // random fish

                    System.out.println("\nwaiting...");

                    /*
                     * waiting for specific time
                     * 
                     * EACH SECOND IS ONE DAY
                     */
                    eachFish.wait(periodOfTime * 100);
                    System.out.println("Its been " + periodOfTime + " days, and...");

                    // checking if the fish is alive to be able to breed, 50 days is max a fish can
                    // live
                    if (eachFish.getLongevity() + periodOfTime >= 50) {
                        System.out.println("One Fish Died");
                        FishThread deadFish = new FishThread(); // creating deadFish
                        deadFishes.add(deadFish);
                    } else if (allFishes.get(randomIndexOfFIsh).getLongevity() + periodOfTime >= 50) {
                        System.out.println("One Fish Died");
                        FishThread deadFish = new FishThread(); // creating deadFish
                        deadFishes.add(deadFish);

                        // if both chosen fish are alive then they breed.
                    } else {
                        if (eachFish.getGender() != allFishes.get(randomIndexOfFIsh).getGender()) {

                            FishThread newFish = new FishThread(); // creating babyFish
                            newFishes.add(newFish); // adding babyFish to new pool, in which every new fish will be
                                                    // stored

                            System.out.println("New fish is created! its " + newFish.getGender()); // seeing what gender
                                                                                                   // it
                                                                                                   // is

                            ex.execute(newFish); // making new Fish its own thread

                            if (newFish.getGender() == 'M') { // getting number of males and females if new pool
                                males++;
                            } else {
                                females++;
                            }

                        } else { // if fish have same gender, nothing will happen
                            System.out.println("nothing happened yet..");
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } // safety

            }

        }
        // getting overall info, after every fish had baby atleast once
        System.out.println("After " + wholeTime + " days, there is total of "
                + ((allFishes.size() + newFishes.size()) - deadFishes.size()) + " fish in the pool.");

        // terminating threads IMPORTANT
        ex.shutdown();
        while (!ex.isTerminated()) {
        }

        System.out.println("Finished all threads"); // to see that everything is finished.
    }

}

// create a threadpool for random number. DONE
// every thread will have name Nn or Mn based on their gender. DONE
// after random time check the process.