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

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.edu.fateczl.Lista;
import model.Disciplina;
import model.Professor;

public class DisciplinaController implements ActionListener {
	
	private JTextField tfDisciplinaCodigo;
	private JTextField tfDisciplinaNome;
	private JComboBox<String> cbDisciplinaDias; // Tipo correto para JComboBox
	private JTextField tfDisciplinaHorario;
	private JTextField tfDisciplinaCarga; // Quantidade de horas
	private JTextField tfDisciplinaCurso; // Código do curso
	private JTextArea taDisciplinaLista;


	// Construtor atualizado
	public DisciplinaController(
		JTextField tfDisciplinaCodigo, 
		JTextField tfDisciplinaNome, 
		JTextField tfDisciplinaQuantHoras,
		JTextField tfDisciplinaHorario, 
		JTextField tfDisciplinaCarga,
		JTextField tfDisciplinaCurso,
		JTextArea taDisciplinaListaCursosOuDisciplinas,
		JComboBox<String> cbDisciplinaDias
	) {
		this.tfDisciplinaCodigo = tfDisciplinaCodigo;
		this.tfDisciplinaNome = tfDisciplinaNome;
		this.tfDisciplinaCarga = tfDisciplinaCarga;
		this.tfDisciplinaHorario = tfDisciplinaHorario;
		this.tfDisciplinaCarga = tfDisciplinaCarga;
		this.tfDisciplinaCurso = tfDisciplinaCurso;
		this.taDisciplinaLista = taDisciplinaListaCursosOuDisciplinas;
		this.cbDisciplinaDias = cbDisciplinaDias; // Atribui o JComboBox
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
		if (cmd.equals("Excluir")) {
			try {
				busca();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
	}

	private void busca() throws IOException {
		Disciplina disciplina = new Disciplina();
		disciplina.setCodigoDisciplina(tfDisciplinaCodigo.getText());
		disciplina.setNomeDisciplina(tfDisciplinaNome.getText());
		  disciplina.setDiaSemana((String) cbDisciplinaDias.getSelectedItem());  
		  disciplina.setHoraInicio(Integer.parseInt(tfDisciplinaHorario.getText()));
	        disciplina.setQuantHoras(Integer.parseInt(tfDisciplinaCarga.getText()));
		   disciplina.setCodigoCurso(tfDisciplinaCurso.getText());
		
		disciplina = buscaDisciplina(disciplina);
		
		if (disciplina.getNomeDisciplina() != null) {
			taDisciplinaLista.setText("Disciplina: " + disciplina.getNomeDisciplina() + 
					" - Código: " + disciplina.getCodigoDisciplina() + " - Dia da Semana: " + disciplina.getCodigoDisciplina() 
					+ " - Hora Início: " + disciplina.getHoraInicio() + " - Horas semanais: " + disciplina.getQuantHoras() + " - Curso: " 
					+ disciplina.getCodigoCurso());
		} else {
			taDisciplinaLista.setText("Disciplina não encontrada!");
			
		}
	}
	
	//BUSCAR DISCIPLINA ATRAVÉS DO CÓDIGO DA DISCIPLINA
	private Disciplina buscaDisciplina(Disciplina disciplina) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File arq = new File(path, "arquivoDisciplina.csv");
		
		
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader (fis); 
			BufferedReader buffer = new BufferedReader (isr);
			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				if (vetLinha[0].equals(disciplina.getCodigoDisciplina())) {
					disciplina.setNomeDisciplina(vetLinha[1]);
					disciplina.setDiaSemana(vetLinha[2]);
					disciplina.setHoraInicio(Integer.parseInt(vetLinha[3]));
					disciplina.setQuantHoras(Integer.parseInt(vetLinha[4]));
					disciplina.setCodigoCurso(vetLinha[5]);
			
					break; 
				}
				linha = buffer.readLine();
				
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return disciplina;
	}

	private void cadastro() throws Exception {
		Disciplina disciplina = new Disciplina();
		disciplina.setCodigoDisciplina(tfDisciplinaCodigo.getText());
		disciplina.setNomeDisciplina(tfDisciplinaNome.getText());
		disciplina.setDiaSemana((String) cbDisciplinaDias.getSelectedItem());  
		disciplina.setHoraInicio(Integer.parseInt(tfDisciplinaHorario.getText()));
	    disciplina.setQuantHoras(Integer.parseInt(tfDisciplinaCarga.getText()));
		disciplina.setCodigoCurso(tfDisciplinaCurso.getText());	
		
		CursoController cCont = new CursoController();
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "curso.csv"); 
		boolean cursoExiste = cCont.cursoCadastrado(arq, disciplina.getCodigoCurso());   
		if (cursoExiste==true) {
			arq = new File(path, "Arquivodisciplina.csv"); 
			boolean cadastrado = disciplinaCadastrada(arq, disciplina.getCodigoDisciplina());
			if( cadastrado == true ) {
				atualizaDisciplina(disciplina.toString());
			}
			else {
		cadastraDisciplina(disciplina.toString());
			}
		tfDisciplinaCodigo.setText("");
		tfDisciplinaNome.setText("");
		tfDisciplinaHorario.setText("");
		tfDisciplinaCarga.setText("");
		tfDisciplinaCurso.setText("");
		
		}else {
			taDisciplinaLista.setText("Curso não existe, verifique o codigo");
		}

	}


	private void atualizaDisciplina(Disciplina disciplina) throws Exception {
		// TODO Auto-generated method stub
		 String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	        File arq = new File(path, "arquivoDisciplina.csv");
	        File auxArq = new File(path, "arquivoDisciplinaAux.csv");
	        if(arq.exists()&&arq.isFile()) {
	        	BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
	    		BufferedWriter pw = new BufferedWriter(new FileWriter(auxArq,true));
	    		Lista<Disciplina> listaDisciplinas = new Lista<Disciplina>() ;
	    		 String linha;
	             while ((linha = fw.readLine()) != null) {
	                 String[] vetLinha = linha.split(";");
	    		Disciplina disciplinaAux = new Disciplina();
	    		if(!vetLinha[0].equals( disciplina.getCodigoDisciplina())) {
	    			if(listaDisciplinas.isEmpty()) {    	    		disciplinaAux.setCodigoDisciplina(vetLinha[0]);
	    	    		disciplinaAux.setNomeDisciplina(vetLinha[1]);
	    	    		disciplinaAux.setDiaSemana(vetLinha[2]);
	    	    		try {
	    	    		disciplinaAux.setHoraInicio(Integer.parseInt(vetLinha[3]));
	    	    		} catch (Exception e) { 		
	    	    		}
	    	    		disciplinaAux.setQuantHoras(Integer.parseInt(vetLinha[4]));
	    	    		disciplina.setCodigoCurso(vetLinha[5]);
	    	 
	    	   
	    	    		
	    	        listaDisciplinas.addFirst(disciplina);}
	    	    	else {
	    	    		disciplinaAux.setCodigoDisciplina(vetLinha[0]);
	    	    		disciplina.setNomeDisciplina(vetLinha[1]);
	    	    		disciplina.setDiaSemana(vetLinha[2]);
	    	    		try {
	    	    		disciplinaAux.setHoraInicio(Integer.parseInt(vetLinha[3]));
	    	    		} catch (Exception e) { 		
	    	    		}
	    	    		disciplinaAux.setQuantHoras(Integer.parseInt(vetLinha[4]));
	    	    		disciplina.setCodigoCurso(vetLinha[5]);
	    	 
	    	   
	    	    		
	    	        listaDisciplinas.addLast(disciplinaAux);
	    	    	}
	    		}
	    			
	    		}
	        }
	}

	//CADASTRAR NOVA DISCIPLINA
	private void cadastraDisciplina(String csvDisciplina) throws Exception {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "arquivoDisciplina.csv"); 
		if (!arq.exists()) {
            // Cria o arquivo vazio, caso ele não exista
            arq.createNewFile();
            System.out.println("Arquivo arquivoDisciplina.csv criado.");
        }
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
		BufferedWriter pw = new BufferedWriter(new FileWriter(arq,true));
		
		Lista<Disciplina>listaDisciplinas = montarLista();
		 String[] vetLinha = csvDisciplina.split(";");
        Disciplina disciplina = new Disciplina();

		if(listaDisciplinas.isEmpty()) {
    		disciplina.setCodigoDisciplina(vetLinha[0]);
    		disciplina.setNomeDisciplina(vetLinha[1]);
    		disciplina.setDiaSemana(vetLinha[2]);
    		try {
    		disciplina.setHoraInicio(Integer.parseInt(vetLinha[3]));
    		} catch (Exception e) { 		
    		}
    		disciplina.setQuantHoras(Integer.parseInt(vetLinha[4]));
    		disciplina.setCodigoCurso(vetLinha[5]);  		
        listaDisciplinas.addFirst(disciplina);}
    	else {
    		disciplina.setCodigoDisciplina(vetLinha[0]);
    		disciplina.setNomeDisciplina(vetLinha[1]);
    		disciplina.setDiaSemana(vetLinha[2]);
    		try {
    		disciplina.setHoraInicio(Integer.parseInt(vetLinha[3]));
    		} catch (Exception e) { 		
    		}
    		disciplina.setQuantHoras(Integer.parseInt(vetLinha[4]));
    		disciplina.setCodigoCurso(vetLinha[5]);
 
   
    		
        listaDisciplinas.addLast(disciplina);
    	}
	
        pw.write(csvDisciplina+"\r\n");
		
		pw.flush();
		pw.close();
		fw.close();
		taDisciplinaLista.setText("Disciplina cadastrado com sucesso!");
	}

	private Lista<Disciplina> montarLista() throws Exception {
		// TODO Auto-generated method stub
		 String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	        File arq = new File(path, "arquivoDisciplina.csv");
	        Lista<Disciplina> listaDisciplinas = new Lista<>();
	        if (!arq.exists()) {
	            // Cria o arquivo vazio, caso ele não exista
	            arq.createNewFile();
	            System.out.println("Arquivo arquivoDisciplina.csv criado.");
	        }
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq))); 
     String linha;
     
     while ((linha = fw.readLine()) != null) {
         String[] vetLinha = linha.split(";");
         Disciplina disciplina = new Disciplina();
		if(listaDisciplinas.isEmpty()) {
    		disciplina.setCodigoDisciplina(vetLinha[0]);
    		disciplina.setNomeDisciplina(vetLinha[1]);
    		disciplina.setDiaSemana(vetLinha[2]);
    		try {
    		disciplina.setHoraInicio(Integer.parseInt(vetLinha[3]));
    		} catch (Exception e) { 		
    		}
    		disciplina.setQuantHoras(Integer.parseInt(vetLinha[4]));
    		disciplina.setCodigoCurso(vetLinha[5]);
 
   
    		
        listaDisciplinas.addFirst(disciplina);}
    	else {
    		disciplina.setCodigoDisciplina(vetLinha[0]);
    		disciplina.setNomeDisciplina(vetLinha[1]);
    		disciplina.setDiaSemana(vetLinha[2]);
    		try {
    		disciplina.setHoraInicio(Integer.parseInt(vetLinha[3]));
    		} catch (Exception e) { 		
    		}
    		disciplina.setQuantHoras(Integer.parseInt(vetLinha[4]));
    		disciplina.setCodigoCurso(vetLinha[5]);
 
   
    		
        listaDisciplinas.addLast(disciplina);
    	}
          
              
          
	}
     fw.close();
     return(listaDisciplinas);
     
     
	}

	//CHECAR SE A DISCIPLINA JÁ ESTÁ CADASTRADA ATRAVÉS DO CÓDIGO DA DISCIPLINA
	private boolean disciplinaCadastrada(File arq, String disciplinaCodigo) throws IOException {
		 if (!arq.exists()) {
		        return false; 
		    }

		    try (BufferedReader ler = new BufferedReader(new FileReader(arq))) {
		        String linha;
		        while ((linha = ler.readLine()) != null) {
		            String[] vetLinha = linha.split(";");
		            if (vetLinha[0].equals(disciplinaCodigo)) {
		                return true; 
		            }
		        }
		    }
		    return false; 
	}
}

