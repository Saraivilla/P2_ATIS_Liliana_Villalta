/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author Sarai Villalta
 */
public class validaciones {

    private static final Scanner entrada = new Scanner(System.in);

    //VALIDACIONES DE ENTRADAS
    //intenta convertir entrada en entero
    public static int validarEntero(String entradaInt) {
        while (true) {
            System.out.print(entradaInt);
            try {
                int num = Integer.parseInt(entrada.nextLine().trim());
                return num;
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número entero.");
            }
        }
    }

    //valida que el campo no esté vacío
    public static String validarString(String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            valor = entrada.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Error: el campo no puede estar vacío.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    //valida que la fecha se ingrese en formato AAAA-MM-DD
    public static LocalDate validarFecha(String entradaFecha) {
        while (true) {
            System.out.print(entradaFecha);
            try {
                return LocalDate.parse(entrada.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Por favor ingrese fecha con formato AAAA-MM-DD.");
            }
        }
    }

    //valida que la hora se ingrese en formato AAAA-MM-DD
    public static LocalTime validarHora(String entradaHora) {
        while (true) {
            System.out.print(entradaHora);
            try {
                return LocalTime.parse(entrada.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Por favor ingrese hora con formato HH:MM (24h).");
            }
        }
    }

    //Valida que las opciones del menú estén dentro del rango definido
    public static int validarOpcion(String mensaje, int min, int max) {
        while (true) {
            int opcion = validarEntero(mensaje);
            if (opcion >= min && opcion <= max) {
                return opcion;
            }
            System.out.println("Opción inválida. Debe estar entre " + min + " y " + max + ".");
        }
    }
}
