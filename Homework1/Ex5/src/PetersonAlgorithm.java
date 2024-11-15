import java.util.concurrent.atomic.AtomicIntegerArray;

public class PetersonAlgorithm {
    protected final int n;
    protected final AtomicIntegerArray level;
    protected final AtomicIntegerArray victim;

    // Constructor initializes n and the arrays
    public PetersonAlgorithm(int n) {
        this.n = n;
        this.level = new AtomicIntegerArray(n);
        this.victim = new AtomicIntegerArray(n);
    }

    public void lock(int i) {
        for (int L = 1; L < n; L++) {
            level.set(i, L);
            victim.set(L, i);

            boolean waiting = true;
            while (waiting) {
                waiting = false;

                for (int k = 0; k < n; k++) {
                    if (k != i && level.get(k) >= L && victim.get(L) == i ) {
                        waiting = true;
                        break;
                    }
                }
            }
        }
    }

    public void unlock(int i) {
        level.set(i, 0);
    }

}
