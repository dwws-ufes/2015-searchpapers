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

import searchpapers.domain.Institute;
import searchpapers.domain.Keyword;
import searchpapers.persistence.KeywordDAO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

@WebServlet(urlPatterns = { "/data/keyword/*" })
public class KeywordRDF extends HttpServlet {
	
	@EJB
	private KeywordDAO keywordDAO;
	
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
			model = getRDFkeywords(model, myNS);
		}
		else{
			Keyword keyword = keywordDAO.getById(id);
			if (keyword != null){
				model = getRDFkeyword(model, myNS, keyword);
			}
		}

		try(PrintWriter out = response.getWriter()){
			  model.write(out, "RDF/XML-ABBREV");
		}		
	}	
	
	private Model getRDFkeywords(Model model, String myNS){
	    
	    List<Keyword> keywords = keywordDAO.getKeywords();
	    
	    for(int i =0;i<keywords.size();i++){		    	
	    	Keyword keyword = keywords.get(i);	    	
	    	model = getRDFkeyword(model, myNS, keyword);	    	
	    }
	    
	    return model;
	}
	
	
	private Model getRDFkeyword(Model model, String myNS, Keyword keyword){				
    	    	
		Resource resourceKeyword = model.createResource(myNS+"keyword/"+keyword.getId());
		resourceKeyword.addProperty(RDF.type, SPAPER.KEYWORD);
		resourceKeyword.addProperty(SPAPER.WORD, keyword.getWord());
		
    	return model;
	}

}
