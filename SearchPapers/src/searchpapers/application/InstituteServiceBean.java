package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Institute;
import searchpapers.persistence.InstituteDAO;

@Stateless
public class InstituteServiceBean implements InstituteService {

	@EJB private InstituteDAO instituteDAO;
	
	private List<Institute> institutes;
	
	@Override
	public List<Institute> getInstitutes() {
		if (institutes == null){
			institutes = new ArrayList<Institute>();
			institutes.addAll(instituteDAO.getInstitutes());
		}
		
		Collections.sort( institutes, new Comparator<Institute>(){
			@Override
			public int compare(Institute arg0, Institute arg1) {
				// TODO Auto-generated method stub
				return arg0.getName().compareTo(arg1.getName());
			}			
		});
				
		return institutes;
	}

	@Override
	public Institute salvar(Institute objeto) {
		return instituteDAO.salvar(objeto);	
	}

	@Override
	public Institute atualizar(Institute objeto) {
		return instituteDAO.salvar(objeto);	
	}

	@Override
	public void deletar(Institute objeto) {
		objeto = instituteDAO.getById(objeto.getId());
		if (objeto != null){
			instituteDAO.deletar(objeto);
		}
		
	}


	public List<Institute> getByName(String name) {
		return instituteDAO.getByName(name);
	}
	

	public List<Institute> getByLikeName(String name) {
		return instituteDAO.getByLikeName(name);
	}

	@Override
	public Institute getById(Long id) {
		return instituteDAO.getById(id);
	}

	
}
