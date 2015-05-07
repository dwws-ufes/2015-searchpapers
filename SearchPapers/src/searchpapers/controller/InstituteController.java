package searchpapers.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import searchpapers.application.AuthorService;
import searchpapers.application.InstituteService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.message.MessageController;


@Named
@SessionScoped
public class InstituteController extends MessageController implements Serializable {
	@EJB
	private InstituteService instituteService;
	
	@EJB
	private AuthorService authorService;
    
	private Institute institute = new Institute();
	
	protected boolean readOnly = false;
	
	private List<Institute> institutes;
	
	private Map<Long, Institute> arrayInstitutes = new TreeMap<Long, Institute>();
	
	private String  filter;
	
	private String valorFiltro;
	
	private boolean filtering;
	
	
	public String getFilter(){
		return filter;
	}
	
	public void setFilter(String filter){
		this.filter =  filter;
	}
	
	public String getValorFiltro(){
		return valorFiltro;
	}	

	public void setValorFiltro(String filter){
		this.valorFiltro =  filter;
	}
	
	public boolean getFiltering(){
		return filtering;
	}
	

	public Institute getInstitute(){
		return institute;
	}
	
	public void setInstitute(Institute Institute){
		this.institute = Institute;
	}
	
	public List<Institute> getInstitutes(){
		return (List<Institute>)institutes;
	}
	
	//-------------------------------
		// Tratamento do array para exibir na tela as alteracoes
		
		
		public List<Institute> getArrayInstitutes() {
			return new ArrayList<Institute>(arrayInstitutes.values());
		}
		
		public void addArray(Institute k) {
			arrayInstitutes.put(k.getId(), k);
		}
		
		public void deletarArray(Institute k) {
			arrayInstitutes.remove(k.getId());
		}
		
		public void listarArray(){
			for (int i = 0; i < institutes.size(); i++) {
				arrayInstitutes.put(institutes.get(i).getId(), institutes.get(i));
			}
		}
		///----------------------------
		
		@Inject
		public String listar(){
			institutes = instituteService.getInstitutes();
			listarArray();
			
			return "/Institute/listInstitute.xhtml";
		}
		
	
	public String salvar(Institute Institute){
		try{

		  if (Institute.getId()== null){
		    instituteService.salvar(institute); 
		    institutes.add(institute);;
		    setMessageKey("OK", "cad.sucesso");	
		    listar();		    
		  }
		  else{
		    instituteService.atualizar(institute); 
		    setMessageKey("OK", "atu.sucesso");			    
		  }
		  Institute = new Institute();
		}
		catch(Exception e){
			
		setMessageChar("ERRO", e.getMessage());  
		}
		
		return "/institute/listInstitute.xhtml";
	}
	
	public void deletar(){		
		try{
			  if (institute.getId()== null){
				  setMessageKey("ERRO", "obj.nao_localizado");			   
			  }
			  else{
				  List<Author> authors = authorService.getByInstitute(institute);
				  
				  if ( authors.isEmpty() ){
					  instituteService.deletar(institute); 
					  deletarArray(institute);			  
					  setMessageKey("OK", "del.sucesso");
				  }
				  else {
					  setMessageKey("ERRO", "del.ex_relacao");
				  } 
			  }
			}
			catch(Exception e){
				setMessageChar("ERRO", e.getMessage());  
			}
	}
	
	public void cadastrar(){
		salvar(institute);
	}
	
	public void atualizar(Institute institute){
		salvar(institute);
	}
	
	public Institute getSelectedEntity(){
		return institute;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	
	
	public void cancelFilter(){
		filtering = false;
		this.institute = new Institute();
		this.filter = "";
		this.valorFiltro = "";
	
	}
	public String cancelarFiltro(){
		cancelFilter();
		institutes = instituteService.getInstitutes();
		arrayInstitutes.clear();
		listarArray();
	    return "";
	}
	
	public void filtrar(){
		try{
			if ((this.filter != null)&&(this.filter != "")&&(!this.filter.equals(""))){
				if ((this.filter == "name") ||  (this.filter.equals("name"))) {
										
					institutes = instituteService.getByLikeName(valorFiltro);
					arrayInstitutes.clear();
					listarArray();
				}else
			    if ((this.filter == "id")||  (this.filter.equals("id"))){
			    	institute = instituteService.getById(Long.parseLong(valorFiltro));
			    	institutes = new ArrayList<Institute>();
			    	institutes.add(institute);
					arrayInstitutes.clear();
					listarArray();
			    }
				setMessageKey("OK", "pesq.sucesso");		
				this.filter = "";
				this.valorFiltro = "";
			}
			else{
				setMessageKey("OK", "filtro.nao.preenchido");	
				institutes = instituteService.getInstitutes();
				arrayInstitutes.clear();
				listarArray();
				
			}
		}
		catch(Exception e){			
			setMessageChar("ERRO", e.getMessage());  
		}
		
	}
	
	public String novo(){
		readOnly = false;
		this.institute = new Institute();
		return "/Institute/formInstitute.xhtml";
	}
	
	public String visualizar(){
		readOnly = true;
		setMessageChar("OK", isReadOnly()+"");
		return "/Institute/formInstitute.xhtml";
	}
	
	public String editar(){
		 if (institute.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		 else{
		   readOnly = false;		
		   return "/Institute/formInstitute.xhtml";
		 }
		 return null;
	}
	
	public String rowSelect(){
		setMessageChar("OK", "teste");
		//this.Institute = Institute;
		return "";
	}
}
