/*
 * Copyright 2012 Sadig <http://www.sadig.com>. All rights reserved.
 */
package br.unisinos.lb2.cloudbox.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que contém apenas informações SOBRE o arquivo. Não contém os bytes
 * @author Leandro Tonietto <ltonietto@unisinos.br>
 * 
 */
public class Metadados {
    
        private int     idRegistro; // o numero de identificação
	private String  nome; //Nome do arquivo
	private Usuario usuario; // Usuario que criou, mas nos registros estara o idUsuario
	private int     tamanho; // Tamanho do arquivo
	private Date    criacao; //data criado
	private Date    envio; //ultima data de alteração
	private String  localFis; //local fisico do arquivo
	
	public Metadados(int id, String arqNome, Usuario usuario, int arqTama, Date dtCriado, Date dtEnvio, String local){
		this.idRegistro = id;
		this.nome       = arqNome; 
		this.usuario    = usuario; 
		this.tamanho    = arqTama;
		this.criacao    = dtCriado; 
		this.envio      = dtEnvio;
		this.localFis   = local;
	}
        
        public Boolean confirmaMetadados(int idReg, Usuario usuario){
            
            if (this.usuario.equals(usuario) && this.idRegistro == idReg) {             
                return true;
            }else{
                return false;
            }
            
        }
        
        public Boolean confirmaMetadados(int idReg, String nomeArq, Usuario usuario){
            
            if (this.usuario.equals(usuario) && this.nome.equalsIgnoreCase(nomeArq) && this.idRegistro == idReg) {             
                return true;
            }else{
                return false;
            }
            
        }
        
        public Boolean confirmaMetadados( String nomeArq, Usuario usuario){
            
            if (this.usuario.equals(usuario) && this.nome.equalsIgnoreCase(nomeArq)) {             
                return true;
            }else{
                return false;
            }
            
        }
        
        public Boolean confirmaMetadados(Usuario usu){
            
            if (this.usuario.equals(usu)) {             
                return true;
            }else{
                return false;
            }
            
        }
        
        public Metadados getMetadados(Usuario usuario){
            boolean existe = confirmaMetadados(usuario);
            
            if (existe) {
                return new Metadados(this.idRegistro, this.nome, this.usuario, this.tamanho, this.criacao, this.envio, this.localFis);
            }else{
                return null;
            }
        
        }
        
        public void updateMetadados(Metadados meta){
            this.tamanho = meta.getTamanho();
            this.envio   = meta.getEnvio();
        
        }

    public Date getCriacao() {
        return criacao;
    }

    public Date getEnvio() {            
        return envio;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public String getLocalFis() {
        return localFis;
    }

    public String getNome() {
        return nome;
    }

    public int getTamanho() {
        return tamanho;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setLocalFis(String localFis) {
        this.localFis = localFis;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }
    
    
        
        
	
}
