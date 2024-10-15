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
            
            // Calcula número de páginas y referencias necesarias
            int numPaginasMatriz = (int) Math.ceil((ancho * alto * 3.0) / tamañoPagina); // Bytes por pixel (RGB)
            int longitudMensaje = imagen.leerLongitud();
            System.out.println(longitudMensaje);
            int numPaginasVector = (int) Math.ceil((longitudMensaje * 1.0) / tamañoPagina); // 1 byte por carácter del mensaje
            int numReferencias = (17 * longitudMensaje)+16; // Total de referencias (imagen + mensaje)
    
            // Usa try-with-resources para asegurar que el archivo se cierre correctamente
            try (FileWriter writer = new FileWriter(archivoSalida)) {
    
                // Escribe el encabezado en el archivo
                writer.write("TP=" + tamañoPagina + "\n");
                writer.write("NF=" + alto + "\n");
                writer.write("NC=" + ancho + "\n");
                writer.write("NR=" + numReferencias + "\n");
                writer.write("NP=" + (numPaginasMatriz + numPaginasVector) + "\n");
    
                // Genera y escribe las referencias para la matriz de la imagen
                for (int i = 0; i < 16; i++) {
                    int fila= i/(ancho*3);
                    int columna= (i%(ancho*3))/3;
                    int cIndex= (i%(ancho*3))%3;
                    //Veo que letra tiene que ir
                    String colorRef= cIndex==0? "R": cIndex==1? "G": "B";
                    
                        int paginaVirtual = i / tamañoPagina;
                        int desplazamiento = i % tamañoPagina;
                    
                    
                        writer.write("Imagen[" + fila + "][" + columna + "]." + colorRef + "," + paginaVirtual + "," + desplazamiento + ",R\n");
                    
                }

                //Genera el vector de referencias
                int pMensaje= (0);
                for (int pC = 0; pC < longitudMensaje; pC++) {
                    int inicio= numPaginasMatriz +(pMensaje/tamañoPagina);
                    int desplazamiento= pMensaje%tamañoPagina;

                    for (int j= 0; j<8;j ++){
                        int nByte= 16 +(pC*8)+j;
                        int f= nByte/(ancho*3);
                        int c= (nByte%(ancho*3))/3;
                        int cIndex= (j%(ancho*3))%3;
                        
                        String colorRef= cIndex==0? "R": cIndex==1? "G": "B";
                        

                        int pag= (nByte)/(tamañoPagina);
                        int des= (nByte)%(tamañoPagina);

                        writer.write("Mensaje[" + pMensaje + "]." + inicio + "," + desplazamiento + ",W\n");

                        writer.write("Imagen[" + f + "][" + c + "]." + colorRef + "," + pag + "," + des + ",R\n");
                        
                    
                    }
                    
                int paginaCaracter= numPaginasMatriz +(pC/tamañoPagina);
                int desplazamientoCaracter= pC%tamañoPagina;

                writer.write("Mensaje[" + pC + "]," + paginaCaracter + "," + desplazamientoCaracter + ",W\n");
                pMensaje++;
    
               
                }
            }
    
            System.out.println("Archivo de referencias generado: " + archivoSalida);
    
        }
        catch (IOException e) {
            System.err.println("Error al escribir el archivo de referencias: " + e.getMessage());
        } 
        
        
    }
    
}



