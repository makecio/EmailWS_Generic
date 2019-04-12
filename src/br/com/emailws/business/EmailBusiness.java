package br.com.emailws.business;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import br.com.emailws.model.Parametros;
import br.com.emailws.model.Retorno;
import br.com.emailws.threads.ServerMultithreaded;

public class EmailBusiness {
	
   final static Logger logger = Logger.getLogger(EmailBusiness.class);
	 
   private static EmailBusiness instance;
     
   public static EmailBusiness getBancoInstance(){
	     if(instance==null)
	         instance = new EmailBusiness();
	     return instance;
	 }   


   public Retorno newThreadEmail(Parametros parametros, ServletContext context){
	   
	   Retorno retorno = new Retorno();
	   
	   try{
		   
		   Thread thread = new Thread(new ServerMultithreaded(parametros, context));
		   thread.start();
		   		   
		   retorno.setMensagem("sucesso");
	   }catch(Exception e){
		   logger.error(e.getMessage());
		   retorno.setMensagem("error");
	   }
	   
	   return retorno;
   }
   
   
}
