package br.com.emailws.model;

import java.io.Serializable;

public class Parametros implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4508613748893471001L;
	
	private String emailFrom;
	private String emailTo;
	private String assunto;
	private String titulo;
	private String corpoHtml;
	private String link;
	private String anexos;

	public String getAnexos() {
		return anexos;
	}
	public void setAnexos(String anexos) {
		this.anexos = anexos;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCorpoHtml() {
		return corpoHtml;
	}
	public void setCorpoHtml(String corpoHtml) {
		this.corpoHtml = corpoHtml;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
