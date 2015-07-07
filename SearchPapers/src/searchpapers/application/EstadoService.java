package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Estado;

@Local
public interface EstadoService {

	List<Estado> getEstados();
}
