/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import utils.exception;
import data.persistencia;
import utils.validaciones;
import reservasaulas.*;
import enums.*;

/**
 *
 * @author Sarai Villalta
 */
public class GestorReservas {

    // CRUD RESERVAS
    private List<Reserva> reservas;
    private GestorAulas gestorAulas;
    private persistencia persistencia;

    public GestorReservas(List<Reserva> reservas, GestorAulas gestorAulas, persistencia persistencia) {
        this.reservas = reservas;
        this.gestorAulas = gestorAulas;
        this.persistencia = persistencia;
    }

    // CREAR RESERVA
    public void crearReserva() {
        if (gestorAulas.getAulas().isEmpty()) {
            System.out.println("No se han registrado aulas.");
            return;
        }

        System.out.println("\n======= Reserva de aulas =======");

        //muestra aulas para facilitar la selección
        gestorAulas.listarAulasPorId();
        int codigoAula = validaciones.validarEntero("Ingrese código del aula: ");
        Aula aula = gestorAulas.buscarAulaPorCodigo(codigoAula);

        //Ingreso y validaciones de datos
        if (aula == null) {
            System.out.println("Aula no encontrada.");
            return;
        }

        LocalDate fecha = validaciones.validarFecha("Fecha (AAAA-MM-DD): ");
        if (fecha.isBefore(LocalDate.now())) {
            System.out.println("La fecha no puede ser pasada.");
            return;
        }

        LocalTime horaInicio = validaciones.validarHora("Hora inicio (HH:MM): ");
        LocalTime horaFin = validaciones.validarHora("Hora fin (HH:MM): ");

        if (fecha.isEqual(LocalDate.now()) && horaInicio.isBefore(LocalTime.now())) {
            System.out.println("La hora no puede ser antes de la hora actual.");
            return;
        }

        if (!horaFin.isAfter(horaInicio)) {
            System.out.println("Hora fin debe ser después de inicio.");
            return;
        }

        String usuario = validaciones.validarString("Responsable: ");

        System.out.println("\n1. Clase \n2. Evento \n3. Práctica");
        int tipo = validaciones.validarOpcion("Tipo reserva: ", 1, 3);

        String correlativoReserva = String.valueOf(Reserva.getNuevoCorrelativoReserva());
        Reserva reserva = null;

        switch (tipo) {
            case 1:
                reserva = new ReservaClase(correlativoReserva, aula, fecha, horaInicio, horaFin, usuario, "ACTIVA");
                break;
            case 2:
                System.out.println("1. Conferencia\n2. Taller\n3. Reunión");
                int eventoTipo = validaciones.validarOpcion("Tipo de evento: ", 1, 3);
                TipoEvento tipoEvento;
                switch (eventoTipo) {
                    case 1:
                        tipoEvento = TipoEvento.CONFERENCIA;
                        break;
                    case 2:
                        tipoEvento = TipoEvento.TALLER;
                        break;
                    default:
                        tipoEvento = TipoEvento.REUNION;
                        break;
                }
                reserva = new ReservaEvento(tipoEvento, correlativoReserva, aula, fecha, horaInicio, horaFin, usuario, "ACTIVA");
                break;
            case 3:
                reserva = new ReservaPractica(correlativoReserva, aula, fecha, horaInicio, horaFin, usuario, "ACTIVA");
                break;
        }

        try {
            reserva.validar();
        } catch (exception.ReservaInvalidaException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // VALIDAR CONFLICTOS
        if (existeConflicto(reserva, aula, fecha, horaInicio, horaFin)) {
            return;
        }

        reservas.add(reserva);
        System.out.println("Reserva creada correctamente!!!");
        persistencia.guardarTodoCSV(gestorAulas.getAulas(), reservas);
    }
    //Varifica si hay conflicto entre reservas

    private boolean existeConflicto(Reserva reserva, Aula aula, LocalDate fecha, LocalTime hInicio, LocalTime hFin) {
        boolean conflicto = reservas.stream()
                .filter(r -> r.getAula().getCodigoAula() == aula.getCodigoAula())
                .filter(r -> r.getFecha().equals(fecha))
                .filter(r -> r.getEstado().equalsIgnoreCase("ACTIVA"))
                .anyMatch(r -> conflictoHorario(r.getHoraInicioReserva(), r.getHoraFinReserva(), hInicio, hFin));

        if (conflicto) {
            System.out.println("Conflicto de reserva: el aula ya tiene una reserva activa en la fecha u hora indicada.");
            return true;
        }
        return false;
    }

    //verifica conflictos de horarios (coincidentes)
    private boolean conflictoHorario(LocalTime hInicio, LocalTime hFin, LocalTime bInicio, LocalTime bFin) {
        return !hFin.isBefore(bInicio) && !bFin.isBefore(hInicio);
    }

    // LISTAR RESERVAS
    public void listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas...");
            return;
        }

        System.out.println("\n1. Fecha \n2. Aula \n3. Responsable");
        int ordenReserva = validaciones.validarEntero("Ordenar por: ");
        Comparator<Reserva> comparador;
        switch (ordenReserva) {
            case 1:
                comparador = Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicioReserva);
                break;
            case 2:
                comparador = Comparator.comparing(r -> r.getAula().getNombre());
                break;
            case 3:
                comparador = Comparator.comparing(Reserva::getUsuarioReserva);
                break;
            default:
                comparador = Comparator.comparing(Reserva::getCodigoReserva);
        };

        reservas.stream().sorted(comparador).forEach(r -> System.out.println(
                "Reserva N° [" + r.getCodigoReserva() + "] Aula: " + r.getAula().getNombre()
                + " Fecha: " + r.getFecha() + " " + r.getHoraInicioReserva() + "-"
                + r.getHoraFinReserva() + " Responsable: " + r.getUsuarioReserva()
                + " Estado: " + r.getEstado()
        ));
    }

    // LISTAR RESERVAS ACTIVAS (para modificar o cancelar)
    public void listarReservasActivas() {
        List<Reserva> reservasActivas = reservas.stream()
                .filter(r -> r.getEstado().equals("ACTIVA"))
                .collect(Collectors.toList());

        if (reservasActivas.isEmpty()) {
            System.out.println("No hay reservas activas...");
            return;
        }

        reservasActivas.sort(Comparator.comparing(Reserva::getCodigoReserva));

        System.out.println("\n====== RESERVAS ACTIVAS ======");
        reservasActivas.forEach(r -> System.out.println(
                "Reserva N° [" + r.getCodigoReserva() + "] Aula: " + r.getAula().getNombre()
                + " Fecha: " + r.getFecha() + " " + r.getHoraInicioReserva() + "-"
                + r.getHoraFinReserva() + " Responsable: " + r.getUsuarioReserva()
                + " Estado: " + r.getEstado()
        ));
    }

    // "filtro" de reservas por responsable
    public void reservasPorResponsable() {
        String filtro = validaciones.validarString("Nombre de responsable: ").toLowerCase();
        List<Reserva> encontrados = reservas.stream()
                .filter(r -> r.getUsuarioReserva().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron reservas.");
        } else {
            encontrados.forEach(r -> System.out.println(
                    "Reserva N° [" + r.getCodigoReserva() + "] | " + r.getUsuarioReserva()
                    + " | " + r.getFecha() + " | " + r.getHoraInicioReserva() + "-" + r.getHoraFinReserva()
                    + " | " + r.getEstado()
            ));
        }
    }

    // MODIFICAR RESERVA
    public void modificarReserva() {
        listarReservasActivas();
        String id = validaciones.validarString("Código de reserva a modificar: ");
        Reserva rsv = reservas.stream()
                .filter(r -> r.getCodigoReserva().equalsIgnoreCase(id))
                .findFirst().orElse(null);

        if (rsv == null) {
            System.out.println("Reserva no encontrada...");
            return;
        }

        if (rsv.getEstado().equalsIgnoreCase("CANCELADA")) {
            System.out.println("No puede modificar una reserva cancelada.");
            return;
        }

        String nuevo = validaciones.validarString("Nuevo responsable: ");
        if (!nuevo.trim().isEmpty()) {
            rsv.setUsuarioReserva(nuevo);
        }

        System.out.print("Modificar horario? (s/n): ");
        String s = new Scanner(System.in).nextLine().trim();
        if (s.equalsIgnoreCase("s")) {
            LocalTime hInicial = validaciones.validarHora("Hora inicio: ");
            LocalTime hFinal = validaciones.validarHora("Hora fin: ");
            if (!hFinal.isAfter(hInicial)) {
                System.out.println("Hora fin debe ser después de inicio.");
                return;
            }

            if (rsv.getFecha().isEqual(LocalDate.now()) && hInicial.isBefore(LocalTime.now())) {
                System.out.println("No se puede asignar una hora de inicio pasada.");
                return;
            }

            boolean conflicto = reservas.stream()
                    .filter(x -> !x.getCodigoReserva().equalsIgnoreCase(rsv.getCodigoReserva()))
                    .filter(x -> x.getAula().getCodigoAula() == rsv.getAula().getCodigoAula())
                    .filter(x -> x.getFecha().equals(rsv.getFecha()))
                    .filter(x -> x.getEstado().equalsIgnoreCase("ACTIVA"))
                    .anyMatch(x -> conflictoHorario(x.getHoraInicioReserva(), x.getHoraFinReserva(), hInicial, hFinal));

            if (conflicto) {
                System.out.println("Conflicto de horarios: el aula ya tiene una reserva activa en el horario indicado.");
                return;
            }

            rsv.setHoraInicioReserva(hInicial);
            rsv.setHoraFinReserva(hFinal);
        }

        System.out.println("Reserva modificada de manera exitosa.");
        persistencia.guardarTodoCSV(gestorAulas.getAulas(), reservas);
    }

    // CANCELAR RESERVA
    public void cancelarReserva() {
        listarReservasActivas();
        String id = validaciones.validarString("Código de reserva a cancelar: ");
        Reserva rsv = reservas.stream().filter(r -> r.getCodigoReserva().equalsIgnoreCase(id)).findFirst().orElse(null);

        if (rsv == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }

        rsv.setEstado("CANCELADA");
        System.out.println("Reserva cancelada.");
        persistencia.guardarTodoCSV(gestorAulas.getAulas(), reservas);
    }

    // ACTUALIZAR RESERVAS HISTÓRICAS
    public void reservaHistorica() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate fechaActual = ahora.toLocalDate();
        LocalTime horaActual = ahora.toLocalTime();

        for (Reserva r : reservas) {
            if (r.getEstado().equals("ACTIVA")) {
                if (r.getFecha().isBefore(fechaActual) || (r.getFecha().isEqual(fechaActual) && r.getHoraFinReserva().isBefore(horaActual))) {
                    r.setEstado("HISTORICO");
                }
            }
        }

        //Guardar datos
        persistencia.guardarTodoCSV(gestorAulas.getAulas(), reservas);
    }
}
