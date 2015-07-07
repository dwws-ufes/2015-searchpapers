package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Estado;
import searchpapers.domain.Paper;
import searchpapers.domain.Usuario;
import searchpapers.domain.Usuario_;

@Stateless
public class UsuarioJPADAO implements UsuarioDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Usuario getUsuario(String nome, String senha) { 
		try { 
			    String jpql = "Select u from Usuario u where u.nome = :nome and u.senha = :senha";
				TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
				query.setParameter("nome", nome);
				query.setParameter("senha", senha);
			    
				return query.getSingleResult();
				
			} catch (NoResultException e) {
				return null; 
				} 
		}

	public boolean inserirUsuario(Usuario usuario) { 
		try { 
			entityManager.persist(usuario); 
			return true; 
			} catch (Exception e) { 
				e.printStackTrace(); 
				return false; 
				} 
		}

	public boolean deletarUsuario(Usuario usuario) { 
		try { 
			entityManager.remove(usuario); 
			return true; 
			} catch (Exception e) { 
				e.printStackTrace(); 
				return false; 
				} 
		}

	public Usuario salvar(Usuario object) {
		
		if (object.isPersistent()) entityManager.merge(object);
		else entityManager.persist(object);
		
		return object;
	}
	
	public List<Usuario> getByName(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> root = cq.from(Usuario.class);
		cq.where(cb.equal(cb.lower(root.get(Usuario_.nome)), name.toLowerCase()));
		return (List<Usuario>) entityManager.createQuery(cq).getResultList();
	}
	
	public List<Usuario> getUsuarios(){

		String jpql = "select p from Usuario p ORDER BY p.nome ASC";
		TypedQuery<Usuario> query = (TypedQuery<Usuario>) entityManager.createQuery(jpql, Usuario.class);
	    
		return query.getResultList();		
	}
	
	public Usuario getById(Long id){
		Usuario result = (Usuario) entityManager.find(Usuario.class, id);		
		return result;
	}
	
	public List<Usuario> getUsuarioByPaper(Long id){

		String jpql = "select u from Paper p join p.usuarios u where p.id = :id";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
		query.setParameter("id", id);
	    
		return query.getResultList();		
	}

}
