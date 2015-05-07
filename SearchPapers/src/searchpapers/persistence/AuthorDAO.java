package searchpapers.persistence;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Keyword;
import br.ufes.inf.nemo.util.ejb3.persistence.BaseDAO;

@Local
public interface AuthorDAO{

	Author salvar(Author object);
	void deletar(Author object);
	List<Author> getAuthors();
	Author getById(Long id);
	List<Author> getByName(String name);
	List<Author> getByLikeName(String name);
	List<Author> getByEmail(String email);
	List<Author> getByInstitute(Institute institute);
}
