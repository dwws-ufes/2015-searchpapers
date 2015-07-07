package searchpapers.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import searchpapers.controller.SampleEntity;

@Entity
public class Estado implements Serializable, SampleEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sigla;
	
	private String descricao;
	
	@OneToMany(mappedBy = "estado")
	private List<Cidade> cidades;
	
	@OneToMany(mappedBy = "estado")
	private List<Usuario> usuarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj != null){
	     // if(this.id == ((Estado) obj).id) {
		  if(this.id.equals(((Estado) obj).id)) {
	        return true;
	      }else {
	        return false;
	      }
		}
		return false;
	}
	
}
