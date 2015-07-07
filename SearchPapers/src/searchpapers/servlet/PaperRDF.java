package searchpapers.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import searchpapers.domain.Author;
import searchpapers.domain.Journal;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.persistence.PaperDAO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Servlet implementation class ListPaperInRdfServlet
 */
@WebServlet("/data/paper/*")
public class PaperRDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private PaperDAO paperDAO;
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/xml");
		
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix( "spaper", "http://localhost:8080/SearchPapers/" );		
		String myNS = "http://localhost:8080/SearchPapers/data/";
		
		Long id = null;
		
		try{ 
	    	id = Long.valueOf(request.getPathInfo().substring(1)); 
	    }
		catch(Exception e){} 
		
		if (id == null){
			model = getRDFpapers(model, myNS);
		}
		else{
			Paper paper = paperDAO.getById(id);
			if (paper != null){
				model = getRDFpaper(model, myNS, paper);
			}
		}

		try(PrintWriter out = response.getWriter()){
			  model.write(out, "RDF/XML-ABBREV");
		}
	}
	
	
	private Model getRDFpapers(Model model, String myNS){
			    
	    List<Paper> papers = paperDAO.getPapers();
	    
	    for(int i =0;i<papers.size();i++){		    	
	    	Paper paper = papers.get(i);	    	
	    	model = getRDFpaper(model, myNS, paper);	    	
	    }
	    
	    return model;
	}
	
	private Model getRDFpaper(Model model, String myNS, Paper paper){
		
		Resource resourcePaper = model.createResource(myNS+"paper/"+paper.getId());
    	resourcePaper.addProperty(RDF.type, SPAPER.PAPER);
    	resourcePaper.addProperty(SPAPER.TITLE, paper.getTitle());
    	
    	if (paper.getSummary() != null && !paper.getSummary().equals(null)){
    		resourcePaper.addProperty(SPAPER.SUMMARY, paper.getSummary());
    	}
    	if (paper.getYear() != null && !paper.getYear().equals(null)){
    		resourcePaper.addProperty(SPAPER.YEAR, paper.getYear().toString());
    	}
    	if (paper.getUrlPaper() != null && !paper.getUrlPaper().equals(null)){
    		resourcePaper.addProperty(SPAPER.WEB_ADDRESS, paper.getUrlPaper());
    	}
    	
    	if (paper.getJournal() != null && !paper.getJournal().equals(null)){
    		Journal journal = paper.getJournal();
    		Resource resourceJournal = model.createResource(myNS+"journal/"+journal.getId());
    		resourceJournal.addProperty(RDF.type, SPAPER.JOURNAL);
    		resourceJournal.addProperty(SPAPER.NAME, journal.getName());
    		
    		resourcePaper.addProperty(SPAPER.HAS_JOURNAL, resourceJournal);
    	}
    	
    	if (!paper.getKeywords().isEmpty()){
    		for(int i =0;i<paper.getKeywords().size();i++){	
    			Keyword keyword = paper.getKeywords().get(i);
    			Resource resourceKeyword = model.createResource(myNS+"keyword/"+keyword.getId());
    			resourceKeyword.addProperty(RDF.type, SPAPER.KEYWORD);
    			resourceKeyword.addProperty(SPAPER.WORD, keyword.getWord());
    			
    			resourcePaper.addProperty(SPAPER.HAS_KEYWORD, resourceKeyword);
    		}    		
    	}
    	
    	if (!paper.getAuthors().isEmpty()){
    		for(int i =0;i<paper.getAuthors().size();i++){	
    			Author author = paper.getAuthors().get(i);
    			Resource resourceAuthor = model.createResource(myNS+"author/"+author.getId());
    			resourceAuthor.addProperty(RDF.type, SPAPER.AUTHOR);
    			resourceAuthor.addProperty(SPAPER.NAME, author.getName());
    			
    			if (author.getEmail() != null && !author.getEmail().equals(null)){
    				resourceAuthor.addProperty(SPAPER.EMAIL, author.getEmail()); 
    			}
    			
    			resourcePaper.addProperty(SPAPER.HAS_AUTHOR, resourceAuthor);
    		}
    	}

    	return model;
	}
	

}
