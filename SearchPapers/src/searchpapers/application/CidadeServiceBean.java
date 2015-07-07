package searchpapers.application;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Cidade;
import searchpapers.persistence.CidadeDAO;

@Stateless
public class CidadeServiceBean implements CidadeService {

	@EJB private CidadeDAO cidadeDAO;
	
	public List<Cidade> getCidadesByEstado(Long id){
		return cidadeDAO.getCidadesByEstado(id);
	}
}
