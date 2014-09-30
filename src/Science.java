import java.util.Arrays;
import java.util.LinkedList;

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
	
	public String lettersToString() { 
		String r = "";
		for(char c : labDays) { 
			r += c + "<";
		}
		return r;
	}
	
	public static char[] stringToLetters(final String string) { 
		final LinkedList<Character> theChars = new LinkedList<Character>();
		
		for(Character theChar : string.toCharArray()) { 
			final int charVal = (int) theChar;
			
			//Between 'A' and 'G'
			if(charVal >= 65 && charVal <= 71) { 
				theChars.add(theChar);
			}
		}
		
		final char[] answer = new char[theChars.size()];
		int counter = 0;
		for(Character c : theChars) { 
			answer[counter] = c;
			counter++;
		}
		return answer;
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
		try { 
			JSONObject theObj = new JSONObject();
			theObj.put("name", getScienceName());
			theObj.put("letters", lettersToString());
			return theObj;
		}
		catch(Exception e) { 
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "Science String: scienceName=" + scienceName + ", labDays="
				+ Arrays.toString(labDays) + " days";
	}
}