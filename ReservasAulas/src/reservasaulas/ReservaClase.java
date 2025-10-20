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
public class ReservaClase extends Reserva {

    public ReservaClase(String codigoReserva, Aula aula, LocalDate fecha, LocalTime horaInicioReserva, LocalTime horaFinReserva, String responsableReserva, String estado) {
        super(codigoReserva, aula, fecha, horaInicioReserva, horaFinReserva, responsableReserva, estado);
    }

    //Validaciones para clase (horario predefinido y extrictamente reserva clases)
    @Override
    public boolean validar() throws exception.ReservaInvalidaException {
        if (aula.getTipoAula() != TipoAula.TEORICA) {
            throw new exception.ReservaInvalidaException("La reserva de clases debe hacerse en aula teórica.");
        }
        if (horaInicioReserva.isBefore(LocalTime.of(7, 0))) {
            throw new exception.ReservaInvalidaException("No se puede reservar aula teórica antes de las 7:00AM.");
        }
        if (horaFinReserva.isAfter(LocalTime.of(18, 30))) {
            throw new exception.ReservaInvalidaException("La reserva de clase debe finalizar antes de las 6:30PM.");
        }
        return true;
    }

}
