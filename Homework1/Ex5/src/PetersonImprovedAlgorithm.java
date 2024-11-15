import java.util.concurrent.atomic.AtomicIntegerArray;

public class PetersonImprovedAlgorithm extends PetersonAlgorithm{

    private static AtomicIntegerArray accessCount; // Contor pentru accesări echitabile

    PetersonImprovedAlgorithm(int n){
        super(n);
        accessCount = new AtomicIntegerArray(n);
    }

    public void lock(int i) {
        for (int L = 1; L < n; L++) {
            level.set(i, L);
            victim.set(L, i);

            boolean waiting = true;
            while (waiting) {
                waiting = false;

                for (int k = 0; k < n; k++) {
                    // Dacă alt thread k are prioritate mai mare și nu a avut acces recent
                    if (k != i && level.get(k) >= L && (victim.get(L) == i ||
                            accessCount.get(i) > accessCount.get(k))) {
                        waiting = true;
                        break;
                    }
                }
            }
        }
    }

    public void unlock(int i) {
        accessCount.incrementAndGet(i); // Incrementăm contorul pentru thread-ul curent
        level.set(i, 0); // Resetăm nivelul pentru a elibera secțiunea critică
    }

}
