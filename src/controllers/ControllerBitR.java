package controllers;

import paginacion.SimuladorMemoria;

public class ControllerBitR extends Thread {
    private final SimuladorMemoria memoria;
    private boolean terminado;

    // Constructor que inicializa la memoria
    public ControllerBitR(SimuladorMemoria memoria) {
        this.memoria = memoria;
        this.terminado = false;
    }

    // Método que se ejecuta cuando el hilo inicia
    @Override
    public void run() {
        try {
            while (!terminado) {
                Thread.sleep(2);  // Espera 2 ms
                memoria.limpiarBitsReferencia();  // Limpia los bits de referencia
            }
        } catch (InterruptedException e) {
            System.out.println("Error al detener el HiloBitR");
        }
    }

    // Método para detener el hilo
    public void detenerHilo() {
        terminado = true;
    }
}
