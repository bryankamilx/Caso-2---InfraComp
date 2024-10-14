package esteganografia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Mensaje {
    private char[] contenido;

    // Constructor vacío
    public Mensaje() {}

    // Constructor que inicializa con contenido
    public Mensaje(char[] contenido) {
        this.contenido = contenido;
    }

    // Método para leer el mensaje desde un archivo de texto
    public void leerDesdeArchivo(String rutaArchivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            StringBuilder sb = new StringBuilder();
            String linea;
            
            // Leer cada línea y construir el mensaje
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            
            // Convertir el mensaje en un array de char
            contenido = sb.toString().toCharArray();
            br.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Método para guardar el mensaje en un archivo de texto
    public void guardarEnArchivo(String rutaArchivo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));
            
            // Escribir el contenido en el archivo
            bw.write(contenido);
            bw.close();
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Getter del contenido del mensaje
    public char[] getContenido() {
        return contenido;
    }

    // Setter del contenido del mensaje
    public void setContenido(char[] contenido) {
        this.contenido = contenido;
    }
}
