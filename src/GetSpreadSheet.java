import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

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
		this.p12Name = properties.getProperty("p12-name");
		this.fileID = properties.getProperty("fileID");

		try {
			final HttpTransport httpTransport = new NetHttpTransport();
			final JacksonFactory jsonFactory = new JacksonFactory();

			final GoogleCredential credential = new GoogleCredential.Builder()
			.setTransport(httpTransport)
			.setJsonFactory(jsonFactory)
			.setServiceAccountId(email)
			.setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE))
			.setServiceAccountPrivateKeyFromP12File(new java.io.File(p12Name))
			.build();
			
			final Drive service = new Drive.Builder(httpTransport, jsonFactory,null).setHttpRequestInitializer(credential)
					.setApplicationName("PHS Lab Days").build();
			final File file = service.files().get(fileID).execute();
		} 
		catch (Exception e) {
			System.out.println("error " + e.toString());
		}
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
