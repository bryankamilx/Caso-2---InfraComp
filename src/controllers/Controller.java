package controllers;

import java.util.List;
import paginacion.Contador;
import paginacion.SimuladorMemoria;
import paginacion.TablaDePaginas;

public class Controller extends Thread {

    private TablaDePaginas tablaIntercambio;
    private TablaDePaginas tablaPrincipal;
    private List<int[]> listaInstrucciones;
    private SimuladorMemoria memoriaFisica;
    private Contador contadorHitsMisses;

    public Controller(TablaDePaginas tablaIntercambio, TablaDePaginas tablaPrincipal, List<int[]> listaInstrucciones, SimuladorMemoria memoriaFisica, Contador contadorHitsMisses) {
        this.tablaPrincipal = tablaPrincipal;
        this.tablaIntercambio = tablaIntercambio;
        this.listaInstrucciones = listaInstrucciones;
        this.memoriaFisica = memoriaFisica;
        this.contadorHitsMisses = contadorHitsMisses;
    }

    @Override
    public void run() {
        for (int[] instruccion : listaInstrucciones) {
            int pagina = instruccion[0];
            int tipoOperacion = instruccion[2];

            if (esFalloDePagina(pagina)) {
                manejarFalloDePagina(pagina, tipoOperacion);
            } else {
                memoriaFisica.consultarMarco(tablaPrincipal.obtenerEstadoPagina(pagina));
                contadorHitsMisses.addHit();
            }

            pausarEjecucion();
        }
    }

    private boolean esFalloDePagina(int pagina) {
        return tablaPrincipal.obtenerEstadoPagina(pagina) == -1;
    }

    private void manejarFalloDePagina(int pagina, int tipoOperacion) {
        contadorHitsMisses.addMiss();
        int marcoDisponible = memoriaFisica.obtenerMarcoLibre();

        if (marcoDisponible == -1) {
            int[] marcoASwap = memoriaFisica.realizarSwap(pagina, tipoOperacion);
            actualizarTablasParaSwap(pagina, marcoASwap);
        } else {
            cargarPaginaEnMarcoDisponible(pagina, tipoOperacion, marcoDisponible);
        }
    }

    private void actualizarTablasParaSwap(int pagina, int[] marcoASwap) {
        tablaIntercambio.asignarPagina(marcoASwap[1], marcoASwap[1]);
        tablaIntercambio.asignarPagina(pagina, -1);

        tablaPrincipal.asignarPagina(pagina, marcoASwap[0]);
        tablaPrincipal.asignarPagina(marcoASwap[1], -1);

        memoriaFisica.consultarMarco(marcoASwap[0]);
    }

    private void cargarPaginaEnMarcoDisponible(int pagina, int tipoOperacion, int marcoDisponible) {
        memoriaFisica.asignarMarco(marcoDisponible, pagina, tipoOperacion);
        tablaPrincipal.asignarPagina(pagina, marcoDisponible);
        tablaIntercambio.asignarPagina(pagina, -1);

        memoriaFisica.consultarMarco(marcoDisponible);
    }

    private void pausarEjecucion() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
