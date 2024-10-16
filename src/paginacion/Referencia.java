package paginacion;

public class Referencia {
    private String tipo;       // Tipo de referencia (Imagen o Mensaje)
    private int fila;          // Fila en la matriz de la imagen (o 0 para el mensaje)
    private int columna;       // Columna en la matriz de la imagen (o 0 para el mensaje)
    private int paginaVirtual; // Número de página virtual
    private int desplazamiento;// Desplazamiento dentro de la página
    private String accion;     // Acción (R para lectura, W para escritura)

    // Constructor
    public Referencia(String tipo, int fila, int columna, int paginaVirtual, int desplazamiento, String accion) {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.paginaVirtual = paginaVirtual;
        this.desplazamiento = desplazamiento;
        this.accion = accion;
    }

    // Getters para los atributos de la clase
    public String getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getPaginaVirtual() {
        return paginaVirtual;
    }

    public int getDesplazamiento() {
        return desplazamiento;
    }

    public String getAccion() {
        return accion;
    }

    // Método toString para facilitar la visualización
    @Override
    public String toString() {
        return tipo + "[" + fila + "][" + columna + "], Página Virtual: " + paginaVirtual + ", Desplazamiento: " + desplazamiento + ", Acción: " + accion;
    }
}

