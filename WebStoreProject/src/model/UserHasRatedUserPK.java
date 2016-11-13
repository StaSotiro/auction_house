package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the user_has_rated_user database table.
 * 
 */
@Embeddable
public class UserHasRatedUserPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_user_from", insertable=false, updatable=false)
	private int idUserFrom;

	@Column(name="id_user_to", insertable=false, updatable=false)
	private int idUserTo;

	public UserHasRatedUserPK() {
	}
	public int getIdUserFrom() {
		return this.idUserFrom;
	}
	public void setIdUserFrom(int idUserFrom) {
		this.idUserFrom = idUserFrom;
	}
	public int getIdUserTo() {
		return this.idUserTo;
	}
	public void setIdUserTo(int idUserTo) {
		this.idUserTo = idUserTo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserHasRatedUserPK)) {
			return false;
		}
		UserHasRatedUserPK castOther = (UserHasRatedUserPK)other;
		return 
			(this.idUserFrom == castOther.idUserFrom)
			&& (this.idUserTo == castOther.idUserTo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUserFrom;
		hash = hash * prime + this.idUserTo;
		
		return hash;
	}
}