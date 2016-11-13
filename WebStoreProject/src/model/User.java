package model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String address;

	@Column(name="cell_phone")
	private String cellPhone;

	private String country;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_birth")
	private Date dateOfBirth;

	@Column(name="email_address")
	private String emailAddress;

	private String fname;

	private float latitude;

	private String lname;

	private float longitude;

	private String password;

	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="rated_by")
	private int ratedBy;

	private float rating;

	@Column(name="tax_id")
	private String taxId;

	private String username;

	private boolean validated;

	//bi-directional many-to-one association to Auction
	@OneToMany(mappedBy="user1")
	private List<Auction> auctions1=new ArrayList<Auction>();

	//bi-directional many-to-one association to Auction
	@OneToMany(mappedBy="user2")
	private List<Auction> auctions2=new ArrayList<Auction>();;

	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="user")
	private List<Bid> bids=new ArrayList<Bid>();;

	//bi-directional many-to-one association to EndedAuction
	@OneToMany(mappedBy="user1")
	private List<EndedAuction> endedAuctions1= new ArrayList<EndedAuction>();

	//bi-directional many-to-one association to EndedAuction
	@OneToMany(mappedBy="user2")
	private List<EndedAuction> endedAuctions2= new ArrayList<EndedAuction>();;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user1")
	private List<Message> messages1=new ArrayList<Message>();;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="user2")
	private List<Message> messages2=new ArrayList<Message>();

	//bi-directional many-to-one association to UserHasRatedUser
	@OneToMany(mappedBy="user1")
	private List<UserHasRatedUser> userHasRatedUsers1;

	//bi-directional many-to-one association to UserHasRatedUser
	@OneToMany(mappedBy="user2")
	private List<UserHasRatedUser> userHasRatedUsers2;

	//bi-directional many-to-one association to UserRatedAuction
	@OneToMany(mappedBy="user")
	private List<UserRatedAuction> userRatedAuctions;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getRatedBy() {
		return this.ratedBy;
	}

	public void setRatedBy(int ratedBy) {
		this.ratedBy = ratedBy;
	}

	public float getRating() {
		return this.rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getTaxId() {
		return this.taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getValidated() {
		return this.validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public List<Auction> getAuctions1() {
		return this.auctions1;
	}

	public void setAuctions1(List<Auction> auctions1) {
		this.auctions1 = auctions1;
	}

	public Auction addAuctions1(Auction auctions1) {
		getAuctions1().add(auctions1);
		auctions1.setUser1(this);

		return auctions1;
	}

	public Auction removeAuctions1(Auction auctions1) {
		getAuctions1().remove(auctions1);
		auctions1.setUser1(null);

		return auctions1;
	}

	public List<Auction> getAuctions2() {
		return this.auctions2;
	}

	public void setAuctions2(List<Auction> auctions2) {
		this.auctions2 = auctions2;
	}

	public Auction addAuctions2(Auction auctions2) {
		getAuctions2().add(auctions2);
		auctions2.setUser2(this);

		return auctions2;
	}

	public Auction removeAuctions2(Auction auctions2) {
		getAuctions2().remove(auctions2);
		auctions2.setUser2(null);

		return auctions2;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setUser(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);

		return bid;
	}

	public List<EndedAuction> getEndedAuctions1() {
		return this.endedAuctions1;
	}

	public void setEndedAuctions1(List<EndedAuction> endedAuctions1) {
		this.endedAuctions1 = endedAuctions1;
	}

	public EndedAuction addEndedAuctions1(EndedAuction endedAuctions1) {
		getEndedAuctions1().add(endedAuctions1);
		endedAuctions1.setUser1(this);

		return endedAuctions1;
	}

	public EndedAuction removeEndedAuctions1(EndedAuction endedAuctions1) {
		getEndedAuctions1().remove(endedAuctions1);
		endedAuctions1.setUser1(null);

		return endedAuctions1;
	}

	public List<EndedAuction> getEndedAuctions2() {
		return this.endedAuctions2;
	}

	public void setEndedAuctions2(List<EndedAuction> endedAuctions2) {
		this.endedAuctions2 = endedAuctions2;
	}

	public EndedAuction addEndedAuctions2(EndedAuction endedAuctions2) {
		getEndedAuctions2().add(endedAuctions2);
		endedAuctions2.setUser2(this);

		return endedAuctions2;
	}

	public EndedAuction removeEndedAuctions2(EndedAuction endedAuctions2) {
		getEndedAuctions2().remove(endedAuctions2);
		endedAuctions2.setUser2(null);

		return endedAuctions2;
	}

	public List<Message> getMessages1() {
		return this.messages1;
	}

	public void setMessages1(List<Message> messages1) {
		this.messages1 = messages1;
	}

	public Message addMessages1(Message messages1) {
		getMessages1().add(messages1);
		messages1.setUser1(this);

		return messages1;
	}

	public Message removeMessages1(Message messages1) {
		getMessages1().remove(messages1);

		return messages1;
	}

	public List<Message> getMessages2() {
		return this.messages2;
	}

	public void setMessages2(List<Message> messages2) {
		this.messages2 = messages2;
	}

	public Message addMessages2(Message messages2) {
		getMessages2().add(messages2);
		messages2.setUser2(this);

		return messages2;
	}

	public Message removeMessages2(Message messages2) {
		getMessages2().remove(messages2);

		return messages2;
	}

	public List<UserHasRatedUser> getUserHasRatedUsers1() {
		return this.userHasRatedUsers1;
	}

	public void setUserHasRatedUsers1(List<UserHasRatedUser> userHasRatedUsers1) {
		this.userHasRatedUsers1 = userHasRatedUsers1;
	}

	public UserHasRatedUser addUserHasRatedUsers1(UserHasRatedUser userHasRatedUsers1) {
		getUserHasRatedUsers1().add(userHasRatedUsers1);
		userHasRatedUsers1.setUser1(this);

		return userHasRatedUsers1;
	}

	public UserHasRatedUser removeUserHasRatedUsers1(UserHasRatedUser userHasRatedUsers1) {
		getUserHasRatedUsers1().remove(userHasRatedUsers1);
		userHasRatedUsers1.setUser1(null);

		return userHasRatedUsers1;
	}

	public List<UserHasRatedUser> getUserHasRatedUsers2() {
		return this.userHasRatedUsers2;
	}

	public void setUserHasRatedUsers2(List<UserHasRatedUser> userHasRatedUsers2) {
		this.userHasRatedUsers2 = userHasRatedUsers2;
	}

	public UserHasRatedUser addUserHasRatedUsers2(UserHasRatedUser userHasRatedUsers2) {
		getUserHasRatedUsers2().add(userHasRatedUsers2);
		userHasRatedUsers2.setUser2(this);

		return userHasRatedUsers2;
	}

	public UserHasRatedUser removeUserHasRatedUsers2(UserHasRatedUser userHasRatedUsers2) {
		getUserHasRatedUsers2().remove(userHasRatedUsers2);
		userHasRatedUsers2.setUser2(null);

		return userHasRatedUsers2;
	}

	public List<UserRatedAuction> getUserRatedAuctions() {
		return this.userRatedAuctions;
	}

	public void setUserRatedAuctions(List<UserRatedAuction> userRatedAuctions) {
		this.userRatedAuctions = userRatedAuctions;
	}

	public UserRatedAuction addUserRatedAuction(UserRatedAuction userRatedAuction) {
		getUserRatedAuctions().add(userRatedAuction);
		userRatedAuction.setUser(this);

		return userRatedAuction;
	}

	public UserRatedAuction removeUserRatedAuction(UserRatedAuction userRatedAuction) {
		getUserRatedAuctions().remove(userRatedAuction);
		userRatedAuction.setUser(null);

		return userRatedAuction;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username
				+ ", birthdate=" + dateOfBirth + ", email=" + emailAddress
				+ ", firstname=" + fname
				+ ", lastname=" + lname + ", password=" + password
				+ ", address=" + address
				+ ", country=" + country
				+ ", tax id=" + taxId
				+ ", cell phone=" + cellPhone
				+ ", phone number=" + phoneNumber
				+ ", validated=" + validated
				+ ",rating=" + rating
				+ ",rated by=" + ratedBy
				+ "]";
	}
	public JSONObject toJSON(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("id", id);
			obj.put("first name", fname);
			obj.put("lastname", lname);
			obj.put("birthdate", dateOfBirth);
			obj.put("email", emailAddress);
			obj.put("username", username);
			obj.put("password", password);
			obj.put("address", address);
			obj.put("country", country);
			obj.put("tax id", taxId);
			obj.put("cell phone", cellPhone);
			obj.put("phone number", phoneNumber);	
			obj.put("validated", validated);
		} catch (JSONException e){
			e.printStackTrace();
		}		
		return obj;		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}