package searchpapers.persistence;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Institute;
import searchpapers.domain.Recomendacao;
import searchpapers.domain.Usuario;

@Local
public interface RecomendacaoDAO {

	Recomendacao salvar(Recomendacao object);
	void deletar(Recomendacao object);
	List<Recomendacao> getRecomendacoes();
	Recomendacao getById(Long id);
	Recomendacao getById(Long id,  Usuario destinatario);
	List<Recomendacao> getByDestinatario(Usuario destinatario);
	List<Recomendacao> getByLikeMensagem(String mensagem, Usuario destinatario);
	List<Recomendacao> getByLikeTituloArtigo(String titulo, Usuario destinatario);
}
