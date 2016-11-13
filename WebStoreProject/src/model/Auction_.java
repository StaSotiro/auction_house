package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-04-24T12:52:47.016+0300")
@StaticMetamodel(Auction.class)
public class Auction_ {
	public static volatile SingularAttribute<Auction, Integer> id;
	public static volatile SingularAttribute<Auction, String> buyPrice;
	public static volatile SingularAttribute<Auction, String> currentBid;
	public static volatile SingularAttribute<Auction, Date> ends;
	public static volatile SingularAttribute<Auction, String> itemLocation;
	public static volatile SingularAttribute<Auction, String> name;
	public static volatile SingularAttribute<Auction, Date> started;
	public static volatile SingularAttribute<Auction, User> user1;
	public static volatile SingularAttribute<Auction, User> user2;
	public static volatile ListAttribute<Auction, Category> categories;
}
