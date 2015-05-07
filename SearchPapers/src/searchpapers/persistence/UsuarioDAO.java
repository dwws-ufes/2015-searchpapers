package searchpapers.persistence;


import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Usuario;

@Local
public interface UsuarioDAO {
	
	Usuario getUsuario(String nome, String senha);
	boolean inserirUsuario(Usuario usuario);
	boolean deletarUsuario(Usuario usuario);
	Usuario salvar(Usuario object);
	List<Usuario> getByName(String name);
}
