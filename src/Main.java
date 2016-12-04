import java.io.File;

public class Main {
	public static void main(String[] args) {
		if (args == null || args.length == 0)
			return;
		String srcFilePath = args[0];
		String destFilePath = args[0];
		try {
			PoeFixer.fix(new File(srcFilePath), new File(destFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
