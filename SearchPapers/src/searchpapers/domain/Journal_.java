package searchpapers.domain;

import java.util.ArrayList;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Journal.class)
public class Journal_ {
	public static volatile SingularAttribute<Journal, Long> id;
	public static volatile SingularAttribute<Journal, String> name;
	public static volatile SingularAttribute<Journal, ArrayList<Paper>> papers;
}
