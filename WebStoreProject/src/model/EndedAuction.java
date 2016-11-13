package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ended_auctions database table.
 * 
 */
@Entity
@Table(name="ended_auctions")
@NamedQuery(name="EndedAuction.findAll", query="SELECT e FROM EndedAuction e")
public class EndedAuction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String description;

	@Column(name="ended_auctionscol")
	private String endedAuctionscol;

	@Column(name="item_location")
	private String itemLocation;

	private float latitude;

	private float longitude;

	private String name;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="seller")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="buyer")
	private User user2;

	public EndedAuction() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndedAuctionscol() {
		return this.endedAuctionscol;
	}

	public void setEndedAuctionscol(String endedAuctionscol) {
		this.endedAuctionscol = endedAuctionscol;
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

}