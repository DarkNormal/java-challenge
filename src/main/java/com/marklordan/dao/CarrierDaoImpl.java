package com.marklordan.dao;

import com.marklordan.HibernateSessionFactory;
import com.marklordan.model.Carrier;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class CarrierDaoImpl implements Dao<Carrier> {

    private SessionFactory sessionFactory;

    public CarrierDaoImpl() {
        this.sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @Override
    public Carrier get(long id) {
        Session session = sessionFactory.openSession();
        Carrier carrier = (Carrier) session.get(Carrier.class, id);
        session.close();
        return carrier;
    }

    public Carrier getCarrierByName(String carrierName){
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Carrier where carrier_name =:name ")
                .setString("name", carrierName);
        Carrier carrier = (Carrier) query.uniqueResult();
        session.close();
        return carrier;
    }

    @Override
    public List<Carrier> getAll() {
        Session session = sessionFactory.openSession();
        List<Carrier> allCarriers = (List<Carrier>) session.createCriteria(Carrier.class).list();
        session.close();
        return allCarriers;
    }

    @Override
    public long save(Carrier carrier) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Long itemId = (Long) session.save(carrier);

        session.getTransaction().commit();
        session.close();

        return itemId;
    }

    @Override
    public void update(Carrier carrier) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.saveOrUpdate(carrier);

        session.getTransaction().commit();
        session.close();
    }
}
