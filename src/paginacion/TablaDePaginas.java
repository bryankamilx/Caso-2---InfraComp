package paginacion;

public class TablaDePaginas {

    private int[] tabla;

    public TablaDePaginas(int tamanio) {
        tabla = new int[tamanio];
        inicializarTabla();
    }

    private void inicializarTabla() {
        for (int i = 0; i < tabla.length; i++) {
            tabla[i] = -1;
        }
    }

    public synchronized void llenarTablaConIndices() {
        for (int i = 0; i < tabla.length; i++) {
            tabla[i] = i;
        }
    }

    public synchronized int obtenerEstadoPagina(int pagina) {
        return tabla[pagina];
    }

    public synchronized void asignarPagina(int indice, int valor) {
        tabla[indice] = valor;
    }

    public void imprimirTabla() {
        for (int i = 0; i < tabla.length; i++) {
            System.out.println("Índice " + i + " -> Valor: " + tabla[i]);
        }
    }
}
