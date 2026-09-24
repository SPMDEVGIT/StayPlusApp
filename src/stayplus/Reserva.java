package stayplus;

import java.util.ArrayList;

/**
 * Reserva de un huésped: agrupa habitaciones y servicios, calcula el valor
 * total (con descuento) y controla el ciclo de vida mediante su estado:
 * Pendiente, Confirmada, En curso, Finalizada o Cancelada.
 * Las fechas se manejan como texto con formato AAAA-MM-DD.
 */
public class Reserva {
    private String codigo;
    private String fechaRealizacion;
    private String fechaEntrada;
    private String fechaSalida;
    private String estado;
    private String metodoPago; // "Tarjeta de crédito", "Transferencia bancaria" o "Efectivo"
    private double porcentajeDescuento;
    private double valorTotal;
    private Huesped huesped;
    private ArrayList<Habitacion> habitaciones;
    private ArrayList<ServicioAdicional> serviciosUtilizados;

    /** Crea una reserva en estado "Pendiente", sin habitaciones, servicios ni descuento. */
    public Reserva(String codigo, Huesped huesped, String fechaRealizacion, String fechaEntrada, String fechaSalida, String metodoPago) {
        this.codigo = codigo;
        this.huesped = huesped;
        this.fechaRealizacion = fechaRealizacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.metodoPago = metodoPago;
        this.estado = "Pendiente";
        this.porcentajeDescuento = 0;
        this.valorTotal = 0;
        this.habitaciones = new ArrayList<Habitacion>();
        this.serviciosUtilizados = new ArrayList<ServicioAdicional>();
    }

    /** Agrega una habitación y recalcula el valor total. */
    public void agregarHabitacion(Habitacion h) {
        habitaciones.add(h);
        calcularValorTotal();
    }

    /** Agrega un servicio utilizado y recalcula el valor total. */
    public void agregarServicio(ServicioAdicional s) {
        serviciosUtilizados.add(s);
        calcularValorTotal();
    }

    /**
     * Aplica un descuento porcentual a la reserva.
     * Solo los huéspedes frecuentes tienen descuento, entre 0 y 100 %.
     *
     * @return true si se aplicó; false si el huésped no es frecuente o el porcentaje es inválido
     */
    public boolean aplicarDescuento(double porcentaje) {
        if (!huesped.esFrecuente() || porcentaje < 0 || porcentaje > 100) {
            return false;
        }
        porcentajeDescuento = porcentaje;
        calcularValorTotal();
        return true;
    }

    /** @return número de noches entre la fecha de entrada y la de salida */
    public int calcularNoches() {
        return convertirADias(fechaSalida) - convertirADias(fechaEntrada);
    }

    /**
     * Subtotal sin descuento: (suma de precios por noche de las habitaciones × noches)
     * más el precio de cada servicio utilizado.
     */
    public double calcularSubtotal() {
        double sumaNoche = 0;
        for (Habitacion h : habitaciones) {
            sumaNoche += h.getPrecioNoche();
        }
        double subtotal = sumaNoche * calcularNoches();
        for (ServicioAdicional s : serviciosUtilizados) {
            subtotal += s.getPrecio();
        }
        return subtotal;
    }

    /** Calcula el subtotal menos el descuento, lo guarda en {@code valorTotal} y lo devuelve. */
    public double calcularValorTotal() {
        double subtotal = calcularSubtotal();
        valorTotal = subtotal - (subtotal * porcentajeDescuento / 100);
        return valorTotal;
    }

    /**
     * Cambia el estado si la transición es válida y actualiza el estado de las
     * habitaciones: Confirmada -> Reservada, En curso -> Ocupada,
     * Finalizada/Cancelada -> Disponible.
     * Transiciones: Pendiente -> Confirmada/Cancelada; Confirmada -> En curso/Cancelada; En curso -> Finalizada
     *
     * @return true si el cambio se realizó; false si la transición no es válida
     */
    public boolean cambiarEstado(String nuevoEstado) {
        boolean valida = false;
        if (estado.equals("Pendiente")) {
            valida = nuevoEstado.equals("Confirmada") || nuevoEstado.equals("Cancelada");
        } else if (estado.equals("Confirmada")) {
            valida = nuevoEstado.equals("En curso") || nuevoEstado.equals("Cancelada");
        } else if (estado.equals("En curso")) {
            valida = nuevoEstado.equals("Finalizada");
        }
        if (!valida) {
            return false;
        }
        estado = nuevoEstado;
        String estadoHabitacion = null;
        if (nuevoEstado.equals("Confirmada")) {
            estadoHabitacion = "Reservada";
        } else if (nuevoEstado.equals("En curso")) {
            estadoHabitacion = "Ocupada";
        } else if (nuevoEstado.equals("Finalizada") || nuevoEstado.equals("Cancelada")) {
            estadoHabitacion = "Disponible";
        }
        if (estadoHabitacion != null) {
            for (Habitacion h : habitaciones) {
                h.setEstado(estadoHabitacion);
            }
        }
        return true;
    }

    /**
     * Indica si el rango de esta reserva se solapa con el rango dado.
     * Dos rangos se cruzan si cada uno empieza antes de que termine el otro
     * (el día de salida de uno puede ser el de entrada del otro).
     */
    public boolean seCruzaCon(String fechaEntrada, String fechaSalida) {
        return this.fechaEntrada.compareTo(fechaSalida) < 0 && fechaEntrada.compareTo(this.fechaSalida) < 0;
    }

    /** @return true si la reserva incluye una habitación con el mismo número */
    public boolean contieneHabitacion(Habitacion h) {
        for (Habitacion hab : habitaciones) {
            if (hab.getNumero() == h.getNumero()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte AAAA-MM-DD en un número de días para poder restar fechas.
     * Cuenta los años completos anteriores (con sus bisiestos), los meses
     * transcurridos y el día; suma un día si el año es bisiesto y ya pasó febrero.
     */
    private int convertirADias(String fecha) {
        int anio = Integer.parseInt(fecha.substring(0, 4));
        int mes = Integer.parseInt(fecha.substring(5, 7));
        int dia = Integer.parseInt(fecha.substring(8, 10));
        int[] diasMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int dias = (anio - 1) * 365 + (anio - 1) / 4 - (anio - 1) / 100 + (anio - 1) / 400;
        for (int i = 0; i < mes - 1; i++) {
            dias += diasMes[i];
        }
        boolean bisiesto = (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
        if (bisiesto && mes > 2) {
            dias += 1;
        }
        return dias + dia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    public ArrayList<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(ArrayList<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public ArrayList<ServicioAdicional> getServiciosUtilizados() {
        return serviciosUtilizados;
    }

    public void setServiciosUtilizados(ArrayList<ServicioAdicional> serviciosUtilizados) {
        this.serviciosUtilizados = serviciosUtilizados;
    }

    /** Detalle multilínea de la reserva: fechas, estado, habitaciones, servicios y totales. */
    @Override
    public String toString() {
        String texto = "Reserva " + codigo + " | Huésped: " + huesped.getNombreCompleto() + "\n";
        texto += "  Realizada: " + fechaRealizacion + " | Entrada: " + fechaEntrada + " | Salida: " + fechaSalida
                + " | Noches: " + calcularNoches() + "\n";
        texto += "  Estado: " + estado + " | Pago: " + metodoPago + "\n";
        texto += "  Habitaciones:\n";
        if (habitaciones.isEmpty()) {
            texto += "    (ninguna)\n";
        }
        for (Habitacion h : habitaciones) {
            texto += "    " + h + "\n";
        }
        texto += "  Servicios:\n";
        if (serviciosUtilizados.isEmpty()) {
            texto += "    (ninguno)\n";
        }
        for (ServicioAdicional s : serviciosUtilizados) {
            texto += "    " + s + "\n";
        }
        texto += "  Subtotal: $" + String.format("%,.0f", calcularSubtotal()) + "\n";
        texto += "  Descuento: " + porcentajeDescuento + " %\n";
        texto += "  Total: $" + String.format("%,.0f", valorTotal);
        return texto;
    }
}
