/*
 * Copyright 2012 Sadig <http://www.sadig.com>. All rights reserved.
 */
package br.unisinos.lb2.cloudbox.model;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Servidor que armazena bytes dos arquivos do usuário
 *
 * @author Leandro Tonietto <ltonietto@unisinos.br>
 */
public class SRD {

    private BufferedReader in = null;
    private BufferedWriter out = null;
    private String[] separa;
    private FileReader fr;
    private FileWriter fw;
    private ArrayList<Arquivo> arquivos;
    private FileOutputStream fos;
    private BufferedOutputStream oos;
    private FileInputStream fis;
    private BufferedInputStream ois;

    public SRD() {
        arquivos = new ArrayList<Arquivo>();
    }

    public void addArquivo(Metadados meta, byte[] b, int pos) {
        arquivos.add(pos, new Arquivo(meta, b));
    }

    public void addArquivo(Arquivo arquivo, int pos) {
        arquivos.add(pos, arquivo);
    }

    public Arquivo getArquivo(int pos) {
        return arquivos.get(pos);
    }

    public Arquivo getArquivo(Metadados meta) {
        for (int i = 0; i < arquivos.size(); i++) {
            if (arquivos.get(i).getMetadados().equals(meta)) {
                return arquivos.get(i);
            }
        }

        return null;
    }

    public void removeArquivo(Metadados meta) throws FileNotFoundException, IOException {
        int pos = 0;
        boolean passou = false;
        for1:
        for (int i = 0; i < arquivos.size(); i++) {
            if (arquivos.get(i).getMetadados().equals(meta)) {
                pos = i;
                passou = true;
                break for1;
            }
        }

        if (passou) {
            File fl;
            fl = new File(arquivos.get(pos).getMetadados().getLocalFis());
            fl.delete();
            arquivos.remove(pos);
        }
    }

    /**
     * @author erico.medeiros
     *
     * @param servArq - Servidor de arquivos (SRD1,SRD2,SRD3)
     * @param meta - Lista de metadados, já populada, para verificação e
     * cadastro de arquivos
     * @param path
     *
     * Verificar quando que for um servidor ou outro, por isso tem que ter uma
     * String com o caminho para o arquivo do servidor, verificar também para
     * quando for a primeira vez que o programa esta rodando no computador.
     *
     * Metodo para carregar aquivos para o servido....
     *
     */
    public void updateNewArquivo(Metadados meta, byte[] b, Usuario usuario) {
        for (int i = 0; i < arquivos.size(); i++) {
            if (arquivos.get(i).getMetadados().confirmaMetadados(meta.getIdRegistro(), usuario)) {
                arquivos.get(i).getMetadados().updateMetadados(meta);
                arquivos.get(i).setBytes(b);
            }

        }

    }

    public int getNumbArquivo() {
        return arquivos.size();
    }

    public void uploadRecordedArquivos(Metadados metadados)
            throws FileNotFoundException,
            IOException {

        try {
            File f;
            byte[] contArq;
            f = new File(metadados.getLocalFis());
            fis = new FileInputStream(f);
            ois = new BufferedInputStream(fis);
            contArq = new byte[fis.available()];
            ois.read(contArq);
            arquivos.add(new Arquivo(metadados, contArq));

        } finally {
            ois.close();
        }

    }

    public void getUsuarios(ArrayList<Usuario> usuarios) throws FileNotFoundException, IOException {
        String linha;

        try {
            fr = new FileReader("./usuarios.csv");
            in = new BufferedReader(fr);
            linha = in.readLine();

            for (int i = 0; linha != null; i++) {
                separa = linha.split(";");
                usuarios.add(i, new Usuario(Integer.parseInt(separa[0]),
                        separa[1],
                        separa[2],
                        separa[3],
                        separa[4]));
                linha = in.readLine();

            }

        } finally {

            in.close();
        }
    }

    public void getMetadados(ArrayList<Usuario> usuarios,
            ArrayList<Metadados> metadados) throws
            FileNotFoundException,
            IOException,
            ParseException {

        String linha;
        Integer lrUsu = null;
        int idUsu;
        try {
            fr = new FileReader("./metadados.csv");
            in = new BufferedReader(fr);

            linha = in.readLine();
            for (int i = 0; linha != null; i++) {
                separa = linha.split(";");
                idUsu = Integer.parseInt(separa[2]);

                for (int j = 0; j < usuarios.size(); j++) {
                    if (usuarios.get(j).veriIdUsuario(idUsu)) {
                        lrUsu = j;
                        break;
                    }
                }

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date dt1 = formatter.parse(separa[4]);
                Date dt2 = formatter.parse(separa[5]);


                metadados.add(i, new Metadados(Integer.parseInt(separa[0]),
                        separa[1],
                        usuarios.get(lrUsu),
                        Integer.parseInt(separa[3]),
                        dt1,
                        dt2,
                        separa[6]));

                linha = in.readLine();



            }
        } finally {
            in.close();
        }
    }

    public void recordUsuariosArqui(ArrayList<Usuario> usuarios) throws IOException {
        String linha;

        try {

            fw = new FileWriter("./usuarios.csv");
            out = new BufferedWriter(fw);

            for (int i = 0; i < usuarios.size(); i++) {
                linha = usuarios.get(i).getIdUsuario() + ";"
                        + usuarios.get(i).getNome() + ";"
                        + usuarios.get(i).getUsuario() + ";"
                        + usuarios.get(i).getSenha() + ";"
                        + usuarios.get(i).getLocal() + ";";
                out.write(linha);
                out.newLine();
            }


        } finally {
            out.close();
        }

    }

    public void recordMetadadosArqui(ArrayList<Metadados> metadados) throws IOException {
        String linha;

        try {

            fw = new FileWriter("./metadados.csv");
            out = new BufferedWriter(fw);

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 0; i < metadados.size(); i++) {
                linha = metadados.get(i).getIdRegistro() + ";"
                        + metadados.get(i).getNome() + ";"
                        + metadados.get(i).getUsuario().getIdUsuario() + ";"
                        + metadados.get(i).getTamanho() + ";"
                        + formatter.format(metadados.get(i).getCriacao()) + ";"
                        + formatter.format(metadados.get(i).getEnvio()) + ";"
                        + metadados.get(i).getLocalFis() + ";";
                out.write(linha);
                out.newLine();
                out.flush();
            }

        } finally {
            out.close();

        }

    }

    /**
     *
     * @throws IOException
     */
    public void recordArquivos() throws IOException {
        File fl;
        for (int i = 0; i < arquivos.size(); i++) {
            fl = new File(arquivos.get(i).getMetadados().getLocalFis());
            fos = new FileOutputStream(fl);
            oos = new BufferedOutputStream(fos);
            oos.write(arquivos.get(i).getBytes());
            oos.flush();
            oos.close();

        }

        if (oos != null) {
            oos.close();
        }

    }

    public void encerrarServidor() throws IOException {

        this.recordArquivos();

        if (fis != null) {
            fis.close();
        }
        if (fos != null) {
            fos.close();
        }
        if (fr != null) {
            fr.close();
        }
        if (fw != null) {
            fw.close();
        }
        if (in != null) {
            in.close();
        }
        if (ois != null) {
            ois.close();
        }
        if (oos != null) {
            oos.close();
        }
        if (out != null) {
            out.close();
        }


    }
}
