package searchpapers.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import searchpapers.application.AuthorService;
import searchpapers.application.PaperService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class AuthorController extends MessageController implements Serializable, SampleEntity {

	@EJB
	private AuthorService authorService;
	
	@EJB
	private PaperService paperService;
    
	private Author author = new Author();
	
	protected boolean readOnly = false;
	
	private List<Author> authors;
	
	private Map<Long, Author> arrayAuthors = new TreeMap<Long, Author>();
	
	private String  filter;
	
	private String valorFiltro;
	
	private boolean filtering;
	
	private Institute institute;
	
	public Institute getInstitute(){
		return institute;
	}
	
	public void setInstitute(Institute institute){
		this.institute =  institute;
	}
	
	public String getFilter(){
		return filter;
	}
	
	public void setFilter(String filter){
		this.filter =  filter;
	}
	
	public String getValorFiltro(){
		return valorFiltro;
	}	

	public void setValorFiltro(String filter){
		this.valorFiltro =  filter;
	}
	
	public boolean getFiltering(){
		return filtering;
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
	
	//-------------------------------
		// Tratamento do array para exibir na tela as alteracoes
		
		
		public List<Author> getArrayAuthors() {
			return new ArrayList<Author>(arrayAuthors.values());
		}
		
		public void addArray(Author k) {
			arrayAuthors.put(k.getId(), k);
		}
		
		public void deletarArray(Author k) {
			arrayAuthors.remove(k.getId());
		}
		
		public void listarArray(){
			for (int i = 0; i < authors.size(); i++) {
				arrayAuthors.put(authors.get(i).getId(), authors.get(i));
			}
		}
		///----------------------------
		
		@Inject
		public String listar(){
			authors = authorService.getAuthors();
			listarArray();
			
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
		}
		catch(Exception e){
			
		setMessageChar("ERRO", e.getMessage());  
		}
		
		return "/author/listAuthor.xhtml";
	}
	
	public void deletar(){		
		try{
			  if (author.getId()== null){
				  setMessageKey("ERRO", "obj.nao_localizado");			   
			  }
			  else{
				  List<Paper> papers = paperService.getPaperByAuthor(author.getId());
				  
				  if ( papers.isEmpty() ){
					  authorService.deletar(author); 
					  deletarArray(author);			  
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
	
	public void cadastrar(){
		salvar(author);
	}
	
	public void atualizar(Author author){
		salvar(author);
	}
	
	public Author getSelectedEntity(){
		return author;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	
	
	public void cancelFilter(){
		filtering = false;
		this.author = new Author();
		this.filter = "";
		this.valorFiltro = "";
	
	}
	public String cancelarFiltro(){
		cancelFilter();
		authors = authorService.getAuthors();
		arrayAuthors.clear();
		listarArray();
	    return "";
	}
	
	public void filtrar(){
		try{
			if ((this.filter != null)&&(this.filter != "")&&(!this.filter.equals(""))){
				if ((this.filter == "name") ||  (this.filter.equals("name"))) {
					
					authors = authorService.getByLikeName(valorFiltro);
					arrayAuthors.clear();
					listarArray();
				}else
			    if ((this.filter == "email")||  (this.filter.equals("email"))){
			    	authors = authorService.getByEmail(valorFiltro);
					arrayAuthors.clear();
					listarArray();
			    }else
			    if ((this.filter == "institute")||  (this.filter.equals("institute"))){
			    	authors = authorService.getByInstitute(institute);
					arrayAuthors.clear();
					listarArray();
			    }else
				 if ((this.filter == "id")||  (this.filter.equals("id"))){
					author = authorService.getById(Long.parseLong(valorFiltro));
				    authors = new ArrayList<Author>();
				    authors.add(author);
					arrayAuthors.clear();
					listarArray();
				 }
				setMessageKey("OK", "pesq.sucesso");		
				this.filter = "";
				this.valorFiltro = "";
			}
			else{
				setMessageKey("OK", "filtro.nao.preenchido");	
				authors = authorService.getAuthors();
				arrayAuthors.clear();
				listarArray();
				
			}
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage());  
		}
		
	}
	
	public String novo(){
		readOnly = false;
		this.author = new Author();
		return "/author/formAuthor.xhtml";
	}
	
	public String visualizar(){
		readOnly = true;
		setMessageChar("OK", isReadOnly()+"");
		return "/author/formAuthor.xhtml";
	}
	
	public String editar(){
		 if (author.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		 else{
		   readOnly = false;
		   return "/author/formAuthor.xhtml";
		 }
		 return null;
	}
	
	public String rowSelect(){
		setMessageChar("OK", "teste");
		//this.author = author;
		return "";
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
