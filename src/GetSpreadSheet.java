import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Arrays;

public class GetSpreadSheet {
	private final String clientID;
	private final String clientSecret;
	private final String redirectURI;
	
	public GetSpreadSheet() { 
		final Properties properties = new Properties();
		try { 
			properties.load(new FileInputStream("information.properties"));
		}
		catch(Exception e) { 
			System.out.println("ERROR: " + e.toString());
		}
	
		this.clientID = properties.getProperty("client_id");
		this.clientSecret = properties.getProperty("client_secret");
		this.redirectURI = properties.getProperty("redirect_uri");
	}

	public static void main(String[] ryan) { 
	}
}
