package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklordan.dao.CarrierDaoImpl;
import com.marklordan.dao.DriverDaoImpl;
import com.marklordan.dao.LocationDaoImpl;
import com.marklordan.model.Carrier;
import com.marklordan.model.Driver;
import com.marklordan.model.Location;
import com.marklordan.service.DriverService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.easymock.EasyMock.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context.xml"})
public class DriverServiceTest {

    private static final String rawJsonTestResponse = "[{\"oper_nbr\":69,\"carrier_cd\":\"GLX \",\"last_name\":\"GARCIA\",\"first_name\":\"ABEL\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":82,\"carrier_cd\":\"GLX \",\"last_name\":\"ROCHA\",\"first_name\":\"FERNANDO\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":96,\"carrier_cd\":\"GLX \",\"last_name\":\"GALVAN\",\"first_name\":\"JESUS\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":314,\"carrier_cd\":\"GLI \",\"last_name\":\"FINNIE\",\"first_name\":\"EUGENE\",\"middle_init\":\" \",\"home_loc_6\":400554,\"home_loc_3\":\"MEM\",\"oper_class\":\"R\"},{\"oper_nbr\":315,\"carrier_cd\":\"GLI \",\"last_name\":\"SMITH\",\"first_name\":\"CURTIS\",\"middle_init\":\"H\",\"home_loc_6\":400554,\"home_loc_3\":\"MEM\",\"oper_class\":\"R\"},{\"oper_nbr\":327,\"carrier_cd\":\"GLX \",\"last_name\":\"GARCIA\",\"first_name\":\"RICK\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":370,\"carrier_cd\":\"GLX \",\"last_name\":\"RODRIGUEZ\",\"first_name\":\"JESUS\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":393,\"carrier_cd\":\"GLX \",\"last_name\":\"CORDERO\",\"first_name\":\"SERGIO\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":424,\"carrier_cd\":\"GLX \",\"last_name\":\"FARRIS\",\"first_name\":\"BILL\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":432,\"carrier_cd\":\"GLX \",\"last_name\":\"REYES\",\"first_name\":\"GABRIELA\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":464,\"carrier_cd\":\"ARO \",\"last_name\":\"INVALID\",\"first_name\":\"ARROW TRAILWAYS\",\"middle_init\":\" \",\"home_loc_6\":680174,\"home_loc_3\":\"AUS\",\"oper_class\":\"R\"}]";

    @Test
    public void testProcessJsonResultset() throws IOException {
        DriverService driverService = new DriverService();

        DriverDaoImpl driverDao = EasyMock.createMock(DriverDaoImpl.class);
        CarrierDaoImpl carrierDao = EasyMock.createMock(CarrierDaoImpl.class);
        LocationDaoImpl locationDao = EasyMock.createMock(LocationDaoImpl.class);

        ReflectionTestUtils.setField(driverService, "driverDao", driverDao);
        ReflectionTestUtils.setField(driverService, "carrierDao", carrierDao);
        ReflectionTestUtils.setField(driverService, "locationDao", locationDao);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(rawJsonTestResponse);

        EasyMock.expect(carrierDao.getCarrierByName(anyString()))
                .andReturn(null).anyTimes();
        EasyMock.expect(carrierDao.save(anyObject(Carrier.class)))
                .andReturn(0L).anyTimes();

        EasyMock.expect(locationDao.getLocationByLocationId(anyLong()))
                .andReturn(null).anyTimes();
        EasyMock.expect(locationDao.save(anyObject(Location.class)))
                .andReturn(0L).anyTimes();

        EasyMock.expect(driverDao.save(anyObject(Driver.class)))
                .andReturn(0L).anyTimes();


        EasyMock.replay(carrierDao);
        EasyMock.replay(locationDao);
        EasyMock.replay(driverDao);

        driverService.processJsonResultset(jsonNode);
        driverService.persistRecordsToDb();

        EasyMock.verify(carrierDao);
        EasyMock.verify(locationDao);
        EasyMock.verify(driverDao);
        EasyMock.reset(carrierDao);
        EasyMock.reset(locationDao);
        EasyMock.reset(driverDao);
    }
}
