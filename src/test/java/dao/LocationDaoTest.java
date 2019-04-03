package dao;

import com.marklordan.dao.LocationDaoImpl;
import com.marklordan.model.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LocationDaoTest extends DaoTest{

    private LocationDaoImpl locationDao;

    @Before
    public void setUp(){
        locationDao = new LocationDaoImpl();
    }

    @Test
    public void testNewLocationSavedToDatabaseAndAbilityToQuery(){
        long id = createLocation();
        assertThat(id, is(greaterThan(0L)));

        Location locationById = locationDao.get(id);
        assertThat(locationById.getId(), is(equalTo(id)));

        Location locationByLocationId = locationDao.getLocationByLocationId(locationById.getLocationId());
        assertThat(locationByLocationId, is(notNullValue()));
        assertThat(locationById.getId(), is(equalTo(locationByLocationId.getId())));
    }

    @Test
    public void testSelectAllLocations(){
        createLocation();
        createLocation();
        List<Location> allLocations = locationDao.getAll();
        assertThat(allLocations, is(notNullValue()));
        assertThat(allLocations, is(not(empty())));
    }

    private long createLocation(){
        Location location = new Location(locationCounter, "TEST LOCATION");
        long id = locationDao.save(location);
        locationCounter++;
        return id;
    }
}
