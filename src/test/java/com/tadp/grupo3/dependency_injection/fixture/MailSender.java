package com.tadp.grupo3.dependency_injection.fixture;

public class MailSender {
	String usuario;
	String password;
	String smtp;
	Integer puerto;
	
	public MailSender(String usuario, String password, String smtp, Integer puerto) {
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

	public Integer getPuerto() {
		return puerto;
	}
}
