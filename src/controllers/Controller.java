package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import paginacion.*;

public class Controller extends Thread {

    private final SimuladorMemoria memoria;
    private final BufferedReader lector;
    private final TablaDePaginas tablaPaginas;
    private final ControllerBitR hiloBitR;

    // Constructor que inicializa los objetos de memoria, lector, tabla de páginas y el hilo de bits de referencia
    public Controller(SimuladorMemoria memoria, BufferedReader lector, TablaDePaginas tablaPaginas, ControllerBitR hiloBitR) {
        this.memoria = memoria;
        this.lector = lector;
        this.tablaPaginas = tablaPaginas;
        this.hiloBitR = hiloBitR;
    }

    // Método principal que se ejecuta cuando el hilo inicia
    @Override
    public void run() {
        try {
            for (int i = 0; i < tablaPaginas.obtenerTamaño(); i++) {
                Thread.sleep(1);  // Pausa de 1 ms
                String linea = lector.readLine();  // Lee la línea del archivo
                String[] partes = linea.split(",");
                int paginaVirtual = Integer.parseInt(partes[1]);

                // Verifica si la página está en la tabla de páginas
                int resultadoTabla = tablaPaginas.buscarPaginaVirtual(paginaVirtual);

                // Si la página no está en la tabla de páginas
                if (resultadoTabla == -1) {
                    int paginaReal = memoria.paginaReemplazable();  // Busca una página para reemplazar
                    memoria.insertar(paginaReal, partes);  // Inserta la página en la memoria
                    tablaPaginas.insertar(paginaVirtual, paginaReal);  // Actualiza la tabla de páginas
                }
                // Si la página está en la tabla, no se hace nada
            }

            hiloBitR.detenerHilo();  // Detiene el hilo de bits de referencia
        } catch (IOException e) {
            System.out.println("Error al leer el archivo, inténtalo de nuevo.");
        } catch (InterruptedException ex) {
            System.out.println("No se pudo detener el HiloRAM.");
        }
    }
}
