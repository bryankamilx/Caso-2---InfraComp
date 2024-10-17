package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controllers.Controller;
import controllers.ControllerBitR;
import paginacion.Contador;
import paginacion.Imagen;
import paginacion.SimuladorMemoria;
import paginacion.TablaDePaginas;

public class Main {

    public Main() {}

    public static void main(String[] args) {
        Main instanciaApp = new Main();
        instanciaApp.ejecutarPrograma();
    }

    public void ejecutarPrograma() {
        mostrarMenu();
        try {
            BufferedReader lectorEntrada = new BufferedReader(new InputStreamReader(System.in));
            int opcionSeleccionada = Integer.parseInt(lectorEntrada.readLine());

            while (opcionSeleccionada != 0) {
                switch (opcionSeleccionada) {
                    case 1:
                        generarReferenciasPaginas();
                        break;
                    case 2:
                        calcularEstadisticasPaginacion();
                        break;
                    default:
                        System.out.println("Opción inválida, por favor intente nuevamente.");
                        break;
                }
                mostrarMenu();
                opcionSeleccionada = Integer.parseInt(lectorEntrada.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Saliendo del programa...");
    }

    public void mostrarMenu() {
        System.out.println("\n---- Menú Principal ----");
        System.out.println("1. Generar referencias de páginas");
        System.out.println("2. Calcular estadísticas de paginación");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public void generarReferenciasPaginas() {
        try {
            BufferedReader lectorEntrada = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Especifique el tamaño de la página en bytes: ");
            int tamanoPagina = Integer.parseInt(lectorEntrada.readLine());

            System.out.print("Especifique la ruta del archivo de imagen: ");
            String rutaImagen = lectorEntrada.readLine();

            Imagen imagen = new Imagen(rutaImagen);

            int altoImagen = imagen.alto;
            int anchoImagen = imagen.ancho;

            int longitudMensaje = imagen.leerLongitud();
            int totalReferencias = (longitudMensaje * 8 * 2) + 16 + longitudMensaje;

            double paginasImagen = Math.ceil((double) (altoImagen * anchoImagen * 3) / tamanoPagina);
            double paginasMensaje = Math.ceil((double) longitudMensaje / tamanoPagina);
            int paginasTotales = (int) paginasImagen + (int) paginasMensaje;

            System.out.print("Especifique el nombre del archivo de salida: ");
            String nombreArchivoSalida = lectorEntrada.readLine();
            PrintWriter escritor = new PrintWriter(nombreArchivoSalida);

            escritor.println("P=" + tamanoPagina);
            escritor.println("NF=" + altoImagen);
            escritor.println("NC=" + anchoImagen);
            escritor.println("NR=" + totalReferencias);
            escritor.println("NP=" + paginasTotales);

            int fila = 0, columna = 0, indiceColor = 0, pagina = 0, paginaMensaje = pagina + (int) paginasImagen;
            int desplazamiento = 0, desplazamientoMensaje = 0;
            String[] colores = {"R", "G", "B"};

            for (int i = 0; i < 16; i++) {
                escritor.println("Imagen[" + fila + "][" + columna + "]." + colores[indiceColor] + "," + pagina + "," + desplazamiento + ",R");
                indiceColor++;
                desplazamiento++;

                if (fila >= altoImagen) fila = 0;
                if (indiceColor >= 3) { columna++; indiceColor = 0; }
                if (columna >= anchoImagen) { columna = 0; fila++; }
                if (desplazamiento >= tamanoPagina) { desplazamiento = 0; pagina++; }
            }

            for (int i = 0; i < longitudMensaje; i++) {
                escritor.println("Mensaje[" + i + "]," + paginaMensaje + "," + desplazamientoMensaje + ",W");

                for (int j = 0; j < 8; j++) {
                    escritor.println("Imagen[" + fila + "][" + columna + "]." + colores[indiceColor] + "," + pagina + "," + desplazamiento + ",R");
                    escritor.println("Mensaje[" + i + "]," + paginaMensaje + "," + desplazamientoMensaje + ",W");

                    indiceColor++;
                    desplazamiento++;

                    if (desplazamiento >= tamanoPagina) { desplazamiento = 0; pagina++; }
                    if (indiceColor >= 3) { columna++; indiceColor = 0; }
                    if (columna >= anchoImagen) { columna = 0; fila++; }
                    if (fila >= altoImagen) fila = 0;
                }
                desplazamientoMensaje++;
                if (desplazamientoMensaje >= tamanoPagina) { desplazamientoMensaje = 0; paginaMensaje++; }
            }
            escritor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calcularEstadisticasPaginacion() {
        try {
            BufferedReader lectorEntrada = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Indique la ruta del archivo de referencias: ");
            String rutaArchivoReferencias = lectorEntrada.readLine();
            
            System.out.print("Especifique el número de marcos de página: ");
            int numMarcos = Integer.parseInt(lectorEntrada.readLine());

            File archivo = new File(rutaArchivoReferencias);
            Scanner lectorArchivo = new Scanner(archivo);
            List<int[]> listaReferencias = new ArrayList<>();

            int tamanoPagina = Integer.parseInt(lectorArchivo.nextLine().split("=")[1]);
            int numFilasImagen = Integer.parseInt(lectorArchivo.nextLine().split("=")[1]);
            int numColumnasImagen = Integer.parseInt(lectorArchivo.nextLine().split("=")[1]);
            int totalReferencias = Integer.parseInt(lectorArchivo.nextLine().split("=")[1]);
            int paginasTotales = Integer.parseInt(lectorArchivo.nextLine().split("=")[1]);

            TablaDePaginas tablaSwap = new TablaDePaginas(paginasTotales);
            TablaDePaginas tablaReal = new TablaDePaginas(paginasTotales);
            tablaSwap.llenarTablaConIndices();

            SimuladorMemoria simuladorMemoria = new SimuladorMemoria(numMarcos);
            Contador contadorHitsMisses = new Contador();

            while (lectorArchivo.hasNextLine()) {
                String[] linea = lectorArchivo.nextLine().split(",");
                int[] datosReferencia = new int[3];
                datosReferencia[0] = Integer.parseInt(linea[1]);
                datosReferencia[1] = Integer.parseInt(linea[2]);
                datosReferencia[2] = linea[3].equals("R") ? 0 : 1;
                listaReferencias.add(datosReferencia);
            }

            Controller controladorPaginas = new Controller(tablaSwap, tablaReal, listaReferencias, simuladorMemoria, contadorHitsMisses);
            ControllerBitR actualizadorBits = new ControllerBitR(simuladorMemoria);

            lectorArchivo.close();

            controladorPaginas.start();
            actualizadorBits.start();

            controladorPaginas.join();
            actualizadorBits.interrupt();

            System.out.println("Resultados de la simulación:");
            System.out.printf("Tamaño de la página: %d bytes\n", tamanoPagina);
            System.out.printf("Marcos de página asignados: %d\n", numMarcos);
            System.out.printf("Total de referencias: %d\n", totalReferencias);
            System.out.printf("Número de aciertos (hits): %d\n", contadorHitsMisses.getHits());
            System.out.printf("Número de fallas (misses): %d\n", contadorHitsMisses.getMiss());

            double tiempoHit = 0.000025;
            double tiempoSwap = 10.0;
            double tiempoTotalHits = tiempoHit * contadorHitsMisses.getHits();
            double tiempoTotalMisses = (contadorHitsMisses.getMiss() * tiempoSwap) + (contadorHitsMisses.getMiss() * tiempoHit);
            double tiempoTotal = tiempoTotalHits + tiempoTotalMisses;

            System.out.printf("Tiempo total para la simulación: %.4f ms\n", tiempoTotal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
