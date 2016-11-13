package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the user_rated_auction database table.
 * 
 */
@Entity
@Table(name="user_rated_auction")
@NamedQuery(name="UserRatedAuction.findAll", query="SELECT u FROM UserRatedAuction u")
public class UserRatedAuction implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserRatedAuctionPK id;

	private float rating;

	@Temporal(TemporalType.DATE)
	private Date time;

	//bi-directional many-to-one association to Auction
	@ManyToOne
	@JoinColumn(name="id_auction")
	private Auction auction;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;

	public UserRatedAuction() {
	}

	public UserRatedAuctionPK getId() {
		return this.id;
	}

	public void setId(UserRatedAuctionPK id) {
		this.id = id;
	}

	public float getRating() {
		return this.rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Auction getAuction() {
		return this.auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}