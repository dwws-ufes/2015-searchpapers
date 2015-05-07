package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Author;
import searchpapers.domain.Author_;
import searchpapers.domain.Institute;

@Stateless
public class AuthorJPADAO implements AuthorDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Author salvar(Author object){
		
		if (object.isPersistent()) entityManager.merge(object);
		else entityManager.persist(object);
		
		return object;
	}
	
	public void deletar(Author object) {
		
		entityManager.remove(entityManager.merge(object));
	}
	
	public List<Author> getAuthors(){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
		cq.from(Author.class);
	
		 List<Author> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}
	
	public Author getById(Long id){
		Author result = (Author) entityManager.find(Author.class, id);
		
		return result;
	}
	
	public List<Author> getByLikeName(String name){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
		Root<Author> root = cq.from(Author.class);
		cq.where(cb.like(cb.lower(root.get(Author_.name)), "%" + name.toLowerCase() + "%"));
		return (List<Author>) entityManager.createQuery(cq).getResultList();	
	}
	
	public List<Author> getByName(String name){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
		Root<Author> root = cq.from(Author.class);
		cq.where(cb.equal(cb.lower(root.get(Author_.name)), name.toLowerCase()));
		return (List<Author>) entityManager.createQuery(cq).getResultList();	
	}
	
	public List<Author> getByEmail(String email){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
		Root<Author> root = cq.from(Author.class);
		cq.where(cb.like(cb.lower(root.get(Author_.email)), "%" + email.toLowerCase() + "%"));
		return (List<Author>) entityManager.createQuery(cq).getResultList();
	}
	
	public List<Author> getByInstitute(Institute institute){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
		Root<Author> root = cq.from(Author.class);
		cq.where(cb.equal((root.get(Author_.institute)), institute));
		return (List<Author>) entityManager.createQuery(cq).getResultList();	
		
	}
	
}
