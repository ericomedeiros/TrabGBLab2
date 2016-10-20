/*
 * Copyright 2012 Sadig <http://www.sadig.com>. All rights reserved.
 */
package br.unisinos.lb2.cloudbox.model;

/**
 * Classe que representa um arquivo do usuário. Contém os metadados e os bytes do arquivo.
 * @author Leandro Tonietto <ltonietto@unisinos.br>
 */
public class Arquivo {
	private Metadados metadados;
	private byte[] bytes;

    public Arquivo(Metadados metadados, byte[] bytes) {
        this.metadados = metadados;
        this.bytes = bytes;
    }

	public Metadados getMetadados() {
		return metadados;
	}

	public byte[] getBytes() {
		return bytes;
	}

    public void setMetadados(Metadados metadados) {
        this.metadados = metadados;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
        
        

}
