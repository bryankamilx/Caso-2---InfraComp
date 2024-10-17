package controllers;

import paginacion.SimuladorMemoria;

public class ControllerBitR extends Thread {

    private SimuladorMemoria memoriaFisica;

    public ControllerBitR(SimuladorMemoria memoriaFisica) {
        this.memoriaFisica = memoriaFisica;
    }

    @Override
    public void run() {
        while (true) {
            actualizarBitsReferencia();
            esperarActualizacion();
        }
    }

    private void actualizarBitsReferencia() {
        memoriaFisica.refrescarBitsReferencia();
    }

    private void esperarActualizacion() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("El controlador principal ha finalizado y ha interrumpido el controlador de bits de referencia para concluir su tarea.");
        }
    }
}
