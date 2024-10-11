package esteganografia;

import java.io.*;

public class Imagen {
    // Atributos
    byte[] header = new byte[54];
    byte[][][] imagen;
    int alto, ancho; // en píxeles
    int padding;
    String nombre;

    // Constructor: Lee una imagen en formato BMP
    public Imagen(String input) {
        nombre = new String(input);
        try {
            FileInputStream fis = new FileInputStream(nombre);
            fis.read(header);

            // Extraer el ancho y alto de la imagen desde la cabecera (Little Endian)
            ancho = ((header[21] & 0xFF) << 24) | ((header[20] & 0xFF) << 16) | ((header[19] & 0xFF) << 8) | (header[18] & 0xFF);
            alto = ((header[25] & 0xFF) << 24) | ((header[24] & 0xFF) << 16) | ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);

            System.out.println("Ancho: " + ancho + " px, Alto: " + alto + " px");
            imagen = new byte[alto][ancho][3];

            int rowSizeSinPadding = ancho * 3;   
            // El tamaño de la fila debe ser múltiplo de 4 bytes
            padding = (4 - (rowSizeSinPadding % 4)) % 4;

            // Leer y almacenar los datos de los píxeles (BGR)
            byte[] pixel = new byte[3];
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    fis.read(pixel);
                    imagen[i][j][0] = pixel[0];   // Azul
                    imagen[i][j][1] = pixel[1];   // Verde
                    imagen[i][j][2] = pixel[2];   // Rojo
                }
                fis.skip(padding);  // Saltar padding
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para esconder bits en la imagen
    private void escribirBits(int contador, int valor, int numbits) {
        int bytesPorFila = ancho * 3; // ancho de la imagen en bytes
        int mascara;
        for (int i = 0; i < numbits; i++) {
            int fila = (8 * contador + i) / bytesPorFila;
            int col = ((8 * contador + i) % bytesPorFila) / 3;
            int color = ((8 * contador + i) % bytesPorFila) % 3;

            mascara = valor >> i;
            mascara = mascara & 1;
            imagen[fila][col][color] = (byte) ((imagen[fila][col][color] & 0xFE) | mascara);
        }
    }

    // Método para esconder un mensaje en la imagen
    public void esconder(char[] mensaje, int longitud) {
        int contador = 0;
        byte elByte;

        // Esconder la longitud del mensaje en los primeros 16 bits
        escribirBits(contador, longitud, 16);
        contador = 2;  // El primer byte del mensaje se almacena después de la longitud

        for (int i = 0; i < longitud; i++) {
            elByte = (byte) mensaje[i];
            escribirBits(contador, elByte, 8);
            contador++;
            if (i % 1000 == 0) {
                System.out.println("Van " + i + " caracteres de " + longitud);
            }
        }
    }

    // Método para escribir la imagen modificada en un archivo BMP
    public void escribirImagen(String output) {
        byte pad = 0;
        try {
            FileOutputStream fos = new FileOutputStream(output);
            fos.write(header);

            byte[] pixel = new byte[3];
            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    pixel[0] = imagen[i][j][0];
                    pixel[1] = imagen[i][j][1];
                    pixel[2] = imagen[i][j][2];
                    fos.write(pixel);
                }
                for (int k = 0; k < padding; k++) fos.write(pad);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para leer la longitud del mensaje escondido
    public int leerLongitud() {
        int longitud = 0;
        for (int i = 0; i < 16; i++) {
            int col = (i % (ancho * 3)) / 3;
            longitud = longitud | (imagen[0][col][((i % (ancho * 3)) % 3)] & 1) << i;
        }
        return longitud;
    }

    // Método para recuperar el mensaje escondido en la imagen
    public void recuperar(char[] cadena, int longitud) {
        int bytesFila = ancho * 3;
        for (int posCaracter = 0; posCaracter < longitud; posCaracter++) {
            cadena[posCaracter] = 0;
            for (int i = 0; i < 8; i++) {
                int numBytes = 16 + (posCaracter * 8) + i;
                int fila = numBytes / bytesFila;
                int col = (numBytes % bytesFila) / 3;
                cadena[posCaracter] = (char) (cadena[posCaracter] | (imagen[fila][col][((numBytes % bytesFila) % 3)] & 1) << i);
            }
        }
    }
}

