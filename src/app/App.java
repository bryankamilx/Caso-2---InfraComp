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

        int tamañoPagina = 256;  // Puedes cambiar el tamaño de página según lo necesites
        String nombreImagen = "C:\\Users\\Bryan\\Documents\\Caso 2 - InfraComp\\Archivos\\caso2-parrots.bmp";
        String archivoSalida = "C:\\Users\\Bryan\\Documents\\Caso 2 - InfraComp\\Archivos\\referencias.txt";

        ArchivoReferencias.generarReferencias(tamañoPagina, nombreImagen, archivoSalida);

        System.out.println("Archivo de referencias generado en: " + archivoSalida);
        
    }
}



