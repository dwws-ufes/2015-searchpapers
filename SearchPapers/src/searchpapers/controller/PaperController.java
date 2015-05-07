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
import searchpapers.application.InstituteService;
import searchpapers.application.KeywordService;
import searchpapers.application.PaperService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;


@Named
@SessionScoped
public class PaperController extends MessageController implements Serializable {	

	@EJB
	private PaperService paperService;
	
	//-- paper
	private Paper paper = new Paper();
	public Paper getPaper(){
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	//-- paper
	
	
	private List<Paper> papers;
	public List<Paper> getPapers(){
		return papers;
	}
	
	@Inject
	void listar(){
		papers = paperService.getPapers();
		listarArray();
	}
	
	

	
	//-------------------------------
	private Map<Long, Paper> arrayPapers = new TreeMap<Long, Paper>();
	
	public List<Paper> getArrayPapers() {
		return new ArrayList<Paper>(arrayPapers.values());
	}	
	public void addArray(Paper k) {
		arrayPapers.put(k.getId(), k);
	}	
	public void deletarArray(Paper k) {
		arrayPapers.remove(k.getId());
	}	
	public void limparArray() {
		arrayPapers.clear();
	}	
	public void listarArray(){
		for (int i = 0; i < papers.size(); i++) {
			arrayPapers.put(papers.get(i).getId(), papers.get(i));
		}
	}
	///----------------------------
	

	//-------filtro
	
	private String  filter;
	public String getFilter(){
		return filter;
	}	
	public void setFilter(String filter){
		this.filter =  filter;
	}
	
	private String valorFiltro;
	public String getValorFiltro(){
		return valorFiltro;
	}	
	public void setValorFiltro(String filter){
		this.valorFiltro =  filter;
	}
	
	private boolean filtering;
	public boolean getFiltering(){
		return filtering;
	}
		
	public void cancelFilter(){
		filtering = false;
		this.paper = new Paper();
		setValorFiltro("");
	
	}
	public String cancelarFiltro(){
		cancelFilter();
		limparArray();
		listar();
	    return "";
	}
	
	public void filtrar(){
		try{
			if ((this.filter != null)&&(this.filter != "")&&(!this.filter.equals(""))){
				if ((this.filter == "title") ||  (this.filter.equals("title"))) {					
					papers = paperService.getByLikeName(valorFiltro);
					limparArray();
					listarArray();
					
				}else
			    if ((this.filter == "id")||  (this.filter.equals("id"))){			         
			    	paper = paperService.getById(Long.parseLong(valorFiltro));
			    	papers = new ArrayList<Paper>();
			    	papers.add(paper);
					limparArray();
					listarArray();
			    }
				setMessageKey("OK", "pesq.sucesso");		
				this.filter = "";
				this.valorFiltro = "";
			}
			else{
				setMessageKey("OK", "filtro.nao.preenchido");	
				limparArray();
				listar();				
			}
		}
		catch(Exception e){
			setMessageChar("OK", "errooooo");
			setMessageChar("ERRO", e.getMessage());  
		}
		
	}	
		
	//-------filtro
	
	//--- Lista 
	protected boolean readOnly = false;		
	public boolean isReadOnly() {
		return readOnly;
	}
	
	public Paper getSelectedEntity(){
		return paper;
	}
	
	public String rowSelect(){
		setMessageChar("OK", "teste");
		return "";
	}
	//--- Lista
	
	
	//----- CRUD
	
	
	public String novo(){	
		readOnly = false;
		this.paper = new Paper();
		limparArrayKeyword();
		limparArrayAuthor();
		return "/paper/formPaper.xhtml";
	}
	
	public String editar(){	
		if (paper.getId()== null){
			setMessageKey("ERRO", "obj.nao_localizado");	
			return "";
		  }
		else{
			readOnly = false;
			listarArrayKeyword();
			listarArrayAuthor();
			return "/paper/formPaper.xhtml";
		}
	}
	
	
	public String cadastrarPaper(){
		try{
			
		  if (paper.getId()== null){
			  
			  	List<Paper> list = paperService.getByName(paper.getTitle());
				
				if ( list.isEmpty()){	
					List<Keyword> keywords = new ArrayList<Keyword>(arrayKeywords.values());
					if ( keywords.isEmpty() ){
						setMessageKey("ERRO", "paper.vazio_keyword");
					}
					else
					{
						paper.setKeywords(keywords);
						
						List<Author> authors = new ArrayList<Author>(arrayAuthors.values());
						if ( authors.isEmpty() ){
							setMessageKey("ERRO", "paper.vazio_Author");
						}
						else{
							paper.setAuthors(authors);	
							
							paperService.salvar(paper); 
							listar();
							setMessageKey("OK", "cad.sucesso");														
						}
					}
				}
				else{
					setMessageKey("ERRO", "obj.ja_cadastrado");	
				}
		  }
		  else{
			  List<Keyword> keywords = new ArrayList<Keyword>(arrayKeywords.values());	
			  paper.setKeywords(keywords);	
			  
			  List<Author> authors = new ArrayList<Author>(arrayAuthors.values());	
			  paper.setAuthors(authors);	
			  
			  paperService.atualizar(paper); 			  
			  setMessageKey("OK", "atu.sucesso");
		  }	

		  return "/paper/listPaper.xhtml";
		  
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage()); 
			return"";
		}
	}
	
	
	public void deletar(){
		try{
		  if (paper.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		  else{
			  paperService.deletar(paper); 
			  deletarArray(paper);			  
			  setMessageKey("OK", "del.sucesso");
		  }
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage());  
		}
	}
	//----- CRUD
	
	
	//-------------------- keywords
	@EJB
	private KeywordService keywordService;
	
	private Keyword keyword = new Keyword();
	public Keyword getKeyword(){
		return keyword;
	}
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	
	//--- Array keyword
	private Map<Long, Keyword> arrayKeywords = new TreeMap<Long, Keyword>();
	
	public List<Keyword> getArrayKeywords() {			
		return new ArrayList<Keyword>(arrayKeywords.values());
	}	
	private void addArrayKeyword(Keyword k) {
		arrayKeywords.put(k.getId(), k);
	}		
	private void delArrayKeyword(Keyword k) {		
		arrayKeywords.remove(k.getId());
	}	
	private void limparArrayKeyword(){
		arrayKeywords.clear();
	}
	public void listarArrayKeyword(){	
		limparArrayKeyword();
		for (int i = 0; i < paper.getKeywords().size(); i++) {			
			arrayKeywords.put(paper.getKeywords().get(i).getId(), paper.getKeywords().get(i));
		}
	}
	//--- Array keyword
	
	public void cadastrarKeyword(){	
		try{
			
			Keyword temp = keyword;
			List<Keyword> list = keywordService.getByName(temp.getWord());
				
			if ( list.isEmpty()){					
				keywordService.salvar(temp); 
				addArrayKeyword(temp);	
				setMessageKey("OK", "cad.sucesso");
			}
			else{
				setKeyword(list.get(0));
				addArrayKeyword(list.get(0));	
				setMessageKey("OK", "add.sucesso");
			}
		}
		catch(Exception e){
			setMessageKey("ERRO", e.getMessage());  
		}			
		setKeyword(new Keyword());						
	}
	
	public void deletarKeyword(Keyword k){	
		delArrayKeyword(k);
		setMessageKey("OK", "del.sucesso");
	}		
	//--- Cadastro keyword	
	
	
	//-------------------- Institute
	@EJB
	private InstituteService instituteService;
	
	private Institute institute = new Institute();
	public Institute getInstitute(){
		return institute;
	}
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}	
	//-------------------- Institute
	
	
	//-------------------- Autor
	@EJB
	private AuthorService authorService;
	
	private Author author = new Author();
	public Author getAuthor(){
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	//--- Array Author
	private Map<Long, Author> arrayAuthors = new TreeMap<Long, Author>();
	
	public List<Author> getArrayAuthors() {		
		return new ArrayList<Author>(arrayAuthors.values());
	}	
	private void addArrayAuthor(Author a) {
		arrayAuthors.put(a.getId(), a);
	}		
	private void delArrayAuthor(Author a) {		
		arrayAuthors.remove(a.getId());
	}
	private void limparArrayAuthor(){
		arrayAuthors.clear();
	}
	public void listarArrayAuthor(){	
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
				
				List<Institute> listInstitute = instituteService.getByName(institute.getName());
				Institute tempInstitute;
				if ( listInstitute.isEmpty()){
					instituteService.salvar(institute);
					tempInstitute = institute;
				}
				else
				{
					tempInstitute = listInstitute.get(0);
				}
				
				tempAuthor.setInstitute(tempInstitute);				
				authorService.salvar(tempAuthor); 
				addArrayAuthor(tempAuthor);	
				setMessageKey("OK", "cad.sucesso");
			}
			else
			{
				tempAuthor = listAuthor.get(0);
				addArrayAuthor(tempAuthor);	
				setMessageKey("OK", "add.sucesso");
			}	
		}
		catch(Exception e){
			setMessageKey("ERRO", e.getMessage());  
		}			
		setInstitute(new Institute());
		setAuthor(new Author());						
	}
	
	public void deletarAuthor(Author a){	
		delArrayAuthor(a);
		setMessageKey("OK", "del.sucesso");
	}		
	//--- Cadastro Autor	
	//-------------------- Autor

	
}
