package stayplus;

import java.util.Scanner;

/**
 * Punto de entrada de StayPlus: menú de consola que le permite al usuario
 * operar el {@link Hotel}. Cada opción pide los datos por teclado, llama al
 * hotel y explica por qué se rechazó una operación cuando falla.
 */
public class Main {
    static Scanner sc = new Scanner(System.in);
    static Hotel hotel = new Hotel("StayPlus", "900123456-7", "Calle 21 # 14-35, Armenia", "6067451234", "www.stayplus.com");

    /** Carga datos de prueba y repite el menú hasta que el usuario elija 0 (salir). */
    public static void main(String[] args) {
        cargarDatosDePrueba();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1:
                    registrarHuesped();
                    break;
                case 2:
                    registrarHabitacion();
                    break;
                case 3:
                    registrarServicio();
                    break;
                case 4:
                    crearReserva();
                    break;
                case 5:
                    agregarHabitacionAReserva();
                    break;
                case 6:
                    agregarServicioAReserva();
                    break;
                case 7:
                    aplicarDescuento();
                    break;
                case 8:
                    cambiarEstadoReserva();
                    break;
                case 9:
                    cambiarEstadoHabitacion();
                    break;
                case 10:
                    consultarHuespedPorTelefono();
                    break;
                case 11:
                    calcularIngresos();
                    break;
                case 12:
                    verDetalleReserva();
                    break;
                case 13:
                    listarInformacion();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /** Imprime las opciones del menú principal. */
    static void mostrarMenu() {
        System.out.println();
        System.out.println("===== StayPlus =====");
        System.out.println("1. Registrar huésped");
        System.out.println("2. Registrar habitación");
        System.out.println("3. Registrar servicio adicional");
        System.out.println("4. Crear reserva");
        System.out.println("5. Agregar habitación a una reserva");
        System.out.println("6. Agregar servicio adicional a una reserva");
        System.out.println("7. Aplicar descuento a una reserva (huésped frecuente)");
        System.out.println("8. Cambiar estado de una reserva");
        System.out.println("9. Cambiar estado de una habitación");
        System.out.println("10. Consultar huésped por teléfono");
        System.out.println("11. Calcular ingresos por fecha");
        System.out.println("12. Ver detalle de una reserva");
        System.out.println("13. Listar información del hotel");
        System.out.println("0. Salir");
    }

    /** Registra huéspedes, habitaciones y servicios iniciales para poder probar el sistema. */
    static void cargarDatosDePrueba() {
        hotel.registrarHuesped(new Huesped("Ana Gómez", "1001", "3001234567", "ana@mail.com", "Colombia", true));
        // 8128 es un número perfecto
        hotel.registrarHuesped(new Huesped("John Smith", "2002", "8128", "john@mail.com", "Estados Unidos", false));

        hotel.registrarHabitacion(new Habitacion(101, 1, "Individual", 1, 120000));
        hotel.registrarHabitacion(new Habitacion(201, 2, "Doble", 2, 180000));
        hotel.registrarHabitacion(new Habitacion(301, 3, "Suite", 4, 350000));

        hotel.registrarServicio(new ServicioAdicional("S1", "Restaurante", "Servicio de restaurante", 60000, true));
        hotel.registrarServicio(new ServicioAdicional("S2", "Lavandería", "Lavado de ropa", 25000, true));
        hotel.registrarServicio(new ServicioAdicional("S3", "Transporte", "Transporte al aeropuerto", 40000, false));
        hotel.registrarServicio(new ServicioAdicional("S4", "Servicio a la habitación", "Comidas en la habitación", 30000, true));
    }

    // ---------- Operaciones del menú ----------

    static void registrarHuesped() {
        String nombre = leerTexto("Nombre completo: ");
        String documento = leerTexto("Documento: ");
        String telefono = leerTelefono("Teléfono: ");
        String correo = leerTexto("Correo: ");
        String pais = leerTexto("País: ");
        String[] opciones = {"Sí", "No"};
        boolean frecuente = elegir("¿Es huésped frecuente?", opciones).equals("Sí");
        if (hotel.registrarHuesped(new Huesped(nombre, documento, telefono, correo, pais, frecuente))) {
            System.out.println("Huésped registrado correctamente.");
        } else {
            System.out.println("Rechazado: ya existe un huésped con ese documento.");
        }
    }

    static void registrarHabitacion() {
        int numero = leerEntero("Número de habitación: ");
        int piso = leerEntero("Piso: ");
        String[] tipos = {"Individual", "Doble", "Suite"};
        String tipo = elegir("Tipo de habitación", tipos);
        int capacidad = leerEntero("Capacidad: ");
        double precio = leerDouble("Precio por noche: ");
        if (hotel.registrarHabitacion(new Habitacion(numero, piso, tipo, capacidad, precio))) {
            System.out.println("Habitación registrada correctamente.");
        } else {
            System.out.println("Rechazado: ya existe una habitación con ese número.");
        }
    }

    static void registrarServicio() {
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        double precio = leerDouble("Precio: ");
        String[] opciones = {"Sí", "No"};
        boolean disponible = elegir("¿Está disponible?", opciones).equals("Sí");
        if (hotel.registrarServicio(new ServicioAdicional(codigo, nombre, descripcion, precio, disponible))) {
            System.out.println("Servicio registrado correctamente.");
        } else {
            System.out.println("Rechazado: ya existe un servicio con ese código.");
        }
    }

    static void crearReserva() {
        String documento = leerTexto("Documento del huésped: ");
        Huesped huesped = hotel.buscarHuespedPorDocumento(documento);
        if (huesped == null) {
            System.out.println("Rechazado: no existe un huésped con ese documento.");
            return;
        }
        String codigo = leerTexto("Código de la reserva: ");
        String fechaRealizacion = leerFecha("Fecha de realización");
        String fechaEntrada = leerFecha("Fecha de entrada");
        String fechaSalida = leerFecha("Fecha de salida");
        String[] pagos = {"Tarjeta de crédito", "Transferencia bancaria", "Efectivo"};
        String metodoPago = elegir("Método de pago", pagos);
        Reserva reserva = new Reserva(codigo, huesped, fechaRealizacion, fechaEntrada, fechaSalida, metodoPago);
        if (hotel.crearReserva(reserva)) {
            System.out.println("Reserva creada correctamente (estado Pendiente).");
        } else if (hotel.buscarReserva(codigo) != null) {
            System.out.println("Rechazado: ya existe una reserva con ese código.");
        } else {
            System.out.println("Rechazado: la fecha de salida debe ser posterior a la de entrada.");
        }
    }

    static void agregarHabitacionAReserva() {
        String codigo = leerTexto("Código de la reserva: ");
        int numero = leerEntero("Número de habitación: ");
        Reserva reserva = hotel.buscarReserva(codigo);
        Habitacion habitacion = hotel.buscarHabitacion(numero);
        if (reserva == null) {
            System.out.println("Rechazado: la reserva no existe.");
        } else if (habitacion == null) {
            System.out.println("Rechazado: la habitación no existe.");
        } else if (!reserva.getEstado().equals("Pendiente")) {
            System.out.println("Rechazado: la reserva debe estar Pendiente.");
        } else if (hotel.agregarHabitacionAReserva(codigo, numero)) {
            System.out.println("Habitación agregada. Total: $" + String.format("%,.0f", reserva.getValorTotal()));
        } else {
            System.out.println("Rechazado: la habitación no está disponible en esas fechas (cruce de fechas o mantenimiento).");
        }
    }

    static void agregarServicioAReserva() {
        String codigo = leerTexto("Código de la reserva: ");
        String codigoServicio = leerTexto("Código del servicio: ");
        Reserva reserva = hotel.buscarReserva(codigo);
        ServicioAdicional servicio = hotel.buscarServicio(codigoServicio);
        if (reserva == null) {
            System.out.println("Rechazado: la reserva no existe.");
        } else if (servicio == null) {
            System.out.println("Rechazado: el servicio no existe.");
        } else if (!servicio.isDisponible()) {
            System.out.println("Rechazado: el servicio no está disponible.");
        } else if (hotel.agregarServicioAReserva(codigo, codigoServicio)) {
            System.out.println("Servicio agregado. Total: $" + String.format("%,.0f", reserva.getValorTotal()));
        } else {
            System.out.println("Rechazado: los servicios solo se agregan a reservas En curso.");
        }
    }

    static void aplicarDescuento() {
        String codigo = leerTexto("Código de la reserva: ");
        double porcentaje = leerDouble("Porcentaje de descuento (0-100): ");
        Reserva reserva = hotel.buscarReserva(codigo);
        if (reserva == null) {
            System.out.println("Rechazado: la reserva no existe.");
        } else if (hotel.aplicarDescuentoAReserva(codigo, porcentaje)) {
            System.out.println("Descuento aplicado. Total: $" + String.format("%,.0f", reserva.getValorTotal()));
        } else if (!reserva.getHuesped().esFrecuente()) {
            System.out.println("Rechazado: el huésped no es frecuente.");
        } else {
            System.out.println("Rechazado: el porcentaje debe estar entre 0 y 100.");
        }
    }

    static void cambiarEstadoReserva() {
        String codigo = leerTexto("Código de la reserva: ");
        Reserva reserva = hotel.buscarReserva(codigo);
        if (reserva == null) {
            System.out.println("Rechazado: la reserva no existe.");
            return;
        }
        System.out.println("Estado actual: " + reserva.getEstado());
        String[] estados = {"Confirmada", "En curso", "Finalizada", "Cancelada"};
        String nuevo = elegir("Nuevo estado", estados);
        if (hotel.cambiarEstadoReserva(codigo, nuevo)) {
            System.out.println("Estado cambiado a " + nuevo + ".");
        } else if (nuevo.equals("Confirmada") && reserva.getHabitaciones().isEmpty()
                && reserva.getEstado().equals("Pendiente")) {
            System.out.println("Rechazado: no se puede confirmar una reserva sin habitaciones.");
        } else {
            System.out.println("Rechazado: transición no válida de " + reserva.getEstado() + " a " + nuevo + ".");
        }
    }

    static void cambiarEstadoHabitacion() {
        int numero = leerEntero("Número de habitación: ");
        Habitacion habitacion = hotel.buscarHabitacion(numero);
        if (habitacion == null) {
            System.out.println("Rechazado: la habitación no existe.");
            return;
        }
        String[] estados = {"Disponible", "Mantenimiento"};
        String nuevo = elegir("Nuevo estado", estados);
        habitacion.setEstado(nuevo);
        System.out.println("Habitación " + numero + " ahora está en estado " + nuevo + ".");
    }

    static void consultarHuespedPorTelefono() {
        String telefono = leerTelefono("Teléfono: ");
        Huesped huesped = hotel.buscarHuespedPorTelefono(telefono);
        if (huesped == null) {
            System.out.println("No existe un huésped con ese teléfono.");
            return;
        }
        System.out.println(huesped);
        // Los teléfonos de hasta 18 dígitos caben en un long
        if (hotel.esNumeroPerfecto(Long.parseLong(telefono))) {
            System.out.println("El número " + telefono + " SÍ es perfecto.");
        } else {
            System.out.println("El número " + telefono + " NO es perfecto.");
        }
    }

    static void calcularIngresos() {
        String fecha = leerFecha("Fecha");
        double total = hotel.calcularIngresosPorFecha(fecha);
        System.out.println("Ingresos de reservas realizadas el " + fecha + ": $" + String.format("%,.0f", total));
    }

    static void verDetalleReserva() {
        String codigo = leerTexto("Código de la reserva: ");
        Reserva reserva = hotel.buscarReserva(codigo);
        if (reserva == null) {
            System.out.println("Rechazado: la reserva no existe.");
        } else {
            System.out.println(reserva);
        }
    }

    static void listarInformacion() {
        System.out.println("--- Hotel ---");
        System.out.println(hotel.getNombreComercial() + " | NIT: " + hotel.getNit() + " | " + hotel.getDireccion()
                + " | Tel: " + hotel.getTelefono() + " | " + hotel.getPaginaWeb());
        System.out.println("--- Huéspedes ---");
        for (Huesped h : hotel.getHuespedes()) {
            System.out.println(h);
        }
        System.out.println("--- Habitaciones ---");
        for (Habitacion h : hotel.getHabitaciones()) {
            System.out.println(h);
        }
        System.out.println("--- Servicios ---");
        for (ServicioAdicional s : hotel.getServicios()) {
            System.out.println(s);
        }
        System.out.println("--- Reservas ---");
        if (hotel.getReservas().isEmpty()) {
            System.out.println("(sin reservas)");
        }
        for (Reserva r : hotel.getReservas()) {
            System.out.println(r);
        }
    }

    // ---------- Lectura de datos ----------
    // Cada método repite la pregunta hasta recibir un valor válido.

    /** Lee una línea de texto sin espacios sobrantes. */    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    /** Lee un entero y consume el salto de línea para no afectar lecturas de texto posteriores. */
    static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Valor no válido. " + mensaje);
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextDouble()) {
            sc.nextLine();
            System.out.print("Valor no válido. " + mensaje);
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }

    /** Lee una fecha AAAA-MM-DD; valida el formato, el mes (1-12) y el día (1-31). */
    static String leerFecha(String mensaje) {
        while (true) {
            String fecha = leerTexto(mensaje + " (AAAA-MM-DD): ");
            boolean formato = fecha.length() == 10 && fecha.charAt(4) == '-' && fecha.charAt(7) == '-';
            for (int i = 0; formato && i < 10; i++) {
                if (i != 4 && i != 7 && !Character.isDigit(fecha.charAt(i))) {
                    formato = false;
                }
            }
            if (formato) {
                int mes = Integer.parseInt(fecha.substring(5, 7));
                int dia = Integer.parseInt(fecha.substring(8, 10));
                if (mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31) {
                    return fecha;
                }
            }
            System.out.println("Fecha no válida. Use el formato AAAA-MM-DD con mes 1-12 y día 1-31.");
        }
    }

    /** Lee un teléfono compuesto solo por dígitos, de máximo 18 (cabe en un long). */
    static String leerTelefono(String mensaje) {
        while (true) {
            String telefono = leerTexto(mensaje);
            boolean valido = telefono.length() > 0 && telefono.length() <= 18;
            for (int i = 0; valido && i < telefono.length(); i++) {
                if (!Character.isDigit(telefono.charAt(i))) {
                    valido = false;
                }
            }
            if (valido) {
                return telefono;
            }
            System.out.println("Teléfono no válido: solo dígitos, máximo 18.");
        }
    }

    /** Muestra las opciones numeradas y devuelve el texto de la que el usuario elija. */
    static String elegir(String titulo, String[] opciones) {
        System.out.println(titulo + ":");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  " + (i + 1) + ". " + opciones[i]);
        }
        int eleccion = leerEntero("Elija una opción: ");
        while (eleccion < 1 || eleccion > opciones.length) {
            eleccion = leerEntero("Opción no válida. Elija una opción: ");
        }
        return opciones[eleccion - 1];
    }
}
