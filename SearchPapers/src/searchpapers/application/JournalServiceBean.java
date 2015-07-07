package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Journal;
import searchpapers.persistence.JournalDAO;

@Stateless
public class JournalServiceBean implements JournalService {

	@EJB private JournalDAO journalDAO;
	
	private List<Journal> journals;
	
	@Override
	public List<Journal> getJournals() {
		if (journals == null){
			journals = new ArrayList<Journal>();
			journals.addAll(journalDAO.getJournals());
		}
		
		Collections.sort( journals, new Comparator<Journal>(){
			@Override
			public int compare(Journal arg0, Journal arg1) {
				// TODO Auto-generated method stub
				return arg0.getName().compareTo(arg1.getName());
			}			
		});
				
		return journals;
	}

	@Override
	public Journal salvar(Journal objeto) {
		return journalDAO.salvar(objeto);	
	}

	@Override
	public Journal atualizar(Journal objeto) {
		return journalDAO.salvar(objeto);	
	}

	@Override
	public void deletar(Journal objeto) {
		objeto = journalDAO.getById(objeto.getId());
		if (objeto != null){
			journalDAO.deletar(objeto);
		}
		
	}

	public List<Journal> getByName(String name) {
		return journalDAO.getByName(name);
	}

	@Override
	public Journal getById(Long id) {
		return journalDAO.getById(id);
	}

	
}
