package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.persistence.AuthorDAO;

@Stateless
public class AuthorServiceBean implements AuthorService {
	
	@EJB private AuthorDAO authorDAO;
	
	
    private List<Author> authors;
	
	
	public List<Author> getAuthors(){
		
		if (authors == null){
			authors = new ArrayList<Author>();
			authors.addAll(authorDAO.getAuthors());
		}
		
		Collections.sort( authors, new Comparator<Author>(){
			@Override
			public int compare(Author arg0, Author arg1) {
				// TODO Auto-generated method stub
				return arg0.getName().compareTo(arg1.getName());
			}			
		});
				
		return authors;
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
}
