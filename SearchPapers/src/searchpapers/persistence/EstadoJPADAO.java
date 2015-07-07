package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import searchpapers.domain.Estado;

@Stateless
public class EstadoJPADAO implements EstadoDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Estado> getEstados(){

		String jpql = "select p from Estado p ORDER BY p.descricao ASC";
		TypedQuery<Estado> query = (TypedQuery<Estado>) entityManager.createQuery(jpql, Estado.class);
	    
		return query.getResultList();		
	}
}
