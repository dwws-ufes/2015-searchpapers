package searchpapers.domain;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Paper.class)
public class Paper_ {
	public static volatile SingularAttribute<Paper, Long> id;
	public static volatile SingularAttribute<Paper, String> title;
	public static volatile SingularAttribute<Paper, Integer> year;
	public static volatile SingularAttribute<Paper, String> summary;
	public static volatile SingularAttribute<Paper, List<Author>> authors;
	public static volatile SingularAttribute<Paper, List<Keyword>> keywords;
	public static volatile SingularAttribute<Paper, byte[]> arquivoPdf;
	public static volatile SingularAttribute<Paper, Journal> journal;
	public static volatile SingularAttribute<Paper, String> urlPaper;
}
