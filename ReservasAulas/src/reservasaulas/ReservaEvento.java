/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservasaulas;

import utils.exception;
import enums.TipoEvento;
import enums.TipoAula;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author Sarai Villalta
 */
public class ReservaEvento extends Reserva {

    private TipoEvento tipoEvento;

    public ReservaEvento(TipoEvento tipoEvento, String codigoReserva, Aula aula, LocalDate fecha, LocalTime horaInicioReserva, LocalTime horaFinReserva, String responsableReserva, String estado) {
        super(codigoReserva, aula, fecha, horaInicioReserva, horaFinReserva, responsableReserva, estado);
        this.tipoEvento = tipoEvento;
    }

    //Validaciones para evento (horario predefinido y extrictamente reserva eventos)
    @Override
    public boolean validar() throws exception.ReservaInvalidaException {
        if (aula.getTipoAula() != TipoAula.AUDITORIO) {
            throw new exception.ReservaInvalidaException("La reserva de eventos debe hacerse en auditorios.");
        }

        if (horaInicioReserva.isBefore(LocalTime.of(9, 0))) {
            throw new exception.ReservaInvalidaException("No se puede reservar auditorio antes de las 9:00AM.");
        }
        if (horaFinReserva.isAfter(LocalTime.of(15, 00))) {
            throw new exception.ReservaInvalidaException("La reserva del evento debe finalizar antes de las 3:00PM.");
        }

        return true;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

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

}
