package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import searchpapers.domain.Cidade;

@Stateless
public class CidadeJPADAO implements CidadeDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Cidade> getCidadesByEstado(Long id){

		String jpql = "select p from Cidade p join p.estado k where k.id = :id ORDER BY p.nome ASC";
		TypedQuery<Cidade> query = entityManager.createQuery(jpql,  Cidade.class);
		query.setParameter("id", id);
	    
		return query.getResultList();		
	}
}
