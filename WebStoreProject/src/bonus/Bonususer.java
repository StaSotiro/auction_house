package bonus;

import java.util.Comparator;

public class Bonususer implements Comparable<Bonususer>{
	private int id;
	private int count;



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public int compareTo(Bonususer o) {
		return o.getCount() - this.count;
	}

	public static Comparator<Bonususer> Bonuscomarator =
			new Comparator<Bonususer>() {

		@Override
		public int compare(Bonususer o1, Bonususer o2) {
			return o1.compareTo(o2);
		}

	};
}
