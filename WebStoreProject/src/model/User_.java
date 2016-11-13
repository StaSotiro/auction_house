package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-04-24T12:52:47.091+0300")
@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> address;
	public static volatile SingularAttribute<User, String> cellPhone;
	public static volatile SingularAttribute<User, String> country;
	public static volatile SingularAttribute<User, Date> dateOfBirth;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, String> fname;
	public static volatile SingularAttribute<User, String> lname;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> phoneNumber;
	public static volatile SingularAttribute<User, String> taxId;
	public static volatile SingularAttribute<User, String> username;
	public static volatile ListAttribute<User, Auction> auctions1;
	public static volatile ListAttribute<User, Auction> auctions2;
}
