public class Science { 
	private final String scienceName;
	private final char[] labDays;
	public Science(final String scienceName, char[] labDays) { 
		this.scienceName = scienceName;
		this.labDays = labDays;
	}
	public String getScienceName() {
		return scienceName;
	}
	public char[] getLabDays() {
		return labDays;
	}
	public boolean isLabDay(final char today) { 
		for(char theDay : labDays) { 
			if(theDay == today) { 
				return true;
			}
		}
		return false;
	}
}