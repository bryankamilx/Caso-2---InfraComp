package paginacion;
public class Contador {

    private int hits;
    private int miss;

    public Contador() {
        this.hits = 0;
        this.miss = 0;
    }

    public synchronized void addHit() {

        hits = hits + 1;

    }

    public synchronized void addMiss() {

        miss = miss + 1;

    }

    public synchronized int getHits() {
        return this.hits;
    }

    public synchronized int getMiss() {
        return this.miss;
    }
    
}
