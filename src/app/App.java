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
        int tamañoPagina = 512;  // Ajusta el tamaño de página según sea necesario
        String nombreImagen = "archivos/caso2-parrots_mod.bmp";  // Ruta relativa al archivo de imagen dentro del proyecto
        String archivoSalida = "archivos/referencias.txt";  // Ruta relativa al archivo de salida dentro del proyecto

        // Genera el archivo de referencias
        ArchivoReferencias.generarReferencias(tamañoPagina, nombreImagen, archivoSalida);

        System.out.println("Archivo de referencias generado en: " + archivoSalida);
    }
}



