package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the user_has_rated_user database table.
 * 
 */
@Entity
@Table(name="user_has_rated_user")
@NamedQuery(name="UserHasRatedUser.findAll", query="SELECT u FROM UserHasRatedUser u")
public class UserHasRatedUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserHasRatedUserPK id;

	private float rating;

	@Temporal(TemporalType.DATE)
	private Date time;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user_from")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user_to")
	private User user2;

	public UserHasRatedUser() {
	}

	public UserHasRatedUserPK getId() {
		return this.id;
	}

	public void setId(UserHasRatedUserPK id) {
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