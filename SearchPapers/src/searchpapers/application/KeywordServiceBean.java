package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Institute;
import searchpapers.domain.Keyword;
import searchpapers.persistence.KeywordDAO;

@Stateless
public class KeywordServiceBean implements KeywordService {

	@EJB private KeywordDAO keywordDAO;
	private List<Keyword> keywords;
	
	
	public List<Keyword> getKeywords(){
		
		if (keywords == null){
			keywords = new ArrayList<Keyword>();
			keywords.addAll(keywordDAO.getKeywords());
		}
		
		Collections.sort( keywords, new Comparator<Keyword>(){
			@Override
			public int compare(Keyword arg0, Keyword arg1) {
				// TODO Auto-generated method stub
				return arg0.getWord().compareTo(arg1.getWord());
			}			
		});
				
		return keywords;
	}
	
	public Keyword salvar(Keyword objeto){
		return keywordDAO.salvar(objeto);		
	}
	
	public Keyword atualizar(Keyword objeto){
		return keywordDAO.salvar(objeto);		
	}
	
	
	public void deletar(Keyword objeto){
		objeto = keywordDAO.getById(objeto.getId());
		if (objeto != null){
			keywordDAO.deletar(objeto);
		}	
		else {
			//mensagem de erroooooo
		}
	}
	
	public List<Keyword> getByName(String word){
		return keywordDAO.getByName(word);
	}
	
	public List<Keyword> getByLikeName(String word){
		return keywordDAO.getByLikeName(word);
	}
	
	public Keyword getById(Long id) {
		return keywordDAO.getById(id);
	}
	
}
