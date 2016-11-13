package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-04-24T12:52:47.088+0300")
@StaticMetamodel(Category.class)
public class Category_ {
	public static volatile SingularAttribute<Category, String> name;
	public static volatile SingularAttribute<Category, String> desciption;
	public static volatile ListAttribute<Category, Auction> auctions;
}
