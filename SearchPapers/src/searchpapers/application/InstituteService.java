package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Institute;
import searchpapers.domain.Institute;

@Local
public interface InstituteService {

	 List<Institute> getInstitutes();
	 Institute salvar(Institute objeto);
	 Institute atualizar(Institute objeto);
	 void deletar(Institute objeto);
	 List<Institute> getByName(String name);
	 List<Institute> getByLikeName(String name);
	 Institute getById(Long id);
	 //Institute getByAuthor(Institute institute);
	 
	 Institute getInstituteWeb(String name);
}
