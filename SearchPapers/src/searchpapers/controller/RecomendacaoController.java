package searchpapers.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import searchpapers.application.RecomendacaoService;
import searchpapers.domain.Author;
import searchpapers.domain.Institute;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.domain.Recomendacao;
import searchpapers.domain.Usuario;
import searchpapers.message.MessageController;



@Named
@SessionScoped
public class RecomendacaoController extends MessageController implements Serializable{

	@EJB private RecomendacaoService recomendacaoService;
	@Inject private UsuarioController usuarioController;

	
	private String  filter;
	
	private String valorFiltro;
	
	private boolean filtering;
	
	private Recomendacao recomendacao = new Recomendacao();
	public Recomendacao getRecomendacao() {
		return recomendacao;
	}
	public void setRecomendacao(Recomendacao recomendacao) {
		this.recomendacao = recomendacao;
	}

	
	private List<Recomendacao> recomendacoes = new ArrayList<Recomendacao>();
	public List<Recomendacao> getRecomendacoes() {
		//Usuario destinatario = usuarioController.getUsuarioLogado();		
		//recomendacoes = recomendacaoService.getByDestinatario(destinatario); 
		return recomendacoes;
	}
	public void setRecomendacoes(List<Recomendacao> recomendacoes) {
		this.recomendacoes = recomendacoes;
	}
	

	
	private Map<Long, Recomendacao> arrayRecomendacoes = new TreeMap<Long, Recomendacao>();
	public List<Recomendacao> getArrayRecomendacoes() {
		//listarArray(null);
		return new ArrayList<Recomendacao>(arrayRecomendacoes.values());
	}	
	public void addArray(Recomendacao k) {
		arrayRecomendacoes.put(k.getId(), k);
	}	
	public void deletarArray(Recomendacao k) {
		arrayRecomendacoes.remove(k.getId());
	}	
	public void listarArray(List<Recomendacao> recomendacoes){
		arrayRecomendacoes.clear();		
		Usuario destinatario = usuarioController.getUsuarioLogado();		
		
		if (recomendacoes == null){
			recomendacoes = recomendacaoService.getByDestinatario(destinatario);
		}
		
		for (int i = 0; i < recomendacoes.size(); i++) {
			arrayRecomendacoes.put(recomendacoes.get(i).getId(), recomendacoes.get(i));
		}
	}
		
	
	@Inject
	public String listar(){		
		listarArray(null);	
		return  "/home.xhtml?faces-redirect=true"; 
	}
	
	
	////// Filtros
    
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
	
	public void cancelFilter(){
		filtering = false;
		this.recomendacao = new Recomendacao();
		this.filter = "";
		this.valorFiltro = "";
	
	}
	public String cancelarFiltro(){
		cancelFilter();
		Usuario destinatario = usuarioController.getUsuarioLogado();		
	    recomendacoes = recomendacaoService.getByDestinatario(destinatario); // retorna as recomenda��es que tem como destinat�rio o usu�rio logado.
		listarArray(null);
	    return "";
	}
	
	public String filtrar(){
		try{
			Usuario destinatario = usuarioController.getUsuarioLogado();							
			recomendacoes = new ArrayList<Recomendacao>();
			
			if ((this.filter != null)&&(this.filter != "")&&(!this.filter.equals(""))){
				if ((this.filter == "tituloArtigo") ||  (this.filter.equals("tituloArtigo"))) {
					recomendacoes = recomendacaoService.getByLikeTituloArtigo(valorFiltro, destinatario);
					listarArray(recomendacoes);
				}else
			    if ((this.filter == "id")||  (this.filter.equals("id"))){
			    	recomendacao = recomendacaoService.getById(Long.parseLong(valorFiltro), destinatario);
			    	if (recomendacao != null){
				    	recomendacoes.add(recomendacao);
			    	}
					listarArray(recomendacoes);
			    }else
				    if ((this.filter == "mensagem")||  (this.filter.equals("mensagem"))){
				    	recomendacoes = recomendacaoService.getByLikeMensagem(valorFiltro, destinatario);
						listarArray(recomendacoes);
				    }
				setMessageKey("OK", "pesq.sucesso");		
				this.filter = "";
				this.valorFiltro = "";
			}
			else{
				setMessageKey("OK", "filtro.nao.preenchido");	
				listarArray(null);
				
			}
		}
		catch(Exception e){			
			setMessageChar("ERRO", e.getMessage());  
		}
		return null;
	}
	
	////// end filtros
	
	public String salvar(){
		try{			  
			
		  if (recomendacao.getId()== null){
			  recomendacaoService.salvar(recomendacao); 
			  recomendacoes.add(recomendacao);			  
		    setMessageKey("OK", "cad.sucesso");	
		    
		    try{
		    	enviarEmail(recomendacao);
		    	setMessageKey("OK", "email.sucesso");
		    }catch(Exception e){
		    	setMessageChar("ERRO", "Erro ao enviar o email."); 
		    	setMessageChar("ERRO", e.getMessage()); 
		    }
		    
		    listar();		    
		  }
		  else{ 
			recomendacaoService.atualizar(recomendacao); 
		    setMessageKey("OK", "atu.sucesso");			    
		  }
		  recomendacao = new Recomendacao();
		}
		catch(Exception e){
		  setMessageChar("ERRO", e.getMessage());  
		}
		
		return "/paper/listPaper.xhtml";
	}
	
	public void deletar(Long id){	
		recomendacao = recomendacaoService.getById(id);
		try{
			  if (recomendacao.getId()== null){
				  setMessageKey("ERRO", "obj.nao_localizado");			   
			  }
			  else{				  
				  recomendacaoService.deletar(recomendacao); 
				  deletarArray(recomendacao);
				  //getRecomendacoes();
				  setMessageKey("OK", "del.sucesso");				  
			  }
			}
			catch(Exception e){ 
				setMessageChar("ERRO", e.getMessage());  
			}
	}

	public String novo(Paper paper){	
		
		this.recomendacao = new Recomendacao();
		this.recomendacao.setPaper(paper);
		
		Usuario uLog = usuarioController.getUsuarioLogado();		
				
		if (uLog != null) {
		  this.recomendacao.setRemetente(uLog);
		}		
		
		return "/recomendacao/formRecomendacao.xhtml";
	}
	
	public String cancelar(){	
		return "/paper/listPaper.xhtml?faces-redirect=true";
	}
	
	
	public void enviarEmail( Recomendacao recomendacao){
		
			
		Properties props = new Properties();
        /** Parametros de conexao com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
       // Session session = Session.getDefaultInstance(props,null);

        Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication() 
                         {
                               return new PasswordAuthentication("searchpapers.t2@gmail.com", "devwebt2");
                         }
                    });

        /** Ativa Debug para sessao */
        session.setDebug(true);

        try {

              Message message = new MimeMessage(session);
              message.setFrom(new InternetAddress("searchpapers.t2@gmail.com")); //Remetente

              Address[] toUser = InternetAddress //Destinatario(s)
                         .parse(recomendacao.getDestinatario().getEmail());  //"seuamigo@gmail.com, seucolega@hotmail.com, seuparente@yahoo.com.br"

              message.setRecipients(Message.RecipientType.TO, toUser);
              message.setSubject("Recomendacao de artigos");//Assunto
              message.setText(recomendacao.toString());
              /**M�todo para enviar a mensagem criada*/
              Transport.send(message);

              System.out.println("Feito!!!");

         } catch (MessagingException e) {
              throw new RuntimeException(e);
        }

	}
}
