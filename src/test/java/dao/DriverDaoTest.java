package dao;

import com.marklordan.dao.CarrierDaoImpl;
import com.marklordan.dao.DriverDaoImpl;
import com.marklordan.dao.LocationDaoImpl;
import com.marklordan.model.Carrier;
import com.marklordan.model.Driver;
import com.marklordan.model.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DriverDaoTest extends DaoTest{

    private DriverDaoImpl driverDao;
    private CarrierDaoImpl carrierDao;
    private LocationDaoImpl locationDao;
    private static int driverCounter = 1;

    @Before
    public void setUp(){
        driverDao = new DriverDaoImpl();
        carrierDao = new CarrierDaoImpl();
        locationDao = new LocationDaoImpl();
    }

    @Test
    public void testNewDriverSavedToDatabaseAndAbilityToQuery(){
        long id = createDriver();
        assertThat(id, is(greaterThan(0L)));

        Driver driverById = driverDao.get(id);
        assertThat(driverById.getId(), is(equalTo(id)));
    }

    @Test
    public void testSelectAllDrivers(){
        createDriver();
        createDriver();
        List<Driver> allDrivers = driverDao.getAll();
        assertThat(allDrivers, is(notNullValue()));
        assertThat(allDrivers, is(not(empty())));
    }

    private long createDriver(){
        Carrier carrier = new Carrier("TEST CARRIER " + System.currentTimeMillis());
        carrierDao.save(carrier);
        Location location = new Location(locationCounter, "TEST LOCATION");
        locationDao.save(location);
        Driver driver = new Driver(driverCounter, "Mark", ' ', "Lordan", 'C', carrier, location);
        long id = driverDao.save(driver);
        driverCounter++;
        locationCounter++;
        return id;
    }

}
