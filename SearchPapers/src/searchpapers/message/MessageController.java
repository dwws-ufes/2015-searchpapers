package searchpapers.message;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageController {
	
	
	private void setMessage(String tipo, String key, String msg){

		final String stipo;
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		if ((msg == null) && (key != null)){
			Application app = context.getApplication();
			ResourceBundle bundle = app.getResourceBundle(context, "msgs");
			msg = bundle.getString(key);
		}
		
		switch (tipo) {
			case "OK": stipo = "MsgOk"; break;
			case "ERRO": stipo = "MsgErro"; break;
			default: stipo = "MsgOk";
		}

		context.addMessage(stipo, new FacesMessage(msg));
	}
	
	public void setMessageKey(String tipo, String key){
		setMessage(tipo, key, null);
	}
	
	public void setMessageChar(String tipo, String msg){
		setMessage(tipo, null, msg);
	}
	
	
}
