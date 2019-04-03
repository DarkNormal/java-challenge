package com.marklordan.dao;

import com.marklordan.HibernateSessionFactory;
import com.marklordan.model.Driver;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.logging.Logger;

public class DriverDaoImpl implements Dao<Driver>{

    private static final Logger log = Logger.getLogger(DriverDaoImpl.class.getSimpleName());

    private SessionFactory sessionFactory;

    public DriverDaoImpl() {
        this.sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @Override
    public Driver get(long id) {
        Session session = sessionFactory.openSession();
        return (Driver) session.get(Driver.class, id);
    }

    public Driver getDriverByDriverId(long driverId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Driver where driver_id =:driverId ")
                .setLong("driverId", driverId);
        Driver driver = (Driver) query.uniqueResult();
        Hibernate.initialize(driver.getCarrier());
        Hibernate.initialize(driver.getLocation());
        session.close();
        return driver;
    }

    @Override
    public List<Driver> getAll() {
        Session session = sessionFactory.openSession();
        List<Driver> allLocations = (List<Driver>) session.createCriteria(Driver.class).list();
        session.close();
        return allLocations;
    }

    @Override
    public long save(Driver driver) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Long itemId = (Long) session.save(driver);

        session.getTransaction().commit();
        session.close();

        return itemId;
    }

    @Override
    public void update(Driver driver) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.saveOrUpdate(driver);

        session.getTransaction().commit();
        session.close();
    }

    public Driver getSingleRecord() {
        log.info("Getting single record from Driver table...");
        Session session = sessionFactory.openSession();
        return (Driver) session.createCriteria(Driver.class).setMaxResults(1).uniqueResult();
    }
}
