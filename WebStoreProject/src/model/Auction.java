package model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the auctions database table.
 * 
 */
@Entity
@Table(name="auctions")
@NamedQuery(name="Auction.findAll", query="SELECT a FROM Auction a")
public class Auction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="buy_price")
	private float buyPrice;

	@Column(name="current_bid")
	private float currentBid;

	@Lob
	private String description;

	@Temporal(TemporalType.DATE)
	private Date ends;

	@Column(name="first_bid")
	private float firstBid;

	@Column(name="item_location")
	private String itemLocation;

	private float latitude;

	private float longitude;

	private String name;

	@Column(name="number_of_bids")
	private int numberOfBids;

	@Temporal(TemporalType.DATE)
	private Date started;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="seller")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="high_bidder")
	private User user2;

	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="auction")
	private List<Bid> bids=new ArrayList<Bid>();

	@ManyToMany
	@JoinTable(name="auctions_has_category",
	joinColumns={@JoinColumn(name="id", nullable=false)},
	inverseJoinColumns={@JoinColumn(name="name", nullable=false)})
	private List<Category> categories=new ArrayList<Category>();

	//bi-directional many-to-one association to Image
	@OneToMany(mappedBy="auction")
	private List<Image> images=new ArrayList<Image>();

	//bi-directional many-to-one association to UserRatedAuction
	@OneToMany(mappedBy="auction")
	private List<UserRatedAuction> userRatedAuctions;

	public Auction() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getBuyPrice() {
		return this.buyPrice;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public float getCurrentBid() {
		return this.currentBid;
	}

	public void setCurrentBid(float currentBid) {
		this.currentBid = currentBid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEnds() {
		return this.ends;
	}

	public void setEnds(Date ends) {
		this.ends = ends;
	}

	public float getFirstBid() {
		return this.firstBid;
	}

	public void setFirstBid(float firstBid) {
		this.firstBid = firstBid;
	}

	public String getItemLocation() {
		return this.itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfBids() {
		return this.numberOfBids;
	}

	public void setNumberOfBids(int numberOfBids) {
		this.numberOfBids = numberOfBids;
	}

	public Date getStarted() {
		return this.started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setAuction(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);

		return bid;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Image> getImages() {
		return this.images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image addImage(Image image) {
		getImages().add(image);
		image.setAuction(this);

		return image;
	}

	public Image removeImage(Image image) {
		getImages().remove(image);
		return image;
	}

	public List<UserRatedAuction> getUserRatedAuctions() {
		return this.userRatedAuctions;
	}

	public void setUserRatedAuctions(List<UserRatedAuction> userRatedAuctions) {
		this.userRatedAuctions = userRatedAuctions;
	}

	public UserRatedAuction addUserRatedAuction(UserRatedAuction userRatedAuction) {
		getUserRatedAuctions().add(userRatedAuction);
		userRatedAuction.setAuction(this);

		return userRatedAuction;
	}

	public UserRatedAuction removeUserRatedAuction(UserRatedAuction userRatedAuction) {
		getUserRatedAuctions().remove(userRatedAuction);
		userRatedAuction.setAuction(null);

		return userRatedAuction;
	}

}