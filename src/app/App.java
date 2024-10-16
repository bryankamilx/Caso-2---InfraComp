package app;
import java.io.*;

import esteganografia.Imagen;
import esteganografia.Mensaje;
import paginacion.SimuladorPaginacion;
import paginacion.Pagina;
import paginacion.TablaPaginas;
import utilidades.ArchivoReferencias;
import utilidades.Temporizador;


public class App {
    public static void main(String[] args) {
        int numMarcos = 4;           // Número de marcos de página
        int tiempoHit = 25;          // Tiempo en ns para un hit
        int tiempoFallo = 10;        // Tiempo en ms para un fallo de página
        String archivoReferencias = "archivos/referencias.txt"; // Archivo de referencias

        SimuladorPaginacion simulador = new SimuladorPaginacion(numMarcos, tiempoHit, tiempoFallo);
        simulador.cargarReferencias(archivoReferencias);
        simulador.ejecutarSimulacion();
        simulador.mostrarResultados();
    }
}



