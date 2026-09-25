package stayplus;

import java.util.ArrayList;

/**
 * Clase central del sistema: guarda los datos del hotel y sus colecciones de
 * huéspedes, habitaciones, servicios y reservas, y aplica las reglas de negocio
 * (unicidad, disponibilidad por fechas, estados de reserva, descuentos).
 * Los métodos que pueden fallar devuelven false o null en lugar de lanzar excepciones.
 */
public class Hotel {
    private String nombreComercial;
    private String nit;
    private String direccion;
    private String telefono;
    private String paginaWeb;
    private ArrayList<Huesped> huespedes;
    private ArrayList<Habitacion> habitaciones;
    private ArrayList<Reserva> reservas;
    private ArrayList<ServicioAdicional> servicios;

    public Hotel(String nombreComercial, String nit, String direccion, String telefono, String paginaWeb) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.paginaWeb = paginaWeb;
        this.huespedes = new ArrayList<Huesped>();
        this.habitaciones = new ArrayList<Habitacion>();
        this.reservas = new ArrayList<Reserva>();
        this.servicios = new ArrayList<ServicioAdicional>();
    }

    /** Registra un huésped; devuelve false si ya existe uno con el mismo documento. */
    public boolean registrarHuesped(Huesped huesped) {
        if (buscarHuespedPorDocumento(huesped.getDocumento()) != null) {
            return false;
        }
        huespedes.add(huesped);
        return true;
    }

    /** Registra una habitación; devuelve false si ya existe una con el mismo número. */
    public boolean registrarHabitacion(Habitacion habitacion) {
        if (buscarHabitacion(habitacion.getNumero()) != null) {
            return false;
        }
        habitaciones.add(habitacion);
        return true;
    }

    /** Registra un servicio; devuelve false si ya existe uno con el mismo código. */
    public boolean registrarServicio(ServicioAdicional servicio) {
        if (buscarServicio(servicio.getCodigo()) != null) {
            return false;
        }
        servicios.add(servicio);
        return true;
    }

    /**
     * Registra la reserva en el hotel y en el historial de su huésped.
     *
     * @return false si el código ya existe o la salida no es posterior a la entrada
     */
    public boolean crearReserva(Reserva reserva) {
        if (buscarReserva(reserva.getCodigo()) != null) {
            return false;
        }
        // La salida debe ser posterior a la entrada
        if (reserva.getFechaSalida().compareTo(reserva.getFechaEntrada()) <= 0) {
            return false;
        }
        reservas.add(reserva);
        reserva.getHuesped().agregarReserva(reserva);
        return true;
    }

    // Métodos de búsqueda: devuelven el objeto encontrado o null si no existe

    public Huesped buscarHuespedPorDocumento(String documento) {
        for (Huesped h : huespedes) {
            if (h.getDocumento().equals(documento)) {
                return h;
            }
        }
        return null;
    }

    public Huesped buscarHuespedPorTelefono(String telefono) {
        for (Huesped h : huespedes) {
            if (h.getTelefono().equals(telefono)) {
                return h;
            }
        }
        return null;
    }

    public Habitacion buscarHabitacion(int numero) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == numero) {
                return h;
            }
        }
        return null;
    }

    public ServicioAdicional buscarServicio(String codigo) {
        for (ServicioAdicional s : servicios) {
            if (s.getCodigo().equals(codigo)) {
                return s;
            }
        }
        return null;
    }

    public Reserva buscarReserva(String codigo) {
        for (Reserva r : reservas) {
            if (r.getCodigo().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Indica si la habitación puede reservarse en el rango dado.
     * Una habitación no está libre si está en mantenimiento o si una reserva
     * activa (no cancelada ni finalizada) que la incluye se cruza con las fechas.
     */
    public boolean habitacionDisponibleEnFechas(Habitacion habitacion, String fechaEntrada, String fechaSalida) {
        if (habitacion.getEstado().equals("Mantenimiento")) {
            return false;
        }
        for (Reserva r : reservas) {
            boolean activa = !r.getEstado().equals("Cancelada") && !r.getEstado().equals("Finalizada");
            if (activa && r.contieneHabitacion(habitacion) && r.seCruzaCon(fechaEntrada, fechaSalida)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Agrega una habitación a una reserva.
     *
     * @return false si la reserva o la habitación no existen, la reserva no está
     *         Pendiente, o la habitación no está disponible en esas fechas
     */
    public boolean agregarHabitacionAReserva(String codigoReserva, int numeroHabitacion) {
        Reserva reserva = buscarReserva(codigoReserva);
        Habitacion habitacion = buscarHabitacion(numeroHabitacion);
        if (reserva == null || habitacion == null) {
            return false;
        }
        if (!reserva.getEstado().equals("Pendiente")) {
            return false;
        }
        if (!habitacionDisponibleEnFechas(habitacion, reserva.getFechaEntrada(), reserva.getFechaSalida())) {
            return false;
        }
        reserva.agregarHabitacion(habitacion);
        return true;
    }

    /**
     * Agrega un servicio adicional a una reserva.
     *
     * @return false si algo no existe, el servicio no está disponible o la reserva no está "En curso"
     */
    public boolean agregarServicioAReserva(String codigoReserva, String codigoServicio) {
        Reserva reserva = buscarReserva(codigoReserva);
        ServicioAdicional servicio = buscarServicio(codigoServicio);
        if (reserva == null || servicio == null) {
            return false;
        }
        if (!servicio.isDisponible()) {
            return false;
        }
        // Los servicios se solicitan durante la estadía
        if (!reserva.getEstado().equals("En curso")) {
            return false;
        }
        reserva.agregarServicio(servicio);
        return true;
    }

    /** Aplica un descuento a la reserva indicada; delega las reglas en {@link Reserva#aplicarDescuento}. */
    public boolean aplicarDescuentoAReserva(String codigoReserva, double porcentaje) {
        Reserva reserva = buscarReserva(codigoReserva);
        if (reserva == null) {
            return false;
        }
        return reserva.aplicarDescuento(porcentaje);
    }

    /**
     * Cambia el estado de una reserva. Además de las transiciones válidas de
     * {@link Reserva#cambiarEstado}, no permite confirmar una reserva sin habitaciones.
     */
    public boolean cambiarEstadoReserva(String codigoReserva, String nuevoEstado) {
        Reserva reserva = buscarReserva(codigoReserva);
        if (reserva == null) {
            return false;
        }
        // No se confirma una reserva sin habitaciones
        if (nuevoEstado.equals("Confirmada") && reserva.getHabitaciones().isEmpty()) {
            return false;
        }
        return reserva.cambiarEstado(nuevoEstado);
    }

    /**
     * Un número es perfecto si es igual a la suma de sus divisores propios
     * (por ejemplo 6 = 1+2+3 y 8128). Se usa para el número de teléfono del huésped.
     */
    public boolean esNumeroPerfecto(long numero) {
        if (numero < 2) {
            return false;
        }
        long suma = 1;
        // Se recorre solo hasta la raíz del número porque los teléfonos tienen 10 dígitos:
        // recorrer hasta numero / 2 sería demasiado lento. Cada divisor i trae su pareja numero / i.
        for (long i = 2; i * i <= numero; i++) {
            if (numero % i == 0) {
                suma += i;
                if (numero / i != i) {
                    suma += numero / i;
                }
            }
        }
        return suma == numero;
    }

    /** Suma el valor total de las reservas cuya fecha de realización es la indicada. */
    public double calcularIngresosPorFecha(String fecha) {
        double total = 0;
        for (Reserva r : reservas) {
            if (r.getFechaRealizacion().equals(fecha)) {
                total += r.getValorTotal();
            }
        }
        return total;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public ArrayList<Huesped> getHuespedes() {
        return huespedes;
    }

    public void setHuespedes(ArrayList<Huesped> huespedes) {
        this.huespedes = huespedes;
    }

    public ArrayList<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(ArrayList<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public ArrayList<ServicioAdicional> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<ServicioAdicional> servicios) {
        this.servicios = servicios;
    }
}
