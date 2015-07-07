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
public class Journal implements Serializable, SampleEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	@OneToMany(mappedBy = "journal")
	private List<Paper> papers;

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}

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

	
	public boolean isPersistent(){
		return (id != null);
	}
	
	
}
