package searchpapers.application;

import java.util.List;

import javax.ejb.Local;

import searchpapers.domain.Author;
import searchpapers.domain.Usuario;

@Local
public interface UsuarioService {
	Usuario getUsuario(String nome, String senha);
	boolean inserirUsuario(Usuario usuario);
	boolean deletarUsuario(Usuario usuario);
	Usuario salvar(Usuario objeto); 
	List<Usuario> getByName(String name);
	Usuario getById(Long id);
	Usuario atualizar(Usuario objeto);
	List<Usuario> getUsuarios();
	List<Usuario> getUsuarioByPaper(Long id);
	
	Usuario getLocalizacaoWeb(String name);
}
