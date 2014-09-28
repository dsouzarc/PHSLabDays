import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGridException;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class SendMessages {
	private static final Scanner theScanner = new Scanner(System.in);

	public static void main(String[] ryan) throws Exception {
		final Properties properties = new Properties();
		properties.load(new FileInputStream("information.properties"));

		final String username = properties.getProperty("username");
		final String password = properties.getProperty("password");
		final String phone = properties.getProperty("phone")
				+ Variables.VERIZON;

		final SendGrid sendgrid = new SendGrid(username, password);

		Email email = new Email();
		email.addTo(phone);
		// email.addToName("What");
		email.setFrom("dsouzarc@gmail.com");
		email.setSubject("Augustus?");
		email.setText("If you get this, text me (Ryan D'souza)");
		try {
			sendgrid.send(email);
			System.out.println("Sent!");
		} catch (SendGridException e) {
			e.printStackTrace();
			System.out.println("Error" + e.toString());
		}
	}

}
