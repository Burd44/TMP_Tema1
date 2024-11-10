import java.util.concurrent.atomic.AtomicIntegerArray;

public class PetersonAlgorithm {
    private static final int n = 10;
    private static AtomicIntegerArray level = new AtomicIntegerArray(n);
    private static AtomicIntegerArray accessCount = new AtomicIntegerArray(n); // Contor pentru accesări echitabile
    private static AtomicIntegerArray victim = new AtomicIntegerArray(n);

    public static void lock(int i) {
        for (int L = 1; L < n; L++) {
            level.set(i, L);
            victim.set(L, i);

            boolean waiting = true;
            while (waiting) {
                waiting = false;

                for (int k = 0; k < n; k++) {
                    // Dacă alt thread k are prioritate mai mare și nu a avut acces recent
                    if (k != i && level.get(k) >= L && victim.get(L) == i &&
                            accessCount.get(i) > accessCount.get(k)) {
                        waiting = true;
                        break;
                    }
                }
            }
        }
    }

    public static void unlock(int i) {
        accessCount.incrementAndGet(i); // Incrementăm contorul pentru thread-ul curent
        level.set(i, 0); // Resetăm nivelul pentru a elibera secțiunea critică
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    lock(id);
                    // Sectiunea critica
                    System.out.println("Thread " + id + " is in the critical section.");
                    unlock(id);
                }
            });
        }

        for (int i = 0; i < n; i++) {
            threads[i].start();
        }

        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
