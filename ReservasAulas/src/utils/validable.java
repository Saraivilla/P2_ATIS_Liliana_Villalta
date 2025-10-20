/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import utils.exception;

/**
 *
 * @author Sarai Villalta
 */
public interface validable {

    boolean validar() throws exception.ReservaInvalidaException;
}
