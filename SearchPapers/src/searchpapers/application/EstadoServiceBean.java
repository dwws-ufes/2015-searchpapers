package searchpapers.application;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Estado;
import searchpapers.persistence.EstadoDAO;

@Stateless
public class EstadoServiceBean implements EstadoService {

	@EJB private EstadoDAO estadoDAO;
	
    public List<Estado> getEstados(){
							
		return estadoDAO.getEstados();
	}
}
