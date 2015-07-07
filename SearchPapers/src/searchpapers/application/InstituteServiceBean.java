package searchpapers.application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

import searchpapers.domain.Institute;
import searchpapers.persistence.InstituteDAO;
import searchpapers.sparql.SparqlProperties;

@Stateless
public class InstituteServiceBean implements InstituteService {

	@EJB private InstituteDAO instituteDAO;
	
	@EJB private SparqlProperties sparqlProperties;
	
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
	
	public Institute getInstituteWeb(String name) { 
		System.out.println("entrou = carrega Institute web");

		if (name != null && name.length() > 0) {
						
			Institute institute = new Institute();
			
			String query = sparqlProperties.getProp("prop.prefixes.wiki") +
						   sparqlProperties.getProp("prop.wiki.getInfoInstitute");			       
			       query = query.replace(":name", name);
			       
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://wiki.rkbexplorer.com/sparql/", query);
			ResultSet results = queryExecution.execSelect();
			if (results.hasNext()) {
				QuerySolution querySolution = results.next();
				institute.setName(querySolution.getLiteral("nameInstitute").toString());
				institute.setWebAdress(querySolution.getLiteral("webAdress").toString());											
			}
			
			return institute;
		}
		else{
			return null;
		}		
	}

	
}
