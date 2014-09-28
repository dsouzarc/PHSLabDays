import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;

import java.awt.datatransfer.*;
import java.awt.Toolkit;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;

import javax.swing.JOptionPane;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GetSpreadSheet {
	private final String CLIENT_ID;
	private final String CLIENT_SECRET;
	private final String REDIRECT_URI;
	private final String email;

	public GetSpreadSheet() throws IOException { 
		final Properties properties = new Properties();
		try { 
			properties.load(new FileInputStream("information.properties"));
		}
		catch(Exception e) { 
			System.out.println("ERROR: " + e.toString());
		}
	
		this.CLIENT_ID = properties.getProperty("client_id").replace(" ", "");
		this.CLIENT_SECRET = properties.getProperty("client_secret").replace(" ", "");
		this.REDIRECT_URI = properties.getProperty("redirect_uri").replace(" ", "");
		this.email = properties.getProperty("client-email");

	    /*HttpTransport httpTransport = new NetHttpTransport();
	    JsonFactory jsonFactory = new JacksonFactory();
	   
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
		        .setAccessType("online")
		        .setApprovalPrompt("auto").build();
		
		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
	    System.out.println("Please open the following URL in your browser then type the authorization code:");
	    System.out.println(url);
	    
	    StringSelection stringSelection = new StringSelection (url);
	    Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
	    clpbrd.setContents (stringSelection, null);*/

	    try { 
	    	
			 HttpTransport httpTransport = new NetHttpTransport();
			  JacksonFactory jsonFactory = new JacksonFactory();
			  Collection<String> type = new ArrayList<String>();
			  type.add(DriveScopes.DRIVE);
			  GoogleCredential credential;
			try {
				credential = new GoogleCredential.Builder()
				      .setTransport(httpTransport)
				      .setJsonFactory(jsonFactory)
				      .setServiceAccountId(email)
				      
				      .setServiceAccountScopes(type)
				      //.setServiceAccountScopes(DriveScopes.DRIVE)
				      .setServiceAccountPrivateKeyFromP12File(
				          new java.io.File("PHS Lab Days-4732fc4dfac8.p12"))
				      .build();
				  Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
			      .setHttpRequestInitializer(credential).setApplicationName("PHS Lab Days").build();
				  File file = service.files().get("15rZIuDh1414DJHqEJrEMChlEVULdutRpKWOJa1zHgeg").execute();
			      System.out.println("Title: " + file.getTitle());

			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	
		   
		    /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    //String code = br.readLine();
		    
		    final String text = JOptionPane.showInputDialog("Code:");  
		    
		    GoogleTokenResponse response = flow.newTokenRequest(text).setRedirectUri(REDIRECT_URI).execute();
		    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		    System.out.println("DOWN");
		    //Create a new authorized API client
		    Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
	        .setHttpRequestInitializer(credential)
	        .setApplicationName("PHS Lab Days")
	        .build();
		    
		    System.out.println("DOWN HERE");
		    
		    List<File> result = new ArrayList<File>();
		    Files.List request = service.files().list().setQ("title contains 'PHS' and title contains 'Lab'");// service.files().list();

		    do {
		      try {
		        FileList files = request.execute();

		        result.addAll(files.getItems());
		        request.setPageToken(files.getNextPageToken());
		      } catch (IOException e) {
		        System.out.println("An error occurred: " + e);
		        request.setPageToken(null);
		      }
		    } while (request.getPageToken() != null &&
		             request.getPageToken().length() > 0);
		    
		    for(File file : result)  
		    	System.out.println(file.getTitle());
		    
	
		    GoogleCredential credential5 = new GoogleCredential.Builder().build();
		    
		    
		    
		    
		    
		    File file = service.files().get("15rZIuDh1414DJHqEJrEMChlEVULdutRpKWOJa1zHgeg").execute();
		      System.out.println("Title: " + file.getTitle());

		    System.out.println(file.getWebContentLink());*/
	    }
	    catch(Exception e) { 
	    	System.out.println("error " + e.toString());
	    }
	}
	
	private void setup() { 
		 HttpTransport httpTransport = new NetHttpTransport();
		  JacksonFactory jsonFactory = new JacksonFactory();
		  Collection<String> type = new ArrayList<String>();
		  type.add(DriveScopes.DRIVE);
		  GoogleCredential credential;
		try {
			credential = new GoogleCredential.Builder()
			      .setTransport(httpTransport)
			      .setJsonFactory(jsonFactory)
			      .setServiceAccountId(email)
			      .setServiceAccountScopes(type)
			      //.setServiceAccountScopes(DriveScopes.DRIVE)
			      .setServiceAccountPrivateKeyFromP12File(
			          new java.io.File("PHS Lab Days-4732fc4dfac8.p12"))
			      .build();
			  Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
		      .setHttpRequestInitializer(credential).build();
			  File file = service.files().get("15rZIuDh1414DJHqEJrEMChlEVULdutRpKWOJa1zHgeg").execute();
		      System.out.println("Title: " + file.getTitle());

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private InputStream downloadFile(Drive service, File file) {
	    if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
	      try {
	        HttpResponse resp =
	            service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
	        return resp.getContent();
	      } 
	      catch (IOException e) {
	        e.printStackTrace();
	        return null;
	      }
	    } else {
	      // The file doesn't have any content stored on Drive.
	      return null;
	    }
	  }

	public static void main(String[] ryan) throws Exception {
		new GetSpreadSheet();
	}
}
