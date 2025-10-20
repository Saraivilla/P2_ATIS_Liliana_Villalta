/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservasaulas;

import utils.exception;
import enums.TipoAula;

/**
 *
 * @author Sarai Villalta
 */
public class Aula {

    //Atributos
    private int codigoAula;
    private String nombre;
    private int capacidad;
    private TipoAula tipoAula;

    //Constructor
    public Aula(int codigoAula, String nombre, int capacidad, TipoAula tipoAula) {
        this.codigoAula = codigoAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipoAula = tipoAula;
    }

    //Getters y Setters
    public int getCodigoAula() {
        return codigoAula;
    }

    public void setCodigoAula(int codigoAula) {
        this.codigoAula = codigoAula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    //Valida que la capacidad no sea negativa
    public void setCapacidad(int capacidad) throws exception.AulaInvalidaException {
        if (capacidad < 0) {
            throw new exception.AulaInvalidaException("La capacidad no puede ser un número negativo");
        }
        this.capacidad = capacidad;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }
}
