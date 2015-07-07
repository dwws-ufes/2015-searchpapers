package searchpapers.persistence;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Cidade;

@Local
public interface CidadeDAO {

	List<Cidade> getCidadesByEstado(Long id);
}
