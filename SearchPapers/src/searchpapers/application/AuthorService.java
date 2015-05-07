package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;

@Local
public interface AuthorService {
	 List<Author> getAuthors();
	 Author salvar(Author objeto);
	 Author atualizar(Author objeto);
	 void deletar(Author objeto);
	 List<Author> getByName(String name);
	 List<Author> getByLikeName(String name);
	 List<Author> getByEmail(String email);
	 List<Author> getByInstitute(Institute institute);
	 Author getById(Long id);
}
