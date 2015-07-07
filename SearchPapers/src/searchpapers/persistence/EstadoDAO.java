package searchpapers.persistence;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Estado;

@Local
public interface EstadoDAO {

	List<Estado> getEstados();
}
