package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.Main;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UpdateUtil {

	/**
	 * updates
	 * @throws RuntimeException if something is bad
	 */
	public static void update() {
		try {
			File gitRepo = new File("git_repo" + File.separator); // makes the git repo folder
			if (!gitRepo.isDirectory()) {// checks does it exist
				ProcessOutput out = runProcess(gitRepo.getParentFile(), "git", "clone", "https://github.com/WinteryFox/HoroBot.git"); // git clones it
				if (out.getExitCode() != 0) // checks for success
					throw new RuntimeException("git clone failed. output " + out.getOutput()); // reports error
			} else {
				ProcessOutput out = runProcess(gitRepo, "git", "pull"); // git pulls it
				if (out.getExitCode() != 0) // checks for success
					throw new RuntimeException("git pull failed. output " + out.getOutput()); // reports error
			}
			ProcessOutput out = runProcess(gitRepo, "mvn", "clean", "package"); // compiles it
			if (out.getExitCode() != 0) // checks for success
				throw new RuntimeException("mvn failed. output " + out.getOutput()); // reports error
			File jar = new File(URLDecoder.decode( // decodes the result
					Main.class.getProtectionDomain() // gets the protection domain
							.getCodeSource() // gets code source
							.getLocation() // gets its location
							.toURI() // converts it to URI
							.getPath(), "UTF-8")); // gets the path component and returns it back to decoder
			Files.copy(getAssembledFile(gitRepo), jar.toPath(), StandardCopyOption.REPLACE_EXISTING); // copies the files
		} catch (Exception e) {
			//throw new RuntimeException("Unexpected exception", e); // exception ""handling""
			e.printStackTrace();
		}
	}

	/**
	 * gets the assembled file
	 * @return the assembled file
	 */
	private static Path getAssembledFile(File buildDir) {
		File[] files = new File(buildDir.getAbsolutePath() + File.separator + "target").listFiles(); // list files
		if (files == null) throw new RuntimeException("Something broke"); // error reporting
		for (File f : files) // loops files
			if (f.getAbsolutePath().endsWith("-jar-with-dependencies.jar")) // attempted to locate the file
				return f.toPath(); // gets the file
		throw new RuntimeException("Didn't find assembled file"); // error reporting
	}

	/**
	 * converts process' output to string
	 * @param wd   working directory
	 * @param args command
	 * @return output
	 */
	private static ProcessOutput runProcess(File wd, String... args) throws IOException, InterruptedException {
		Process process = new ProcessBuilder() // creates a process builder
				.redirectErrorStream(true) // combine streams
				.directory(wd) // set working directory
				.command(args) // sets the command
				.start(); // starts
		return new ProcessOutput(process); // returns the process output;
	}

	/**
	 * Process result
	 */
	private static class ProcessOutput {
		private final String output;
		private final int exitCode;

		/**
		 * Processes the process
		 * @param process the process to process
		 */
		ProcessOutput(Process process) throws IOException, InterruptedException {
			output = IOUtils.toString(process.getInputStream(), Charset.defaultCharset()); // reads output
			process.waitFor(); // waits for the process
			exitCode = process.exitValue(); // gets the exit code
		}

		/**
		 * @return output
		 */
		public String getOutput() {
			return output; // gets the output
		}

		/**
		 * @return exit code
		 */
		public int getExitCode() {
			return exitCode; // gets the exit code
		}
	}
}
