package br.com.emailws.resource;


import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

import com.sun.jersey.core.util.Base64;

import br.com.emailws.business.EmailBusiness;
import br.com.emailws.model.Parametros;
import br.com.emailws.model.Retorno;

@Path("/email")
public class EmailResources {
	
	final static Logger logger = Logger.getLogger(EmailResources.class);
	 
    @Context
    ServletContext context;
	   
	@GET
	@Path("/enviar/{emailFrom}/{emailTo}/{assunto}/{titulo}/{corpoHtml}/{link}")
	@Produces(MediaType.APPLICATION_XML)
	public Retorno enviar(@PathParam("emailFrom") String emailFrom, @PathParam("emailTo") String emailTo, 
							@PathParam("assunto") String assunto, @PathParam("titulo") String titulo,
							@PathParam("corpoHtml") String corpoHtml, @PathParam("link") String link) {
		
		logger.info("Nova requisição de envio de e-mail.");
		
		Parametros parametros = new Parametros();
		
		parametros.setEmailFrom(Base64.base64Decode(emailFrom));
		parametros.setEmailTo(Base64.base64Decode(emailTo));
		parametros.setAssunto(Base64.base64Decode(assunto));
		parametros.setTitulo(Base64.base64Decode(titulo));
		parametros.setCorpoHtml(Base64.base64Decode(corpoHtml));
		
		String linkTratado = link.replace("¬¬", "/");
		parametros.setLink(Base64.base64Decode(linkTratado));
		
		logger.info("emailFrom: "+ parametros.getEmailFrom());
		logger.info("emailTo: "+ parametros.getEmailTo());
		logger.info("assunto"+ parametros.getAssunto());
		logger.info("titulo: "+ parametros.getTitulo());
		logger.info("corpoHtml: "+ parametros.getCorpoHtml());
		logger.info("link: "+ parametros.getLink());
		
		return EmailBusiness.getBancoInstance().newThreadEmail(parametros, context);
		
	}
	
	@GET
	@Path("/enviar/{emailFrom}/{emailTo}/{assunto}/{titulo}/{corpoHtml}/{link}/{anexos}")
	@Produces(MediaType.APPLICATION_XML)
	public Retorno enviar(@PathParam("emailFrom") String emailFrom, @PathParam("emailTo") String emailTo, 
							@PathParam("assunto") String assunto, @PathParam("titulo") String titulo,
							@PathParam("corpoHtml") String corpoHtml, @PathParam("link") String link, @PathParam("anexos") String anexos) {
		
		logger.info("Nova requisição de envio de e-mail.");
				
		Parametros parametros = new Parametros();
		
		parametros.setEmailFrom(Base64.base64Decode(emailFrom));
		parametros.setEmailTo(Base64.base64Decode(emailTo));
		parametros.setAssunto(Base64.base64Decode(assunto));
		parametros.setTitulo(Base64.base64Decode(titulo));
		parametros.setCorpoHtml(Base64.base64Decode(corpoHtml));
		parametros.setAnexos(Base64.base64Decode(anexos));
		
		String linkTratado = link.replace("¬¬", "/");
		parametros.setLink(Base64.base64Decode(linkTratado));
		
		logger.info("emailFrom: "+ parametros.getEmailFrom());
		logger.info("emailTo: "+ parametros.getEmailTo());
		logger.info("assunto"+ parametros.getAssunto());
		logger.info("titulo: "+ parametros.getTitulo());
		logger.info("corpoHtml: "+ parametros.getCorpoHtml());
		logger.info("link: "+ parametros.getLink());
		logger.info("anexo: "+ parametros.getAnexos());
		
		return EmailBusiness.getBancoInstance().newThreadEmail(parametros, context);
		
	}
	 
}
