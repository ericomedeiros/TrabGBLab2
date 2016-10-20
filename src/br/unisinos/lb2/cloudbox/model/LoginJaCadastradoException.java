/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.lb2.cloudbox.model;

/**
 *
 * @author Unisinos
 */
public class LoginJaCadastradoException extends Exception{
    
    public LoginJaCadastradoException(){
        super("O login informado já está cadastrado.");
    }
    
}
