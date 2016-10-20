package br.unisinos.lb2.cloudbox.model;

public class LoginIvalidoException extends Exception{

	public LoginIvalidoException() {
		super("Login Invalido, usuario ou senha incorretos.");
	}

}
