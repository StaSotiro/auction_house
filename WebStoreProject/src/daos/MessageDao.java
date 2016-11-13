package daos;

import model.Message;

public interface MessageDao {
	public int create(Message message);
	Message find(int id);
	int delete_inbox(Message message);
	int delete_sent(Message message);
	int get_unread_number(int id);
}
