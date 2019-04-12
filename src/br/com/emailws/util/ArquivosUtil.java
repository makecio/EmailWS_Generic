package br.com.emailws.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

public class ArquivosUtil {

	final static Logger logger = Logger.getLogger(ArquivosUtil.class);
	
	public String lerArquivo(File file){
		
		String conteudoHtml = "";
		StringBuffer conteudo = new StringBuffer();
		 
		 try {
		      FileReader arq = new FileReader(file.getPath().toString());
		      BufferedReader lerArq = new BufferedReader(arq);
		      String linha = lerArq.readLine();
		      
		      while (linha != null) {
		    	  
	    		  if(linha.trim().length() > 0 ){
	    			  
				       if(!linha.trim().equals("")){
				        	conteudo.append(linha+"\n");
				       }
	    			
				       linha = lerArq.readLine();
			    	}else{
			    	   linha = lerArq.readLine();
			    	}
		    	  
		      }
		 
		      arq.close();
		    } catch (IOException e) {
		    	logger.error("Erro na abertura do arquivo: " +e.getMessage());
		    } 
		 
		 conteudoHtml = conteudo.toString();
				 
		 return conteudoHtml;
	}


}
