package paginacion;

public class SimuladorMemoria {
    private final String[] marcos;       // Representa los marcos de la memoria
    private final boolean[] referenciado;  // Bits de referencia
    private final boolean[] modificado;    // Bits de modificación

    // Constructor para inicializar la memoria con un número de marcos de página
    public SimuladorMemoria(int cantidadMarcos) {
        marcos = new String[cantidadMarcos];
        referenciado = new boolean[cantidadMarcos];
        modificado = new boolean[cantidadMarcos];
    }

    // Método para insertar un mensaje en un marco específico
    public synchronized void insertar(int posicion, String[] mensaje) {
        marcos[posicion] = mensaje[0];
        referenciado[posicion] = true;
        modificado[posicion] = mensaje[3].equals("W");  // Marca si se modificó
    }

    // Método para leer el contenido de un marco en una posición específica
    public synchronized String leer(int posicion) {
        return marcos[posicion];
    }

    // Algoritmo NRU para seleccionar la página a reemplazar
    public synchronized int paginaReemplazable() {
        // (1) No referenciada, no modificada
        for (int i = 0; i < marcos.length; i++) {
            if (!referenciado[i] && !modificado[i]) {
                return i;
            }
        }
        // (2) No referenciada, modificada
        for (int i = 0; i < marcos.length; i++) {
            if (!referenciado[i] && modificado[i]) {
                return i;
            }
        }
        // (3) Referenciada, no modificada
        for (int i = 0; i < marcos.length; i++) {
            if (referenciado[i] && !modificado[i]) {
                return i;
            }
        }
        // (4) Referenciada, modificada
        for (int i = 0; i < marcos.length; i++) {
            if (referenciado[i] && modificado[i]) {
                return i;
            }
        }
        return -1;  // Si no se encuentra ninguna página para reemplazar
    }

    // Limpia los bits de referencia (los pone a falso)
    public synchronized void limpiarBitsReferencia() {
        for (int i = 0; i < referenciado.length; i++) {
            referenciado[i] = false;
        }
    }
}
