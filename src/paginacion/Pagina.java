package paginacion;

public class Pagina {
    private int id;       // Identificador de la página virtual
    private boolean bitR; // Bit de referencia: true si la página fue referenciada recientemente
    private boolean bitM; // Bit de modificación: true si la página fue modificada (escritura)

    // Constructor
    public Pagina(int id) {
        this.id = id;
        this.bitR = false;
        this.bitM = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public boolean isBitR() {
        return bitR;
    }

    public void setBitR(boolean bitR) {
        this.bitR = bitR;
    }

    public boolean isBitM() {
        return bitM;
    }

    public void setBitM(boolean bitM) {
        this.bitM = bitM;
    }

    // Reiniciar el bit R periódicamente (simula el proceso de reloj)
    public void resetBitR() {
        this.bitR = false;
    }

    @Override
    public String toString() {
        return "Pagina{id=" + id + ", bitR=" + bitR + ", bitM=" + bitM + "}";
    }
}

