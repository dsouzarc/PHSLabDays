import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGridException;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
public class SendMessages {
	private static final Scanner theScanner = new Scanner(System.in);
	private static final String fileName = "PHS Lab Days - Form Responses 1.csv";

	public static void main(String[] ryan) throws Exception {
		final Properties properties = new Properties();
		properties.load(new FileInputStream("information.properties"));

		final String username = properties.getProperty("username");
		final String password = properties.getProperty("password");
		final String phone = properties.getProperty("phone")
				+ Variables.VERIZON;
		
		final Person[] thePeople = getPeople();
		
		for(Person person : thePeople) { 
			System.out.println(person.toString());
		}
		

		/*final SendGrid sendgrid = new SendGrid(username, password);

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
		}*/
		getPeople();
	}
	
	private static Person[] getPeople() { 
		try { 
			final LinkedList<Person> thePeople = new LinkedList<Person>();
			
			final BufferedReader theReader = 
					new BufferedReader(new FileReader(fileName));
			
			//Un-needed stuff (like titles/table names, etc.)
			theReader.readLine();
			Pattern commaPattern = Pattern.compile(",(?![^\"\"]*\\))") ;
			while(theReader.ready()) { 
				final String line = theReader.readLine();
				System.out.println(line);
				final String[] data = (line.replace(", ", "|").split(","));
				
				final String name = data[1];
				final String number = data[2];
				final String carrier = assignCarrier(data[3].toLowerCase());
				final boolean everyday = data[3].contains("Every");
				final String science1 = data[4];
				final char[] labDays1 = getLabDays(data[5]);
				final String science2 = data[6];
				final char[] labDays2 = getLabDays(data[7]);
				final String misc = data[8];
				final char[] miscDays = getLabDays(data[9]);
				
				final Person person = new Person(name, number, carrier, 
						new Science[]{new Science(science1, labDays1), new Science(science2, labDays2)}, 
						new Science(misc, miscDays), everyday);
				thePeople.add(person);
			}
			
			return thePeople.toArray(new Person[thePeople.size()]);
		}
		catch(Exception e) { 
			System.out.println("Error: " + e.toString());
		}
		return null;
	}
	
	private static char[] getLabDays(String text) { 
		text = text.replace("\"", "");
		
		try { 
			final String[] days = text.split("|");
			final char[] chars = new char[days.length];
			for(int i = 0; i < days.length; i++) { 
				chars[i] = days[i].charAt(0);
			}
			return chars;
		}
		catch(Exception e) { 
			return null;
		}
	}

	private static String assignCarrier(final String name) { 
		if(name.contains("verizon")) { 
			return Variables.VERIZON;
		}
		if(name.contains("at")) { 
			return Variables.ATT;
		}
		if(name.contains("t-mobile")) { 
			return Variables.TMOBILE;
		}
		if(name.contains("virgin mobile")) { 
			return Variables.VIRGINMOBILE;
		}
		if(name.contains("cingular")) { 
			return Variables.CINGULAR;
		}
		if(name.contains("sprint")) { 
			return Variables.SPRINT;
		}
		if(name.contains("Nextel")) { 
			return Variables.NEXTEL;
		}
		System.out.println("ERROR: " + name);
		return Variables.VERIZON;
	}
}
