/*
 * Copyright 2012 Sadig <http://www.sadig.com>. All rights reserved.
 */
package br.unisinos.lb2.cloudbox.model;



/**
 * Entidade que representa um usuário válido no servidor
 * @author Leandro Tonietto <ltonietto@unisinos.br>
 */
public class Usuario {
    
    private int      idUsuario;
    private String   nome;
    private String   local;
    private String   usuario;
    private String   senha;

    public Usuario(int id, String nome,String usuario, String senha, String local ) {
        this.idUsuario = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.local = local;
    }
    
    public boolean confirmaLogin(String user, String pass){
        
        if (this.usuario.equals(user) && this.senha.equals(pass)) {
            return true;
        } else {
            return false;
        }
        
    }
    
    public boolean veriIdUsuario(int idUsu){
        if(idUsu == this.idUsuario){
            return true;
        }else{
           return false;
        }
    }
    
    public boolean veriLoginUsuario(String login){
        if(login.equals(this.usuario)){
            return true;
        }else{
           return false;
        }
    }
    
    /**
	 * @return the nome
	 */
	protected String getNome() {
		return nome;
	}

	/**
	 * @return the local
	 */
	protected String getLocal() {
		return local;
	}

	/**
	 * @return the usuario
	 */
	protected String getUsuario() {
		return usuario;
	}

	/**
	 * @return the senha
	 */
	protected String getSenha() {
		return senha;
	}

        public int getIdUsuario() {
            return idUsuario;
        }
        
        

	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!this.nome.equals(other.getNome())) {
            return false;
        }
        if (!this.local.equals( other.getLocal())) {
            return false;
        }
        if (!this.usuario.equals( other.getUsuario())) {
            return false;
        }
        if (!this.senha.equals( other.getSenha())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "nome=" + nome + ", local=" + local + ", usuario=" + usuario + ", senha=" + senha + '}';
    }
    
    
  
	
}
