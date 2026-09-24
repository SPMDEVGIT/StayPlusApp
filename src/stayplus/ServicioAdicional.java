package stayplus;

/**
 * Servicio extra que el hotel ofrece (restaurante, lavandería, etc.) y que
 * se puede cobrar dentro de una reserva mientras esté disponible.
 */
public class ServicioAdicional {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponible;

    /**
     * @param codigo identificador único del servicio
     * @param precio valor que se suma al subtotal cada vez que se usa
     * @param disponible false si el servicio no se puede solicitar por ahora
     */
    public ServicioAdicional(String codigo, String nombre, String descripcion, double precio, boolean disponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return codigo + " | " + nombre + " | " + descripcion + " | $" + String.format("%,.0f", precio)
                + " | " + (disponible ? "Disponible" : "No disponible");
    }
}
