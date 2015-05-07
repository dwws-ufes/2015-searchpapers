package searchpapers.persistence;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Keyword;

@Local
public interface KeywordDAO {

	Keyword salvar(Keyword object);
	void deletar(Keyword object);
	List<Keyword> getKeywords();
    Keyword getById(Long id);
    List<Keyword> getByLikeName(String word);
    List<Keyword> getByName(String word);
}
