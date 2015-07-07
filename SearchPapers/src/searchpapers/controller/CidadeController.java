package searchpapers.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import searchpapers.application.CidadeService;
import searchpapers.domain.Cidade;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class CidadeController extends MessageController implements Serializable, SampleEntity {

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@EJB private CidadeService cidadeService;	

	public List<Cidade> getCidades(Long id){
		return cidadeService.getCidadesByEstado(id);
	}
	*/
}
