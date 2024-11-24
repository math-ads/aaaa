package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Login;
import view.TelaMenu;


public class LoginController implements ActionListener {
	private JTextField tfLoginUsuario;
	private JTextField  tfLoginSenha;
	 private JTextArea taAvisos;
	
	public LoginController( JTextField tfLoginUsuario, JTextField  tfLoginSenha, JTextArea taAvisos ) {
		this.tfLoginUsuario = tfLoginUsuario;
		this.tfLoginSenha = tfLoginSenha;
		this.taAvisos= taAvisos;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Cadastrar-se")) {
			try {
				cadastroLogin();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (cmd.equals("Entrar")) {

				try {
					entrar();
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
	}
	
    // modulo de resposta ao botao entrar
	private void entrar() throws IOException, PropertyVetoException {
	    Login login = new Login();
	    login.setusuario(tfLoginUsuario.getText());
	    login.setsenha(tfLoginSenha.getText());

	    String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	    File arq = new File(path, "arquivoLogin.csv");

	    boolean loginValido = false;
	    boolean usuarioEncontrado = false;

	    if (arq.exists() && arq.isFile()) {
	        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(arq)))) {
	            String linha;
	            while ((linha = buffer.readLine()) != null) {
	                String[] vetLinha = linha.split(";");
	                if (vetLinha[0].equals(login.getusuario())) {
	                    usuarioEncontrado = true;
	                    if (vetLinha[1].equals(login.getsenha())) {
	                        loginValido = true;
	                        break;
	                    }
	                }
	            }
	        }
	    }

	    // Atualiza o JTextArea com a mensagem apropriada
	    if (loginValido) {
	        taAvisos.setText("Login bem-sucedido! Bem-vindo, " + login.getusuario() + ".");
	        abrirTelaMenu(); // Abre a tela principal
	    } else if (usuarioEncontrado) {
	        taAvisos.setText("Senha incorreta. Tente novamente.");
	    } else {
	        taAvisos.setText("Usuário não encontrado. Cadastre-se para continuar.");
	    }
	}

	private void abrirTelaMenu() throws PropertyVetoException {
	    SwingUtilities.invokeLater(() -> {
	        //   TelaPrincipal telaPrincipal = new TelaPrincipal();
	        	TelaMenu telaMenu = new TelaMenu();
	        	telaMenu.setVisible(true);
	    });
	}
		
	private void cadastroLogin() throws IOException {
	
		
		Login login = new Login();
		login.usuario = tfLoginUsuario.getText();
		login.senha = tfLoginSenha.getText();
		System.out.println(login);
		cadastralogin(login.toString());
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
	    File arq = new File(path, "arquivoLogin.csv");

	    boolean usuarioEncontrado = false;

	    if (arq.exists() && arq.isFile()) {
	        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(arq)))) {
	            String linha;
	            while ((linha = buffer.readLine()) != null) {
	                String[] vetLinha = linha.split(";");
	                if (vetLinha[0].equals(login.getusuario())) {
	                    usuarioEncontrado = true;
	                
	                    }
	                }
	            }
	        }
	    if(usuarioEncontrado== true) {
	   
	    	  taAvisos.setText("usuario já encontrado no sistema \n verifique se já se cadastrou ou insira outro usuário" + login.getusuario() + ".");
	    }
	    else {
	    	
	    }
	    }
		
			


	private void cadastralogin(String csvLogin) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "SistemaCadastroDocentes";
		File dir= new File(path);
		if(!dir.exists()) {
			dir.mkdir();}
		File arq= new File(path,"arquivoLogin.csv");
		boolean existe= false;
		if(arq.exists()) {
			existe= true;
		}
		
		FileWriter fw = new FileWriter(arq, existe);
		PrintWriter pw= new PrintWriter(fw);
		pw.write(csvLogin+"\r\n");
		pw.flush();
		pw.close();
		fw.close();

			
		}
		
	}
	

	