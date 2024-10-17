package paginacion;

import java.util.HashMap;
import java.util.Map;

public class SimuladorMemoria {

    private Map<Integer, int[]> memoria;

    public SimuladorMemoria(int marcosDePagina) {
        memoria = new HashMap<>();
        inicializarMemoria(marcosDePagina);
    }

    private void inicializarMemoria(int marcosDePagina) {
        for (int i = 0; i < marcosDePagina; i++) {
            memoria.put(i, new int[]{-1, 0, 0});
        }
    }

    public int obtenerMarcoLibre() {
        for (int i = 0; i < memoria.size(); i++) {
            if (memoria.get(i)[0] == -1) {
                return i;
            }
        }
        return -1;
    }

    public synchronized int[] realizarSwap(int paginaVirtual, int operacion) {
        int marcoParaRemover = seleccionarMarcoParaSwap();
        int[] datosMarcoRemovido = memoria.get(marcoParaRemover);

        int[] nuevaPagina = {paginaVirtual, 1, operacion};
        memoria.put(marcoParaRemover, nuevaPagina);

        System.out.println("Removiendo el marco #" + marcoParaRemover + 
                   " | Página virtual: " + datosMarcoRemovido[0] + 
                   " | Bit de referencia: " + datosMarcoRemovido[1] + 
                   " | Bit de escritura: " + datosMarcoRemovido[2]);


        return new int[]{marcoParaRemover, datosMarcoRemovido[0]};
    }

    public synchronized void asignarMarco(int marcoLibre, int pagina, int tipoOperacion) {
        memoria.put(marcoLibre, new int[]{pagina, 1, tipoOperacion});
    }

    public synchronized int seleccionarMarcoParaSwap() {
        int marcoOptimo = 0;
        int menorReferencia = memoria.get(0)[1];
        int menorEscritura = memoria.get(0)[2];

        for (int i = 1; i < memoria.size(); i++) {
            int[] marcoActual = memoria.get(i);

            if (marcoActual[1] < menorReferencia || (marcoActual[1] == menorReferencia && marcoActual[2] < menorEscritura)) {
                marcoOptimo = i;
                menorReferencia = marcoActual[1];
                menorEscritura = marcoActual[2];
            }
        }

        return marcoOptimo;
    }

    public synchronized void refrescarBitsReferencia() {
        for (int indice : memoria.keySet()) {
            int[] marco = memoria.get(indice);
            memoria.put(indice, new int[]{marco[0], 0, marco[2]});
        }
    }

    public synchronized void consultarMarco(int marco) {
        int[] datosMarco = memoria.get(marco);
        memoria.put(marco, new int[]{datosMarco[0], 1, datosMarco[2]});
    }
}
