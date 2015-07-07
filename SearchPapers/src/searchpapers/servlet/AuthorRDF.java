package searchpapers.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.persistence.AuthorDAO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Servlet implementation class ListAuthorInRdfServlet
 */

@WebServlet(urlPatterns = { "/data/author/*" })
public class AuthorRDF extends HttpServlet {
       
	@EJB
	private AuthorDAO authorDAO;
	
    
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
			model = getRDFauthors(model, myNS);
		}
		else{
			Author author = authorDAO.getById(id);
			if (author != null){
				model = getRDFauthor(model, myNS, author);
			}
		}

		try(PrintWriter out = response.getWriter()){
			  model.write(out, "RDF/XML-ABBREV");
		}
		
	}	
		
	
	private Model getRDFauthors(Model model, String myNS){
	    
	    List<Author> authors = authorDAO.getAuthors();
	    
	    for(int i =0;i<authors.size();i++){		    	
	    	Author author = authors.get(i);	    	
	    	model = getRDFauthor(model, myNS, author);	    	
	    }
	    
	    return model;
	}
	
	
	private Model getRDFauthor(Model model, String myNS, Author author){				
    	    	
		Resource resourceAuthor = model.createResource(myNS+"author/"+author.getId());
		resourceAuthor.addProperty(RDF.type, SPAPER.AUTHOR);
		resourceAuthor.addProperty(SPAPER.NAME, author.getName());
		
		if (author.getEmail() != null && !author.getEmail().equals(null)){
			resourceAuthor.addProperty(SPAPER.EMAIL, author.getEmail()); 
		}
		
		if (author.getInstitute() != null && !author.getInstitute().equals(null)){
			Institute institute = author.getInstitute();
			Resource resourceInstitute = model.createResource(myNS+"institute/"+institute.getId());
			resourceInstitute.addProperty(RDF.type, SPAPER.INSTITUTE);
			resourceInstitute.addProperty(SPAPER.NAME, institute.getName());
			
			if (institute.getWebAdress() != null && !institute.getWebAdress().equals(null)){
				resourceInstitute.addProperty(SPAPER.WEB_ADDRESS, institute.getWebAdress());
			}
			resourceAuthor.addProperty(SPAPER.WORK_FOR, resourceInstitute);
		}

    	return model;
	}


}
