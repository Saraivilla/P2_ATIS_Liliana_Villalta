/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestor;

import data.persistencia;
import utils.validaciones;
import enums.TipoAula;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import reservasaulas.Aula;
import utils.exception;
import reservasaulas.Reserva;

/**
 *
 * @author Sarai Villalta
 */
public class GestorAulas {
// CRUD AULAS

    private List<Aula> aulas;
    private persistencia persistencia;
    private List<Reserva> reservas;

    public GestorAulas(List<Aula> aulas, List<Reserva> reservas, persistencia persistencia) {
        this.aulas = aulas;
        this.reservas = reservas;
        this.persistencia = persistencia;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    // REGISTRAR AULA
    public void registrarAula() {
        System.out.println("\n--- Registrar Aula ---");
        int codigo = validaciones.validarEntero("Código de aula: ");

        if (aulas.stream().anyMatch(a -> a.getCodigoAula() == codigo)) {
            System.out.println("El aula " + codigo + " ya ha sido registrada");
            return;
        }

        String nombre = validaciones.validarString("Nombre de aula: ");
        if (aulas.stream().anyMatch(a -> a.getNombre().equalsIgnoreCase(nombre))) {
            System.out.println("El aula con el nombre '" + nombre + "' ya existe.");
            return;
        }
        int capacidad = 0;

        while (true) {
            try {
                capacidad = validaciones.validarEntero("Capacidad: ");
                if (capacidad < 0) {
                    throw new exception.AulaInvalidaException("La capacidad no puede ser negativa");
                }
                break;
            } catch (exception.AulaInvalidaException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("1. Teórica\n2. Laboratorio\n3. Auditorio");
        int aulaTipo = validaciones.validarEntero("Tipo de aula: ");
        while (aulaTipo < 1 || aulaTipo > 3) {
            System.out.println("Opción inválida. Por favor ingrese 1, 2 o 3.");
            aulaTipo = validaciones.validarEntero("Tipo de aula: ");
        }

        TipoAula tipo;
        switch (aulaTipo) {
            case 1:
                tipo = TipoAula.TEORICA;
                break;
            case 2:
                tipo = TipoAula.LABORATORIO;
                break;
            default:
                tipo = TipoAula.AUDITORIO;
                break;
        };

        Aula a = new Aula(codigo, nombre, capacidad, tipo);
        aulas.add(a);
        persistencia.guardarTodoCSV(aulas, null);
        System.out.println("\n=== Aula " + a.getNombre() + " creada ===");
    }

    // LISTAR AULAS
    public void listarAulas() {
        if (aulas.isEmpty()) {
            System.out.println("No hay aulas registradas...");
            return;
        }

        System.out.println("1. Código\n2. Nombre\n3. Tipo");
        int orden = validaciones.validarEntero("Ordenar por: ");

        List<Aula> aulasCopia = new ArrayList<>(aulas);

        switch (orden) {
            case 1:
                aulasCopia.sort(Comparator.comparingInt(Aula::getCodigoAula));
                break;
            case 2:
                aulasCopia.sort(Comparator.comparing(Aula::getNombre));
                break;
            case 3:
                aulasCopia.sort(Comparator.comparing(a -> a.getTipoAula().toString()));
                break;
            default:
                System.out.println("Opción inválida, mostrando sin ordenar.");
                break;
        }

        System.out.println("\n====== AULAS ======");
        aulasCopia.forEach(a -> System.out.println(
                "[" + a.getCodigoAula() + "] " + a.getNombre()
                + " | Tipo: " + a.getTipoAula()
                + " | Capacidad: " + a.getCapacidad()
        ));
    }

    // MODIFICAR AULA
    public void modificarAula() {
        if (aulas.isEmpty()) {
            System.out.println("No hay aulas registradas...");
            return;
        }

        System.out.println("\n==== Lista de aulas ====");
        aulas.forEach(a -> System.out.println(
                "[" + a.getCodigoAula() + "] " + a.getNombre()
                + " | Tipo: " + a.getTipoAula()
                + " | Capacidad: " + a.getCapacidad()
        ));

        int codigo = validaciones.validarEntero("Ingrese el código del aula a modificar: ");
        Aula aulaModificar = aulas.stream()
                .filter(a -> a.getCodigoAula() == codigo)
                .findFirst()
                .orElse(null);

        if (aulaModificar == null) {
            System.out.println("Código de aula no encontrado.");
            return;
        }

        System.out.println("\n1. Nombre\n2. Capacidad\n3. Tipo de Aula\n4. Salir");
        int opcion = validaciones.validarEntero("Campo a modificar: ");
        boolean modificado = false;

        switch (opcion) {
            case 1:
                String nuevoNombre = validaciones.validarString("Nuevo nombre: ");
                if (!nuevoNombre.trim().isEmpty()) {
                    if (aulas.stream().anyMatch(a -> a != aulaModificar && a.getNombre().equalsIgnoreCase(nuevoNombre))) {
                        System.out.println("El aula con el nombre '" + nuevoNombre + "' ya existe.");
                        return;
                    }
                    aulaModificar.setNombre(nuevoNombre);
                    modificado = true;
                    System.out.println("Nombre actualizado.");
                }
                break;
            case 2:
                while (true) {
                    try {
                        int nuevaCap = validaciones.validarEntero("Nueva capacidad: ");
                        aulaModificar.setCapacidad(nuevaCap);
                        modificado = true;
                        System.out.println("Capacidad actualizada.");
                        break;
                    } catch (exception.AulaInvalidaException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                break;
            case 3:
                System.out.println("1. TEORICA\n2. LABORATORIO\n3. AUDITORIO");
                int tipoInt = validaciones.validarEntero("Nuevo tipo: ");
                TipoAula tipo = null;
                switch (tipoInt) {
                    case 1:
                        tipo = TipoAula.TEORICA;
                        break;
                    case 2:
                        tipo = TipoAula.LABORATORIO;
                        break;
                    case 3:
                        tipo = TipoAula.AUDITORIO;
                        break;
                    default:
                        tipo = null;
                        break;
                }
                ;
                if (tipo != null) {
                    aulaModificar.setTipoAula(tipo);
                    modificado = true;
                    System.out.println("Tipo de aula actualizado.");
                } else {
                    System.out.println("Opción inválida, tipo no modificado.");
                }
                break;
            case 4:
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Opción inválida.");
                break;
        }

        if (modificado) {
            persistencia.guardarTodoCSV(aulas, null);
            System.out.println("Aula modificada exitosamente.");
        }
    }

    public void listarAulasPorId() {
        if (aulas.isEmpty()) {
            System.out.println("No hay aulas...");
            return;
        }

        aulas.sort(Comparator.comparing(Aula::getCodigoAula));

        System.out.println("\n====== Aulas ======");
        aulas.forEach(a -> System.out.println(
                "[" + a.getCodigoAula() + "] " + a.getNombre()
                + " | Capacidad: " + a.getCapacidad()
                + " | Tipo: " + a.getTipoAula()
        ));
    }

    // BUSCAR AULA POR CÓDIGO (para verificar existencia de aulas)
    public Aula buscarAulaPorCodigo(int codigo) {
        return aulas.stream().filter(a -> a.getCodigoAula() == codigo).findFirst().orElse(null);
    }

    public List<Aula> getAulas() {
        return aulas;
    }
}
