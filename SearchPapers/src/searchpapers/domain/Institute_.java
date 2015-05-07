package searchpapers.domain;

import java.util.ArrayList;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Institute.class)
public class Institute_ {
	public static volatile SingularAttribute<Institute, Long> id;
	public static volatile SingularAttribute<Institute, String> name;
	public static volatile SingularAttribute<Institute, ArrayList<Author>> authors;
}
