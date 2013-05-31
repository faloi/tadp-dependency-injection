package com.tadp.grupo3.dependency_injection.fixture;

import com.tadp.grupo3.dependency_injection.Inyectar;

public class GmailLogger {

	private MailSender mailSender;
	private Usuario usuario;

	public MailSender getMailSender() {
		return mailSender;
	}

	@Inyectar
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Inyectar
	private void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
