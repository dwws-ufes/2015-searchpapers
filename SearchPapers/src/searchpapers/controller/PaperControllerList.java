package searchpapers.controller;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import searchpapers.application.PaperService;
import searchpapers.application.UsuarioService;
import searchpapers.domain.Author;
import searchpapers.domain.Paper;
import searchpapers.domain.Usuario;
import searchpapers.file.FileImage;
import searchpapers.file.FilePDF;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class PaperControllerList extends MessageController implements Serializable{
	
	@EJB private PaperService paperService;
	@EJB private UsuarioService usuarioService;
	
	@Inject UsuarioController usuarioController;
	@Inject PaperController paperController;
	
	@Inject FileImage fileImage;
	@Inject FilePDF filePDF;
	
	@Inject
	void listar(){
		getPapers();
		getPapersUsuario();
	}
		
	
	private List<Paper> papersSugest = new ArrayList<Paper>();
	public List<Paper> getPapersSugest() {
		return papersSugest;
	}
	public void setPapersSugest(List<Paper> papersSugest) {
		this.papersSugest = papersSugest;
	}
	

	private String carregaSugest;
	public String getCarregaSugest() {
		if (!papersSugest.isEmpty()){
			RequestContext.getCurrentInstance().execute("PF('dialogSugestao').show();");
		}
		return carregaSugest;
	}
	public void setCarregaSugest(String carregaSugest) {
		this.carregaSugest = carregaSugest;
	}
	public void fecharPaperSugest(){
		RequestContext.getCurrentInstance().execute("PF('dialogSugestao').hide();");
		setPapersSugest(new ArrayList<Paper>());
	}
	
	
	//-- paper
	private Paper paper = new Paper();
	public Paper getPaper(){
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	
	private List<Paper> papers;
	public List<Paper> getPapers(){
		papers = paperService.getPapers();
		return papers;
	}
	
	private List<Paper> papersUsuario;
	public List<Paper> getPapersUsuario(){
		papers = paperService.getPapers();
		List<Paper> listPaperUsuario = new ArrayList<Paper>();	
		Usuario usuario = usuarioController.getUsuarioLogado();
		
		for(int j=0 ; j< papers.size() ; j++){
			List<Usuario> usuarios = new ArrayList<Usuario>();							
			usuarios =  papers.get(j).getUsuarios();
			for(int i=0 ; i< usuarios.size() ; i++){
				if ( usuarios.get(i).getId().equals(usuario.getId()) ) {	
					listPaperUsuario.add(papers.get(j));					
				}							
			}		
		}
		return listPaperUsuario;
	}
	
	

		
	public void exibirArquivo(byte[] arquivoPdf) throws Exception{
		filePDF.exibirPdf(arquivoPdf);		
	}	
	
	public byte[] getImage(byte[] arquivoPdf) throws Exception{
		ByteBuffer arquivoBuffer = null;
		arquivoBuffer = ByteBuffer.wrap(arquivoPdf);   
		BufferedImage bufferImg = filePDF.getPreview(arquivoBuffer);
		
		byte[] imagemByte = fileImage.BufferedImageToByte(bufferImg);

        return imagemByte;
    }
	
	
	public void adicionarPaperToUsuario(Long id){	
		Paper p = new Paper();
		p = paperService.getById(id);		
		if (p.getId()== null){
			setMessageKey("ERRO", "obj.nao_localizado");	
		  }
		else{
			Boolean paperCadastrado = false;			
			Usuario usuario = usuarioController.getUsuarioLogado();

			List<Usuario> listUsuarios = new ArrayList<Usuario>();							
			listUsuarios = usuarioService.getUsuarioByPaper(p.getId());	
			System.out.println("user total inicial "+listUsuarios.size());
			for(int i=0 ; i< listUsuarios.size() ; i++){
				if ( listUsuarios.get(i).getId() == usuario.getId() ) {	
					paperCadastrado = true;
					break;
				}							
			}									
			
			if ( paperCadastrado == true ){
				setMessageKey("ERRO", "paper.ja.cad.lista");	
			}
			else {				
				System.out.println("entrou na cdastrado p usuario");				
				listUsuarios.add(usuario);
				System.out.println("user total final "+listUsuarios.size());
				p.setUsuarios(listUsuarios);					
				paperService.salvar(p); 	
				
				setMessageKey("OK", "paper.cad.lista");
			}	
		}
	}
	
	public void deletarPaperToUsuario(Long id){	
		Paper p = new Paper();
		p = paperService.getById(id);		
		System.out.println("paper = "+p.getTitle());
		if (p.getId()== null){
			setMessageKey("ERRO", "obj.nao_localizado");	
		  }
		else{		
			Usuario usuario = usuarioController.getUsuarioLogado();

			List<Usuario> listUsuarios = new ArrayList<Usuario>();							
			listUsuarios = usuarioService.getUsuarioByPaper(p.getId());			
			for(int i=0 ; i< listUsuarios.size() ; i++){
				if ( listUsuarios.get(i).getId() == usuario.getId() ) {	
					listUsuarios.remove(listUsuarios.get(i));
					break;
				}						
			}	
			p.setUsuarios(listUsuarios);				
			paperService.salvar(p); 	
			
			setMessageKey("OK", "paper.exc.lista");
			getPapersUsuario();		
		}
	}
	
	
	public void sugerirArtigos(List<Author> authors){
		System.out.println("entrou = SUGERIR ARTIGOS");
		
		List<Paper> tempPapers = new ArrayList<Paper>();
		for(int cont=0 ; cont< authors.size() ; cont++){			
			tempPapers.addAll(paperService.getPapersWeb(authors.get(cont).getName()));
		}	
		setPapersSugest(tempPapers);				
	}

}
