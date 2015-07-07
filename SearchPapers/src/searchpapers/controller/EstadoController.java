package searchpapers.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import searchpapers.application.EstadoService;
import searchpapers.domain.Estado;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class EstadoController extends MessageController implements Serializable {

	@EJB
	private EstadoService estadoService;
	
	private Estado estado = new Estado();
	
	private List<Estado> estados;
	
	public Estado getEstado() { 
		return estado; 
	} 
	public void setEstado(Estado estado) { 
		this.estado = estado; 
	}
	public List<Estado> getEstados(){
		return estados;
	}
	
	@Inject
	public String listar(){
		estados = estadoService.getEstados();		
		return null;
	}
	
}
