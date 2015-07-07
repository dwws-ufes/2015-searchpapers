package searchpapers.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Cidade.class)
public class Cidade_ {

	public static volatile SingularAttribute<Cidade, Long> id;
	public static volatile SingularAttribute<Cidade, String> nome;
	public static volatile SingularAttribute<Cidade, Estado> estado;
}
