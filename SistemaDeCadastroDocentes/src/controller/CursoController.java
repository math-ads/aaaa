package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.edu.fateczl.Lista;
import model.Curso;
import model.Professor;

public class CursoController implements ActionListener {
	
	private JTextField tfCursoCodigo;
	private JTextField tfCursoNome;
	private JTextField cbCursoArea;
	private JTextArea taCursoLista;

	public CursoController(JTextField tfCursoCodigo, JTextField tfCursoNome, JTextField cbCursoArea,
			JTextArea taCursoLista) {

		this.tfCursoCodigo = tfCursoCodigo;
		this.tfCursoNome = tfCursoNome;
		this.cbCursoArea = cbCursoArea;
		this.taCursoLista = taCursoLista;
	}
	public CursoController() {
		
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Salvar")) {
			try {
				cadastro();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Buscar")) {
			try {
				busca();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Excluir")) {
			excluir();}
	}

	private void excluir() {
		// TODO Auto-generated method stub
		
	}

	private void busca() throws IOException {
		Curso curso = new Curso();
		curso.setCodigoCurso(tfCursoCodigo.getText());
		curso = buscaCurso(curso);
		
		if (curso.getNomeCurso() != null) {
			taCursoLista.setText("Curso: " + curso.getNomeCurso() + " - Código: " + curso.getCodigoCurso() + " - Área do Conhecimento: " + curso.getAreaConhecimento());
		} else {
			taCursoLista.setText("Curso não encontrado!");
			
		}
	}
	
	//BUSCAR CURSO ATRAVÉS DO CÓDIGO DO CURSO
	private Curso buscaCurso(Curso curso) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File arq = new File(path, "curso.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader (fis); 
			BufferedReader buffer = new BufferedReader (isr);
			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				if (vetLinha[0].equals(curso.getCodigoCurso())) {
					curso.setNomeCurso(vetLinha[1]);
					curso.setAreaConhecimento(vetLinha[2]);
					break; 
				}
				linha = buffer.readLine();
				
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return curso;
	}

	private void cadastro() throws Exception {
		
		Curso curso = new Curso();
		curso.setCodigoCurso(tfCursoCodigo.getText());
		curso.setNomeCurso(tfCursoNome.getText());
		curso.setAreaConhecimento(cbCursoArea.getText());
		 
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "curso.csv"); 
		boolean cadastrado = cursoCadastrado(arq, curso.getCodigoCurso());
		
		if(cadastrado==true) {
			atualizaCurso(curso);
		}
		else {
	
			cadastraCurso(curso.toString());
		}
		
		tfCursoCodigo.setText("");
		tfCursoNome.setText("");
		cbCursoArea.setText("");	
		
	}

	private void atualizaCurso(Curso curso) throws Exception {
		// TODO Auto-generated method stub
		 String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	        File arq = new File(path, "curso.csv");
	        File auxArq= new File(path, "cursoAux.csv");
	        if(arq.exists()&&arq.isFile()) {
	}BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
	BufferedWriter pw = new BufferedWriter(new FileWriter(auxArq,true));
	Lista<Curso> listaCurso = new Lista<Curso>() ;    	
    String linha;
    while ((linha = fw.readLine()) != null) {
        String[] vetLinha = linha.split(";");
        Curso cursoAux = new Curso();
        if(!vetLinha[0].equals(curso.getCodigoCurso())) {
        	{if(listaCurso.isEmpty()) {
          		cursoAux.setCodigoCurso(vetLinha[0]);
          		cursoAux.setNomeCurso(vetLinha[1]);
          		cursoAux.setAreaConhecimento(vetLinha[2]);
          		
          		
              listaCurso.addFirst(curso);}
          	else {
          		cursoAux.setCodigoCurso(vetLinha[0]);
          		cursoAux.setNomeCurso(vetLinha[1]);
          		cursoAux.setAreaConhecimento(vetLinha[2]);
          		
          		
              listaCurso.addLast(curso);}      
    		pw.write(linha+"\r\n");
        }} else {
            	if(listaCurso.isEmpty()) {
     
                  listaCurso.addFirst(curso);}
              	else {
                  listaCurso.addLast(curso);}      
        		pw.write(curso.toString()+"\r\n");
        }
        	
        
    }
    	pw.flush();
		pw.close();
		fw.close();
		// Substitui o arquivo original pelo auxiliar
        arq.delete();
        auxArq.renameTo(arq);
		taCursoLista.setText("Curso atualizado");}

	
	

	//CADASTRAR NOVO CURSO
	private void cadastraCurso(String csvCurso) throws Exception {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "curso.csv"); 
		//monta a lista para atalizar
		Lista<Curso> listaCurso= montarLista();
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
		BufferedWriter pw = new BufferedWriter(new FileWriter(arq,true));
		 String[] vetLinha = csvCurso.split(";");
         Curso curso= new Curso();
         if(listaCurso.isEmpty()) {
      		curso.setCodigoCurso(vetLinha[0]);
      		curso.setNomeCurso(vetLinha[1]);
      		curso.setAreaConhecimento(vetLinha[2]);
      		
      		
          listaCurso.addFirst(curso);}
      	else {
      		curso.setCodigoCurso(vetLinha[0]);
      		curso.setNomeCurso(vetLinha[1]);
      		curso.setAreaConhecimento(vetLinha[2]);
      		
      		
          listaCurso.addLast(curso);}
        
		pw.write(csvCurso+"\r\n");
		
		pw.flush();
		pw.close();
		fw.close();
		taCursoLista.setText("Curso cadastrado com sucesso!");
	}
		
		
		
	
	
	//CHECAR SE O CURSO JÁ ESTÁ CADASTRADO ATRAVÉS DO CÓDIGO DO CURSO
	public boolean cursoCadastrado(File arq, String cursoCodigo) throws IOException {
		if (!arq.exists()) {
	        return false; 
	    }

	    try (BufferedReader ler = new BufferedReader(new FileReader(arq))) {
	        String linha;
	        while ((linha = ler.readLine()) != null) {
	            String[] vetLinha = linha.split(";");
	            if (vetLinha[0].equals(cursoCodigo)) {
	                return true; 
	            }
	        }
	    }
	    return false; 
	}
	
	public static Lista<Curso> montarLista() throws Exception {
		   String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	        File arq = new File(path, "curso.csv");
	        Lista<Curso> listaCurso = new Lista<>();
	        if (!arq.exists()) {
	            // Cria o arquivo vazio, caso ele não exista
	            arq.createNewFile();
	            System.out.println("Arquivo curso.csv criado.");
	        }
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq))); 
     String linha;
		 while ((linha = fw.readLine()) != null) {
          String[] vetLinha = linha.split(";");
            Curso curso = new Curso();
			if(listaCurso.isEmpty()) {
     		curso.setCodigoCurso(vetLinha[0]);
     		curso.setNomeCurso(vetLinha[1]);
     		curso.setAreaConhecimento(vetLinha[2]);
     		
     		
         listaCurso.addFirst(curso);}
     	else {
     		curso.setCodigoCurso(vetLinha[0]);
     		curso.setNomeCurso(vetLinha[1]);
     		curso.setAreaConhecimento(vetLinha[2]);
     		
     		
         listaCurso.addLast(curso);}
           
               
           }
		 
		fw.close();
		
		return listaCurso;
}
	
}
