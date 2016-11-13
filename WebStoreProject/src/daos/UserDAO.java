package daos;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import model.User;

public interface UserDAO 
{
	public User find(int id);

    public List<User> list();

    public int create(User user);

    public User LogIn(String username, String pass);
    
	List<JSONObject> jlist();

	int edit_auction(int id, String desc, float buy, float first, String ends, String location);

	int delete_auction(int id_user, int id_auction);

	int verify(int id);

	User find_by_name(String usrname);

	int rate(int id, String name, float value);
}
