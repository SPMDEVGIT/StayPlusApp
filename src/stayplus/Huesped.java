package stayplus;

import java.util.ArrayList;

/**
 * Persona que se hospeda en el hotel. Guarda sus datos de contacto, si es
 * huésped frecuente (lo que le da derecho a descuentos) y el historial de
 * sus reservas.
 */
public class Huesped {
    private String nombreCompleto;
    private String documento;
    private String telefono;
    private String correo;
    private String pais;
    private boolean frecuente;
    private ArrayList<Reserva> reservas;

    /**
     * Crea un huésped sin reservas.
     *
     * @param documento identificador único del huésped en el hotel
     * @param frecuente true si el huésped puede recibir descuentos
     */
    public Huesped(String nombreCompleto, String documento, String telefono, String correo, String pais, boolean frecuente) {
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.pais = pais;
        this.frecuente = frecuente;
        this.reservas = new ArrayList<Reserva>();
    }

    /** Añade una reserva al historial del huésped. */
    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    /** Indica si el huésped es frecuente (equivale a {@link #isFrecuente()}). */
    public boolean esFrecuente() {
        return frecuente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean isFrecuente() {
        return frecuente;
    }

    public void setFrecuente(boolean frecuente) {
        this.frecuente = frecuente;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    /** Resumen en una línea con los datos del huésped y su número de reservas. */
    @Override
    public String toString() {
        return nombreCompleto + " | Doc: " + documento + " | Tel: " + telefono + " | Correo: " + correo
                + " | País: " + pais + " | Frecuente: " + (frecuente ? "Sí" : "No")
                + " | Reservas: " + reservas.size();
    }
}
