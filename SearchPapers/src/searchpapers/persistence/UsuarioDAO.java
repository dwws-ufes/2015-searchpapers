package searchpapers.persistence;


import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Usuario;

@Local
public interface UsuarioDAO {
	
	Usuario salvar(Usuario object);
	boolean deletarUsuario(Usuario usuario);
	boolean inserirUsuario(Usuario usuario);
	
	Usuario getUsuario(String nome, String senha);	
	List<Usuario> getUsuarios();
	List<Usuario> getByName(String name);
	Usuario getById(Long id);
	List<Usuario> getUsuarioByPaper(Long id);
	
}
