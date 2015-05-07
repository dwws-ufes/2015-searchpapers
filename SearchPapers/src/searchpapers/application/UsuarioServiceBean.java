package searchpapers.application;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import searchpapers.domain.Usuario;
import searchpapers.persistence.UsuarioDAO;

@Stateless
public class UsuarioServiceBean implements UsuarioService {

	@EJB 
	private UsuarioDAO usuarioDAO;
	
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
}
