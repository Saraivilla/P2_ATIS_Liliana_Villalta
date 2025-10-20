/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservasaulas;

import utils.exception;
import enums.TipoAula;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author Sarai Villalta
 */
public class ReservaPractica extends Reserva {

    public ReservaPractica(String codigoReserva, Aula aula, LocalDate fecha, LocalTime horaInicioReserva, LocalTime horaFinReserva, String responsableReserva, String estado) {
        super(codigoReserva, aula, fecha, horaInicioReserva, horaFinReserva, responsableReserva, estado);
    }

    //Validaciones para practica (horario predefinido y extrictamente reserva de practicas)
    @Override
    public boolean validar() throws exception.ReservaInvalidaException {
        if (aula.getTipoAula() != TipoAula.LABORATORIO) {
            throw new exception.ReservaInvalidaException("La reserva de practicas debe hacerse en aula de laboratorio.");
        }

        if (horaInicioReserva.isBefore(LocalTime.of(7, 0))) {
            throw new exception.ReservaInvalidaException("No se puede reservar laboratorio antes de las 7:00AM.");
        }
        if (horaFinReserva.isAfter(LocalTime.of(13, 00))) {
            throw new exception.ReservaInvalidaException("La reserva del laboratorio debe finalizar antes de la 1:00PM.");
        }

        return true;
    }
}
