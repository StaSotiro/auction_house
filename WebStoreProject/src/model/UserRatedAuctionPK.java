package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the user_rated_auction database table.
 * 
 */
@Embeddable
public class UserRatedAuctionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_user", insertable=false, updatable=false)
	private int idUser;

	@Column(name="id_auction", insertable=false, updatable=false)
	private int idAuction;

	public UserRatedAuctionPK() {
	}
	public int getIdUser() {
		return this.idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public int getIdAuction() {
		return this.idAuction;
	}
	public void setIdAuction(int idAuction) {
		this.idAuction = idAuction;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserRatedAuctionPK)) {
			return false;
		}
		UserRatedAuctionPK castOther = (UserRatedAuctionPK)other;
		return 
			(this.idUser == castOther.idUser)
			&& (this.idAuction == castOther.idAuction);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUser;
		hash = hash * prime + this.idAuction;
		
		return hash;
	}
}