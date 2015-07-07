package searchpapers.persistence;

import java.util.List;

import searchpapers.domain.Journal;

public interface JournalDAO {

	Journal salvar(Journal object);
	void deletar(Journal object);
	List<Journal> getJournals();
	Journal getById(Long id);
	List<Journal> getByName(String name);
}
