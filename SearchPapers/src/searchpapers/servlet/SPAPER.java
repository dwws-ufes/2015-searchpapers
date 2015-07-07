package searchpapers.servlet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class SPAPER {
	
	protected static final String uri ="http://localhost:8080/SearchPapers/";
	
	public static String getURI() {
        return uri;
	}
	
	private static Model m = ModelFactory.createDefaultModel();
	    
	
	public static final Resource AUTHOR = m.createResource(uri + "AUTHOR" );
	public static final Resource PAPER = m.createResource(uri + "PAPER" );
	public static final Resource JOURNAL = m.createResource(uri + "JOURNAL" );
	public static final Resource KEYWORD = m.createResource(uri + "KEYWORD" );
	public static final Resource INSTITUTE = m.createResource(uri + "INSTITUTE" );
	
	public static final Property HAS_JOURNAL = m.createProperty(uri, "HAS_JOURNAL" );
	public static final Property HAS_KEYWORD = m.createProperty(uri, "HAS_KEYWORD" );
	public static final Property HAS_AUTHOR = m.createProperty(uri, "HAS_AUTHOR" );
	public static final Property WORK_FOR = m.createProperty(uri, "WORK_FOR" );
	
	public static final Property NAME = m.createProperty(uri, "NAME" );
	public static final Property TITLE = m.createProperty(uri, "TITLE" );
	public static final Property SUMMARY = m.createProperty(uri, "SUMMARY" );
	public static final Property YEAR = m.createProperty(uri, "YEAR" );
	public static final Property WEB_ADDRESS = m.createProperty(uri, "WEB_ADDRESS" );
	public static final Property WORD = m.createProperty(uri, "WORD" );
	public static final Property EMAIL = m.createProperty(uri, "EMAIL" );

}
