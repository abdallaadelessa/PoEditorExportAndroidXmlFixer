import java.io.File;

public class Main {
	public static void main(String[] args) {
		if (args == null || args.length == 0)
			return;
		String srcFilePath = args[0];
		String destFilePath = args[0];
		try {
			PoeFixer fixer = new PoeFixer();
			// Add Resource 1
			//<string-array name="mcy_alphacomm_register_gender_list">
			//	<item>Herr</item>
			//	<item>Frau</item>
			//</string-array>
			fixer.addExtraResourceString(
					"<string-array name=\"mcy_alphacomm_register_gender_list\">\n" 
						+ "<item>Herr</item>\n"
						+ "<item>Frau</item>\n"
					+ "</string-array>");
			// Add Resource 2 
			//<string-array name="mcy_transfer_landing_dialog_choose_amount_items">
			//	<item>3 Euro Guthaben übertragen</item>
			//	<item>5 Euro Guthaben übertragen</item>
			//	<item>10 Euro Guthaben übertragen</item>
			//</string-array>
			fixer.addExtraResourceString(
					"<string-array name=\"mcy_transfer_landing_dialog_choose_amount_items\">"
						+ "<item>3 Euro Guthaben übertragen</item>\n" 
						+ "<item>5 Euro Guthaben übertragen</item>\n"
						+ "<item>10 Euro Guthaben übertragen</item>\n" 
					+ "</string-array>");
			// Start Fixing the xml
			fixer.fix(new File(srcFilePath), new File(destFilePath));
		} catch (Exception e) {
			logError(e);
		}
	}
	
	private static void logError(Exception e) {
		e.printStackTrace();
	}
}
