package searchpapers.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import searchpapers.application.AuthorService;
import searchpapers.application.InstituteService;
import searchpapers.application.PaperService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class AuthorController extends MessageController implements Serializable, SampleEntity {

	@EJB private AuthorService authorService;	
	@EJB private PaperService paperService;	
	@EJB private InstituteService instituteService;
	
    
	private Author author = new Author();
	
	//protected boolean readOnly = false;
	
	private List<Author> authors;
			
	private Institute institute;
	
	private List<Institute> institutes;
	

	private List<Paper> papers;			
	public List<Paper> getPapers() {
		return papers;
	}
	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}
	
	public List<Institute> getInstitutes(){
	   institutes = instituteService.getInstitutes();
	   return institutes;
	}
	public Institute getInstitute(){
		return institute;
	}
	
	public void setInstitute(Institute institute){
		this.institute =  institute;
	}
	
	
	public Author getAuthor(){
		return author;
	}
	
	public void setAuthor(Author author){
		this.author = author;
	}
	
	public List<Author> getAuthors(){
		return (List<Author>)authors;
	}
	
	
	@Inject
	public String listar(){
		authors = authorService.getAuthors();
		return "/author/listAuthor.xhtml";
	}
		
	
	public String salvar(Author author){
		try{			  
			
		  if (author.getId()== null){
		    authorService.salvar(author); 
		    authors.add(author);
		    setMessageKey("OK", "cad.sucesso");	
		    listar();		    
		  }
		  else{
		    authorService.atualizar(author); 
		    setMessageKey("OK", "atu.sucesso");			    
		  }
		  author = new Author();
		  papers = new ArrayList<Paper>();
		}
		catch(Exception e){
			
		setMessageChar("ERRO", e.getMessage());  
		}
		
		return "/author/listAuthor.xhtml";
	}
	
	public void deletar(Author author){		
		try{
			  if (author.getId()== null){
				  setMessageKey("ERRO", "obj.nao_localizado");			   
			  }
			  else{
				  List<Paper> papers = paperService.getPaperByAuthor(author.getId());
				  
				  if ( papers.isEmpty() ){
					  authorService.deletar(author); 	  
					  setMessageKey("OK", "del.sucesso");
				  }
				  else {
					  setMessageKey("ERRO", "del.ex_relacao");
				  } 
			  }
			}
			catch(Exception e){
				setMessageChar("ERRO", e.getMessage());  
			}
	}
		
	/*public boolean isReadOnly() {
		return readOnly;
	}	
 */
	public String novo(){
		//readOnly = false;
		this.author = new Author();
		this.papers = new ArrayList<Paper>();
		return "/author/formAuthor.xhtml";
	}
	
	
	public String editar(Long Id){
		System.out.println("entrou == editar");
		author = authorService.getById(Id);	
		
		 if (author.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		 else{
		   //readOnly = false;
		   sugereArtigos();
		   return "/author/formAuthor.xhtml";
		 }
		 return null;
	}
	
	@Override
	public Long getId() {
		return null;
	}
	

	
	public void carregaInformacaoWeb() throws IOException, URISyntaxException{
		try{

			String name = author.getName();			
			
			Author tempAuthor = authorService.getAuthorWeb(name);
			if (tempAuthor != null){
				tempAuthor.setEmail(author.getEmail());
				author = tempAuthor;
			}
			institutes = getInstitutes();
			
			System.out.println("entrou = carrega informacao author");
			sugereArtigos();
		
		}catch(Exception e){
			setMessageChar("ERRO", e.getMessage());  
		}
	}
		
	public void sugereArtigos(){
			System.out.println("entrou = no sugereArtigos");
		try{

			String name = author.getName();
			papers = paperService.getPapersWeb(name);
		
	     }catch(Exception e){
				System.out.println("erro no sugereArtigos "+e.getMessage()); 
				setMessageChar("ERRO", e.getMessage());  
		}
	}
	
	

	
}
