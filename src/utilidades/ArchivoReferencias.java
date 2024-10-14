package utilidades;

import esteganografia.Imagen;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivoReferencias {
    
    public static void generarReferencias(int tamañoPagina, String nombreImagen, String archivoSalida) {
        try {
            // Carga la imagen
            Imagen imagen = new Imagen(nombreImagen);
            int ancho = imagen.getAncho();
            int alto = imagen.getAlto();
            
            // Abre el archivo de salida
            FileWriter writer = new FileWriter(archivoSalida);
            
            // Calcula número de referencias y páginas necesarias
            int numPaginasMatriz = (ancho * alto * 3) / tamañoPagina; // Ajustar según el tamaño de los píxeles
            int longitudMensaje = imagen.leerLongitud();
            int numPaginasVector = longitudMensaje / tamañoPagina;
            int numReferencias = (ancho * alto * 3) + longitudMensaje;
            
            // Escribe el encabezado en el archivo
            writer.write("TP=" + tamañoPagina + "\n");
            writer.write("NF=" + alto + "\n");
            writer.write("NC=" + ancho + "\n");
            writer.write("NR=" + numReferencias + "\n");
            writer.write("NP=" + (numPaginasMatriz + numPaginasVector) + "\n");
            
            // Genera y escribe las referencias
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    for (int color = 0; color < 3; color++) {
                        int pagina = (i * ancho * 3 + j * 3 + color) / tamañoPagina;
                        int desplazamiento = (i * ancho * 3 + j * 3 + color) % tamañoPagina;
                        writer.write("Imagen[0][" + i + "].R," + pagina + "," + desplazamiento + ",R\n");
                    }
                }
            }
            
            // Referencias para el mensaje
            for (int k = 0; k < longitudMensaje; k++) {
                int pagina = (alto * ancho * 3 + k) / tamañoPagina;
                int desplazamiento = (alto * ancho * 3 + k) % tamañoPagina;
                writer.write("Mensaje[0]," + pagina + "," + desplazamiento + ",R\n");
            }
            
            writer.close();
            System.out.println("Archivo de referencias generado: " + archivoSalida);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

