package searchpapers.application;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

import searchpapers.domain.Author;
import searchpapers.domain.Usuario;
import searchpapers.persistence.UsuarioDAO;
import searchpapers.sparql.SparqlProperties;

@Stateless
public class UsuarioServiceBean implements UsuarioService {

	@EJB 
	private UsuarioDAO usuarioDAO;
	@EJB
	private SparqlProperties sparqlProperties;
	
	public Usuario getUsuario(String nome, String senha) { 
		return usuarioDAO.getUsuario(nome, senha);
	}
	
	public boolean inserirUsuario(Usuario usuario) { 
		return usuarioDAO.inserirUsuario(usuario);
	}
	
	public boolean deletarUsuario(Usuario usuario) { 
		return usuarioDAO.deletarUsuario(usuario);
	}
	
	public Usuario salvar(Usuario objeto) {
		return usuarioDAO.salvar(objeto);	
	}
	
	public List<Usuario> getByName(String name) {
		return usuarioDAO.getByName(name);
	}
	
	public Usuario getById(Long id) {
		return usuarioDAO.getById(id);
	}
	
	public Usuario atualizar(Usuario objeto){
		return usuarioDAO.salvar(objeto);		
	}
	
	public List<Usuario> getUsuarios(){
		return usuarioDAO.getUsuarios();
	}
	
	public List<Usuario> getUsuarioByPaper(Long id){
		return usuarioDAO.getUsuarioByPaper(id);
	}
	
	public Usuario getLocalizacaoWeb(String name){
		
		Usuario tempUsuario = new Usuario(); 
		String query = sparqlProperties.getProp("prop.prefixes.dbpedia") +
					   sparqlProperties.getProp("prop.dbpedia.getLocalizacaoUsuario");	       
	       	   query = query.replace(":place", name.toLowerCase());
	       	
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet results = queryExecution.execSelect();
		if (results.hasNext()) {
			
			QuerySolution querySolution = results.next();
			Literal lat = querySolution.getLiteral("lat");
			Literal longi = querySolution.getLiteral("long");
			Literal comentario = querySolution.getLiteral("comment");	
			
			if ((lat != null) & (longi != null)){
				tempUsuario.setLatLong(lat.getValue() + ", " + longi.getValue());
			}
						
			tempUsuario.setComentario(comentario.toString());
		}
		
		return tempUsuario;
	}
	
}
