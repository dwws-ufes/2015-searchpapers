package searchpapers.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import searchpapers.controller.SampleEntity;

@Entity
public class Institute implements Serializable, SampleEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	@OneToMany(mappedBy = "institute")
	private List<Author> authors;

	private String webAdress;
	
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

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	
	
	
	public String getWebAdress() {
		return webAdress;
	}

	public void setWebAdress(String webAdress) {
		this.webAdress = webAdress;
	}

	public boolean isPersistent(){
		return (id != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj != null){
	      if(this.id == ((Institute) obj).id) {
	        return true;
	      }else {
	        return false;
	      }
		}
		return false;
	}
}
