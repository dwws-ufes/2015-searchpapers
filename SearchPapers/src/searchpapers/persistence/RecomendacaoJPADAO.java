package searchpapers.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import searchpapers.domain.Estado;
import searchpapers.domain.Institute_;
import searchpapers.domain.Paper;
import searchpapers.domain.Recomendacao;
import searchpapers.domain.Recomendacao_;
import searchpapers.domain.Usuario;

@Stateless
public class RecomendacaoJPADAO implements RecomendacaoDAO {@PersistenceContext
	private EntityManager entityManager;
	
	public Recomendacao salvar(Recomendacao object){
		
		if (object.isPersistent()) entityManager.merge(object);
		else entityManager.persist(object);
		
		return object;
	}
	
	public void deletar(Recomendacao object) {
		
		entityManager.remove(entityManager.merge(object));
	}
	
	public List<Recomendacao> getRecomendacoes(){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recomendacao> cq = cb.createQuery(Recomendacao.class);
		cq.from(Recomendacao.class);
	
		 List<Recomendacao> result = entityManager.createQuery(cq).getResultList();
		
		 return result;
	}
	
	public Recomendacao getById(Long id){
		Recomendacao result = (Recomendacao) entityManager.find(Recomendacao.class, id);
		
		return result;
	}
	
	public Recomendacao getById(Long id, Usuario destinatario){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recomendacao> cq = cb.createQuery(Recomendacao.class);
		Root<Recomendacao> root = cq.from(Recomendacao.class);
		cq.where(cb.equal(root.get(Recomendacao_.destinatario), destinatario));
		cq.where(cb.equal(root.get(Recomendacao_.id), id));
		
		Recomendacao result = (Recomendacao) entityManager.createQuery(cq).getSingleResult();		
		return result;
	}
	
	public List<Recomendacao> getByDestinatario(Usuario destinatario){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recomendacao> cq = cb.createQuery(Recomendacao.class);
		Root<Recomendacao> root = cq.from(Recomendacao.class);
		cq.where(cb.equal(root.get(Recomendacao_.destinatario), destinatario));
		return (List<Recomendacao>) entityManager.createQuery(cq).getResultList();	
	}
	
	@Override
	public List<Recomendacao> getByLikeTituloArtigo(String titulo, Usuario destinatario) {
		Long id = destinatario.getId();
		String jpql = "select r from Recomendacao r "+
					  " join r.paper p "+
					  " join r.destinatario d "+
					  "	where UPPER(p.title) LIKE '%" + titulo.toUpperCase().trim() + "%' "+
					  " and d.id = :id ";
		TypedQuery<Recomendacao> query = entityManager.createQuery(jpql, Recomendacao.class);
		query.setParameter("id", id);

		return query.getResultList();
	}
	
	@Override
	public List<Recomendacao> getByLikeMensagem(String mensagem, Usuario destinatario) {
		Long id = destinatario.getId();
		String jpql = "select r from Recomendacao r "+
					  " join r.destinatario d "+
					  "	where UPPER(r.mensagem) LIKE '%" + mensagem.toUpperCase().trim() + "%' "+
					  " and d.id = :id ";
		TypedQuery<Recomendacao> query = entityManager.createQuery(jpql, Recomendacao.class);
		query.setParameter("id", id);
		return query.getResultList();
	}
}
