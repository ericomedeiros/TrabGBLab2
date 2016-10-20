/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.lb2.cloudbox.model;

/**
 *
 * @author wins
 */
public class SemArquivosGravadosException extends Exception {

    public SemArquivosGravadosException() {
        super("Você não possui nenhum arquivo registrado.");
    }
    
}
