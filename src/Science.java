import java.util.Arrays;

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
		if(labDays == null) { 
			return false;
		}
		for(char theDay : labDays) { 
			if(theDay == today) { 
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Science [scienceName=" + scienceName + ", labDays="
				+ Arrays.toString(labDays) + "]";
	}
}