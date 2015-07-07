package searchpapers.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.file.FileImage;
import searchpapers.persistence.AuthorDAO;
import searchpapers.sparql.SparqlProperties;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

@Stateless
public class AuthorServiceBean implements AuthorService {
	
	@EJB private AuthorDAO authorDAO;
	
	@EJB private SparqlProperties sparqlProperties;
	
	@EJB private InstituteService instituteService;
	
	@Inject private FileImage fileImage; 
	
   
	public List<Author> getAuthors(){
		
		return authorDAO.getAuthors();
	}
	
	public Author salvar(Author objeto){
		return authorDAO.salvar(objeto);		
	}
	
	public Author atualizar(Author objeto){
		return authorDAO.salvar(objeto);		
	}
	
	
	public void deletar(Author objeto){
		objeto = authorDAO.getById(objeto.getId());
		if (objeto != null){
			authorDAO.deletar(objeto);
		}				
	}
	
	public List<Author> getByName(String name){
		return authorDAO.getByName(name);
	}
	
	public List<Author> getByLikeName(String name){
		return authorDAO.getByLikeName(name);
	}
	
	public List<Author> getByEmail(String email){
		return authorDAO.getByEmail(email);
	}
	public  List<Author> getByInstitute(Institute institute){
		return authorDAO.getByInstitute(institute);
	}
	public Author getById(Long id){
		return authorDAO.getById(id);
	}

	
	public Author getAuthorWeb(String name){		
	
		if (name != null && name.length() > 0){
			
			String query = sparqlProperties.getProp("prop.prefixes.wiki") + 
					   	   sparqlProperties.getProp("prop.wiki.getInfoAutor");
			query = query.replace(":name", name.toLowerCase());
			
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://wiki.rkbexplorer.com/sparql/", query);

			try{				
				ResultSet results = queryExecution.execSelect();
				
				if (results.hasNext()) {
					QuerySolution querySolution = results.next();
					
					Author author = new Author();
					List<Author> listAuthor = getByName(name);
					if (!listAuthor.isEmpty()){
						author = listAuthor.get(0);					
					}
					else{
						author.setName(querySolution.getLiteral("name").toString());
						author.setUrlFoto(querySolution.getLiteral("foto").toString());
	
						try {
							URL urlObj = new URL(author.getUrlFoto());                             
							HttpURLConnection  httpConnection = (HttpURLConnection)urlObj.openConnection();
							httpConnection.setRequestMethod("GET");
							InputStream inputStream = httpConnection.getInputStream();
					        
					        byte [] arquivo = fileImage.fileToByte(inputStream);
					        author.setArquivoFoto(arquivo);
	
						} catch (IOException e) {
							System.out.println("erro carrega imagem foto "+e.getMessage()); 
						}
						
						Institute institute = new Institute();
						institute.setName(querySolution.getLiteral("nameInstitute").toString());
						institute.setWebAdress(querySolution.getLiteral("webAdress").toString());				
						
						List<Institute> listInstitute = instituteService.getByName(institute.getName());
						if ( listInstitute.isEmpty()){
							institute = instituteService.salvar(institute);
							author.setInstitute(institute);
						}	
						else{
						  author.setInstitute(listInstitute.get(0));
						}
						
						System.out.println("autor autor = "+author.getName());
						//salvar(author);							
					}						
					return author;
				}
				else {
					return null;
				}
			}catch(Exception e){ 
				System.out.println("erro busca autor "+e.getMessage()); 
				return null;
			}									
		}else{
			return null;
		}
	}
	

	
}
