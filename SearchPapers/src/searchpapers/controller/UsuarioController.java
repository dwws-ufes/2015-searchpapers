package searchpapers.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;

import searchpapers.application.CidadeService;
import searchpapers.application.UsuarioService;
import searchpapers.domain.Cidade;
import searchpapers.domain.Usuario;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class UsuarioController extends MessageController implements Serializable, SampleEntity {
	
	@EJB private UsuarioService usuarioService;
	@EJB private CidadeService cidadeService;
	
	private Usuario usuario = new Usuario();
	public Usuario getUsuario() { 
		if (usuario != null) {
			if(usuario.getCidade() != null){
			  usuario.setEstado(usuario.getCidade().getEstado());
			  refreshEstado();
			}
		}
		return usuario; 
	} 	
	public void setUsuario(Usuario usuario) { 
		this.usuario = usuario; 
	}
	
	
	private List<Usuario> usuarios;
	public List<Usuario> getUsuarios(){
		this.usuarios = usuarioService.getUsuarios();
		return usuarios;
	}
	
	private List<Cidade> cidades;
	public List<Cidade> getCidades(){
		return cidades;
	}
	
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	private String logado;	
	public String getLogado() throws IOException { 		
		Usuario uLog = getUsuarioLogado();
		if (uLog == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
			response.sendRedirect("/SearchPapers/index.html");
			
		    return "";		
		}
		else{
			return uLog.getNome();
		}			
	} 
		
    public Usuario getUsuarioLogado() { 		
		Usuario uLog = new Usuario();
		try{
			uLog = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado"); 
			
			if (uLog == null) {
				FacesContext context = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
				response.sendRedirect("/SearchPapers/index.html");
			
				return null;		
			}
			else{
				System.out.println("usuario logado = "+uLog.getId()+"- "+uLog.getNome());
				return uLog;
			}	
		}catch(Exception e){
			return null;
		}		
	} 
	


    public String salvar(){
    	try{
    		//novoUsuario
    		if (usuario.getId()== null){
   			 
  			  	List<Usuario> list = usuarioService.getByName(usuario.getNome());  				
  				if (list.isEmpty()){	
  					usuarioService.salvar(usuario); 
  				    setMessageKey("OK", "cad.sucesso");
  				    return "/login.xhtml?faces-redirect=true";
  				}
  				else{
  					setMessageKey("ERRO", "usuario.ja_cadastrado");	
  					return "";
  				}	    
  		  	}
    		//UsuarioExistente
  		  	else{	
  		  		Usuario tempUsuario = usuarioService.getById(usuario.getId());
  		  		if (tempUsuario != null){
  		  			usuarioService.atualizar(usuario);
  		  			removeUsuarioLogado();
  		  			includeUsuarioLogado(usuario);
  		  			return "";
  		  		}
  		  		else{
  		  			setMessageKey("ERRO", "usuario.nao_cadastado");	
					return "";
  		  		}
  			}  
    	}
    	catch(Exception e){			
			setMessageChar("ERRO", e.getMessage()); 
			return "";
		}
    }

	
	public String novo(){		
		try{
		  this.usuario = new Usuario();
		  cidades = new ArrayList<Cidade>();
		  return 	"/usuario/formNovoUsuario.xhtml?faces-redirect=true";
		}catch(Exception e){
			System.out.print("erro novo");
			setMessageChar("ERRO", e.getMessage()); 
			return "";
		}		
	}
	
	public String cancelar(){
		usuario = new Usuario();		
		return "/login.xhtml?faces-redirect=true";
	}
	
	
	public void refreshEstado(){ 		
		try{
		  this.cidades = cidadeService.getCidadesByEstado(this.usuario.getEstado().getId());
		}catch(Exception e){		
		  setMessageChar("ERRO", e.toString());
		}
	}
	
	
	public String login() { 
		try{
			usuario = usuarioService.getUsuario(usuario.getNome(), usuario.getSenha()); 			
			if (usuario == null) { 
				usuario = new Usuario(); 
				setMessageKey("OK", "usuario.nao_encontrado");	
				FacesContext.getCurrentInstance().validationFailed();
				return null; 
			} 
			else { 
				removeUsuarioLogado();
				includeUsuarioLogado(usuario);			
				return "/home.xhtml?faces-redirect=true"; 
			} 
		}catch(Exception e){
			setMessageChar("ERRO", e.getMessage()); 
			return "";
		}
	}
	
	private void includeUsuarioLogado(Usuario usuario){
		FacesContext fc = FacesContext.getCurrentInstance();		
		fc.getExternalContext().getSessionMap().put("usuariologado",usuario);					
	}
	
	private void removeUsuarioLogado(){
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.validationFailed();
		fc.getExternalContext().getSessionMap().remove("usuariologado"); 
	}
	
	public String logout(){
		usuario = new Usuario();
		removeUsuarioLogado();
		return "/index.html?faces-redirect=true";
	}
	
	
	public void carregaMapa(){
		System.out.println("entrei no carregaMapa");
		try{
	    	String name = "";
		
		   if((!usuario.equals(null)) && (usuario != null)){
			   if(usuario.getCidade() != null){
		         name = usuario.getCidade().getNome();		         
			   }
		   }
		
		   if (name != null && name.length() > 0) {				  
				Usuario tempUsuario = usuarioService.getLocalizacaoWeb(name);
				usuario.setLatLong(tempUsuario.getLatLong());					
				usuario.setComentario(tempUsuario.getComentario());
			}
		
		}catch(Exception e){
			System.out.println("erro no carregaMapa "+e.getMessage()); 
			setMessageChar("ERRO", e.getMessage());  
		}
	}
	
}
