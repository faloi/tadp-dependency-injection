package com.tadp.grupo3.dependency_injection.fixture;

public class MailSender {
	String usuario;
	String password;
	String smtp;
	int puerto;
	
	public MailSender(String usuario, String password, String smtp, int puerto) {
		this.usuario = usuario;
		this.password = password;
		this.smtp = smtp;
		this.puerto = puerto;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}

	public String getSmtp() {
		return smtp;
	}

	public int getPuerto() {
		return puerto;
	}
}
