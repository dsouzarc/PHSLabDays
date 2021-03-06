import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;

public class Person {
	private final String name, phoneNumber, carrier;
	private final Science scienceDay;
	private final Science misc;
	private final boolean everyDay; // Message everyday or just on lab days

	public static final Calendar todayCalendar = new GregorianCalendar();

	public static char letterDay = 'C';
	public static String message = "Good Morning"; // Can also be 'hi!'
	public static int numSchoolDaysOver = 18;
	public static String noSchool = "Fri, Oct 3rd, No School";

	public Person(final String name, final String phoneNumber,
			final String carrier, final Science scienceDay, final Science misc,
			final boolean everyDay) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.carrier = carrier;
		this.scienceDay = scienceDay;
		this.misc = misc;
		this.everyDay = everyDay;
	}
	
	/** Returns true if person should get a message */
	public boolean shouldGetMessage() { 
		if(everyDay) { 
			return true;
		}
		
		if(isMonday()) { 
			return true;
		}
		
		if(isDay(scienceDay)) { 
			return true;
		}
		if(isDay(misc)) { 
			return true;
		}
		return false;
	}
	
	/** Return true if today is Monday */
	public static boolean isMonday() { 
		return todayCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}
	
	/** Returns true if it's the science's lab day */
	private static boolean isDay(final Science theScience) { 
		if(theScience == null) { 
			return false;
		}
		
		for(char labDay : theScience.getLabDays()) { 
			if(labDay == letterDay) { 
				return true;
			}
		}
		return false;
	}

	public static String getLetterDay() {
		if (letterDay == 'A' || letterDay == 'E' || letterDay == 'F') {
			return "Today is an '" + letterDay + "' day. ";
		} else {
			return "Today is a '" + letterDay + "' day. ";
		}
	}

	public String getGreeting() {
		// Good Morning Ryan! or Good Morning!
		String text = message;

		// If there is no name, return Good Morning!
		if (name.length() <= 1) {
			return message + "! ";
		}

		// If there is a name with space (first and last)
		if (name.contains(" ")) {
			// Message = Good Morning Ryan!
			return text + " " + name.substring(0, name.indexOf(" ")) + "! ";
		}
		// Default
		else {
			return text + " " + name + "! ";
		}
	}

	public String getMessage() {
		final StringBuilder text = new StringBuilder("");

		text.append(getLetterDay());

		// Add lab day info.
		if (scienceDay.isLabDay(letterDay)) {
			text.append("Today is a lab day for " + scienceDay.getScienceName()
					+ " ");
		}

		if (misc.isLabDay(letterDay)) {
			text.append("Misc: " + misc.getScienceName());
		}

		// If it's not Monday and we don't get it everyday
		if (!isMonday() && !everyDay) {
			return text.toString();
		}

		if (isMonday()) {
			text.append("Days of School Left: " + (180 - numSchoolDaysOver)
					+ ". ");
			text.append("Next Break: " + noSchool);
		}

		return text.toString();
	}

	public boolean getEveryday() {
		return this.everyDay;
	}

	public JSONObject getJSON() {
		return getJSON(this);
	}

	public static JSONObject getJSON(final Person thePerson) {
		final JSONObject theObj = new JSONObject();
		theObj.put("name", thePerson.getName());
		theObj.put("phone", thePerson.getPhoneNumber());
		theObj.put("carrier", thePerson.getCarrier());
		theObj.put("everyday", thePerson.getEveryday());
		theObj.put("science", thePerson.getScience().getJSON());
		theObj.put("misc", thePerson.getMisc().getJSON());
		return theObj;
	}

	public static Person getPerson(final JSONObject theObj) {
		final String name = theObj.getString("name");
		final String phone = theObj.getString("phone");
		final String carrier = theObj.getString("carrier");
		final boolean everyday = theObj.getBoolean("everyday");
		final Science sci = Science.getScience(theObj.getJSONObject("science"));
		final Science misc = Science.getScience(theObj.getJSONObject("misc"));
		return new Person(name, phone, carrier, sci, misc, everyday);
	}

	@Override
	public String toString() {
		return "Person String: " + name + ", phoneNumber=" + phoneNumber
				+ ", carrier=" + carrier + ", scienceClasses="
				+ scienceDay.toString() + ", misc=" + misc + ", everyDay="
				+ everyDay + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Person)) {
			return false;
		}

		final Person otherP = (Person) other;
		return this.phoneNumber.equals(otherP.getPhoneNumber());
	}

	@Override
	public int hashCode() {
		return (this.name + this.carrier).hashCode();
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public Science getScience() {
		return scienceDay;
	}

	public Science getMisc() {
		return misc;
	}

	public boolean isEveryday() {
		return everyDay;
	}
}
