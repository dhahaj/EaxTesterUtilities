package com.tester.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import processing.core.*;
import static processing.core.PApplet.print;
import static processing.core.PApplet.println;
import processing.javafx.*;
import static java.lang.System.out;

/**
 * Class specifically written to run the UP programming software.
 * 
 * @author dmh
 */
public final class ExecUpProgram {

	private static boolean RUNNING = false;
	private final static String WORKING_DIR = "C:\\tester";
	private final static String LOG_FILENAME = "execUp.log";
	private static String FIRMWARE_PATH;// = WORKING_DIR + "\\Resources\\firmware\\";
	private static Path FIRMWARE_FILE = null;
	private static String USER = null;
	private static File LOG_FILE = new File(WORKING_DIR + "\\" + LOG_FILENAME);
	private static File BATCHFILE = new File("program.bat");
	public static boolean DEBUG = true;

	public static void main(String[] args) {
		// File file = getResourceAsFile("program.bat");
		// Path p = file.toPath();
		// Path target = Paths.get("C:\\tester\\program.bat");
		// try {
		// Path n = Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
		// println(n);
		// println(FileUtilities.readFile(file));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return;

		// Check for the correct arguments
		if (args.length < 2) {
			println("\nError.\n\tUsage: java -jar execUp.jar [username] [path to firmware]\n");
			return;
		} else if (args.length > 2) {
			println("\nError: Too many arguments passed.\n\tUsage: java -jar execUp.jar [username] [path to firmware]\n");
			return;
		}

		USER = new String(args[0]); // Get the username from the arguments
		FIRMWARE_PATH = new String(args[1]); // Get the firmware path from the arguments
		FIRMWARE_FILE = Paths.get(FIRMWARE_PATH); // Create a File instance from the passed argument.

		if (!checkFirmwareFile(FIRMWARE_FILE)) {
			println("\nError with the passed firmware file.");
			return;
		}

		if (!(new File("C:\\tester").exists())) {
			println("Error: Missing required tester folder (C:\\tester\\).");
			return;
		}

		if (!BATCHFILE.exists()) {
			println("Batch file not found! Creating from resources.");
			createBatchFile();
		}

		RUNNING = true;
		int i = runProgrammer(BATCHFILE.toString(), FIRMWARE_FILE.toString());

		switch (i) {
		case -1:
			println("Internal Error.");
			break;
		case 1:
			println("File error. File not found or incorrect file format");
			break;
		case 2:
			println("Equipment error. Communication test failed, communication error.");
			break;
		case 3:
			println("Programming preparation error. device cannot be erased, etc.");
			break;
		case 4:
			println("Programming error.");
			break;
		case 5:
			println("Verification error.");
			break;
		case 6:
			println("Programming failed due to a need to communicate with user.");
			break;
		case 7:
			println("Device ID error.");
			break;
		case 8:
			println("Not supported.");
			break;
		case 9:
			println("Error of the serial number entered using the /sn parameter.");
			break;
		}
		RUNNING = false;
	}

	/**
	 * Static method for other java programs to access.
	 * 
	 * @param user
	 *            The username.
	 * @param firmware
	 *            Path for the firmware.
	 * @return Returns 0 for successful programming and non-zero for errors.
	 */
	public static int runDefault(String user, Path firmware) {
		USER = user;
		FIRMWARE_FILE = firmware;
		if (!BATCHFILE.exists()) {
			debug("Creating batch file.");
			createBatchFile();
			BATCHFILE = new File("program.bat");
			println(BATCHFILE.toString());
		}
		return runProgrammer(BATCHFILE.toString(), FIRMWARE_FILE.toString());
	}

	private static int runProgrammer(String command, String... arg) {

		// Get the firmware filename minus the extension.
		String name = FIRMWARE_FILE.toFile().getName().replaceAll(".hex", "");
		String today = new SimpleDateFormat("MM/dd").format(new Date());

		// Write the username and file to the serialization data
		StringBuffer sb = new StringBuffer(": data.0020 ");
		for (char c : USER.toCharArray()) // Append the username to the serial data
			sb.append("'" + c + "' ");
		sb.append("' ' ");
		for (char c : name.toCharArray()) // Append the firmware name to the serial data
			sb.append("'" + c + "' ");
		sb.append("' ' ");
		for (char c : today.toCharArray()) // Append the date to the serial data
			sb.append("'" + c + "' ");
		sb.append(';');
		debug("Serial=" + sb.toString());

		// Write the new data to the actual file
		try {
			File serialFile = new File("serial.sn");
			FileUtilities.fileWrite(serialFile, sb.toString());
		} catch (IOException e) {
			debug(e);
			return -1;
		}

		// Parse the arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add(command);
		if (arg.length > 0)
			for (String string : arg)
				arguments.add(string);

		// Instantiate the ProcessBuilder
		ProcessBuilder pb = new ProcessBuilder(arguments);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(LOG_FILE));
		try {
			Process p = pb.start();
			int eValue = -1;
			synchronized (p) {
				p.waitFor();
				eValue = p.exitValue();
				assert pb.redirectInput() == Redirect.PIPE;
				assert pb.redirectOutput().file() == LOG_FILE;
				assert p.getInputStream().read() == -1;
				return eValue;
			}
		} catch (IOException | InterruptedException e) {
			debug(e);
		}
		return 0;
	}

	static void printMap(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			println("Key=" + entry.getKey() + ", value=" + entry.getValue());
		}
	}

	private static void debug(Object o) {
		if (DEBUG)
			println(o);
	}

	private static boolean checkFirmwareFile(Path path) {
		File file = path.toFile();
		if (file == null || file.isDirectory() || !file.exists()) {
			return false;
		}
		file.setReadOnly();
		return true;
	}

	public Path getFirmwareFile() {
		return FIRMWARE_FILE;
	}

	public void setFirmwareFile(Path firmwareFile) {
		FIRMWARE_FILE = firmwareFile;
	}

	public String getUser() {
		return USER;
	}

	public void setUser(String user) {
		USER = user;
	}

	public static File getResourceAsFile(String resourcePath) {
		try {
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
			if (in == null)
				return null;

			File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
			tempFile.deleteOnExit();

			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024]; // copy stream
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1)
					out.write(buffer, 0, bytesRead);
			}
			return tempFile;
		} catch (IOException e) {
			debug(e);
			return null;
		}
	}

	private static void createBatchFile() {
		File batchfile = getResourceAsFile("program.bat");
		Path p = batchfile.toPath();
		Path target = Paths.get("C:\\tester\\program.bat");
		try {
			Files.copy(p, target, StandardCopyOption.COPY_ATTRIBUTES);
		} catch (IOException e) {
			debug(e);
		}
		return;
	}
}
