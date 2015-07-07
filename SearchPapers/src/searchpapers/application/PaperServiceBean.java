package searchpapers.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Author;
import searchpapers.domain.Journal;
import searchpapers.domain.Paper;
import searchpapers.persistence.PaperDAO;
import searchpapers.sparql.SparqlProperties;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

@Stateless
public class PaperServiceBean implements PaperService {

	@EJB private PaperDAO paperDAO;
	
	@EJB private JournalService journalService;
	@EJB private AuthorService authorService;
	
	@EJB private SparqlProperties sparqlProperties;

	
	public List<Paper> getPapers(){
		
		List<Paper> papers = new ArrayList<Paper>();
		papers.addAll(paperDAO.getPapers());

		return papers;
	}
	
	public Paper salvar(Paper objeto){
		return paperDAO.salvar(objeto);		
	}
	
	public Paper atualizar(Paper objeto){
		return paperDAO.salvar(objeto);		
	}
	
	
	public void deletar(Paper objeto){
		objeto = paperDAO.getById(objeto.getId());
		if (objeto != null){
			paperDAO.deletar(objeto);
		}
	}
	
	public Paper getById(Long id){
		Paper result = paperDAO.getById(id);
		return result;
	}
	
	public List<Paper> getByName(String title){
		return paperDAO.getByName(title);
	}
	
	public List<Paper> getByLikeName(String title){
		return paperDAO.getByLikeName(title);
	}
	
	public List<Paper> getPaperByKeyword(Long id){
		return paperDAO.getPaperByKeyword(id);
	}
	
	public List<Paper> getPaperByAuthor(Long id){
		return paperDAO.getPaperByAuthor(id);
	}
	
	
	public Paper getPaperWeb(String title){
		
		Paper paper = getPaperPaperWeb(title);
		
		if (paper != null){
			List<Author> authors = getPaperAuthorWeb(paper);
			if (authors != null){
				paper.setAuthors(authors);	
			}			
			return paper;
		}
		else{
			return null;
		}			
	}
	
	
	private Paper getPaperPaperWeb(String title){
		
		if (title != null && title.length() > 0){			
			
			String query = sparqlProperties.getProp("prop.prefixes.dblp") + 
						   sparqlProperties.getProp("prop.dblp.getInfoPaperPaper");
			query = query.replace(":name", title.toLowerCase());
			
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dblp.rkbexplorer.com/sparql/" , query);			
			try{
				ResultSet results = queryExecution.execSelect();
							
				if(results.hasNext()) {
					
					QuerySolution solution = results.nextSolution();
					
					Paper paper = new Paper();
					paper.setTitle(solution.getLiteral("titulo").toString());
					paper.setYear(solution.getLiteral("sData").getLong());
					paper.setUrlPaper(solution.getLiteral("sEndereco").toString());
					
					String jornal = solution.getLiteral("sJornal").toString();
					List<Journal> listJournal = journalService.getByName(jornal);
					Journal tempJournal = new Journal();
					if ( listJournal.isEmpty()){
						tempJournal.setName(jornal);
						tempJournal = journalService.salvar(tempJournal);
				  	}
					else
					{
						tempJournal = listJournal.get(0);
					}
					paper.setJournal(tempJournal);
					
					return paper;
				}
				else {
					return null;
				}
			}catch(Exception e){ 
				return null;
			}
		}
		else{
			return null;
		}
	}

	private List<Author> getPaperAuthorWeb(Paper paper){
		if (paper != null){			
			String query = sparqlProperties.getProp("prop.prefixes.dblp") + 
						   sparqlProperties.getProp("prop.dblp.getInfoPaperAuthor");
			query = query.replace(":sTitle", paper.getTitle().toLowerCase());
			query = query.replace(":sJornal", paper.getJournal().getName().toLowerCase());
			query = query.replace(":sData", paper.getYear().toString().toLowerCase());
			
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dblp.rkbexplorer.com/sparql/" , query);			
			try{
				ResultSet results = queryExecution.execSelect();
					
				List<String> authorsURI = new ArrayList<String>();
				List<Author> authors = new ArrayList<Author>();	
				while(results.hasNext()) {
					QuerySolution solution = results.nextSolution();
					
					String authorURI = solution.getResource("autor").toString();
					String nmAuthor = solution.getLiteral("sAutor").toString();
					
					if (!authorsURI.contains(authorURI)) {
						authorsURI.add(authorURI);
						Author author = authorService.getAuthorWeb(nmAuthor);
						if (author == null){
							author = new Author();
							author.setName(nmAuthor);							
						}
						
						authorService.salvar(author);
						authors.add(author);	
					}
				}
				
				return authors;
			}catch(Exception e){ 
				System.out.println("erro carregar autores = "+e.getMessage()); 
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	
	public List<Paper> getPapersWeb(String name){
		
		if (name != null && name.length() > 0){
			String query = sparqlProperties.getProp("prop.prefixes.dblp") + 
						   sparqlProperties.getProp("prop.dblp.getInfoPapers");
			query = query.replace(":name", name.toLowerCase());
			
			QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dblp.rkbexplorer.com/sparql/" , query);			
			ResultSet results = queryExecution.execSelect();
			
			List<Paper> papers = new ArrayList<Paper>();
			
			while(results.hasNext()) {
				
				QuerySolution solution = results.nextSolution();
				
				Paper paper = new Paper();
				
				paper.setTitle(solution.getLiteral("titulo").toString());
				paper.setUrlPaper(solution.getLiteral("sEndereco").toString());
											
				papers.add(paper);				
			}		
			
			return papers;			
		}
		else {
			return null;
		}
	}
	
}
