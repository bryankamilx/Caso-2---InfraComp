package paginacion;

import java.util.*;

public class TablaPaginas {
    private int numMarcos; // Número de marcos disponibles en la memoria
    private Map<Integer, Pagina> marcos; // Mapa de las páginas en los marcos
    private Queue<Integer> cola; // Cola de páginas para reemplazo

    // Constructor
    public TablaPaginas(int numMarcos) {
        this.numMarcos = numMarcos;
        this.marcos = new LinkedHashMap<>(numMarcos);
        this.cola = new LinkedList<>();
    }

    // Método para verificar si una página está en memoria
    public boolean contienePagina(int idPagina) {
        return marcos.containsKey(idPagina);
    }

    // Método para agregar o actualizar una página en la tabla
    public void accederPagina(int idPagina, boolean esEscritura) {
        // Si la página ya está en memoria
        if (marcos.containsKey(idPagina)) {
            Pagina pagina = marcos.get(idPagina);
            pagina.setBitR(true);
            if (esEscritura) {
                pagina.setBitM(true);
            }
        } else {
            // Falla de página: agregar la nueva página
            Pagina nuevaPagina = new Pagina(idPagina);
            nuevaPagina.setBitR(true);
            if (esEscritura) {
                nuevaPagina.setBitM(true);
            }
            agregarPagina(nuevaPagina);
        }
    }

    // Método para agregar una página en memoria, aplicando reemplazo si es necesario
    private void agregarPagina(Pagina pagina) {
        if (marcos.size() >= numMarcos) {
            reemplazarPagina();
        }
        marcos.put(pagina.getId(), pagina);
        cola.add(pagina.getId());
    }

    // Método de reemplazo basado en el algoritmo NRU
    private void reemplazarPagina() {
        int idPaginaAEliminar = -1;

        // Clasificar páginas en base a los bits R y M
        for (Integer id : cola) {
            Pagina pagina = marcos.get(id);
            if (!pagina.isBitR() && !pagina.isBitM()) { // Clase 0
                idPaginaAEliminar = id;
                break;
            } else if (!pagina.isBitR() && pagina.isBitM()) { // Clase 1
                idPaginaAEliminar = id;
            } else if (pagina.isBitR() && !pagina.isBitM()) { // Clase 2
                if (idPaginaAEliminar == -1) {
                    idPaginaAEliminar = id;
                }
            } else { // Clase 3
                if (idPaginaAEliminar == -1) {
                    idPaginaAEliminar = id;
                }
            }
        }

        // Eliminar la página seleccionada para reemplazo
        if (idPaginaAEliminar != -1) {
            marcos.remove(idPaginaAEliminar);
            cola.remove(idPaginaAEliminar);
        }
    }

    // Método para actualizar el bit R periódicamente
    public void resetBitR() {
        for (Pagina pagina : marcos.values()) {
            pagina.resetBitR();
        }
    }

    // Método para contar las fallas de página
    public int contarFallas() {
        return Math.max(0, cola.size() - numMarcos);
    }

    // Método para obtener el número de hits
    public int contarHits() {
        return cola.size() - contarFallas();
    }

    @Override
    public String toString() {
        return "TablaPaginas{marcos=" + marcos.values() + "}";
    }
}

