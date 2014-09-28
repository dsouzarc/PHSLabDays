import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Person {
	private final String name, phoneNumber, carrier;
	private final Science[] scienceClasses;
	private final Science misc;
	private final boolean everyDay; //Message everyday or just on lab days
	
	public static final Calendar todayCalendar = new GregorianCalendar();
	
	public static char letterDay = 'A';
	public static String message = "Good Morning"; //Can also be 'hi!'
	public static int numSchoolDaysOver = 16;
	public static String noSchool = "Fri, Oct 3rd, No School";
	
	public Person(final String name, final String phoneNumber, final String carrier, 
			final Science[] scienceDays, final Science misc, final boolean everyDay) { 
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.carrier = carrier;
		this.scienceClasses = scienceDays;
		this.misc = misc;
		this.everyDay = everyDay;
	}
	
	public String getMessage() { 
		final StringBuilder text = new StringBuilder(message);
		
		//Good Morning Ryan! or Good Morning!
		text.append(name.length() > 1 ? " " + name + "! ": "! ");
		
		if(letterDay == 'A') { 
			text.append("Today is an '" + letterDay + "' day. ");
		}
		else { 
			text.append("Today is a '" + letterDay + "' day. ");
		}

		//Add lab day info.
		for(Science science : scienceClasses) { 
			if(science != null && science.isLabDay(letterDay)) { 
				text.append("Today is a lab day for " + science.getScienceName() + " ");
			}
		}
		if(misc.isLabDay(letterDay)) { 
			text.append("Misc: " + misc.getScienceName());
		}
		
		//If it's not Monday and we don't get it everyday
		if(todayCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY && !everyDay) { 
			return text.toString();
		}
		
		if(todayCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) { 
			text.append("Days of School Left: " + (180 - numSchoolDaysOver) + ". ");
			text.append("Next Break: " + noSchool);
		}
		
		return text.toString();
	}
	
	@Override
	public String toString() {
		return "Person [name=" + name + ", phoneNumber=" + phoneNumber
				+ ", carrier=" + carrier + ", scienceClasses="
				+ Arrays.toString(scienceClasses) + ", misc=" + misc
				+ ", everyDay=" + everyDay + "]";
	}

	@Override
	public boolean equals(Object other) { 
		if(!(other instanceof Person)) { 
			return false;
		}
		
		final Person otherP = (Person) other;
		return this.phoneNumber.equals(otherP.getPhoneNumber());
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

	public Science[] getScienceClasses() {
		return scienceClasses;
	}

	public Science getMisc() {
		return misc;
	}

	public boolean isEveryday() {
		return everyDay;
	}
}
