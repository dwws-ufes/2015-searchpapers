package searchpapers.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import searchpapers.application.JournalService;
import searchpapers.application.PaperService;
import searchpapers.domain.Author;
import searchpapers.domain.Journal;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;
import searchpapers.domain.Usuario;
import searchpapers.file.FileImage;
import searchpapers.file.FilePDF;
import searchpapers.message.MessageController;




@Named
@SessionScoped
public class PaperController extends MessageController implements Serializable {	

	@EJB private PaperService paperService;
	@EJB private JournalService journalService;
	
	@Inject UsuarioController usuarioController;
	@Inject PaperControllerList paperControllerList;
	@Inject PaperControllerKeyword paperControllerKeyword;
	@Inject PaperControllerAuthor paperControllerAuthor;
	
	@Inject FileImage fileImage;
	@Inject FilePDF filePDF;
	
	//-- paper
	private Paper paper = new Paper();
	public Paper getPaper(){
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
		
	//--journal		
	private Journal journal = new Journal();
	public Journal getJournal(){
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	

	//--- imagem readOnly
	protected boolean readOnly = true;		
	public boolean isReadOnly() {
		return readOnly;
	}

	
	
	//----- CRUD
	private void limparDados(){
		this.paper = new Paper();
		paperControllerKeyword.setKeyword(new Keyword());
		paperControllerAuthor.setAuthor(new Author());
		this.journal = new Journal();
		this.imagemByte = null;
		this.arquivoByte = null;
		setBufferImg(null);
		setPreviewPdf(null);		
		paperControllerKeyword.limparArrayKeyword();
		paperControllerAuthor.limparArrayAuthor();
	}
	
	public String novo(){	
		readOnly = true;
		limparDados();
		return "/paper/formPaper.xhtml?faces-redirect=true";
	}
	
	public String cancelar(){	
		return "/paper/listPaper.xhtml?faces-redirect=true";
	}
	
	public String editar(Long id){	
		limparDados();
		
		Paper p = paperService.getById(id);		
		if (p.getId()== null){
			setMessageKey("ERRO", "obj.nao_localizado");	
			return "";
		  }
		else{
			paper = p;
			readOnly = false;
			journal = paper.getJournal();
			paperControllerKeyword.listarArrayKeyword(paper);
			paperControllerAuthor.listarArrayAuthor(paper);
			return "/paper/formPaper.xhtml?faces-redirect=true";
		}
	}
	
	
	public String salvar() throws IOException{
		try{					 
			
		  if (paper.getId()== null){

			  	List<Paper> listPaper = paperService.getByName(paper.getTitle());
				
			  	if ( listPaper.isEmpty()){	
			  		//se não existir esse paper da base - cadastra novo
			  		
					List<Keyword> keywords = new ArrayList<Keyword>(paperControllerKeyword.getArrayKeywords());
					if ( keywords.isEmpty() ){
						setMessageKey("ERRO", "paper.vazio_keyword");
					}
					else {
						paper.setKeywords(keywords);
					}
	
					List<Author> authors = new ArrayList<Author>(paperControllerAuthor.getArrayAuthors());
					if ( authors.isEmpty() ){
						setMessageKey("ERRO", "paper.vazio_Author");
					}
					else {
						paper.setAuthors(authors);
					}		
					
					List<Journal> listJournal = journalService.getByName(journal.getName());
					Journal tempJournal;
					if ( listJournal.isEmpty()){
						tempJournal = journalService.salvar(journal);
					}
					else
					{
						tempJournal = listJournal.get(0);
					}					
					paper.setJournal(tempJournal);	
					
					if (arquivoByte == null){
						setMessageKey("OK", "paper.arquivo.erro");									
						return "";	
					}
					else{
						paper.setArquivoPdf(arquivoByte);
						System.out.println("Tamanho do arquivo = "+arquivoByte.length);
					
					
						Usuario usuario = usuarioController.getUsuarioLogado();
						List<Usuario> usuarios = new ArrayList<Usuario>();
						usuarios.add(usuario);
						paper.setUsuarios(usuarios);
						
						paperService.salvar(paper); 
						paperControllerList.sugerirArtigos(paper.getAuthors());
						
						setMessageKey("OK", "paper.cad.sucesso");									
						return "/paper/listPaperUsuario.xhtml?faces-redirect=true";
					}
			  	}	
				else {
					//ja existe cadastrado na base - só add ao usuario				
					paperControllerList.adicionarPaperToUsuario(listPaper.get(0).getId());
					paperControllerList.sugerirArtigos(listPaper.get(0).getAuthors());
					setMessageKey("OK", "paper.add.sucesso");
					return "/paper/listPaperUsuario.xhtml?faces-redirect=true";
				}										
		  }
		  else{
			  List<Keyword> keywords = new ArrayList<Keyword>(paperControllerKeyword.getArrayKeywords());
			  paper.setKeywords(keywords);	
			  
			  List<Author> authors = new ArrayList<Author>(paperControllerAuthor.getArrayAuthors());	
			  paper.setAuthors(authors);	
			  
			  List<Journal> listJournal = journalService.getByName(journal.getName());
			  Journal tempJournal = new Journal();
			  if ( listJournal.isEmpty()){
				tempJournal.setName(journal.getName());  
			  	tempJournal = journalService.salvar(tempJournal);
			  }
			  else
			  {
				tempJournal = listJournal.get(0);
			  }					
			  paper.setJournal(tempJournal);	
			  
			  paperService.atualizar(paper); 			  
			  paperControllerList.sugerirArtigos(paper.getAuthors());
			  
			  setMessageKey("OK", "paper.atu.sucesso");
			  return "/paper/listPaperUsuario.xhtml?faces-redirect=true";
		  }				 
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage()); 
			return "";
		}
	}
	
	
	public void deletar(){
		try{
		  if (paper.getId()== null){
			  setMessageKey("ERRO", "obj.nao_localizado");			   
		  }
		  else{
			  paperService.deletar(paper); 		  
			  setMessageKey("OK", "del.sucesso");
		  }
		}
		catch(Exception e){
			setMessageChar("ERRO", e.getMessage());  
		}
	}
	
	//----- CRUD
	
	

	private Part arquivoForm;
	public Part getArquivoForm() {
		return arquivoForm;
	}
	public void setArquivoForm(Part arquivoForm) {
		this.arquivoForm = arquivoForm;
	}
	
	
	private BufferedImage bufferImg;
	public BufferedImage getBufferImg() {
		return bufferImg;
	}
	public void setBufferImg(BufferedImage bufferImg) {
		this.bufferImg = bufferImg;
	}

	private byte[] imagemByte;
	private StreamedContent previewPdf;
	public StreamedContent getPreviewPdf() {
		DefaultStreamedContent content=null;
	    try{	    		    	
	    	BufferedImage bImg = getBufferImg();	
	    	imagemByte = fileImage.BufferedImageToByte(bImg);	    	
	        content=new DefaultStreamedContent(new ByteArrayInputStream(imagemByte),"image/jpeg");
	        
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return content;
	}
	public void setPreviewPdf(StreamedContent previewPdf) {
		this.previewPdf = previewPdf;
	}
	
		
	private  byte[] arquivoByte;
	public void importarArquivo() throws Exception{
	  try{
		  RequestContext.getCurrentInstance().execute("PF('dialogCarregaPaper').show();");
		  
		  arquivoByte = fileImage.fileToByte(arquivoForm.getInputStream());
		  
		  ByteBuffer arquivoBuffer = null;
		  arquivoBuffer = ByteBuffer.wrap(arquivoByte);   
	
		  setBufferImg(filePDF.getPreview(arquivoBuffer));
		  
		  exibirDados(arquivoBuffer);

		  setMessageKey("OK", "pdf.sucesso");
		  RequestContext.getCurrentInstance().execute("PF('dialogCarregaPaper').hide();");
	  }catch(Exception e){
		  setMessageChar("ERRO", e.getMessage()); 
		  RequestContext.getCurrentInstance().execute("PF('dialogCarregaPaper').hide();");
	  }	  
	}


	
	private void exibirDados(ByteBuffer arquivoBuffer) throws Exception{
		System.out.println("entrou = exibir dados pdf");
		filePDF.getMetaDados(arquivoBuffer);
	      
		if (filePDF.getPaper() != null){
			Paper tempPaper = filePDF.getPaper();
			
			paper.setTitle(tempPaper.getTitle());	
			paper.setSummary(tempPaper.getSummary());
			if (!tempPaper.getKeywords().isEmpty()){				
			for(int cont=0 ; cont< tempPaper.getKeywords().size() ; cont++){
				Keyword tempKeyword = new Keyword();
				tempKeyword.setWord(tempPaper.getKeywords().get(cont).getWord());
				paperControllerKeyword.setKeyword(tempKeyword);
				paperControllerKeyword.cadastrarKeyword();
	         }	
			}
			
			//busca semantica
			tempPaper = paperService.getPaperWeb(tempPaper.getTitle());
			
			paper.setUrlPaper(tempPaper.getUrlPaper());
			paper.setYear(tempPaper.getYear());
			setJournal(tempPaper.getJournal());			

			if (!tempPaper.getAuthors().isEmpty()){	
				for(int cont=0 ; cont< tempPaper.getAuthors().size() ; cont++){					
					Author tempAuthor = new Author();
					tempAuthor.setName(tempPaper.getAuthors().get(cont).getName());			
					paperControllerAuthor.setAuthor(tempAuthor);
					paperControllerAuthor.cadastrarAuthor();
		         }	
			}
			
		}
	}
	
	public void exibirArquivo() throws Exception{
		filePDF.exibirPdf(arquivoByte);		
	}


	
}
