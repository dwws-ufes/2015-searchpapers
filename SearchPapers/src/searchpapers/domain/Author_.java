package searchpapers.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import br.ufes.inf.nemo.util.ejb3.persistence.PersistentObjectSupport_;

@StaticMetamodel(Author.class)
public class Author_ extends PersistentObjectSupport_ {
	public static volatile SingularAttribute<Author, Long> id;
	public static volatile SingularAttribute<Author, String> name;
	public static volatile SingularAttribute<Author, String> email;
	public static volatile SingularAttribute<Author, Institute> institute;
}
