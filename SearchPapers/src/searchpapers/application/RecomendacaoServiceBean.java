package searchpapers.application;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Author;
import searchpapers.domain.Recomendacao;
import searchpapers.domain.Usuario;
import searchpapers.persistence.RecomendacaoDAO;

@Stateless
public class RecomendacaoServiceBean implements RecomendacaoService {

	@EJB private RecomendacaoDAO recomendacaoDAO;
	
	@Override
	public Recomendacao salvar(Recomendacao objeto) {
		return recomendacaoDAO.salvar(objeto);
	}

	public Recomendacao atualizar(Recomendacao objeto){
		return recomendacaoDAO.salvar(objeto);
	}
	@Override
	public void deletar(Recomendacao objeto) {
		objeto = recomendacaoDAO.getById(objeto.getId());
		if (objeto != null){
			recomendacaoDAO.deletar(objeto);
		}	
	}

	@Override
	public List<Recomendacao> getRecomendacoes() {
		return recomendacaoDAO.getRecomendacoes();
	}
	
	public Recomendacao getById(Long id){
		return recomendacaoDAO.getById(id);
	}
	
	public Recomendacao getById(Long id, Usuario destinatario){
		return recomendacaoDAO.getById(id, destinatario);
	}

	@Override
	public List<Recomendacao> getByDestinatario(Usuario destinatario) {
		return recomendacaoDAO.getByDestinatario(destinatario);
	}

	@Override
	public List<Recomendacao> getByLikeMensagem(String mensagem, Usuario destinatario) {
		
		return recomendacaoDAO.getByLikeMensagem(mensagem, destinatario);
	}

	@Override
	public List<Recomendacao> getByLikeTituloArtigo(String titulo, Usuario destinatario) {
		
		return recomendacaoDAO.getByLikeTituloArtigo(titulo, destinatario);
	}

}
