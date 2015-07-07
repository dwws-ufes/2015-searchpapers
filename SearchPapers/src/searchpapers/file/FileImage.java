package searchpapers.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

@Named
@SessionScoped
public class FileImage implements Serializable {
	
	public static byte[] fileToByte(InputStream inputFile) {         
        
		byte[] arquivo = null;
		try{
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	        byte[] buffer = new byte[34394702];    
	        int bytesRead = 0;    
	        while ((bytesRead = inputFile.read(buffer, 0, 34394702)) != -1) {    
	            baos.write(buffer, 0, bytesRead);    
	        }    
	        
	        arquivo = baos.toByteArray();  
	        //inputFile.close();
		}catch (IOException e) {
	         e.printStackTrace();
	      }	
		
        return arquivo;
    }  
	
	
	public static byte[] BufferedImageToByte(BufferedImage bImg) { 
		
		byte[] imagemByte = null;
		try{
	    	ByteArrayOutputStream os = new ByteArrayOutputStream();
	    	ImageIO.write(bImg,"png", os); 
	    	InputStream fis = new ByteArrayInputStream(os.toByteArray());
	    	
	        BufferedInputStream in=new BufferedInputStream(fis);
	        imagemByte = new byte[in.available()];
	        in.read(imagemByte);
	        in.close();	        	        
		}catch (IOException e) {
	         e.printStackTrace();
	    }
		
		return imagemByte;
	}

}
