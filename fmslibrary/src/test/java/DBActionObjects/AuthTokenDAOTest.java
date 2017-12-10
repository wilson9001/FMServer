package DBActionObjects;

import org.junit.Before;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import Models.AuthTokenModel;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthTokenDAOTest
{
    private AuthTokenDAO authTokenDAO;
    private DAO dao;

    @Before
    public void setUp() throws Exception
    {
        authTokenDAO = new AuthTokenDAO();
        dao = new DAO();
    }

	@After
    public void tearDown() throws Exception
    {
        //dao.clear();
    }
	
    @Test
    public void AinsertToken() throws Exception
    {
        AuthTokenModel authTokenModel= new AuthTokenModel("token", "userID", 1);

        assertEquals(authTokenDAO.insertToken(authTokenModel), AuthTokenDAO.insertAuthTokenSuccess);

        assertEquals(authTokenDAO.insertToken(authTokenModel), AuthTokenDAO.SQL_errorMsg);

        authTokenModel = new AuthTokenModel(null, "userID1", 2);

        assertEquals(authTokenDAO.insertToken(authTokenModel), AuthTokenDAO.SQL_errorMsg);

        authTokenModel = new AuthTokenModel("token1", null, 3);

        assertEquals(authTokenDAO.insertToken(authTokenModel), AuthTokenDAO.SQL_errorMsg);
    }

    @Test
    public void BselectAuthToken() throws Exception
    {
        AuthTokenModel authTokenModel = authTokenDAO.selectAuthToken("token");

        assertNotNull(authTokenModel);

        assertEquals(authTokenModel.getUserID(), "userID");
        assertEquals(authTokenModel.getDateTimeCreated(), 1);

        authTokenModel =  authTokenDAO.selectAuthToken("token1");

        assertNull(authTokenModel);
        assertFalse(authTokenDAO.isSQLError());
    }

    @Test
    public void CselectAuthTokenByUser() throws Exception
    {
        AuthTokenModel authTokenModel = authTokenDAO.selectAuthTokenByUser("userID");

        assertNotNull(authTokenModel);

        assertEquals(authTokenModel.getUserID(), "userID");
        assertEquals(authTokenModel.getDateTimeCreated(), 1);

        authTokenModel =  authTokenDAO.selectAuthTokenByUser("user");

        assertNull(authTokenModel);
        assertFalse(authTokenDAO.isSQLError());
    }
}