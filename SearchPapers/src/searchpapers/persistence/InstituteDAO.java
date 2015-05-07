package searchpapers.persistence;

import java.util.List;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;

public interface InstituteDAO {

	Institute salvar(Institute object);
	void deletar(Institute object);
	List<Institute> getInstitutes();
	Institute getById(Long id);
	List<Institute> getByLikeName(String name);
	List<Institute> getByName(String name);
}
