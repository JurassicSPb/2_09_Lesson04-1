package ru.levelp.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.levelp.entities.Message;

import java.util.List;

public class MessageDatabase implements MessageDAO {
    private static final MessageDatabase instance = new MessageDatabase();

    private MessageDatabase() {

    }

    public static MessageDatabase getInstance() {
        return instance;
    }

    @Override
    public void add(Message message) {
        Session session = HibernateManager.getInstance()
                .getSessionFactory()
                .openSession();
        session.beginTransaction();
        session.save(message);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Message get(int id) {
        Session session = HibernateManager.getInstance()
                .getSessionFactory()
                .openSession();
        Message message = (Message) session.createCriteria(Message.class)
                .add(Restrictions.eq("id", id)).uniqueResult();
        return message;
    }

    @Override
    public List<Message> getAll() {
        Session session = HibernateManager.getInstance()
                .getSessionFactory()
                .openSession();
        List<Message> messages = session.createCriteria(Message.class).list();
        session.close();
        return messages;
    }

    @Override
    public Message remove(int id) {
        Session session = HibernateManager.getInstance()
                .getSessionFactory()
                .openSession();
        session.beginTransaction();
        Message m = get(id);
        session.delete(m);
        session.getTransaction().commit();
        session.close();
        return m;
    }

    /**
     * Получение истории сообщений пользователя username
     *
     * @param username - имя пользователя
     * @return список сообщений
     */
    public List<Message> getHistory(String username) {
//        List<Message> messages = session....
        //с коллекцией никаких преобразований не делается
        return messages;
    }
}
