package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklordan.model.Carrier;
import com.marklordan.model.Driver;
import com.marklordan.model.Location;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ModelTest {

    @Test
    public void onCreateModelObjectsVerifyStringsFormatted(){
        Carrier carrier = new Carrier("glx ");
        assertThat(carrier.getCarrierName(), is(equalTo("GLX")));

        carrier.setCarrierName(null);
        assertThat(carrier.getCarrierName(), is(equalTo("")));

        Location location = new Location(1234, "nlx ");
        assertThat(location.getLocationName(), is(equalTo("NLX")));

        Driver driver = new Driver(0, " Mark ", 'D', "Lordan ", 'N', carrier,location);
        assertThat(driver.getFirstName(), is(equalTo("MARK")));
        assertThat(driver.getLastName(), is(equalTo("LORDAN")));
    }

    @Test
    public void testJsonMappingToObjects() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String singleJsonResponse = "{\"oper_nbr\":69,\"carrier_cd\":\"GLX \",\"last_name\":\"GARCIA\",\"first_name\":\"ABEL\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"}";
        JsonNode jsonNode = mapper.readTree(singleJsonResponse);

        Driver driver = mapper.convertValue(jsonNode, Driver.class);

        assertThat(driver.getDriverId(), is(equalTo(69L)));
        assertThat(driver.getCarrier().getCarrierName(), is(equalTo("GLX")));
    }
}
