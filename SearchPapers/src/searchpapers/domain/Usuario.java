package searchpapers.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import searchpapers.controller.SampleEntity;

@Entity
public class Usuario implements Serializable, SampleEntity {
	 @Id @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private String nome;

     private String senha;
     
     private String sobrenome;
     
     private String endereco;

     @ManyToOne
     private Estado estado;
    
     @ManyToOne
     private Cidade cidade;
               
     private String cep;
     
     private String email;
     
     private String latLong;
     
     @Lob
     private String comentario;
     
     @Temporal(TemporalType.DATE)
     private Date dataNascimento;
     
     @OneToMany(mappedBy = "destinatario")
     private List<Recomendacao> recomendacoesRecebidas;
     
     @OneToMany(mappedBy = "remetente")
     private List<Recomendacao> recomendacoesFeitas;
     
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado){
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}
	
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

		
	public List<Recomendacao> getRecomendacoesRecebidas() {
		return recomendacoesRecebidas;
	}

	public void setRecomendacoesRecebidas(List<Recomendacao> recomendacoesRecebidas) {
		this.recomendacoesRecebidas = recomendacoesRecebidas;
	}

	public List<Recomendacao> getRecomendacoesFeitas() {
		return recomendacoesFeitas;
	}

	public void setRecomendacoesFeitas(List<Recomendacao> recomendacoesFeitas) {
		this.recomendacoesFeitas = recomendacoesFeitas;
	}
		
	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean isPersistent(){
		return (id != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj != null){
		  if(this.id.equals(((Usuario) obj).id)) {
	        return true;
	      }else {
	        return false;
	      }
		}
		return false;
	}
}
