package br.unisinos.lb2.cloudbox.model;

import br.unisinos.lb2.cloudbox.model.Metadados;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

public class ListaModel extends AbstractTableModel {
	
    private String cols[] = {"Nome", "Envio", "Tamanho", "SRD", "Criação"};
    private Metadados arquivos[];

    public ListaModel(Metadados arquivos[]) {
        this.arquivos = arquivos;
    }

    @Override
    public String getColumnName(int col) {
        if(col < 7){
            return cols[col];
        }
        return super.getColumnName(col);
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0: break;
            case 4: return String.class;
            case 1: return Long.class;
            case 3: break;
            case 5: return Date.class;
        }
        return super.getColumnClass(col);
    }

    @Override
    public int getRowCount() {
		 if(arquivos == null)
			 return 0;
		 return arquivos.length;
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
	 
	 public void setListaArquivos(Metadados arquivos[]){
		 this.arquivos = arquivos;
                 this.inserirArquivosTabela();
	 }

    @Override
    public Object getValueAt(int row, int col) {
        String[] separa;
        
        if(arquivos == null){
            return null;
        }
        Metadados meta = arquivos[row];
        separa = meta.getLocalFis().split("/");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        switch (col) {
            case 0:
                    return meta.getNome();
            case 1:
                    return formatter.format(meta.getEnvio());
            case 2:
                    return meta.getTamanho();
            case 3:
                    return separa[1];
            case 4:
                    return formatter.format(meta.getCriacao());
        }
        return null;
    }
    
     public void inserirArquivosTabela(){
        //objetos temporários
        Metadados temp;
        String[] separa;
        //vai inserir os arquivos dentro da tabela
        
        //vai percorrer o vetor
        for(int i = 0; i < arquivos.length; i++){
            //vai receber um Metadado
            temp = arquivos[i];
            separa = temp.getLocalFis().split("/");
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            
            //vai inserir os valores na tabela
            //"Nome" - valor, linha , coluna
            this.setValueAt(temp.getNome(), i, 0);
            //"Envio"
            this.setValueAt(formatter.format(temp.getEnvio()), i, 1);
            //"Tamanho"
            this.setValueAt(temp.getTamanho(), i, 2);
            //"SRD"
            this.setValueAt(separa[1], i, 3);
            //"Criação"
            this.setValueAt(formatter.format(temp.getCriacao()), i, 4);
            
            
        }
        
        //vai atualizar a tabela, vai identificar que os valores mudaram
        this.fireTableDataChanged();
    }
	
}
