package searchpapers.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import searchpapers.application.KeywordService;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class PaperControllerKeyword extends MessageController implements Serializable {

	@EJB private KeywordService keywordService;
	
	private Keyword keyword = new Keyword();
	public Keyword getKeyword(){
		return keyword;
	}
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	
	//--- Array keyword
	private Map<Long, Keyword> arrayKeywords = new TreeMap<Long, Keyword>();
	public void setArrayKeywords(Map<Long, Keyword> arrayKeywords) {
		this.arrayKeywords = arrayKeywords;
	}
	public List<Keyword> getArrayKeywords() {			
		return new ArrayList<Keyword>(arrayKeywords.values());
	}	
	
	private void addArrayKeyword(Keyword k) {
		arrayKeywords.put(k.getId(), k);
	}		
	private void delArrayKeyword(Keyword k) {		
		arrayKeywords.remove(k.getId());
	}	
	public void limparArrayKeyword(){
		arrayKeywords.clear();
	}
	public void listarArrayKeyword(Paper paper){	
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
				setMessageKey("OK", "keyword.cad.sucesso");
			}
			else{
				setKeyword(list.get(0));
				addArrayKeyword(list.get(0));	
				setMessageKey("OK", "keyword.add.sucesso");
			}
		}
		catch(Exception e){
			setMessageKey("ERRO", e.getMessage());  
		}			
		setKeyword(new Keyword());						
	}
	
	public void deletarKeyword(Keyword k){	
		delArrayKeyword(k);
		setMessageKey("OK", "keyword.del.sucesso");
	}		
	//--- Cadastro keyword	
	
}
