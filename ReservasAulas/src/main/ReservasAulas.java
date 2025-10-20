/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.persistencia;
import enums.TipoAula;
import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import reservasaulas.Aula;
import reservasaulas.Reserva;
import Gestor.*;
import utils.validaciones;

/**
 *
 * @author Sarai Villalta
 */
public class ReservasAulas {

    /**
     * @param args the command line arguments
     */
    private static final Scanner entrada = new Scanner(System.in);
    private static final persistencia persistencia = new persistencia(); // Maneja carga y guardado de CSV

    //Lista de aulas y reservas
    private static final List<Aula> aulas = new ArrayList<>();
    private static final List<Reserva> reservas = new ArrayList<>();

    //Gestor de operaciones de aulas y reservas
    private static GestorAulas gestorAulas;
    private static GestorReservas gestorReservas;

    public static void main(String[] args) {

        //carga aulas y reservas desde csv
        aulas.addAll(persistencia.cargarAulas());
        reservas.addAll(persistencia.cargarReservas(aulas));

        //inicializa gestor de aulas y reservas
        gestorAulas = new GestorAulas(aulas, reservas, persistencia);
        gestorReservas = new GestorReservas(reservas, gestorAulas, persistencia);

        int op;
        do {
            mostrarMenu();

            gestorReservas.reservaHistorica(); //verifica si las reservas ya pasafon fecha y hora fin

            op = validaciones.validarEntero("Seleccione opción: ");
            try {
                switch (op) {
                    case 1:
                        gestorAulas.registrarAula();
                        break;
                    case 2:
                        gestorAulas.listarAulas();
                        break;
                    case 3:
                        gestorAulas.modificarAula();
                        break;
                    case 4:
                        gestorReservas.crearReserva();
                        break;
                    case 5:
                        gestorReservas.listarReservas();
                        break;
                    case 6:
                        gestorReservas.reservasPorResponsable();
                        break;
                    case 7:
                        gestorReservas.modificarReserva();
                        break;
                    case 8:
                        gestorReservas.cancelarReserva();
                        break;
                    case 9:
                        generarReportesYExportar();
                        break;
                    case 10:
                        persistencia.guardarTodoCSV(aulas, reservas);
                        System.out.println("Datos guardados correctamente.");
                        break;
                    case 11:
                        persistencia.guardarTodoCSV(aulas, reservas);
                        System.out.println("Gracias por utilizar el Gestor de aulas...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (op != 11);
    }

    private static void mostrarMenu() {
        System.out.println("\n======= RESERVAS ESCUELA ITCA =======");
        System.out.println("\n====== AULAS ======");
        System.out.println("1. Registrar aula");
        System.out.println("2. Listar aulas");
        System.out.println("3. Modificar aula");
        System.out.println("\n====== RESERVAS ======");
        System.out.println("4. Crear reserva");
        System.out.println("5. Listar reservas");
        System.out.println("6. Buscar reservas por responsable");
        System.out.println("7. Modificar reserva");
        System.out.println("8. Cancelar reserva");
        System.out.println("\n====== REPORTES ======");
        System.out.println("9. Generar reportes y exportar a TXT");
        System.out.println("10. Guardar CSV ahora");
        System.out.println("\n11. Salir\n");
    }

    // REPORTES
    private static void generarReportesYExportar() {
        Map<Aula, Long> horasPorAula = reservas.stream()
                .filter(r -> r.getEstado().equalsIgnoreCase("ACTIVA") || r.getEstado().equalsIgnoreCase("HISTORICA"))
                .collect(Collectors.groupingBy(Reserva::getAula,
                        Collectors.summingLong(r -> Duration.between(r.getHoraInicioReserva(), r.getHoraFinReserva()).toMinutes())));

        List<Map.Entry<Aula, Long>> top3 = horasPorAula.entrySet().stream()
                .sorted(Map.Entry.<Aula, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        Map<TipoAula, Long> ocupPorTipo = reservas.stream()
                .collect(Collectors.groupingBy(r -> r.getAula().getTipoAula(), Collectors.counting()));

        Map<String, Long> distTipoReserva = reservas.stream()
                .collect(Collectors.groupingBy(r -> r.getClass().getSimpleName(), Collectors.counting()));

        System.out.println("\n--- TOP 3 AULAS ---");
        top3.forEach(e -> System.out.printf("%s -> %d minutos%n", e.getKey().getNombre(), e.getValue()));

        System.out.println("\n--- OCUPACIÓN POR TIPO AULA ---");
        ocupPorTipo.forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));

        System.out.println("\n--- DISTRIBUCIÓN POR TIPO DE RESERVA ---");
        distTipoReserva.forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));

        StringBuilder sb = new StringBuilder();
        sb.append("TOP 3 AULAS\n");
        top3.forEach(e -> sb.append(String.format("%s -> %d minutos%n", e.getKey().getNombre(), e.getValue())));
        sb.append("\nOCUPACIÓN POR TIPO AULA\n");
        ocupPorTipo.forEach((k, v) -> sb.append(String.format("%s -> %d%n", k, v)));
        sb.append("\nDISTRIBUCIÓN POR TIPO DE RESERVA\n");
        distTipoReserva.forEach((k, v) -> sb.append(String.format("%s -> %d%n", k, v)));

        try (FileWriter fw = new FileWriter("registro.txt")) {
            fw.write(sb.toString());
            System.out.println("Registro exportado a registro.txt");
        } catch (IOException e) {
            System.out.println("Error exportando registro: " + e.getMessage());
        }
    }

}
