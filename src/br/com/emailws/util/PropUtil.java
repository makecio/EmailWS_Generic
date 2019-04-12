package br.com.emailws.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;


public class PropUtil {


	final static Logger logger = Logger.getLogger(PropUtil.class);
	
	public Properties getProp(ServletContext context) throws IOException {
		
		logger.info("Leitura do arquivo de propriedades");
		
		String realPath = context.getRealPath("/WEB-INF/email.properties");
		 
		Properties props = new Properties();
				
		props.load(new FileInputStream(new File(realPath)));
		
		return props;

	}


}