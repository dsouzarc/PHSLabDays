import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;

//https://docs.google.com/spreadsheets/d/1OpZPyzOHbBeDHrFaxZbD-5ASiZKM7-U-JNl7PUNXYw4/edit#gid=1828455022

public class SendMessages {

	private static final Scanner theScanner = new Scanner(System.in);
	private static final String fileName = "PHS Lab Days (Responses) - Form Responses 1.csv";
	private static final String storedFileName = "src/numbers.txt";

	private final HashMap<Integer, Person> theMap = new HashMap<Integer, Person>();

	private final String username, password, phone;
	private final SendGrid sendgrid;

	public SendMessages() throws Exception {
		final Properties properties = new Properties();
		properties.load(new FileInputStream("information.properties"));

		this.username = properties.getProperty("username");
		this.password = properties.getProperty("password");
		this.phone = properties.getProperty("phone") + Variables.VERIZON;
		this.sendgrid = new SendGrid(username, password);

		// Gets the people in the saved JSON textfile, adds it to hashmap
		updatePeopleFromTextFile();
		
		//Adds the new people to the HashMap and sends a welcome message
		sendWelcome(getNewPeople());
		saveEveryone();
		sendDaily();
	}
	
	public static void main(String[] ryan) throws Exception {
		final SendMessages theSender = new SendMessages();
		System.out.println("FINE!");
	}
	
	/** Sends a text to everyone who needs it */
	public void sendDaily() {
		Person.letterDay = 'G';
		Person.message = "Good Morning"; // Can also be 'hi!'
		Person.numSchoolDaysOver = 22;
		Person.noSchool = "Thurs, Nov. 6th, No School";

		final Set<Integer> peopleKey = theMap.keySet();

		Email email;
		for (Integer key : peopleKey) {
			final Person person = theMap.get(key);
			if(person.shouldGetMessage()) { 
				System.out.println("HERE: " + person.toString());
				email = new Email();
				email.addTo(person.getPhoneNumber() + person.getCarrier());
				email.setFrom("dsouzarc@gmail.com");
				email.setSubject(person.getGreeting());
				email.setText(person.getMessage() + " Note: Security drill from 9:15-10AM today");
				try {
					sendgrid.send(email);
					System.out.println("Sent Daily! " + person.getPhoneNumber()
							+ person.getGreeting() + "\t" + person.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error sending daily" + e.toString() + "\t"
							+ person.getPhoneNumber());
				}
			}
		}
	}
	
	/** Sends a welcome message to all the new people */
	private void sendWelcome(final LinkedList<Person> newPeople) {
		if(newPeople.size() > 100) { 
			System.out.println("TOO MANY NEW PEOPLE");
			return;
		}
		Email email;
		for (Person person : newPeople) {
			email = new Email();
			email.addTo(person.getPhoneNumber() + person.getCarrier());
			email.setFrom("dsouzarc@gmail.com");
			email.setSubject("Welcome to PHS Lab Days");
			email.setText("If you have any questions, please contact Ryan D'souza @ dsouzarc@gmail.com "
					+ "or (609) 915 4930");
			try {
				sendgrid.send(email);
				System.out.println("Sent Welcome! " + person.getPhoneNumber());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error sending welcome" + e.toString()
						+ "\t" + person.getPhoneNumber());
			}
		}
	}

	/** Sends a message to everyone */
	private void sendMessage(final String subject, final String message) {
		Email email;
		final Set<Integer> peopleKey = theMap.keySet();
		for (Integer key : peopleKey) {
			final Person person = theMap.get(key);
			email = new Email();
			email.addTo(person.getPhoneNumber() + person.getCarrier());
			email.setFrom("dsouzarc@gmail.com");
			email.setSubject(message);
			email.setText(subject);
			try {
				//sendgrid.send(email);
				System.out.println("Sent Message! " + person.getPhoneNumber()
						+ person.getGreeting());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error sending message: " + e.toString()
						+ "\t" + person.getPhoneNumber());
			}
		}
	}

	/**
	 * Reads through all people from csv file If we already have them, don't do
	 * anything If not, add them to a list to return and add them to the hashmap
	 */
	private LinkedList<Person> getNewPeople() {
		final LinkedList<Person> newPeople = new LinkedList<Person>();
		final LinkedList<Person> csvPeople = getPeople();

		for (Person person : csvPeople) {
			if (!theMap.containsKey(person.hashCode())) {
				newPeople.add(person);
				theMap.put(person.hashCode(), person);
			}
		}
		
		System.out.println(newPeople.size() + " NEW PEOPLE");
		System.out.println(csvPeople.size() + " PEOPLE FROM BEFORE");
		System.out.println(theMap.keySet().size() + " TOTAL PEOPLE");
		return newPeople;
	}

	/**
	 * Gets all the people from the textfile Updates the global hashmap with
	 * them
	 */
	private void updatePeopleFromTextFile() {
		try {
			final StringBuilder allData = new StringBuilder("");
			final BufferedReader theReader = new BufferedReader(new FileReader(
					storedFileName));

			while (theReader.ready()) {
				allData.append(theReader.readLine());
			}

			final JSONObject theObj = new JSONObject(allData.toString());
			final JSONArray peopleArray = theObj.getJSONArray("people");

			for (int i = 0; i < peopleArray.length(); i++) {
				final Person tP = Person.getPerson(peopleArray.getJSONObject(i));
				theMap.put(tP.hashCode(), tP);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating people from textfile: "
					+ e.toString());
			System.exit(0);
		}
	}

	/** Saves everyone in HashMap as JSONObjects in text file */
	public void saveEveryone() {
		final File file = new File(storedFileName);
		final JSONObject allPeople = new JSONObject();
		final JSONArray info = new JSONArray();

		final Set<Integer> peopleKey = theMap.keySet();

		for (Integer key : peopleKey) {
			info.put(theMap.get(key).getJSON());
		}

		allPeople.put("people", info);

		try {
			final BufferedWriter theWriter = new BufferedWriter(new FileWriter(file));
			theWriter.write(allPeople.toString());
			theWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error saving: " + e.toString());
		}
	}

	private static LinkedList<Person> getPeople() {
		final LinkedList<Person> thePeople = new LinkedList<Person>();
		try {

			final BufferedReader theReader = new BufferedReader(new FileReader(
					fileName));

			// Un-needed stuff (like titles/table names, etc.)
			theReader.readLine();

			while (theReader.ready()) {
				final String line = theReader.readLine() + ", ";
				final String[] data = (line.replace(", ", "|").replace(",,",
						", ,").split(","));

				/*
				 * System.out.println(line); for(int i = 0; i < data.length;
				 * i++) { System.out.println(i + "\t" + data[i]); }
				 */

				final String name = data[1];
				final String number = formatNumber(data[2]);
				final String carrier = assignCarrier(data[3].toLowerCase());
				final boolean everyday = data[4].contains("Every");
				final String science = data[5];
				final char[] labDays = getLabDays(data[6]);
				final char[] miscDays = getLabDays(data[7]);
				final String misc = data[8];

				final Person person = new Person(name, number, carrier,
						new Science(science, labDays), new Science(misc,
								miscDays), everyday);

				//System.out.println("From file: " + person.toString());

				if (number.length() > 7 && carrier.length() > 4) {
					thePeople.add(person);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error reading from textfile: " + e.toString());
		}
		return thePeople;
	}

	private static String formatNumber(String text) {
		text = text.replace("(", "").replace(")", "");
		text = text.replace("-", "").replace(" ", "");

		// Just in case some idiot puts a police number
		text = text.replace("911", "");
		return text;
	}

	private static char[] getLabDays(String text) {
		final LinkedList<Character> theChars = new LinkedList<Character>();

		for (Character theChar : text.toCharArray()) {
			final int charVal = (int) theChar;

			// Between 'A' and 'G'
			if (charVal >= 65 && charVal <= 71) {
				theChars.add(theChar);
			}
		}

		final char[] answer = new char[theChars.size()];
		int counter = 0;
		for (Character c : theChars) {
			answer[counter] = c;
			counter++;
		}
		return answer;
	}

	private static String assignCarrier(final String name) {
		if (name.contains("verizon")) {
			return Variables.VERIZON;
		}
		if (name.contains("at")) {
			return Variables.ATT;
		}
		if (name.contains("t-mobile")) {
			return Variables.TMOBILE;
		}
		if (name.contains("virgin mobile")) {
			return Variables.VIRGINMOBILE;
		}
		if (name.contains("cingular")) {
			return Variables.CINGULAR;
		}
		if (name.contains("sprint")) {
			return Variables.SPRINT;
		}
		if (name.contains("Nextel")) {
			return Variables.NEXTEL;
		}
		System.out.println("ERROR: " + name);
		return Variables.VERIZON;
	}
}
