package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Recomendacao;
import searchpapers.domain.Usuario;

@Local
public interface RecomendacaoService {
	 List<Recomendacao> getRecomendacoes();
	 Recomendacao salvar(Recomendacao objeto);
	 Recomendacao atualizar(Recomendacao objeto);
	 void deletar(Recomendacao objeto);	 
	 Recomendacao getById(Long id);
	 Recomendacao getById(Long id, Usuario destinatario);
	 List<Recomendacao> getByDestinatario(Usuario destinatario);
	 List<Recomendacao> getByLikeMensagem(String mensagem, Usuario destinatario);
	 List<Recomendacao> getByLikeTituloArtigo(String titulo, Usuario destinatario);
}