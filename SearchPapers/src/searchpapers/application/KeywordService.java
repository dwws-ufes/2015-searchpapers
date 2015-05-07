package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Keyword;

@Local
public interface KeywordService {

	 List<Keyword> getKeywords();
	 Keyword salvar(Keyword objeto);
	 Keyword atualizar(Keyword objeto);
	 void deletar(Keyword objeto);	
	 Keyword getById(Long id);
	 List<Keyword> getByName(String word);
	 List<Keyword> getByLikeName(String word);
}
