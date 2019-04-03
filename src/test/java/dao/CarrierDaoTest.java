package dao;

import com.marklordan.dao.CarrierDaoImpl;
import com.marklordan.model.Carrier;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CarrierDaoTest {

    private CarrierDaoImpl carrierDao;

    @Before
    public void setUp(){
        carrierDao = new CarrierDaoImpl();
    }

    @Test
    public void testNewCarrierSavedToDatabaseAndAbilityToQuery(){
        long id = createCarrier();
        assertThat(id, is(greaterThan(0L)));

        Carrier carrierById = carrierDao.get(id);
        assertThat(carrierById.getId(), is(equalTo(id)));

        Carrier carrierByName = carrierDao.getCarrierByName(carrierById.getCarrierName());
        assertThat(carrierByName, is(notNullValue()));
        assertThat(carrierById.getId(), is(equalTo(carrierByName.getId())));
    }

    @Test
    public void testSelectAllCarriers(){
        createCarrier();
        createCarrier();
        List<Carrier> allCarriers = carrierDao.getAll();

        assertThat(allCarriers, is(notNullValue()));
        assertThat(allCarriers, is(not(empty())));
    }

    private long createCarrier(){
        Carrier carrier = new Carrier("TEST CARRIER " + System.currentTimeMillis());
        long id = carrierDao.save(carrier);
        return id;
    }
}
