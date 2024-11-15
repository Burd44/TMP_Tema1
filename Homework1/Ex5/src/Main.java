import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static final int NUM_THREADS = 10;
    private static final int NUM_ACCESSES = 5; // Number of accesses per thread

    public static void main(String[] args) throws InterruptedException {
        System.out.println("** Important Note **");
        System.out.println("--------------------------------------------------");
        System.out.println("A cycle completes when a thread enters the critical section for the second time. \n" +
                "For fairness, the number of cycles corresponds to the number of accesses each thread makes.");
        System.out.println("--------------------------------------------------\n");

        System.out.println("Running Peterson Algorithm...");
        runAlgorithm(new PetersonAlgorithm(NUM_THREADS));

        System.out.println("\nRunning Peterson Improved Algorithm...");
        runAlgorithm(new PetersonImprovedAlgorithm(NUM_THREADS));
    }

    public static void runAlgorithm(PetersonAlgorithm algorithm) throws InterruptedException {
        List<Integer> cycleArray = new CopyOnWriteArrayList<>(); // Using thread-safe list
        AtomicInteger cycleCounter = new AtomicInteger(0); // Counts how many times threads entered critical section

        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            int threadId = i;
            threads[i] = new Thread(() -> {
                for (int access = 0; access < NUM_ACCESSES; access++) {
                    algorithm.lock(threadId);

                    // Critical section
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    synchronized (cycleArray) {
                        if (cycleArray.contains(threadId)) {
                            printCycle(cycleCounter.incrementAndGet(), cycleArray);
                            cycleArray.clear();
                        }
                    }
                    cycleArray.add(threadId);

                    algorithm.unlock(threadId);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        printCycle(cycleCounter.incrementAndGet(), cycleArray);
    }

    private static void printCycle(int cycleNumber, List<Integer> cycleArray) {
        StringBuilder cycleOutput = new StringBuilder("Cycle " + cycleNumber + ": [");
        for (int i = 0; i < cycleArray.size(); i++) {
            cycleOutput.append(cycleArray.get(i));
            if (i < cycleArray.size() - 1) {
                cycleOutput.append(", ");
            }
        }
        cycleOutput.append("]");
        System.out.println(cycleOutput);
    }
}
