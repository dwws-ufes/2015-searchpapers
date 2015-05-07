package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.persistence.PaperDAO;

@Stateless
public class PaperServiceBean implements PaperService {

	@EJB private PaperDAO paperDAO;
	private List<Paper> papers;
	
	
	public List<Paper> getPapers(){
		
		if (papers == null){
			papers = new ArrayList<Paper>();
			papers.addAll(paperDAO.getPapers());
		}
		
		Collections.sort( papers, new Comparator<Paper>(){
			@Override
			public int compare(Paper arg0, Paper arg1) {
				return arg0.getTitle().compareTo(arg1.getTitle());
			}			
		});
				
		return papers;
	}
	
	public Paper salvar(Paper objeto){
		return paperDAO.salvar(objeto);		
	}
	
	public Paper atualizar(Paper objeto){
		return paperDAO.salvar(objeto);		
	}
	
	
	public void deletar(Paper objeto){
		objeto = paperDAO.getById(objeto.getId());
		if (objeto != null){
			paperDAO.deletar(objeto);
		}	
		else {
			//mensagem de erroooooo
		}
	}
	
	public Paper getById(Long id){
		Paper result = paperDAO.getById(id);
		return result;
	}
	
	public List<Paper> getByName(String title){
		return paperDAO.getByName(title);
	}
	
	public List<Paper> getByLikeName(String title){
		return paperDAO.getByLikeName(title);
	}
	
	public List<Paper> getPaperByKeyword(Long id){
		return paperDAO.getPaperByKeyword(id);
	}
	
	public List<Paper> getPaperByAuthor(Long id){
		return paperDAO.getPaperByAuthor(id);
	}
	
	
}
