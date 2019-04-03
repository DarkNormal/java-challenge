package connect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklordan.connect.ConnectionUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context.xml"})
public class ConnectionUtilTest {

    @Value("${driver.details.url}")
    private String driverBusAssignmentUrl;

    @Value("${driver.details.url.timeout}")
    private int requestTimeout;

    @Value("${driver.details.url.retry.count}")
    private int maxRetryAttempts;

    private static final String rawJsonTestResponse = "{\"results\":[{\"oper_nbr\":69,\"carrier_cd\":\"GLX \",\"last_name\":\"GARCIA\",\"first_name\":\"ABEL\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":82,\"carrier_cd\":\"GLX \",\"last_name\":\"ROCHA\",\"first_name\":\"FERNANDO\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":96,\"carrier_cd\":\"GLX \",\"last_name\":\"GALVAN\",\"first_name\":\"JESUS\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":314,\"carrier_cd\":\"GLI \",\"last_name\":\"FINNIE\",\"first_name\":\"EUGENE\",\"middle_init\":\" \",\"home_loc_6\":400554,\"home_loc_3\":\"MEM\",\"oper_class\":\"R\"},{\"oper_nbr\":315,\"carrier_cd\":\"GLI \",\"last_name\":\"SMITH\",\"first_name\":\"CURTIS\",\"middle_init\":\"H\",\"home_loc_6\":400554,\"home_loc_3\":\"MEM\",\"oper_class\":\"R\"},{\"oper_nbr\":327,\"carrier_cd\":\"GLX \",\"last_name\":\"GARCIA\",\"first_name\":\"RICK\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":370,\"carrier_cd\":\"GLX \",\"last_name\":\"RODRIGUEZ\",\"first_name\":\"JESUS\",\"middle_init\":null,\"home_loc_6\":910327,\"home_loc_3\":\"NLX\",\"oper_class\":\"T\"},{\"oper_nbr\":393,\"carrier_cd\":\"GLX \",\"last_name\":\"CORDERO\",\"first_name\":\"SERGIO\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":424,\"carrier_cd\":\"GLX \",\"last_name\":\"FARRIS\",\"first_name\":\"BILL\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":432,\"carrier_cd\":\"GLX \",\"last_name\":\"REYES\",\"first_name\":\"GABRIELA\",\"middle_init\":null,\"home_loc_6\":681340,\"home_loc_3\":\"HGN\",\"oper_class\":\"T\"},{\"oper_nbr\":464,\"carrier_cd\":\"ARO \",\"last_name\":\"INVALID\",\"first_name\":\"ARROW TRAILWAYS\",\"middle_init\":\" \",\"home_loc_6\":680174,\"home_loc_3\":\"AUS\",\"oper_class\":\"R\"}]}";

    private ConnectionUtil connectionUtil;
    private JsonNode testResponse;
    private RestTemplate restTemplate;


    @Before
    public void setUp() throws IOException {
        connectionUtil = new ConnectionUtil();
        ReflectionTestUtils.setField(connectionUtil, "driverBusAssignmentUrl", driverBusAssignmentUrl);
        ReflectionTestUtils.setField(connectionUtil, "requestTimeout", requestTimeout);
        ReflectionTestUtils.setField(connectionUtil, "maxRetryAttempts", maxRetryAttempts);

        restTemplate = EasyMock.createMock(RestTemplate.class);
        ReflectionTestUtils.setField(connectionUtil, "restTemplate", restTemplate);

        ObjectMapper mapper = new ObjectMapper();
        testResponse = mapper.readTree(rawJsonTestResponse);
    }

    @Test
    public void testUnsuccessful() {
        EasyMock.expect(restTemplate.getForObject(driverBusAssignmentUrl, JsonNode.class))
                .andThrow(new RestClientException("Test Rest Client Exception")).times(maxRetryAttempts);
        EasyMock.replay(restTemplate);

        connectionUtil.retrieveDriverJson();

        EasyMock.verify(restTemplate);
        EasyMock.reset(restTemplate);
    }

    @Test
    public void testSuccessfulRetrieveJsonFromUrl() {
        //Mock 2 rest exceptions with a successful response on the 3rd try
        EasyMock.expect(restTemplate.getForObject(driverBusAssignmentUrl, JsonNode.class))
                .andThrow(new RestClientException("Test Rest Client Exception")).times(maxRetryAttempts - 1)
                .andReturn(testResponse);
        EasyMock.replay(restTemplate);

        connectionUtil.retrieveDriverJson();

        EasyMock.verify(restTemplate);
        EasyMock.reset(restTemplate);
    }
}
