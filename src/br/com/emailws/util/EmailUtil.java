package br.com.emailws.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import br.com.emailws.model.Parametros;


public class EmailUtil implements Serializable{

	final static Logger logger = Logger.getLogger(EmailUtil.class);
	
	private static final long serialVersionUID = 1L;
	
	private String de;
	private String para;
	private String destnormais;
	private String destoculto;
	private String assunto;
	private String mensagem;
		
	public void enviar(ServletContext context) throws IOException{
		
		PropUtil propUtil = new PropUtil();
		Properties config = propUtil.getProp(context);
	 		  
		Session sessao = Session.getDefaultInstance(config);
		
		try {
			
			MimeMessage email = new MimeMessage(sessao);
			email.setFrom(new InternetAddress(this.de));
			
			InternetAddress[]para = this.monstarDestinatarios(this.para);
			if(para !=null){
				email.setRecipients(Message.RecipientType.TO, para);
			}
			
			InternetAddress[]normais = this.monstarDestinatarios(this.destnormais);
			if(normais !=null){
				email.setRecipients(Message.RecipientType.CC,normais);
			}
			InternetAddress[]ocultos = this.monstarDestinatarios(this.destoculto);
			if(ocultos !=null){
				email.setRecipients(Message.RecipientType.BCC,ocultos);
			}
			
			email.setSubject(this.assunto);
			email.setSentDate(new Date());
			email.setContent(this.mensagem, "text/html; charset=utf-8"); 

			Transport.send(email);
			
			logger.info("Email enviado com sucesso!");
		
		} catch (AddressException e) {
			logger.error(e.getMessage());
			return;
		}catch (MessagingException e) {
			logger.error(e.getMessage());
			return;
		}
	}
	
	private InternetAddress[]monstarDestinatarios(String destinatarios) throws AddressException{
		
		InternetAddress[]emails =null;
		if(destinatarios != null && destinatarios.trim().length() > 0){
			String[] lista = destinatarios.split(";");
			emails  = new InternetAddress[lista.length];
			for(int i =0; i < lista.length; i++){
				emails[i] = new InternetAddress(lista[i]);
			}
		}
		return emails;
	}

	public void enviarEmail(Parametros p, ServletContext context) throws IOException {
		
		EmailUtil email = new EmailUtil();
		
		email.setDe(p.getEmailFrom());
		email.setPara(p.getEmailTo());
		email.setAssunto(new String((p.getAssunto().getBytes()) ,"UTF-8"));
		
		ArquivosUtil arqUtil = new ArquivosUtil();
		String realPath = ""; 
		
		if(p.getLink().toLowerCase().trim().equals("null")){
			realPath = context.getRealPath("/WEB-INF/email_noButton.html");
		}else{
			realPath = context.getRealPath("/WEB-INF/email.html");
		}
		
		String msgHtml = arqUtil.lerArquivo(new File(realPath));
		
		msgHtml = msgHtml.replace("?titulo?", trataAcentuacao(new String((p.getTitulo().getBytes()) ,"UTF-8")));
		msgHtml = msgHtml.replace("?corpoHtml?", trataAcentuacao(new String((p.getCorpoHtml().getBytes()) ,"UTF-8")));
		msgHtml = msgHtml.replace("?link?", trataAcentuacao(new String((p.getLink().getBytes()) ,"UTF-8")));
		
		email.setMensagem(msgHtml);
			
		if(!email.getDe().equals("") && !email.getPara().trim().equals("")){
			if(p.getAnexos() != null){
				email.enviarComAnexos(context, p);
			}else{
				email.enviar(context);
			}
		}
		
	}
	
	private void enviarComAnexos(ServletContext context, Parametros p) throws IOException {
		// TODO Auto-generated method stub

		PropUtil propUtil = new PropUtil();
		Properties config = propUtil.getProp(context);
	 		  
		Session sessao = Session.getDefaultInstance(config);
		
		try {
			   
			MimeMessage email = new MimeMessage(sessao);
			email.setFrom(new InternetAddress(this.de));
			
			InternetAddress[]para = this.monstarDestinatarios(this.para);
			if(para !=null){
				email.setRecipients(Message.RecipientType.TO, para);
			}
			
			InternetAddress[]normais = this.monstarDestinatarios(this.destnormais);
			if(normais !=null){
				email.setRecipients(Message.RecipientType.CC,normais);
			}
			InternetAddress[]ocultos = this.monstarDestinatarios(this.destoculto);
			if(ocultos !=null){
				email.setRecipients(Message.RecipientType.BCC,ocultos);
			}
			   
		   email.setSubject(MimeUtility.encodeText(this.assunto, "utf-8", "B"));
		   email.setSentDate(new Date());


		   //
		   // This HTML mail have to 2 part, the BODY and the embedded image
		   //
		   MimeMultipart multipart = new MimeMultipart("related");

		   // first part  (the html)
		   BodyPart messageBodyPart = new MimeBodyPart();
		   String htmlText = this.mensagem;
		   messageBodyPart.setContent(htmlText, "text/html; charset=utf-8");

		   // add it
		   multipart.addBodyPart(messageBodyPart);
			   
		   // put everything together
		   email.setContent(multipart, "text/html; charset=utf-8");
		   
		   String[] anexos = p.getAnexos().split(",");
		   for(String f: anexos) {
			       messageBodyPart = new MimeBodyPart();
			       DataSource source = new FileDataSource(f);

			       messageBodyPart.setDataHandler(new DataHandler(source));
			       messageBodyPart.setFileName(source.getName());
			       multipart.addBodyPart(messageBodyPart);
			       email.setContent(multipart, "text/html; charset=utf-8");
		   }

		  Transport.send(email);
			   
		  logger.info("Email enviado com sucesso!");
				
		} catch (AddressException e) {
			logger.error(e.getMessage());
			return;
		}catch (MessagingException e) {
			logger.error(e.getMessage());
			return;
		}
		
	}

	private String trataAcentuacao(String conteudo){
		
		conteudo = conteudo.replaceAll("�", "&Aacute");
		conteudo = conteudo.replaceAll("�", "&aacute");
		conteudo = conteudo.replaceAll("�", "&Acirc");
		conteudo = conteudo.replaceAll("�", "&acirc");
		conteudo = conteudo.replaceAll("�", "&Agrave");
		conteudo = conteudo.replaceAll("�", "&agrave");
		conteudo = conteudo.replaceAll("�", "&Aring");
		conteudo = conteudo.replaceAll("�", "&aring");
		conteudo = conteudo.replaceAll("�", "&Atilde");
		conteudo = conteudo.replaceAll("�", "&atilde");
		conteudo = conteudo.replaceAll("�", "&Auml");
		conteudo = conteudo.replaceAll("�", "&auml");
		conteudo = conteudo.replaceAll("�", "&AElig");
		conteudo = conteudo.replaceAll("�", "&aelig");
		
		conteudo = conteudo.replaceAll("�", "&Eacute");
		conteudo = conteudo.replaceAll("�", "&eacute");
		conteudo = conteudo.replaceAll("�", "&Ecirc");
		conteudo = conteudo.replaceAll("�", "&ecirc");
		conteudo = conteudo.replaceAll("�", "&Egrave");
		conteudo = conteudo.replaceAll("�", "&egrave");
		conteudo = conteudo.replaceAll("�", "&Euml");
		
		conteudo = conteudo.replaceAll("�", "&ETH");
		conteudo = conteudo.replaceAll("�", "&eth");
		
		conteudo = conteudo.replaceAll("�", "&Iacute");
		conteudo = conteudo.replaceAll("�", "&iacute");
		conteudo = conteudo.replaceAll("�", "&Icirc");
		conteudo = conteudo.replaceAll("�", "&icirc");
		conteudo = conteudo.replaceAll("�", "&Igrave");
		conteudo = conteudo.replaceAll("�", "&igrave");
		conteudo = conteudo.replaceAll("�", "&Iuml");
		conteudo = conteudo.replaceAll("�", "&iuml");
		
		conteudo = conteudo.replaceAll("�", "&Oacute");
		conteudo = conteudo.replaceAll("�", "&oacute");
		conteudo = conteudo.replaceAll("�", "&Ocirc");
		conteudo = conteudo.replaceAll("�", "&ocirc");
		conteudo = conteudo.replaceAll("�", "&Ograve");
		conteudo = conteudo.replaceAll("�", "&ograve");
		conteudo = conteudo.replaceAll("�", "&Oslash");
		conteudo = conteudo.replaceAll("�", "&oslash");
		conteudo = conteudo.replaceAll("�", "&Otilde");
		conteudo = conteudo.replaceAll("�", "&otilde");
		conteudo = conteudo.replaceAll("�", "&Ouml");
		conteudo = conteudo.replaceAll("�", "&ouml");
		
		conteudo = conteudo.replaceAll("�", "&Uacute");
		conteudo = conteudo.replaceAll("�", "&uacute");
		conteudo = conteudo.replaceAll("�", "&Ucirc");
		conteudo = conteudo.replaceAll("�", "&ucirc");
		conteudo = conteudo.replaceAll("�", "&Ugrave");
		conteudo = conteudo.replaceAll("�", "&ugrave");
		conteudo = conteudo.replaceAll("�", "&Uuml");
		conteudo = conteudo.replaceAll("�", "&uuml");
		
		conteudo = conteudo.replaceAll("�", "&Ccedil");
		conteudo = conteudo.replaceAll("�", "&ccedil");
		
		conteudo = conteudo.replaceAll("�", "&Ntilde");
		conteudo = conteudo.replaceAll("�", "&ntilde");
		
		return conteudo;
	}
	
	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getdestnormais() {
		return destnormais;
	}

	public void setdestnormais(String destnormais) {
		this.destnormais = destnormais;
	}

	public String getdestoculto() {
		return destoculto;
	}

	public void setdestoculto(String destoculto) {
		this.destoculto = destoculto;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}