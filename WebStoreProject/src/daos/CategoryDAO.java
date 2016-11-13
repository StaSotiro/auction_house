package daos;

import java.util.List;

import model.Category;

public interface CategoryDAO {
	public Category find(long id);

	public List<Category> list();

	public int create(Category category);

	public Category search_name(String name);

}
