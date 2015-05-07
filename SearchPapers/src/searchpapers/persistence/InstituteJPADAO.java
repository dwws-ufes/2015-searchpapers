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

@Stateless
public class InstituteJPADAO implements InstituteDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Institute salvar(Institute object) {
		if (object.isPersistent()) entityManager.merge(object);
		else entityManager.persist(object);
		
		return object;
	}

	@Override
	public void deletar(Institute object) {
		entityManager.remove(entityManager.merge(object));
	}

	@Override
	public List<Institute> getInstitutes() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Institute> cq = cb.createQuery(Institute.class);
		cq.from(Institute.class);
	
		 List<Institute> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}

	@Override
	public Institute getById(Long id) {
		Institute result = (Institute) entityManager.find(Institute.class, id);
		
		return result;
	}

	@Override
	public List<Institute> getByLikeName(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Institute> cq = cb.createQuery(Institute.class);
		Root<Institute> root = cq.from(Institute.class);
		cq.where(cb.like(cb.lower(root.get(Institute_.name)), "%" + name.toLowerCase() + "%"));
		return (List<Institute>) entityManager.createQuery(cq).getResultList();
	}

	
	public List<Institute> getByName(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Institute> cq = cb.createQuery(Institute.class);
		Root<Institute> root = cq.from(Institute.class);
		cq.where(cb.equal(cb.lower(root.get(Institute_.name)), name.toLowerCase()));
		return (List<Institute>) entityManager.createQuery(cq).getResultList();
	}

}
