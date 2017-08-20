package com.sloverse.extension.zone.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sloverse.extension.zone.util.SloverseSessionProperties;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class LoginEventHandler extends BaseServerEventHandler
{	
	@Override
	public void handleServerEvent(ISFSEvent loginEvent) throws SFSException 
	{
		String loginName = (String)loginEvent.getParameter(SFSEventParam.LOGIN_NAME);
		String loginPassword = (String)loginEvent.getParameter(SFSEventParam.LOGIN_PASSWORD);
		
		ISession loginSession = (ISession)loginEvent.getParameter(SFSEventParam.SESSION);
		IDBManager dbManager = this.getParentExtension().getParentZone().getDBManager();
		
		try (Connection connection = dbManager.getConnection())
		{
			PreparedStatement sql = connection.prepareStatement("SELECT * FROM Player WHERE player_username=?");
			sql.setString(1, loginName);
			ResultSet dbResult = sql.executeQuery();
			
			// Unknown username error.
			if (!dbResult.first())
			{
				SFSErrorData unknownUsernameError = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				unknownUsernameError.addParameter(loginName);
				throw new SFSLoginException(getGenericErrorMessage(), unknownUsernameError);
			}
			
			trace(loginName + " | " + dbResult.getString("player_username"));
			trace(loginPassword + " | " + dbResult.getString("player_password"));
			trace("Id: " + " | " + dbResult.getInt("player_playerID"));
			
			// Password field left blank.
			if (loginPassword.length() == 0)
			{
				SFSErrorData emptyPasswordError = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
				emptyPasswordError.addParameter(loginName);
				throw new SFSLoginException(getGenericErrorMessage(), emptyPasswordError);
			}
			
			// Password did not match database password.
			if (!this.getApi().checkSecurePassword(loginSession, dbResult.getString("player_password"), loginPassword))
			{
				SFSErrorData wrongPasswordError = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
				wrongPasswordError.addParameter(loginName);
				throw new SFSLoginException(getGenericErrorMessage(), wrongPasswordError);
			}
			
			loginSession.setProperty(SloverseSessionProperties.PLAYER_ID, dbResult.getInt("player_playerID"));
		}
		catch (SQLException e) 
		{
			trace(ExtensionLogLevel.WARN, "Well this is awkward... There seems to be an SQL error: " + e.toString());
			throw new SFSException("Well this is awkward... There seems to be an SQL error.");
		}
		
		trace("Successfully logged in " + loginName + "!");
		
	}
	
	private String getGenericErrorMessage() {
		return "The username or password you entered was incorrect.";
	}
}
