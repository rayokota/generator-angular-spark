package <%= packageName %>.util;

<% if (entities.length > 0) { %>import <%= packageName %>.models.*;<% } %>

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    
    private static final SessionFactory SESSION_FACTORY = createSessionFactory();
    private static final ThreadLocal<Session> SESSIONS = new ThreadLocal<>();
    
    public static Session getSession() {
        Session session = SESSIONS.get();
        if (session == null) {
            session = SESSION_FACTORY.openSession();
            SESSIONS.set(session);
        }
        return session;
    }
    
    public static void closeSession() {
        Session session = SESSIONS.get();
        if (session != null) {
            session.close();
            SESSIONS.remove();
        }
    }
    
    private static SessionFactory createSessionFactory() {
        try {
            Configuration configuration = configuration();
            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            return configuration.buildSessionFactory(serviceRegistryBuilder.build());
        } catch (RuntimeException ex) {
            LOGGER.error("Failed to create session factory");
            throw ex;
        }
    }
    
    private static Configuration configuration() {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate-local.cfg.xml");
        return configuration;
    }
    
}
