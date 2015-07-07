package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Journal;
import searchpapers.domain.Journal_;

@Stateless
public class JournalJPADAO implements JournalDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Journal salvar(Journal object) {
		if (object.isPersistent()) entityManager.merge(object);
		else entityManager.persist(object);
		
		return object;
	}

	@Override
	public void deletar(Journal object) {
		entityManager.remove(entityManager.merge(object));
	}

	@Override
	public List<Journal> getJournals() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Journal> cq = cb.createQuery(Journal.class);
		cq.from(Journal.class);
	
		 List<Journal> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}

	@Override
	public Journal getById(Long id) {
		Journal result = (Journal) entityManager.find(Journal.class, id);		
		return result;
	}

	
	public List<Journal> getByName(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Journal> cq = cb.createQuery(Journal.class);
		Root<Journal> root = cq.from(Journal.class);
		cq.where(cb.equal(cb.lower(root.get(Journal_.name)), name.toLowerCase()));
		return (List<Journal>) entityManager.createQuery(cq).getResultList();
	}

}
