/*
 * Copyright 2012 Sadig <http://www.sadig.com>. All rights reserved.
 */
package br.unisinos.lb2.cloudbox.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Classe que representa o servidor central da aplica��o. � aqui que todas as 
 * opera��es devem acontecer, menos aquelas que s�o de interface com o usu�rio 
 * (leitura ou exibi��o de informa��es).
 * @author Leandro Tonietto <ltonietto@unisinos.br>
 */
public class SC {

	// deve manter uma lista de usu�rios, uma lista de metadados e uma 
        //lista de SRDs
        SRD                  SRD1 ;
        SRD                  SRD2 ;
        SRD                  SRD3 ;
        ArrayList<Metadados> metadados; 
        ArrayList<Usuario>   usuarios ;
        
	public SC() {
		// no construtor inicializar os SRDs.
		// carregar informa��es das listas usu�rios e metadados de 
                //arquivo para recuperar informa��es
		
            try {
                SRD1  = new SRD(); 
                SRD2  = new SRD(); 
                SRD3  = new SRD(); 
		metadados = new ArrayList<Metadados>();
		usuarios  = new ArrayList<Usuario>();
                // pega arquivos do .csv
                SRD1.getUsuarios(usuarios);
                
                if(!usuarios.isEmpty()){
                    SRD1.getMetadados(usuarios, metadados);
                
                    if (!metadados.isEmpty()) {
                        for (int i = 0; i < metadados.size(); i++) {

                            if (metadados.get(i).getLocalFis().contains("SRD1")){
                               SRD1.uploadRecordedArquivos(metadados.get(i));
                            }else if (metadados.get(i).getLocalFis().contains("SRD2")){
                               SRD2.uploadRecordedArquivos(metadados.get(i));
                            }else if (metadados.get(i).getLocalFis().contains("SRD3")){
                               SRD3.uploadRecordedArquivos(metadados.get(i));
                            }

                        }


                    }
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "N�o foi encontrado o"
                        + "arquivo(s) de registros, ao final da aplica��o ser�"
                        + " criado novos.","Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(SC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro de "
                        + "conver��o de dados.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
                
	
	}
	
	/**
	 * M�todo que tenta validar um login e senha na lista de usu�rios do 
         * servidor.
	 * @param login
	 * @param senha
	 * @return um objeto do tipo Usuario que representa o usu�rio com o 
         * @return login e senha passados como par�metro.
	 * @throws Exception retorna erro de login inv�lido
	 */
	public Usuario executarLogin(String login, String senha) throws LoginIvalidoException 
                                                             { 
        // TODO: implementar classe para LoginInvalidoException
		// TODO: fazer a valida��o do login e da senha contra os logins 
        //e senhas da lista de usu�rios. 
		// - se algum elemento da lista tiver a mesma "autentica��o", 
        //retorna o objeto;
            if (!usuarios.isEmpty()) {
                
                for (int i = 0; i < usuarios.size(); i++) {
			if (usuarios.get(i).confirmaLogin(login, senha)) {
				return usuarios.get(i);
			}
			
		}
                // - caso contr�rio, retorna exce��o de login inv�lido
                throw new LoginIvalidoException();
            }else{
                	
                JOptionPane.showMessageDialog(null,  "N�o existes " +
                               "usuarios cadastrados, por favor realize o " +
                               "primeiro cadastro.", "Error", JOptionPane.ERROR_MESSAGE);
                
                return null;
           }		
	}
	
	/**
	 * Cria um usu�rio e adiciona o mesmo na lista de usu�rios do servidor.
         * Retorna o objeto de <code>Usuario</code> que foi criado.
	 * @param nome
	 * @param login
	 * @param senha
	 * @param local
	 * @return retorna o registro do usu�rio criado
	 * @throws Exception caso algum erro inerente ao cadastramento seja 
         * detectado
	 */
	public Usuario criarContaUsuario(String nome, String login, 
                                         String senha, String local) throws LoginJaCadastradoException {
            
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).veriLoginUsuario(login)) {
                    throw new LoginJaCadastradoException();
                }
                
            }
            Usuario usu = new Usuario(usuarios.size(),nome,login,senha,local);
            usuarios.add(usu);
                             // TODO: implementar classe para erros de cadastro
            return usu;      // TODO: remover isto! Est� aqui apenas para 
                             //permitir a compila��o da classe...
	}
	
	/**
	 * Envia um arquivo do usu�rio para o servidor. Servidor recebe o 
         * arquivo verifica se � novo ou se atualiza��o, atualiza
	 * metadado na lista de metadados e envia os bytes para serem gravados 
         * em disco pelo SRD apropriadi=o. Neste m�todo � 
	 * feito o balanceamento de carga da ocupa��o dos servidores (SRDs).
	 * @param arquivo arquivo que ser� gravado no SRD e metadados no SC
	 * @param usuario usu�rio que est� logado e portanto � propriet�rio do 
         * arquivo
	 * @throws Exception retorna erro relacionado ao processo para ser 
         * tratado na interface.
	 */
	public void uploadArquivo(Arquivo arquivo, Usuario usuario){ 
                                                            
                // TODO: implementar classe para erros de envio
		// escolher melhor SRD para armazenar arquivo
            boolean existe = false;
            int     pos = 0;
            for1: for (int i = 0; i < metadados.size(); i++) {
                if (metadados.get(i).confirmaMetadados(arquivo.getMetadados().getNome(), 
                                                       usuario)) {
                    
                  existe = true;
                  pos = i;
                  break for1;
                }
                
            }
            
            if (existe) {
                
                metadados.get(pos).updateMetadados(arquivo.getMetadados());
                
                if (metadados.get(pos).getLocalFis().contains("SRD1")) {
                    SRD1.updateNewArquivo(metadados.get(pos), arquivo.getBytes(), usuario);
                }else if (metadados.get(pos).getLocalFis().contains("SRD2")) {
                    SRD2.updateNewArquivo(metadados.get(pos), arquivo.getBytes(), usuario);
                }else if (metadados.get(pos).getLocalFis().contains("SRD3")) {
                    SRD3.updateNewArquivo(metadados.get(pos), arquivo.getBytes(), usuario);
                }
                
                
                
            }else{
                int id ;
                if (metadados.size() > 0) {
                    id = metadados.get(metadados.size() - 1).getIdRegistro() + 1;
                }else{
                    id = 0;
                }
                
                arquivo.getMetadados().setIdRegistro(id);
                
                if(SRD1.getNumbArquivo() == SRD2.getNumbArquivo() && 
                   SRD1.getNumbArquivo() == SRD3.getNumbArquivo() &&
                   SRD2.getNumbArquivo() == SRD3.getNumbArquivo()){
                        
                        if (usuario.getLocal().equalsIgnoreCase("BR")) {
                            
                                arquivo.getMetadados().setLocalFis("./SRD1/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD1.addArquivo(arquivo, SRD1.getNumbArquivo());
                
                        }else if(usuario.getLocal().equalsIgnoreCase("EUA")){
                            
                                arquivo.getMetadados().setLocalFis("./SRD2/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD2.addArquivo(arquivo, SRD2.getNumbArquivo());

                        }else if(usuario.getLocal().equalsIgnoreCase("CN")){
                            
                                arquivo.getMetadados().setLocalFis("./SRD3/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD3.addArquivo(arquivo, SRD3.getNumbArquivo());

                        }else{
                                arquivo.getMetadados().setLocalFis("./SRD1/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD1.addArquivo(arquivo, SRD1.getNumbArquivo());
                        }
                       
                    
                }else{
                        if(SRD1.getNumbArquivo() <= SRD2.getNumbArquivo() && 
                           SRD1.getNumbArquivo() <= SRD3.getNumbArquivo()){

                                arquivo.getMetadados().setLocalFis("./SRD1/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD1.addArquivo(arquivo, SRD1.getNumbArquivo());

                        }else if(SRD2.getNumbArquivo() <= SRD3.getNumbArquivo()){
                                arquivo.getMetadados().setLocalFis("./SRD2/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD2.addArquivo(arquivo, SRD2.getNumbArquivo());
                        }else{
                                arquivo.getMetadados().setLocalFis("./SRD3/"+ usuario.getUsuario() + 
                                                  "_" + arquivo.getMetadados().getNome() + ".arq");
                                SRD3.addArquivo(arquivo, SRD3.getNumbArquivo());
                        }
                    }
                
                
                // gravar metadados na lista
                metadados.add(arquivo.getMetadados());
            }
            
            JOptionPane.showMessageDialog(null, "Upload realizado com sucesso.", "Upload", JOptionPane.INFORMATION_MESSAGE);
            
            
		// enviar arquivo (importante s�o os bytes) para SRD gravar 
                //em disco
		// Dicas:
		// - cuidar problema de arquivo que j� estava no servidor e 
                //deve ser sobrescrito. � mais indicado que ele volte 
		// para o SRD me que j� estava para evitar duplicidade de 
                //arquivos
		// - depois de escolher o melhoer SRd para gravar aqruivo, 
                //atualizar a propriedade dos metadados. Ela ser� utilizada
		// para saber de onde buscar o arquivo quando o usu�rio 
                //solicitar o download
	}
	
	/**
	 * Retorna um arquivo do servidor para ser utilizado pelo usu�rio
	 * @param nomeArquivo nome do arquivo a ser copiado
	 * @param usuario usu�rio que � propriet�rio do arquivo. No caso, o 
         * usu�rio logado.
	 * @return objeto de arquivo contendo os dados e os metadados
	 * @throws Exception erro caso o arquivo n�o esteja na lista ou algum 
         * outro erro inerente ao processo.
	 */
	public Arquivo downloadArquivo(String nomeArquivo, Usuario usuario) throws ArquivoInexistenteException 
                                                              {
                // TODO: implementar classe para erros de recebimento
            // localizad arquivo na lista de metadados e: 
             boolean existe = false;
            int     pos = 0;
            for1: for (int i = 0; i < metadados.size(); i++) {
                if (metadados.get(i).confirmaMetadados(nomeArquivo, usuario)) {
                  existe = true;
                  pos = i;
                  break for1;
                }
                
            }
            
            // - caso ele esteja na lista, solicitar leitura dos dados e 
            //retornar byte dentro de um objeto de Arquivo
            if (existe) {
                
                if (metadados.get(pos).getLocalFis().contains("SRD1")) {
                    if(SRD1.getArquivo(metadados.get(pos)) != null){
                    return SRD1.getArquivo(metadados.get(pos));
                    }
                }else if (metadados.get(pos).getLocalFis().contains("SRD2")) {
                     if(SRD2.getArquivo(metadados.get(pos)) != null){
                    return SRD2.getArquivo(metadados.get(pos));
                    }
                }else if (metadados.get(pos).getLocalFis().contains("SRD3")) {
                     if(SRD3.getArquivo(metadados.get(pos)) != null){
                    return SRD3.getArquivo(metadados.get(pos));
                    }
                }
                
            }

		// - caso n�o esteja na lista, retornar exce��o
		
            throw new ArquivoInexistenteException();
	}
	
	/**
	 * Retorna um array (lista) de metadados para ser exibido na interface.
	 * @param usuario propriet�rio dos arquivos
	 * @return lista dos metadados que o usu�rio tem no servidor
	 * @throws Exception erros inerentes ao processo
	 */
	public Metadados[] listaArquivos(Usuario usuario) throws SemArquivosGravadosException {
		// montar e retornar um array com apenas os metadados de 
                //arquivos que s�o do usu�rio logado
            int tam = 0;
            int contr = 0;
                for (int i = 0; i < metadados.size(); i++) {
                    if (metadados.get(i).confirmaMetadados(usuario)) {
                        tam++;
                    }
                }
                
                if (tam > 0) {
                    Metadados[] meta = new Metadados[tam];
                    for (int i = 0; i < metadados.size(); i++) {
                        if (metadados.get(i).confirmaMetadados(usuario)) {
                           meta[contr] = metadados.get(i).getMetadados(usuario);
                           contr++;
                        }
                    }
                    return meta; 
               }
                
               throw new SemArquivosGravadosException();
                
                 
        }
        
     /**
     *Fun��o que foi esquecida de ser definida... Por isso a criei
     * @param nomeArquivo
     * @param usuarioLogado
     */
    public void excluirArquivo(String nomeArquivo, Usuario usuario) throws ArquivoInexistenteException, FileNotFoundException, IOException{
        
            boolean existe = false;
            int     pos = 0;
            for1: for (int i = 0; i < metadados.size(); i++) {
                if (metadados.get(i).confirmaMetadados(nomeArquivo, usuario)) {
                  existe = true;
                  pos = i;
                  break for1;
                }
                
            }
            
            if (existe) {
                
                if (metadados.get(pos).getLocalFis().contains("SRD1")) {
                    SRD1.removeArquivo(metadados.get(pos));
                }else if (metadados.get(pos).getLocalFis().contains("SRD2")) {
                    SRD2.removeArquivo(metadados.get(pos));
                }else if (metadados.get(pos).getLocalFis().contains("SRD3")) {
                    SRD3.removeArquivo(metadados.get(pos));
                }
                
                metadados.remove(pos);
                
                JOptionPane.showMessageDialog(null, "Arquivo excluido com sucesso.", "Exclus�o", JOptionPane.INFORMATION_MESSAGE);
                
                return;
                
            }
            
            throw new ArquivoInexistenteException();
        
    }
	
	/**
	 * Deve ser invocado para persistir as listas: usu�rios e metadados. 
         * Estas listas devem ser lidas no construtor
	 * do SC. O nome dos arquivos s�o definidos pelos alunos.
	 * @throws Exception erros inerentes ao processo
	 */
	public void encerrar() throws IOException  {
            SRD1.recordUsuariosArqui(usuarios);
            SRD1.recordMetadadosArqui(metadados);
            SRD1.encerrarServidor();
            SRD2.encerrarServidor();
            SRD3.encerrarServidor();
		
	}
}
