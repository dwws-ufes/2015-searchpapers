package searchpapers.file;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import searchpapers.domain.Author;
import searchpapers.domain.Keyword;
import searchpapers.domain.Paper;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

@Named
@SessionScoped
public class FilePDF implements Serializable {

	//-- paper
	private Paper paper;
	public Paper getPaper(){
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	//-- paper
	
	//-- keyword
	private Keyword keyword;
	public Keyword getKeyword(){
		return keyword;
	}
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
	private List<Keyword> keywords;
	public List<Keyword> getKeywords(){
		return keywords;
	}
	//-- keyword
	
	//-- Author
	private Author author;
	public Author getAuthor(){
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	private List<Author> authors;
	public List<Author> getAuthors(){
		return authors;
	}
	//-- Author
	
	public void getMetaDados(ByteBuffer arquivoBuffer) throws Exception  {		
	
		//usando lib Lucene
		
		Paper paper = new Paper();			
		String atributo;
		
		Document doc = PDF.open(arquivoBuffer, "teste");
		for (String key : doc.getAttributeKeys()) {			
		    System.out.printf("testeee = "+"%s: %s", key, doc.getAttribute(key));
		    System.out.println();			   
		}
		
		atributo = (String)doc.getAttribute(Document.ATTR_TITLE);	
		if (atributo != null){
			paper.setTitle((String)doc.getAttribute(Document.ATTR_TITLE));	
			System.out.println("A titulo = "+paper.getTitle());
		}			
		
		atributo = (String)doc.getAttribute(Document.ATTR_SUBJECT);	
		if (atributo != null){
			paper.setSummary((String)doc.getAttribute(Document.ATTR_SUBJECT));
			System.out.println("A abstract = "+paper.getSummary());
		}			
		
		keywords = new ArrayList<Keyword>();
		atributo = (String)doc.getAttribute(Document.ATTR_KEYWORDS);		
		if (atributo != null){
			String[] parts = atributo.split(",");
			
			for(int cont=0 ; cont< parts.length ; cont++){
				Keyword keyword = new Keyword();
				keyword.setWord(parts[cont]);
				keywords.add(keyword);
				System.out.println("A keyword = "+keyword.getWord());
	         }				
		}
		paper.setKeywords(keywords);			
		
		setPaper(paper);		
		
		doc.close();					
	}
	
	
	private Image previewPdf;
	public Image getPreviewPdf() {
		return previewPdf;
	}
	public void setPreviewPdf(Image previewPdf) {
		this.previewPdf = previewPdf;
	}
	
	
	public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
 
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
 
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
 
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            System.out.println("The system does not have a screen");
        }
 
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
 
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
 
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
 
        return bimage;
    }
	
	public BufferedImage getPreview(ByteBuffer arquivoBuffer) throws Exception{ 

		PDFFile pdffile = new PDFFile(arquivoBuffer);
        PDFPage page = pdffile.getPage(1);
		
        Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
		
        Image img = page.getImage(
        			rect.width, 
        			rect.height, //width &amp; height
	                rect, // clip rect
	                null, // null for the ImageObserver
	                true, // fill background with white
	                true); // block until drawing is done
        
        BufferedImage bufferImg = toBufferedImage( img );
        
        return bufferImg;
	}
	
	
	public void exibirPdf(byte[] documentoPdf) throws Exception{
		
		 // http://java-bytecode.blogspot.com.br/2012/08/como-exibir-um-pdf-numa-pagina-jsf.html
		//http://www.guj.com.br/java/55341-abrir-pdf-no-browser-via-servlet
		  		
		  FacesContext fc = FacesContext.getCurrentInstance();

	      // Obtem o HttpServletResponse, objeto responsável pela resposta do
	      // servidor ao browser
	      HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

	      // Limpa o buffer do response
	      response.reset();

	      // Seta o tipo de conteudo no cabecalho da resposta. No caso, indica que o
	      // conteudo sera um documento pdf.
	      response.setContentType("application/pdf");

	      // Seta o tamanho do conteudo no cabecalho da resposta. No caso, o tamanho
	      // em bytes do pdf
	      response.setContentLength(documentoPdf.length);

	      // Seta o nome do arquivo e a disposição: "inline" abre no próprio navegador
	      // Mude para "attachment" para indicar que deve ser feito um download
	      response.setHeader("Content-disposition","inline; filename=arquivo.pdf");
		      try {
	
		         // Envia o conteudo do arquivo PDF para o response
		         response.getOutputStream().write(documentoPdf);
	
		         // Descarrega o conteudo do stream, forçando a escrita de qualquer byte
		         // ainda em buffer
		         response.getOutputStream().flush();
	
		         // Fecha o stream, liberando seus recursos
		         response.getOutputStream().close();
	
		         // Sinaliza ao JSF que a resposta HTTP para este pedido já foi gerada
		         fc.responseComplete();
		      } catch (IOException e) {
		         e.printStackTrace();
		      }	      
	}
	
}
