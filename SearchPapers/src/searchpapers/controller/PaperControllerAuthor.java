package searchpapers.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import searchpapers.application.AuthorService;
import searchpapers.application.InstituteService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class PaperControllerAuthor extends MessageController implements Serializable {
	
	
	@EJB private InstituteService instituteService;
	@EJB private AuthorService authorService;
	
	//-------------------- Institute
	private Institute institute = new Institute();
	public Institute getInstitute(){
		return institute;
	}
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}	
	//-------------------- Institute
	
	
	//-------------------- Autor		
	private Author author = new Author();
	public Author getAuthor(){
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	//--- Array Author
	private Map<Long, Author> arrayAuthors = new TreeMap<Long, Author>();
	public void setArrayAuthors(Map<Long, Author> arrayAuthors) {
		this.arrayAuthors = arrayAuthors;
	}
	public List<Author> getArrayAuthors() {		
		return new ArrayList<Author>(arrayAuthors.values());
	}	
	
	private void addArrayAuthor(Author a) {
		arrayAuthors.put(a.getId(), a);
	}		
	private void delArrayAuthor(Author a) {		
		arrayAuthors.remove(a.getId());
	}
	public void limparArrayAuthor(){
		arrayAuthors.clear();
	}
	public void listarArrayAuthor(Paper paper){	
		limparArrayAuthor();
		for (int i = 0; i < paper.getAuthors().size(); i++) {			
			arrayAuthors.put(paper.getAuthors().get(i).getId(), paper.getAuthors().get(i));
		}
	}
	//--- Array Author

	//--- Cadastro Author
	public void cadastrarAuthor(){	
		try{
			List<Author> listAuthor = authorService.getByName(author.getName());
			Author tempAuthor = new Author();
			
			if ( listAuthor.isEmpty()){	

				tempAuthor = author;				
				authorService.salvar(tempAuthor); 
				addArrayAuthor(tempAuthor);	
				setMessageKey("OK", "author.cad.sucesso");
			}
			else
			{
				tempAuthor = listAuthor.get(0);
				addArrayAuthor(tempAuthor);	
				setMessageKey("OK", "author.add.sucesso");
			}	
		}
		catch(Exception e){
			setMessageKey("ERRO", e.getMessage());  
		}			
		setAuthor(new Author());						
	}
	
	public void deletarAuthor(Author a){	
		delArrayAuthor(a);
		setMessageKey("OK", "author.del.sucesso");
	}		
	//--- Cadastro Autor	
	//-------------------- Autor
}
