package model;

public class Login {
public String usuario;
public String senha;

public Login (String usuario, String senha) {
	this.usuario = usuario;
	this.senha= senha;

}
public Login(){
	this ("","");
}

public String getusuario() {
	return usuario;
}

public void setusuario(String usuario) {
	if (usuario == null || usuario.length() == 0 ) {
		 throw new IllegalArgumentException("Digite usuario");
	} else {
		this.usuario = usuario;
	}
}

public String getsenha() {
	return senha;
}

public void setsenha(String senha) {
	if (senha == null) {
		 throw new IllegalArgumentException("Digite senha");
	} else {
		this.senha = senha;
	}
}
@Override
public String toString() {
	return usuario+";"+senha;
}
}
