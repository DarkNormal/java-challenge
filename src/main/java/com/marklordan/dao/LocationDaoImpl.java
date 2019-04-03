package com.marklordan.dao;

import com.marklordan.HibernateSessionFactory;
import com.marklordan.model.Location;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class LocationDaoImpl implements Dao<Location> {

    private SessionFactory sessionFactory;

    public LocationDaoImpl(){
        this.sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @Override
    public Location get(long id){
        Session session = sessionFactory.openSession();
        Location carrier = (Location) session.get(Location.class, id);
        session.close();
        return carrier;
    }

    public Location getLocationByLocationId(long locationId){
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Location where location_id =:locationId ")
                .setLong("locationId", locationId);
        Location location = (Location) query.uniqueResult();
        session.close();
        return location;
    }

    @Override
    public List<Location> getAll() {
        Session session = sessionFactory.openSession();
        List<Location> allLocations = (List<Location>) session.createCriteria(Location.class).list();
        session.close();
        return allLocations;
    }

    @Override
    public long save(Location location) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Long itemId = (Long) session.save(location);

        session.getTransaction().commit();
        session.close();

        return itemId;
    }

    @Override
    public void update(Location location) {
        //TODO implement update func
    }
}
