package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntityUtil {

	public String fmt(Date date) {
		if (date==null) return "NuLL!";
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		return sdf.format(date);
	}
	
}
