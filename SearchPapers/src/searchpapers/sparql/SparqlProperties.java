package searchpapers.sparql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Stateless;

@Stateless
public class SparqlProperties {	

	public String getProp(String name){ 
		String rs = "";
		try{
			Properties props = new Properties(); 
			FileInputStream file = new FileInputStream( "/Users/Camila/Google Drive/MESTRADO/Sem2 DesenvWeb/workspace_casa/SearchPapers/src/searchpapers/sparql/sparql.properties"); 
			//FileInputStream file = new FileInputStream( "C:/Users/casa/workspace/SearchPapers/src/searchpapers/sparql/sparql.properties");
			props.load(file); 
			rs = props.getProperty(name);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		  }	
		
		return rs; 
	}
	

}
