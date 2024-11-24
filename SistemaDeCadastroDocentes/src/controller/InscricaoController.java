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
import model.Disciplina;
import model.Inscricao;

public class InscricaoController implements ActionListener {
    
    private JTextField tfInscricaoCpfProfessor;
    private JTextField tfInscricaoCodigoDisciplina;
    private JTextField tfInscricaoCodigoProcesso;
    private JTextArea taInscricaoLista;

    // Construtor corrigido
    public InscricaoController(JTextField tfInscricaoCpfProfessor, JTextField tfInscricaoCodigoProcesso, 
                                JTextField tfInscricaoCodigoDisciplina, JTextArea taInscricaoListaDisciplinasOuInscricoes) {
        this.tfInscricaoCpfProfessor = tfInscricaoCpfProfessor;
        this.tfInscricaoCodigoProcesso = tfInscricaoCodigoProcesso;
        this.tfInscricaoCodigoDisciplina = tfInscricaoCodigoDisciplina;
        this.taInscricaoLista = taInscricaoListaDisciplinasOuInscricoes;
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
				excluir();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void excluir() throws Exception {
		Inscricao inscricao = new Inscricao();
		inscricao.setCodigoProcesso(tfInscricaoCodigoProcesso.getText());
		inscricao.setCpfProfessor(tfInscricaoCpfProfessor.getText());
		inscricao.setCodigoDisciplina(tfInscricaoCodigoDisciplina.getText());
		ProfessorController pcont= new ProfessorController();
		CursoController cCont= new CursoController();
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "arquivoprofessor.csv"); 
		boolean professorExiste = pcont.professorCadastrado(arq,inscricao.getCpfProfessor());
		arq = new File(path, "curso.csv"); 
		boolean cursoExiste = cCont.cursoCadastrado(arq, inscricao.getCodigoDisciplina());   
		
		if(professorExiste==true && cursoExiste==true) {
			arq = new File(path, "inscricoes.csv"); 
			boolean cadastrado = inscricaoCadastrada(arq,inscricao.getCodigoDisciplina(),inscricao.getCpfProfessor());
			if( cadastrado == true ) {
				excluirInscricao(inscricao);
			}
			else {
		taInscricaoLista.setText("Inscricao nao registrada");
		
			}
		}
		
		tfInscricaoCodigoProcesso.setText("");
		tfInscricaoCpfProfessor.setText("");
		tfInscricaoCodigoDisciplina.setText("");
		
	}
		
	private void excluirInscricao(Inscricao inscricao) throws Exception {
		// TODO Auto-generated method stub
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
        File arq = new File(path, "inscricoes.csv");
        File auxArq = new File(path, "inscricoesAux.csv");
        if(arq.exists()&&arq.isFile()) {
        	BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
    		BufferedWriter pw = new BufferedWriter(new FileWriter(auxArq,true));
    		Lista<Inscricao> listaInscricoes = new Lista<Inscricao>() ;
    		 String linha;
    		 while ((linha = fw.readLine()) != null) {
                 String[] vetLinha = linha.split(";");
    		Inscricao inscricaoAux= new Inscricao();
    		
    		
    		if (!vetLinha[1].equals(inscricao.getCodigoDisciplina()) && !vetLinha[0].equals(inscricao.getCpfProfessor())) {
    			if(listaInscricoes.isEmpty()) {
    				inscricaoAux.setCpfProfessor(vetLinha[0]);
    				inscricaoAux.setCodigoDisciplina(vetLinha[1]);
    				inscricaoAux.setCodigoProcesso(vetLinha[2]);	
    	        listaInscricoes.addFirst(inscricaoAux);}
    	    	else {
    	    		inscricaoAux.setCpfProfessor(vetLinha[0]);
    				inscricaoAux.setCodigoDisciplina(vetLinha[1]);
    				inscricaoAux.setCodigoProcesso(vetLinha[2]);	
    	        listaInscricoes.addLast(inscricaoAux);
    	        }
    			pw.write(linha);
                pw.newLine();
  		} 
    		
         }
    		 pw.close();
      		fw.close();
      		  arq.delete();
      	        auxArq.renameTo(arq);
          // Substitui o arquivo original pelo auxiliar
      
          	}
    	    	}
	
	@SuppressWarnings("unused")
	private void busca() throws IOException {
		Inscricao inscricao = new Inscricao ();
		inscricao.setCodigoProcesso(tfInscricaoCodigoProcesso.getText());
		inscricao.setCpfProfessor(tfInscricaoCpfProfessor.getText());
		inscricao.setCodigoDisciplina(tfInscricaoCodigoDisciplina.getText());
		
		inscricao = buscaInscricao(inscricao);
		
		if (inscricao.getCpfProfessor() != null) {
			taInscricaoLista.setText("Processo: #" + inscricao.getCodigoProcesso() + " - Professor: " + inscricao.getCpfProfessor() + " - Disciplina: " + inscricao.getCodigoDisciplina());
			
		} else {
			taInscricaoLista.setText("Processo inexistente!");
		}
		
	}

	//BUSCAR INSCRIÇÃO ATRAVÉS DO CÓDIGO DO PROCESSO
	private Inscricao buscaInscricao(Inscricao inscricao) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File arq = new File(path, "inscricoes.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fis = new FileInputStream(arq);
			InputStreamReader isr = new InputStreamReader (fis); 
			BufferedReader buffer = new BufferedReader (isr);
			String linha = buffer.readLine();
			while (linha != null) {
				String[] vetLinha = linha.split(";");
				if (vetLinha[0].equals(inscricao.getCodigoProcesso())) {
					inscricao.setCpfProfessor(vetLinha[1]);
					inscricao.setCodigoDisciplina(vetLinha[2]);
					break; 
				}
				linha = buffer.readLine();
				
			}
			buffer.close();
			isr.close();
			fis.close();
		}
		return inscricao;
	}

	private void cadastro() throws Exception {
		Inscricao inscricao = new Inscricao();
		inscricao.setCodigoProcesso(tfInscricaoCodigoProcesso.getText());
		inscricao.setCpfProfessor(tfInscricaoCpfProfessor.getText());
		inscricao.setCodigoDisciplina(tfInscricaoCodigoDisciplina.getText());
		ProfessorController pcont= new ProfessorController();
		CursoController cCont= new CursoController();
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "arquivoprofessor.csv"); 
		boolean professorExiste = pcont.professorCadastrado(arq,inscricao.getCpfProfessor());
		arq = new File(path, "curso.csv"); 
		boolean cursoExiste = cCont.cursoCadastrado(arq, inscricao.getCodigoDisciplina());   
		
		if(professorExiste==true && cursoExiste==true) {
			arq = new File(path, "inscricoes.csv"); 
			boolean cadastrado = inscricaoCadastrada(arq,inscricao.getCodigoDisciplina(), inscricao.getCpfProfessor());
			if( cadastrado == true ) {
				atualizaInscricao(inscricao);
			}
			else {
		cadastraInscricao(inscricao.toString());
			}
		}
		
		tfInscricaoCodigoProcesso.setText("");
		tfInscricaoCpfProfessor.setText("");
		tfInscricaoCodigoDisciplina.setText("");
		
	}

	private void atualizaInscricao(Inscricao inscricao) throws Exception {
		// TODO Auto-generated method stub
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
        File arq = new File(path, "inscricoes.csv");
        File auxArq = new File(path, "inscricoesAux.csv");
        if(arq.exists()&&arq.isFile()) {
        	BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
    		BufferedWriter pw = new BufferedWriter(new FileWriter(auxArq,true));
    		Lista<Inscricao> listaInscricoes = new Lista<Inscricao>() ;
    		 String linha;
    		 while ((linha = fw.readLine()) != null) {
                 String[] vetLinha = linha.split(";");
    		Inscricao inscricaoAux= new Inscricao();
    		
    		if (!vetLinha[1].equals(inscricao.getCodigoDisciplina()) && !vetLinha[0].equals(inscricao.getCpfProfessor())) {

    			if(listaInscricoes.isEmpty()) {
    				inscricaoAux.setCpfProfessor(vetLinha[0]);
    				inscricaoAux.setCodigoDisciplina(vetLinha[1]);
    				inscricaoAux.setCodigoProcesso(vetLinha[2]);	
    	        listaInscricoes.addFirst(inscricaoAux);}
    	    	else {
    	    		inscricaoAux.setCpfProfessor(vetLinha[0]);
    				inscricaoAux.setCodigoDisciplina(vetLinha[1]);
    				inscricaoAux.setCodigoProcesso(vetLinha[2]);	
    	        listaInscricoes.addLast(inscricaoAux);}
    			pw.write(linha);
                pw.newLine();
  		} else {
  			if(listaInscricoes.isEmpty()) {
	        listaInscricoes.addFirst(inscricao);}
	    	else {	
	        listaInscricoes.addLast(inscricao);}
      		pw.write(inscricao.toString());
              pw.newLine();
          	}
    	    	}
    		 pw.close();
     		fw.close();
         }
          // Substitui o arquivo original pelo auxiliar
        arq.delete();
        auxArq.renameTo(arq);	 
        taInscricaoLista.setText("Inscrição atualizada!");
        }
    	 
    		 
	
	//CADASTRAR NOVA INSCRIÇÃO
	private void cadastraInscricao(String csvInscricao) throws Exception {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "inscricoes.csv"); 
		
		if (!arq.exists()) {
			arq.createNewFile();
		}
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
		BufferedWriter pw = new BufferedWriter(new FileWriter(arq,true));
		Lista<Inscricao>listaInscricoes = montarLista();
		 String[] vetLinha = csvInscricao.split(";");
       Inscricao inscricao = new Inscricao();
       
       if(listaInscricoes.isEmpty()) {
			inscricao.setCpfProfessor(vetLinha[0]);
			inscricao.setCodigoDisciplina(vetLinha[1]);
			inscricao.setCodigoProcesso(vetLinha[2]);	
       listaInscricoes.addFirst(inscricao);}
   	else {
   		inscricao.setCpfProfessor(vetLinha[0]);
			inscricao.setCodigoDisciplina(vetLinha[1]);
			inscricao.setCodigoProcesso(vetLinha[2]);	
       listaInscricoes.addLast(inscricao);
       }
       pw.write(csvInscricao+"\r\n");
		
		pw.flush();
		pw.close();
		fw.close();
		taInscricaoLista.setText("Inscrição cadastrada com sucesso!");
	}
	   

		
	

	//CHECAR SE A INSCRIÇÃO JÁ ESTÁ CADASTRADA ATRAVÉS DO CÓDIGO DO PROCESSO
	private boolean inscricaoCadastrada(File arq, String disciplinaCodigo, String cpf) throws IOException {
		 if (!arq.exists()) {
		        return false; 
		    }

		    try (BufferedReader ler = new BufferedReader(new FileReader(arq))) {
		        String linha;
		        while ((linha = ler.readLine()) != null) {
		            String[] vetLinha = linha.split(";");
		            if (vetLinha[1].equals(disciplinaCodigo)&&vetLinha[0].equals(cpf)) {
		                return true; 
		            }
		        }
		    }
		    return false; 
	}
	private Lista<Inscricao> montarLista() throws Exception {
		// TODO Auto-generated method stub
		 String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	        File arq = new File(path, "inscricoes.csv");
	        Lista<Inscricao> listaInscricoes = new Lista<>();
	        if (!arq.exists()) {
	            // Cria o arquivo vazio, caso ele não exista
	            arq.createNewFile();
	 
	        }
		BufferedReader fw = new BufferedReader(new InputStreamReader(new FileInputStream(arq))); 
     String linha;
     
     while ((linha = fw.readLine()) != null) {
         String[] vetLinha = linha.split(";");
      Inscricao inscricao = new Inscricao();
      
		if(listaInscricoes.isEmpty()) {
			inscricao.setCpfProfessor(vetLinha[0]);
			inscricao.setCodigoDisciplina(vetLinha[1]);
			inscricao.setCodigoProcesso(vetLinha[2]);	
        listaInscricoes.addFirst(inscricao);}
    	else {
    		inscricao.setCpfProfessor(vetLinha[0]);
			inscricao.setCodigoDisciplina(vetLinha[1]);
			inscricao.setCodigoProcesso(vetLinha[2]);	
        listaInscricoes.addLast(inscricao);}
    	}
          
              
     fw.close();
     return(listaInscricoes);
     
     
	}

}
