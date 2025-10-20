/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Sarai Villalta
 */
public class exception extends Exception {

    /**
     * Creates a new instance of <code>exception</code> without detail message.
     */
    public exception() {
    }

    /**
     * Constructs an instance of <code>exception</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    
    //Excepciones personalizadas
    public static class ReservaInvalidaException extends Exception {

        public ReservaInvalidaException(String mensaje) {
            super(mensaje);
        }
    }
    
     public static class AulaInvalidaException extends Exception {

        public AulaInvalidaException(String mensaje) {
            super(mensaje);
        }
    }
}
