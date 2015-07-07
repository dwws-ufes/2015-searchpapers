package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Cidade;

@Local
public interface CidadeService {

	List<Cidade> getCidadesByEstado(Long id);
}
