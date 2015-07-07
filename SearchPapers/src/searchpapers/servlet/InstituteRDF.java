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
import searchpapers.persistence.InstituteDAO;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

@WebServlet(urlPatterns = { "/data/institute/*" })
public class InstituteRDF extends HttpServlet {
	@EJB
	private InstituteDAO instituteDAO;
	
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
			model = getRDFinstitutes(model, myNS);
		}
		else{
			Institute institute = instituteDAO.getById(id);
			if (institute != null){
				model = getRDFinstitute(model, myNS, institute);
			}
		}

		try(PrintWriter out = response.getWriter()){
			  model.write(out, "RDF/XML-ABBREV");
		}		
	}	
	
	private Model getRDFinstitutes(Model model, String myNS){
	    
	    List<Institute> institutes = instituteDAO.getInstitutes();
	    
	    for(int i =0;i<institutes.size();i++){		    	
	    	Institute institute = institutes.get(i);	    	
	    	model = getRDFinstitute(model, myNS, institute);	    	
	    }
	    
	    return model;
	}
	
	
	private Model getRDFinstitute(Model model, String myNS, Institute institute){				
    	    	
		Resource resourceInstitute = model.createResource(myNS+"institute/"+institute.getId());
		resourceInstitute.addProperty(RDF.type, SPAPER.INSTITUTE);
		resourceInstitute.addProperty(SPAPER.NAME, institute.getName());
		
		if (institute.getWebAdress() != null && !institute.getWebAdress().equals(null)){
			resourceInstitute.addProperty(SPAPER.WEB_ADDRESS, institute.getWebAdress());
		}
		
    	return model;
	}
}
