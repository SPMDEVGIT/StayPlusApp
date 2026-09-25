package stayplus;

/**
 * Habitación del hotel. Su estado puede ser "Disponible", "Reservada",
 * "Ocupada" o "Mantenimiento"; las reservas lo actualizan al cambiar de estado.
 */
public class Habitacion {
    private int numero;
    private int piso;
    private String tipo;
    private int capacidad;
    private double precioNoche;
    private String estado;

    /**
     * Crea una habitación; siempre inicia en estado "Disponible".
     *
     * @param numero número único de la habitación
     * @param tipo "Individual", "Doble" o "Suite"
     * @param capacidad cantidad máxima de personas
     * @param precioNoche precio por noche
     */
    public Habitacion(int numero, int piso, String tipo, int capacidad, double precioNoche) {
        this.numero = numero;
        this.piso = piso;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precioNoche = precioNoche;
        this.estado = "Disponible";
    }

    /** @return true si el estado actual es "Disponible" */
    public boolean estaDisponible() {
        return estado.equals("Disponible");
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitación " + numero + " | Piso " + piso + " | " + tipo + " | Capacidad: " + capacidad
                + " | $" + String.format("%,.0f", precioNoche) + " por noche | Estado: " + estado;
    }
}
