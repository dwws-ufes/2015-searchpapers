package searchpapers.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Recomendacao.class)
public class Recomendacao_ {
	public static volatile SingularAttribute<Recomendacao, Long> id;
	public static volatile SingularAttribute<Recomendacao, Usuario> remetente;
	public static volatile SingularAttribute<Recomendacao, Usuario> destinatario;
	public static volatile SingularAttribute<Recomendacao, String> mensagem;
	public static volatile SingularAttribute<Recomendacao, Paper> paper;
}
