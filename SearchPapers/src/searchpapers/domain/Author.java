package searchpapers.domain;



import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import searchpapers.controller.SampleEntity;


@Entity
public class Author implements Serializable, SampleEntity{

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String email;
	
	@Lob
	private byte[] arquivoFoto;
	
	private String urlFoto;
	
	@ManyToOne
	private Institute institute;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	public boolean isPersistent(){
		return (id != null);
	}
	public String toString(){
		return "";
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public byte[] getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(byte[] arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}
	
	
}
