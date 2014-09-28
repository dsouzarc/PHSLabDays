import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GetSpreadSheet {
	private final String CLIENT_ID;
	private final String CLIENT_SECRET;
	private final String REDIRECT_URI;
	private final String email;
	private final String p12Name;
	private final String fileID;

	public GetSpreadSheet() throws IOException {
		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("information.properties"));
		} catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}

		this.CLIENT_ID = properties.getProperty("client_id");
		this.CLIENT_SECRET = properties.getProperty("client_secret");
		this.REDIRECT_URI = properties.getProperty("redirect_uri");
		this.email = properties.getProperty("client-email");
		this.p12Name = properties.getProperty("p12");
		this.fileID = properties.getProperty("fileID");
		
		System.out.println(email);

		try {
			final HttpTransport httpTransport = new NetHttpTransport();
			final JacksonFactory jsonFactory = new JacksonFactory();

			final GoogleCredential credential = new GoogleCredential.Builder()
			.setTransport(httpTransport)
			.setJsonFactory(jsonFactory)
			.setServiceAccountId("1081632893536-2979jv8bee0fjeg1010t5g5ds87l5dak@developer.gserviceaccount.com")
			.setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE))
			.setServiceAccountPrivateKeyFromP12File(new java.io.File(p12Name))
			.build();
			
			System.out.println("HER 2E: " + credential.getAccessToken());
			
			final Drive service = new Drive.Builder(httpTransport, jsonFactory,null).setHttpRequestInitializer(credential)
					.setApplicationName("PHS Lab Days").build();
			System.out.println(service.getBaseUrl());
			final File file = service.files().get(fileID).execute();
		
			Files.List request = service.files().list().setQ("title contains 'PHS' and title contains 'Lab'");
			List<File> result = new ArrayList<File>();
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
			
			for(File r : result) { 
				System.out.println("HERE: " + r.getDownloadUrl() + " NEXT: " + r.getDescription());
			}
			
			
			System.out.println("HERE: " + file.getWebViewLink());
			
			final InputStream theIS = downloadFile(service, file);
			
			final String result1 = convertStreamToString(theIS);
			
			System.out.println(result1);
			
		} 
		catch (Exception e) {
			System.out.println("error " + e.toString());
		}
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	private InputStream downloadFile(Drive service, File file) {
		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
			try {
				HttpResponse resp = service.getRequestFactory()
						.buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				return resp.getContent();
			} catch (IOException e) {
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
