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

import searchpapers.domain.Journal;
import searchpapers.persistence.JournalDAO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

@WebServlet(urlPatterns = { "/data/journal/*" })
public class JournalRDF extends HttpServlet {
	@EJB
	private JournalDAO journalDAO;
	
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
			model = getRDFjournals(model, myNS);
		}
		else{
			Journal journal = journalDAO.getById(id);
			if (journal != null){
				model = getRDFjournal(model, myNS, journal);
			}
		}

		try(PrintWriter out = response.getWriter()){
			  model.write(out, "RDF/XML-ABBREV");
		}		
	}	
	
	private Model getRDFjournals(Model model, String myNS){
	    
	    List<Journal> journals = journalDAO.getJournals();
	    
	    for(int i =0;i<journals.size();i++){		    	
	    	Journal journal = journals.get(i);	    	
	    	model = getRDFjournal(model, myNS, journal);	    	
	    }
	    
	    return model;
	}
	
	
	private Model getRDFjournal(Model model, String myNS, Journal journal){				
    	    	
		Resource resourceJournal = model.createResource(myNS+"journal/"+journal.getId());
		resourceJournal.addProperty(RDF.type, SPAPER.JOURNAL);
		resourceJournal.addProperty(SPAPER.NAME, journal.getName());
		
    	return model;
	}

}
