package br.com.emailws.threads;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import br.com.emailws.model.Parametros;
import br.com.emailws.util.EmailUtil;

public class ServerMultithreaded implements Runnable{

	final static Logger logger = Logger.getLogger(ServerMultithreaded.class);
	 
    protected boolean      isStopped     = false;
    protected Thread       runningThread = null;
    
    private Parametros parametros;
    private ServletContext context;
    
    public ServerMultithreaded(Parametros p, ServletContext c) {
    	parametros = p;
    	context = c;
	}

	public void run(){
    	
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        
        while(! isStopped()){
           
   	  	   try{
   	  		   EmailUtil emailUtil = new EmailUtil();
   	  		   emailUtil.enviarEmail(parametros, context);
   	  	   }catch(Exception e){
   	  		   logger.error(e.getMessage());
   	  	   }
   	  	   
   	  	   this.stop();
   	  	}
        
    }
	

	private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop(){
        this.isStopped = true;
    }
    

}
