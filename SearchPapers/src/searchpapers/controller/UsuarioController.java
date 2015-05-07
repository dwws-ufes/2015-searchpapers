package searchpapers.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import searchpapers.application.UsuarioService;
import searchpapers.domain.Keyword;
import searchpapers.domain.Usuario;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class UsuarioController extends MessageController implements Serializable{
	
	@EJB
	private UsuarioService usuarioService;
	
	private Usuario usuario = new Usuario();
	public Usuario getUsuario() { 
		return usuario; 
	} 
	public void setUsuario(Usuario usuario) { 
		this.usuario = usuario; 
	}
	
	private Usuario usuarioCadastro = new Usuario();
	public Usuario getUsuarioCadastro() { 
		return usuarioCadastro; 
	} 
	public void setUsuarioCadastro(Usuario usuarioCadastro) { 
		this.usuarioCadastro = usuarioCadastro; 
	}
	
	private String logado;
	
	public String getLogado() throws IOException { 
		
		Usuario uLog = new Usuario();
		
		uLog = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado"); 
		
		if (uLog == null) {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
		response.sendRedirect("/SearchPapers/index.html");
		
	     return "";
		
		}
		else
		{
			return uLog.getNome();
		}	
		
	} 
		
	

	public String login() { 
		usuario = usuarioService.getUsuario(usuario.getNome(), usuario.getSenha()); 
		
		if (usuario == null) { 
			usuario = new Usuario(); 
			setMessageKey("OK", "usuario.nao_encontrado");	
			FacesContext.getCurrentInstance().validationFailed();
			return null; 
		} 
		else { 

			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getSessionMap().put("usuariologado",usuario);
			
			return "/home.xhtml?faces-redirect=true"; 
		} 
	}
	
	public String logout(){
		usuario = new Usuario();
		FacesContext.getCurrentInstance().validationFailed();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuariologado"); 

		return "/index.xhtml?faces-redirect=true";
	}
	

	

	public void salvar(){
		try{

		  if (usuarioCadastro.getId()== null){
			 
			  List<Usuario> list = usuarioService.getByName(usuarioCadastro.getNome());
				
				if ( list.isEmpty()){	
					usuarioService.salvar(usuarioCadastro); 
				    setMessageKey("OK", "cad.sucesso");
				}
				else{
					setMessageKey("ERRO", "usuario.ja_cadastrado");	
				}	    
		  }
		  usuarioCadastro = new Usuario();
		}
		catch(Exception e){
			
		setMessageChar("ERRO", e.getMessage());  
		}
	}
	
	
}
