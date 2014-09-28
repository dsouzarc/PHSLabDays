public class Person {
	private final String name, phoneNumber, carrier;
	private final Science[] scienceClasses;
	private final Science misc;
	private final boolean everyDay; //Message everyday or just on lab days
	
	public String message = "Good Morning"; //Can also be 'hi!'

	public Person(final String name, final String phoneNumber, final String carrier, 
			final Science[] scienceDays, final Science misc, final boolean everyDay) { 
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.carrier = carrier;
		this.scienceClasses = scienceDays;
		this.misc = misc;
		this.everyDay = everyDay;
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
	
	public String getMessage() { 
		final StringBuilder text = new StringBuilder(message);
		
		//Good Morning Ryan! or Good Morning!
		text.append(name.length() > 1 ? " " + name + "!": "!");
		
		
		return "";
	}
	
}
