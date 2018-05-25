package com.tester.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtilities {

	/**
	 * Writes a text string to the given file.
	 *
	 * @param file
	 *            The file to be written to.
	 * @param content
	 *            The content to write.
	 * @throws FileNotFoundException
	 *             Thrown if the file doesn't exist, is null, a directory or is read
	 *             only.
	 * @throws IOException
	 *             IOException.
	 */
	static public void fileWrite(File file, String content) throws FileNotFoundException, IOException {
		if (file == null )//|| !file.isFile() || !file.canWrite())
			throw new IllegalArgumentException("Exception writing to file: " + file);
		else if (!file.exists())
			throw new FileNotFoundException("File does not exist: " + file);

		Writer output = new BufferedWriter(new FileWriter(file)); // use buffering
		output.write(content); // FileWriter always assumes default encoding is OK!
		output.close();
	}

	/**
	 * Writes a text string to the given file path.
	 *
	 * @param path
	 *            Path to the file.
	 * @param content
	 *            Content for writing.
	 * @throws IOException
	 *             IOException
	 */
	static public void fileWrite(Path path, String content) throws IOException {
		fileWrite(path.toFile(), content);
	}

	/**
	 * Writes a text string to the given file.
	 *
	 * @param path
	 *            A string representing the path to the file.
	 * @param content
	 *            The content to write.
	 * @throws IOException
	 *             IOException
	 */
	static public void fileWrite(String path, String content) throws IOException {
		fileWrite(Paths.get(path), content);
	}

	/**
	 * Reads text from a <b>File</b> object.
	 *
	 * @param file
	 *            A <b>File</b> object to read.
	 * @return A string of the <b>File's</b> contents.
	 * @throws IOException
	 */
	static public String readFile(File file) throws IOException {
		List<String> lines = new ArrayList<>(Files.readAllLines(file.toPath()));

		StringBuilder contents = new StringBuilder(lines.size());
		lines.forEach((line) -> {
			contents.append(line).append(System.getProperty("line.separator"));
		});
		return contents.toString();
	}

	/**
	 * Reads a file from the class loader.
	 *
	 * @param c
	 *            the class
	 * @param fileName
	 *            the filename
	 * @return the contents
	 */
	public static String getFileContents(Class<?> c, String fileName) {
		StringBuilder result = new StringBuilder("");

		// Get file from resources folder
		File file = getFile(c, fileName);
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
		}
		return result.toString();
	}

	public static File getFile(Class<?> clazz, String name) {
		return new File(clazz.getClassLoader().getResource(name).getFile());
	}

	public static String readInputStream(InputStream is) throws IOException {
		StringBuilder result = new StringBuilder("");
		Scanner s = new Scanner(is, "UTF-8");
		while (s.hasNext()) {
			result.append(s.nextLine());
			result.append("\n");
		}
		s.close();
		return result.toString();
	}

}
