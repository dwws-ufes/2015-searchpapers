package searchpapers.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Recomendacao implements Serializable {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Paper paper;	
	
	@ManyToOne
	private Usuario remetente;
	
	@ManyToOne
	private Usuario destinatario;
	
	private String mensagem;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getRemetente() {
		return remetente;
	}
	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}
	public Usuario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
		
	public Paper getPaper() {
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	public boolean isPersistent(){
		return (id != null);
	}
	public String toString(){
		String str = "";
		str = str + remetente.getNome() +" recomendou o " + paper.toString() + ". \n\n" + mensagem;
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj != null){
		  if(this.id.equals(((Recomendacao) obj).id)) {
	        return true;
	      }else {
	        return false;
	      }
		}
		return false;
	}
	
}
