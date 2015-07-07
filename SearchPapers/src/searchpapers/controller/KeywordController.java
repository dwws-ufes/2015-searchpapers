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

import searchpapers.application.KeywordService;
import searchpapers.application.PaperService;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;


@Named
@SessionScoped
public class KeywordController extends MessageController implements Serializable {	

	@EJB private KeywordService keywordService;
	
	@EJB private PaperService paperService;
	
	protected boolean readOnly = false;
	
	//-- keyword
	private Keyword keyword = new Keyword();
	public Keyword getKeyword(){
		return keyword;
	}
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	//-- keyword
	
	
	private List<Keyword> keywords;
	public List<Keyword> getKeywords(){
		return keywords;
	}
	
	@Inject
	public String listar(){
		keywords = keywordService.getKeywords();
		listarArray();
		
		return "/keyword/listKeyword.xhtml";
	}
	
	

	
	//-------------------------------
	private Map<Long, Keyword> arrayKeywords = new TreeMap<Long, Keyword>();
	
	public List<Keyword> getArrayKeywords() {
		return new ArrayList<Keyword>(arrayKeywords.values());
	}	
	public void addArray(Keyword k) {
		arrayKeywords.put(k.getId(), k);
	}	
	public void deletarArray(Keyword k) {
		arrayKeywords.remove(k.getId());
	}	
	public void limparArray() {
		arrayKeywords.clear();
	}	
	public void listarArray(){
		for (int i = 0; i < keywords.size(); i++) {
			arrayKeywords.put(keywords.get(i).getId(), keywords.get(i));
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
		this.keyword = new Keyword();
		this.filter = "";
		this.valorFiltro = "";
	
	}
	public String cancelarFiltro(){
		cancelFilter();
		arrayKeywords.clear();
		listar();
	    return "";
	}
	
	public void filtrar(){
		try{
			if ((this.filter != null)&&(this.filter != "")&&(!this.filter.equals(""))){
				if ((this.filter == "word") ||  (this.filter.equals("word"))) {
					
					keywords = keywordService.getByLikeName(valorFiltro);
					limparArray();
					listarArray();
					
				}else
			    if ((this.filter == "id")||  (this.filter.equals("id"))){			    	
			    	
			    	keyword = keywordService.getById(Long.parseLong(valorFiltro));
			    	keywords = new ArrayList<Keyword>();
			    	keywords.add(keyword);
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
	public Keyword getSelectedEntity(){
		return keyword;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	
	public String rowSelect(){
		setMessageChar("OK", "teste");
		return "";
	}
	//--- Lista
	
	//----- CRUD
	public String novo(){
		readOnly = false;
		this.keyword = new Keyword();
		return "/keyword/formKeyword.xhtml";
	}
	
	public String editar(){
		if (keyword.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		 else{
		   readOnly = false;		
		   return "/keyword/formKeyword.xhtml";
		 }
		return null;
	}
	
	public void deletar(){
		try{
		  if (keyword.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		  else{
			  List<Paper> papers = paperService.getPaperByKeyword(keyword.getId());
			  
			  if ( papers.isEmpty() ){
				  keywordService.deletar(keyword); 
				  deletarArray(keyword);			  
				  setMessageKey("OK", "keyword.del.sucesso");
			  }
			  else {
				  setMessageKey("ERRO", "keyword.del.ex_relacao");
			  } 
		  }
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage());  
		}
	}
	
	public String cadastrar(){	
		try{
			
			if (keyword.getId()== null){
				List<Keyword> list = keywordService.getByName(keyword.getWord());
					
				if ( list.isEmpty()){	
					keywordService.salvar(keyword); 
					addArray(keyword);	
					setMessageKey("OK", "keyword.cad.sucesso");
				}
				else{
					setKeyword(list.get(0));
					addArray(list.get(0));	
					setMessageKey("ERRO", "keyword.ja_cadastrado");	
				}
			}
			else{
				keywordService.salvar(keyword); 
				setMessageKey("OK", "keyword.atu.sucesso");
			}				
		}
		catch(Exception e){
			setMessageKey("ERRO", e.getMessage());  
		}			
		setKeyword(new Keyword());	
		
		return "/keyword/listKeyword.xhtml";
	}
	//----- CRUD
	
}
