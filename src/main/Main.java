package main;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import controllers.Controller;
import controllers.ControllerBitR;
import paginacion.*;

public class Main {

    // Parámetros de configuración
    private static int tamanoPagina; // en bytes
    private static String nombreArchivo;

    // Atributos relacionados a la imagen y mensaje
    private static Imagen imagen;
    private static int longitudMensaje;
    private static int numReferencias;
    private static int numPaginas;
    private static int numMarcosPagina;

    // Herramientas de entrada
    private static Scanner scanner;
    private static BufferedReader lector;

    public static void main(String[] args) throws Exception {
        int resultado = (86189) / 256;
        System.out.println("Revisión: " + resultado);
        scanner = new Scanner(System.in);
        try {
            boolean terminado = false;
            while (!terminado) {
                System.out.println("\nSelecciona una opción:");
                System.out.println("1. Generar referencias");
                System.out.println("2. Calcular datos");

                String opcion = scanner.next();
                switch (opcion) {
                    case "1":
                        generarReferencias();
                        break;
                    case "2":
                        calcularDatos();
                        terminado = true;
                        break;
                    case "3":
                        escribirImagenYEsconderMensaje();
                        break;
                    case "4":
                        leerYDevolverMensaje();
                        break;
                    default:
                        System.out.println("Opción inválida, intenta de nuevo.");
                }
            }
        } finally {
            System.out.println("\nPrograma finalizado.");
            scanner.close();
        }
    }

    private static void generarReferencias() {
        pedirDatosOpcion1();
        imagen = new Imagen(nombreArchivo + ".bmp");
        longitudMensaje = imagen.leerLongitud();
        System.out.println("Longitud del mensaje: " + longitudMensaje);

        numReferencias = 16 + 17 * longitudMensaje;
        numPaginas = (3 * imagen.ancho * imagen.alto + longitudMensaje + tamanoPagina - 1) / tamanoPagina;

        try {
            FileWriter fileWriter = new FileWriter("output_" + nombreArchivo + ".txt");
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write("P=" + tamanoPagina);
            writer.newLine();
            writer.write("NF=" + imagen.alto);
            writer.newLine();
            writer.write("NC=" + imagen.ancho);
            writer.newLine();
            writer.write("NR=" + numReferencias);
            writer.newLine();
            writer.write("NP=" + numPaginas);
            writer.newLine();

            int columna = 0;
            String[] colores = {"R", "G", "B"};
            int desplazamiento = 0;

            // Generar referencias de la imagen
            for (int i = 0; i < 16; i++) {
                String color = colores[i % 3];
                if (i % 3 == 0 && i != 0) {
                    columna++;
                }
                writer.write("Imagen[0][" + columna + "]." + color + ",0," + desplazamiento + ",R");
                writer.newLine();
                desplazamiento++;
            }

            int fila = 0;
            int pagina = 0;
            int posicionMensaje = 0;
            int paginaMensaje = (3 * imagen.ancho * imagen.alto + tamanoPagina - 1) / tamanoPagina;

            boolean sigueMensaje = false;

            // Generar referencias del mensaje
            for (int i = 16; i < numReferencias; i++) {
                writer.write("Mensaje[" + posicionMensaje + "]," + paginaMensaje + "," + desplazamiento + ",W");
                writer.newLine();

                for (int j = 0; j < 16; j++) {
                    if (sigueMensaje) {
                        writer.write("Mensaje[" + posicionMensaje + "]," + paginaMensaje + "," + desplazamiento + ",W");
                        if (i != numReferencias - 1) {
                            writer.newLine();
                        }
                        sigueMensaje = false;
                    } else {
                        String color = colores[i % 3];
                        if (i % 3 == 0) {
                            columna++;
                            if (columna >= imagen.ancho) {
                                fila++;
                                columna = 0;
                            }
                        }
                        writer.write("Imagen[" + fila + "][" + columna + "]." + color + "," + pagina + "," + desplazamiento + ",R");
                        writer.newLine();
                        desplazamiento++;
                        if (desplazamiento >= tamanoPagina) {
                            pagina++;
                            desplazamiento = 0;
                        }
                        sigueMensaje = true;
                    }
                }
                posicionMensaje++;
                if (posicionMensaje >= tamanoPagina) {
                    posicionMensaje = 0;
                    paginaMensaje++;
                }
            }
            writer.close();
            System.out.println("Archivo generado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo.");
        }
    }

    private static void pedirDatosOpcion1() {
        System.out.println("--> Generar referencias");
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print("Ingrese el tamaño de página (en bytes): ");
                tamanoPagina = scanner.nextInt();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.next();
            }
        }
        System.out.print("Ingrese el nombre del archivo BMP (sin la extensión .bmp): ");
        nombreArchivo = scanner.next();
        System.out.println("El archivo de salida será 'output_" + nombreArchivo + ".txt'.");
    }

    private static void calcularDatos() {
        pedirDatosOpcion2();
        try {
            String linea = lector.readLine();
            tamanoPagina = Integer.parseInt(linea.substring(2));
            lector.readLine(); 
            lector.readLine();
            linea = lector.readLine();
            numReferencias = Integer.parseInt(linea.substring(3));
            linea = lector.readLine();
            numPaginas = Integer.parseInt(linea.substring(3));

            SimuladorMemoria memoria = new SimuladorMemoria(numMarcosPagina);
            TablaDePaginas tablaPaginas = new TablaDePaginas(numReferencias);
            ControllerBitR hiloBitR = new ControllerBitR(memoria);
            Controller hiloRAM = new Controller(memoria, lector, tablaPaginas, hiloBitR);

            hiloRAM.start();
            hiloBitR.start();
            hiloRAM.join();
            System.out.println("Fallos de página: " + tablaPaginas.obtenerFallosPagina());
            System.out.println("Aciertos (hits): " + tablaPaginas.obtenerAciertos());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al procesar los datos.");
        }
        
    }

    private static void pedirDatosOpcion2() {
        System.out.println("--> Calcular datos");
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print("Ingrese el número de marcos de página: ");
                numMarcosPagina = scanner.nextInt();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.next();
            }
        }
        entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print("Ingrese el nombre del archivo de referencias (sin la extensión .txt): ");
                nombreArchivo = scanner.next();
                lector = new BufferedReader(new FileReader(nombreArchivo + ".txt"));
                entradaValida = true;
            } catch (FileNotFoundException e) {
                System.out.println("Archivo no encontrado, inténtelo de nuevo.");
            }
        }
        System.out.println("\nProcesando, por favor espere...");
    }

    public static void escribirImagenYEsconderMensaje() {
        try {
            BufferedReader lectorEntrada = new BufferedReader(new InputStreamReader(System.in));
            
            // Solicitar la ruta de la imagen
            System.out.print("Especifique la ruta del archivo de imagen: ");
            String rutaImagen = lectorEntrada.readLine();
    
            // Leer el mensaje utilizando el método combinado leerYDevolverMensaje
            char[] mensaje = leerYDevolverMensaje();
    
            // Cargar la imagen desde la ruta proporcionada
            Imagen imagen = new Imagen(rutaImagen);
    
            // Esconder el mensaje en la imagen
            imagen.esconder(mensaje, mensaje.length);
    
            // Guardar la imagen modificada en la misma ruta de la imagen original
            imagen.escribirImagen(rutaImagen);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private static int leerArchivoTexto(String rutaMensaje) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerArchivoTexto'");
    }

        // Método para leer el archivo de texto y devolver la longitud del mensaje
        public static char[] leerYDevolverMensaje() {
    char[] mensaje = null;
    try {
        BufferedReader lectorEntrada = new BufferedReader(new InputStreamReader(System.in));
        
        // Solicitar la ruta del archivo de texto desde la consola
        System.out.print("Especifique la ruta del archivo de texto con el mensaje: ");
        String rutaArchivo = lectorEntrada.readLine();

        // Leer la longitud del archivo
        int longitud = 0;
        BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
        while (lector.read() != -1) {
            longitud++;
        }
        lector.close();

        // Leer el contenido del archivo y almacenarlo en un arreglo de chars
        lector = new BufferedReader(new FileReader(rutaArchivo));
        mensaje = new char[longitud];
        lector.read(mensaje);
        lector.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
    return mensaje;
}

        
}
