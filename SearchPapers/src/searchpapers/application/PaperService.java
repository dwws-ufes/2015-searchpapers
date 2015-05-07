package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;

@Local
public interface PaperService {
	 List<Paper> getPapers();
	 Paper salvar(Paper objeto);
	 Paper atualizar(Paper objeto);
	 void deletar(Paper objeto);
	 Paper getById(Long id);
	 List<Paper> getByName(String title);
	 List<Paper> getByLikeName(String title);
	 List<Paper> getPaperByKeyword(Long id);
	 List<Paper> getPaperByAuthor(Long id);
}
