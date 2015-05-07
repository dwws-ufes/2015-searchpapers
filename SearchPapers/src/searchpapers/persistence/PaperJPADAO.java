package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Paper;
import searchpapers.domain.Paper_;

@Stateless
public class PaperJPADAO implements PaperDAO {
	@PersistenceContext
	private EntityManager entityManager;

	
	public Paper salvar(Paper objeto){
		if (objeto.isPersistent()) {
			entityManager.merge(objeto);
		}
		else {
			entityManager.persist(objeto);
		}
		
		return objeto;
	}
	
	public void deletar(Paper objeto) {	
		entityManager.remove(entityManager.merge(objeto));
	}
	
	public List<Paper> getPapers(){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Paper> cq = cb.createQuery(Paper.class);
		cq.from(Paper.class);
	
		 List<Paper> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}
	
	public Paper getById(Long id){
		Paper result = (Paper) entityManager.find(Paper.class, id);		
		return result;
	}
	
	public List<Paper> getByName(String title){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Paper> cq = cb.createQuery(Paper.class);
		Root<Paper> root = cq.from(Paper.class);
		cq.where(cb.equal(cb.lower(root.get(Paper_.title)), title.toLowerCase()));
		return (List<Paper>) entityManager.createQuery(cq).getResultList();
	}
	
	public List<Paper> getByLikeName(String title){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Paper> cq = cb.createQuery(Paper.class);
		Root<Paper> root = cq.from(Paper.class);
		cq.where(cb.like(cb.lower(root.get(Paper_.title)), "%" + title.toLowerCase() + "%"));
		return (List<Paper>) entityManager.createQuery(cq).getResultList();
	}
	
	
	public List<Paper> getPaperByKeyword(Long id){

		String jpql = "select p from Paper p join p.keywords k where k.id = :id";
		TypedQuery<Paper> query = entityManager.createQuery(jpql, Paper.class);
		query.setParameter("id", id);
	    
		return query.getResultList();		
	}
	
	public List<Paper> getPaperByAuthor(Long id){

		String jpql = "select p from Paper p join p.authors a where a.id = :id";
		TypedQuery<Paper> query = entityManager.createQuery(jpql, Paper.class);
		query.setParameter("id", id);
	    
		return query.getResultList();		
	}
	
}
