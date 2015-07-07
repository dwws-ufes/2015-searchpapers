package searchpapers.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Paper {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	private Long year;
	private String urlPaper;
	
	@Lob
	private String summary;	
	
	@Lob
	private byte[] arquivoPdf;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Author> authors;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Keyword> keywords;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Usuario> usuarios;

	@OneToMany(mappedBy = "paper")
    private List<Recomendacao> recomendacoes;
	
	@ManyToOne
	private Journal journal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}
	


	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
	
	public boolean isPersistent(){
		return (id != null);
	}

	public byte[] getArquivoPdf() {
		return arquivoPdf;
	}

	public void setArquivoPdf(byte[] arquivoPdf) {
		this.arquivoPdf = arquivoPdf;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
		public List<Recomendacao> getRecomendacoes() {
		return recomendacoes;
	}

	public void setRecomendacoes(List<Recomendacao> recomendacoes) {
		this.recomendacoes = recomendacoes;
	}
	
	public String toString(){
		String str = "";
		String aut = "";
						
		for (int i = 0; i < authors.size(); i++) {
			aut = aut + authors.get(i).getName() + ", ";			
		}
		
		str =  str + "artigo: " + aut + " (" + year + "). "+ title ;
		return str;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public String getUrlPaper() {
		return urlPaper;
	}

	public void setUrlPaper(String urlPaper) {
		this.urlPaper = urlPaper;
	}
	
	
}
