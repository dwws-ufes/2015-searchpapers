package searchpapers.domain;

import java.util.ArrayList;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Estado.class)
public class Estado_ {
	public static volatile SingularAttribute<Estado, Long> id;
	public static volatile SingularAttribute<Estado, String> sigla;
	public static volatile SingularAttribute<Estado, String> descricao;
	public static volatile SingularAttribute<Estado, ArrayList<Cidade>> cidades;
}
