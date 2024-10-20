package paginacion;

public class TablaDePaginas {
    private final int[] tabla;
    private int fallosPagina = 0;  // Contador de fallos de página
    private int aciertos = 0;      // Contador de aciertos (hits)
    private final int[] tablaInversa;

    // Constructor que inicializa la tabla de páginas
    public TablaDePaginas(int cantidadEntradas) {
        tabla = new int[cantidadEntradas];
        tablaInversa = new int[cantidadEntradas];
        for (int i = 0; i < cantidadEntradas; i++) {
            tabla[i] = -1;            // Páginas no cargadas inicialmente
            tablaInversa[i] = -1;      // Inversa inicializada
        }
    }

    // Método para buscar una página virtual en la tabla
    public int buscarPaginaVirtual(int paginaVirtual) {
        if (tabla[paginaVirtual] == -1) {
            fallosPagina++;
            return -1;  // Retorna -1 si hay fallo de página
        } else {
            aciertos++;
            return tabla[paginaVirtual];  // Retorna la dirección real si no hay fallo
        }
    }

    // Método para insertar una página en la tabla
    public void insertar(int dirPaginaVirtual, int dirPaginaReal) {
        tabla[dirPaginaVirtual] = dirPaginaReal;
        if (tablaInversa[dirPaginaReal] != -1) {
            int paginaVirtualAnterior = tablaInversa[dirPaginaReal];
            tabla[paginaVirtualAnterior] = -1;  // Marca la página anterior como no cargada
        }
        tablaInversa[dirPaginaReal] = dirPaginaVirtual;  // Actualiza la tabla inversa
    }

    // Método para obtener el número de fallos de página
    public int obtenerFallosPagina() {
        return fallosPagina;
    }

    // Método para obtener el número de aciertos (hits)
    public int obtenerAciertos() {
        return aciertos;
    }

    // Método que retorna el tamaño de la tabla
    public int obtenerTamaño() {
        return tabla.length;
    }
}
