package searchpapers.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import searchpapers.application.AuthorService;
import searchpapers.domain.Author;
import searchpapers.file.FileImage;
import searchpapers.message.MessageController;

@Named
@SessionScoped
public class AuthorControllerList extends MessageController implements Serializable{
	
	@EJB private AuthorService authorService;	
	
	
	@Inject FileImage fileImage;

	//-- author
	private Author author = new Author();
	public Author getAuthor(){
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	private List<Author> authors;
	public List<Author> getAuthors(){
		authors = authorService.getAuthors();
		return authors;
	}
	

	
	@Inject
	void listar(){
		getAuthors();
	}

	
	public byte[] getImage(byte[] arquivoFoto) throws Exception{
		
		byte[] image = null;
		if (arquivoFoto != null) {
			InputStream input = new ByteArrayInputStream(arquivoFoto);
			BufferedImage bufferImg = ImageIO.read(input);
		
			image = fileImage.BufferedImageToByte(bufferImg);
		}
		else{
			int width=200, height=200;  
			BufferedImage bufferImg = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );  
			Graphics g = bufferImg.createGraphics(); 
			g.setColor( Color.LIGHT_GRAY );  
			g.fillRect( 0, 0, width, height );
			
			image = fileImage.BufferedImageToByte(bufferImg);
		}
		
		return image;
    }
	
	
}
