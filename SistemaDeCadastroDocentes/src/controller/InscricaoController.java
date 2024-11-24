package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Inscricao;

public class InscricaoController implements ActionListener {
	
	private JTextField tfInscricaoCpfProfessor;
	private JTextField tfInscricaoCodigoDisciplina;
	private JTextField tfInscricaoCodigoProcesso;
	private JTextArea taInscricaoLista;

	public InscricaoController(JTextField tfInscricaoCpfProfessor, JTextField tfInscricaoCodigoDisciplina,
			JTextField tfInscricaoCodigoProcesso, JTextArea taInscricaoLista) {
		
		this.tfInscricaoCpfProfessor = tfInscricaoCpfProfessor;
		this.tfInscricaoCodigoDisciplina = tfInscricaoCodigoDisciplina;
		this.tfInscricaoCodigoProcesso = tfInscricaoCodigoProcesso;
		this.taInscricaoLista = taInscricaoLista;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Cadastrar")) {
			try {
				cadastro();
			} catch (IOException e1) {
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
	}

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

	private void cadastro() throws IOException {
		Inscricao inscricao = new Inscricao();
		inscricao.setCodigoProcesso(tfInscricaoCodigoProcesso.getText());
		inscricao.setCpfProfessor(tfInscricaoCpfProfessor.getText());
		inscricao.setCodigoDisciplina(tfInscricaoCodigoDisciplina.getText());
		
		cadastraInscricao(inscricao.toString());
		tfInscricaoCodigoProcesso.setText("");
		tfInscricaoCpfProfessor.setText("");
		tfInscricaoCodigoDisciplina.setText("");
		
	}

	//CADASTRAR NOVA INSCRIÇÃO
	private void cadastraInscricao(String csvInscricao) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir = new File (path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File arq = new File(path, "inscricoes.csv"); 
		boolean existe = false;
		if (arq.exists()) {
			existe = true;
		}
		
		if (inscricaoCadastrada(arq, csvInscricao.split(";")[0])) {
	        taInscricaoLista.setText("Inscrição já cadastrada!");
	        return; 
	    }

		FileWriter fw = new FileWriter (arq, existe);
		PrintWriter pw = new PrintWriter (fw);
		pw.write(csvInscricao+"\r\n");
		pw.flush();
		pw.close();
		fw.close();
		taInscricaoLista.setText("Inscrição cadastrada com sucesso!");
	}

	//CHECAR SE A INSCRIÇÃO JÁ ESTÁ CADASTRADA ATRAVÉS DO CÓDIGO DO PROCESSO
	private boolean inscricaoCadastrada(File arq, String processoCodigo) throws IOException {
		 if (!arq.exists()) {
		        return false; 
		    }

		    try (BufferedReader ler = new BufferedReader(new FileReader(arq))) {
		        String linha;
		        while ((linha = ler.readLine()) != null) {
		            String[] vetLinha = linha.split(";");
		            if (vetLinha[0].equals(processoCodigo)) {
		                return true; 
		            }
		        }
		    }
		    return false; 
	}


}
