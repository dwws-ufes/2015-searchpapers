package searchpapers.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Keyword.class)
public class Keyword_ {

	public static volatile SingularAttribute<Keyword, Long> id;
	public static volatile SingularAttribute<Keyword, String> word;
}
