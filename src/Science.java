import java.util.Arrays;
import org.json.JSONObject;
import org.json.JSONArray;

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
	
	public static String lettersToString(final char[] labDays) { 
		String result = "";
		
		for(char c : labDays) { 
			result = String.valueOf(c) + "|";
		}
		return result.substring(0, result.length()-1);
	}
	
	public static char[] stringToLetters(final String string) { 
		final String[] semi = string.split("|");
		final char[] result = new char[semi.length];
		for(int i = 0; i < semi.length; i++) { 
			result[i] = semi[i].charAt(0);
		}
		return result;
	}
	
	public static Science getScience(final JSONObject theObject) { 
		try { 
			return new Science(theObject.getString("name"), 
					stringToLetters(theObject.getString("letters")));
		}
		catch(Exception e) { 
			return null;
		}
	}
	
	public JSONObject getJSON() { 
		return getJSON(this);
	}
	
	public static JSONObject getJSON(final Science theScience) { 
		try { 
			JSONObject theObj = new JSONObject();
			theObj.put("name", theScience.getScienceName());
			theObj.put("letters", lettersToString(theScience.getLabDays()));
			return theObj;
		}
		catch(Exception e) { 
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "Science [scienceName=" + scienceName + ", labDays="
				+ Arrays.toString(labDays) + "]";
	}
}