/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import enums.TipoAula;
import enums.TipoEvento;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import reservasaulas.Aula;
import reservasaulas.Reserva;
import reservasaulas.ReservaClase;
import reservasaulas.ReservaEvento;
import reservasaulas.ReservaPractica;

/**
 *
 * @author Sarai Villalta
 */
public class persistencia {
// Guardar todo

    public static void guardarTodoCSV(List<Aula> aulas, List<Reserva> reservas) {
        if (aulas != null && !aulas.isEmpty()) {
            guardarAulas(aulas);
        }
        if (reservas != null && !reservas.isEmpty()) {
            guardarReservas(reservas);
        }
    }

    private static void guardarAulas(List<Aula> aulas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("aulas.csv"))) {
            for (Aula a : aulas) {
                pw.printf("%d,%s,%d,%s%n",
                        a.getCodigoAula(),
                        limpiarTexto(a.getNombre()),
                        a.getCapacidad(),
                        a.getTipoAula());
            }
        } catch (IOException e) {
            System.out.println("Error guardando aulas: " + e.getMessage());
        }
    }

    private static void guardarReservas(List<Reserva> reservas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("reservas.csv"))) {
            for (Reserva r : reservas) {
                String extra = r instanceof ReservaEvento ? ((ReservaEvento) r).getTipoEvento().toString() : "";
                pw.printf("%s,%d,%s,%s,%s,%s,%s,%s%n",
                        r.getCodigoReserva(),
                        r.getAula().getCodigoAula(),
                        r.getFecha(),
                        r.getHoraInicioReserva(),
                        r.getHoraFinReserva(),
                        limpiarTexto(r.getUsuarioReserva()),
                        r.getEstado(),
                        extra);
            }
        } catch (IOException e) {
            System.out.println("Error guardando reservas: " + e.getMessage());
        }
    }

    // Cargar aulas desde CSV
    public static List<Aula> cargarAulas() {
        List<Aula> aulas = new ArrayList<>();
        File f = new File("aulas.csv");
        if (!f.exists()) {
            return aulas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",", 4);
                int codigo = Integer.parseInt(parts[0]);
                String nombre = restaurarTexto(parts[1]);
                int cap = Integer.parseInt(parts[2]);
                TipoAula tipo = TipoAula.valueOf(parts[3]);
                aulas.add(new Aula(codigo, nombre, cap, tipo));
            }
        } catch (Exception e) {
            System.out.println("Error cargando aulas.csv: " + e.getMessage());
        }
        return aulas;
    }

    // Cargar reservas desde CSV
    public static List<Reserva> cargarReservas(List<Aula> aulas) {
        List<Reserva> reservas = new ArrayList<>();
        File f = new File("reservas.csv");
        if (!f.exists()) {
            return reservas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] p = line.split(",", 8);
                String cod = p[0];
                int aulaCod = Integer.parseInt(p[1]);
                LocalDate fecha = LocalDate.parse(p[2]);
                LocalTime hi = LocalTime.parse(p[3]);
                LocalTime hf = LocalTime.parse(p[4]);
                String resp = restaurarTexto(p[5]);
                String estado = p[6];
                String extra = p.length >= 8 ? p[7] : "";

                Aula aula = aulas.stream().filter(a -> a.getCodigoAula() == aulaCod).findFirst().orElse(null);
                if (aula == null) {
                    continue;
                }

                Reserva r;
                if (!extra.trim().isEmpty()) {
                    TipoEvento te = TipoEvento.valueOf(extra);
                    r = new ReservaEvento(te, cod, aula, fecha, hi, hf, resp, estado);
                } else {
                    r = aula.getTipoAula() == TipoAula.LABORATORIO
                            ? new ReservaPractica(cod, aula, fecha, hi, hf, resp, estado)
                            : new ReservaClase(cod, aula, fecha, hi, hf, resp, estado);
                }
                reservas.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error cargando reservas.csv: " + e.getMessage());
        }
        return reservas;
    }

    // Limpia texto para CSV
    private static String limpiarTexto(String s) {
        return s.replace("\n", " ").replace("\r", " ").replace(",", ";");
    }

    // Restaura texto de CSV
    private static String restaurarTexto(String s) {
        return s.replace(";", ",");
    }
}
