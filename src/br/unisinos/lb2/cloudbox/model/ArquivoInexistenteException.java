/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.lb2.cloudbox.model;

/**
 *
 * @author wins
 */
public class ArquivoInexistenteException extends Exception{

    public ArquivoInexistenteException() {
        super("O arquivo que deseja não existe.");
    }
    
    
    
}
