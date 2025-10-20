/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservasaulas;

import utils.exception;
import utils.validable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Sarai Villalta
 */
public abstract class Reserva implements validable {

    //Correlativo "manual" (autoincrementable) para las reservas
    private static int correlativoReserva = 0;

    //Atributos
    protected String codigoReserva;
    protected Aula aula;
    protected LocalDate fecha;
    protected LocalTime horaInicioReserva;
    protected LocalTime horaFinReserva;
    protected String responsableReserva;
    protected String estado;

    //Constructor
    public Reserva(String codigoReserva, Aula aula, LocalDate fecha, LocalTime horaInicioReserva, LocalTime horaFinReserva, String responsableReserva, String estado) {
        this.codigoReserva = codigoReserva;
        this.aula = aula;
        this.fecha = fecha;
        this.horaInicioReserva = horaInicioReserva;
        this.horaFinReserva = horaFinReserva;
        this.responsableReserva = responsableReserva;
        this.estado = estado;
    }

    public abstract boolean validar() throws utils.exception.ReservaInvalidaException;

    //Validar horarios solapados (coincidentes)
    public boolean solapHorarios(Reserva otra) {
        return !horaFinReserva.isBefore(otra.horaInicioReserva)
                && !otra.horaFinReserva.isBefore(horaInicioReserva);
    }

    //Convierte estado a Historico si la fechaFin/horaFin de la reserva ya pasó
    public void actualizarEstadoHistorico() {
        if (estado.equals("ACTIVA")
                && (fecha.isBefore(LocalDate.now())
                || (fecha.isEqual(LocalDate.now()) && horaFinReserva.isBefore(LocalTime.now())))) {
            estado = "HISTORICO";
        }
    }

    //Getters y Setters
    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicioReserva() {
        return horaInicioReserva;
    }

    public void setHoraInicioReserva(LocalTime horaInicioReserva) {
        this.horaInicioReserva = horaInicioReserva;
    }

    public LocalTime getHoraFinReserva() {
        return horaFinReserva;
    }

    public void setHoraFinReserva(LocalTime horaFinReserva) {
        this.horaFinReserva = horaFinReserva;
    }

    public String getUsuarioReserva() {
        return responsableReserva;
    }

    public void setUsuarioReserva(String responsableReserva) {
        this.responsableReserva = responsableReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public static int getNuevoCorrelativoReserva() {
        return ++correlativoReserva;
    }

    public String getCorrelativoReserva() {
        return codigoReserva;
    }

    public void setCorrelativoReservas(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

}
