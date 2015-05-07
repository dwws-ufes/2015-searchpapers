package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Institute;
import searchpapers.domain.Institute_;
import searchpapers.domain.Keyword;
import searchpapers.domain.Keyword_;

@Stateless
public class KeywordJPADAO implements KeywordDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	public Keyword salvar(Keyword objeto){
		if (objeto.isPersistent()) {
			entityManager.merge(objeto);
		}
		else {
			entityManager.persist(objeto);
		}		
		return objeto;
	}
	
	public void deletar(Keyword object) {	
		entityManager.remove(entityManager.merge(object));
	}
	
	public List<Keyword> getKeywords(){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Keyword> cq = cb.createQuery(Keyword.class);
		cq.from(Keyword.class);
	
		 List<Keyword> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}
	
	public Keyword getById(Long id){
		Keyword result = (Keyword) entityManager.find(Keyword.class, id);		
		return result;
	}
	
	public List<Keyword> getByName(String word){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Keyword> cq = cb.createQuery(Keyword.class);
		Root<Keyword> root = cq.from(Keyword.class);
		cq.where(cb.equal(cb.lower(root.get(Keyword_.word)), word.toLowerCase()));
		return (List<Keyword>) entityManager.createQuery(cq).getResultList();
	}
	
	public List<Keyword> getByLikeName(String word){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Keyword> cq = cb.createQuery(Keyword.class);
		Root<Keyword> root = cq.from(Keyword.class);
		cq.where(cb.like(cb.lower(root.get(Keyword_.word)), "%" + word.toLowerCase() + "%"));
		return (List<Keyword>) entityManager.createQuery(cq).getResultList();
	}

	
}
