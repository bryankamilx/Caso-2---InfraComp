package paginacion;

import java.io.*;
import java.util.*;

public class SimuladorPaginacion {
    private TablaPaginas tablaPaginas;
    private int tiempoHit;     // Tiempo de acceso en caso de un hit (en ns)
    private int tiempoFallo;   // Tiempo de acceso en caso de un fallo de página (en ms)
    private int numeroFallas;  // Contador de fallas de página
    private int numeroHits;    // Contador de hits
    private List<Referencia> referencias; // Lista de referencias

    // Constructor
    public SimuladorPaginacion(int numMarcos, int tiempoHit, int tiempoFallo) {
        this.tablaPaginas = new TablaPaginas(numMarcos);
        this.tiempoHit = tiempoHit;
        this.tiempoFallo = tiempoFallo;
        this.numeroFallas = 0;
        this.numeroHits = 0;
        this.referencias = new ArrayList<>();
    }

    // Método para cargar las referencias desde un archivo
    public void cargarReferencias(String archivoReferencias) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoReferencias))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Referencia ref = parseReferencia(linea);
                if (ref != null) {  // Ignorar referencias nulas
                    referencias.add(ref);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de referencias: " + e.getMessage());
        }
    }
    

    // Método para simular la paginación y calcular hits y fallos
    public void ejecutarSimulacion() {
        for (Referencia ref : referencias) {
            boolean esEscritura = ref.getAccion().equals("W");
            if (tablaPaginas.contienePagina(ref.getPaginaVirtual())) {
                numeroHits++;
            } else {
                numeroFallas++;
            }
            tablaPaginas.accederPagina(ref.getPaginaVirtual(), esEscritura);
        }
    }

    // Método para calcular el tiempo total en base a los hits y fallos
    public long calcularTiempoTotal() {
        return (long) numeroHits * tiempoHit + (long) numeroFallas * tiempoFallo * 1_000_000;
    }

    // Método para mostrar los resultados de la simulación
    public void mostrarResultados() {
        System.out.println("Número de fallas de página: " + numeroFallas);
        System.out.println("Número de hits: " + numeroHits);
        System.out.println("Porcentaje de hits: " + (numeroHits * 100.0 / referencias.size()) + "%");
        System.out.println("Tiempo total de acceso: " + calcularTiempoTotal() + " ns");
    }

    // Método auxiliar para parsear una línea de referencia
    // En el método parseReferencia en SimuladorPaginacion
public Referencia parseReferencia(String linea) {
    // Verificar si la línea es vacía o nula antes de procesarla
    if (linea == null || linea.trim().isEmpty()) {
        return null;  // Ignorar las líneas vacías
    }

    // Procesar la línea no vacía
    String[] partes = linea.split(",");
    if (partes.length != 4) {
        System.err.println("Línea malformada en el archivo de referencias: " + linea);
        return null;
    }

    // Ahora continúa con el procesamiento
    String tipoReferencia = partes[0].contains("Imagen") ? "Imagen" : "Mensaje";
    int paginaVirtual = Integer.parseInt(partes[1].trim());
    int desplazamiento = Integer.parseInt(partes[2].trim());
    String accion = partes[3].trim();

    // Retorna el objeto Referencia con los valores extraídos
    return new Referencia(tipoReferencia, 0, 0, paginaVirtual, desplazamiento, accion);
}

}
