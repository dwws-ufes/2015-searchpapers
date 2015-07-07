package searchpapers.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import searchpapers.controller.SampleEntity;

@Entity
public class Cidade implements Serializable, SampleEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;
	
	@ManyToOne
	//@Column(name="id_estado")
	private Estado estado;
	
	@OneToMany(mappedBy = "cidade")
	private List<Usuario> usuarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj != null){
	      if(this.id.equals(((Cidade) obj).id)) {
	        return true;
	      }else {
	        return false;
	      }
		}
		return false;
	}
	
}
