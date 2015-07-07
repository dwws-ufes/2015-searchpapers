package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Journal;

@Local
public interface JournalService {

	 List<Journal> getJournals();
	 Journal salvar(Journal objeto);
	 Journal atualizar(Journal objeto);
	 void deletar(Journal objeto);
	 List<Journal> getByName(String name);
	 Journal getById(Long id);
}
