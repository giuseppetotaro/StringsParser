package org.apache.tika.parser.strings;

import java.io.File;
import java.io.Serializable;

/**
 * Configuration for the "strings" (or strings-alternative) command.
 * 
 * @author gtotaro
 *
 */
public class StringsConfig implements Serializable {
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -1465227101645003594L;

	private String stringsCmd = "strings";

	private String stringsPath = "";

	private int timeout = 120;

	/**
	 * Default constructor.
	 */
	public StringsConfig() {
		// TODO
	}

	/**
	 * Returns the "strings" installation folder.
	 * 
	 * @return the "strings" installation folder.
	 */
	public String getStringsPath() {
		return this.stringsPath;
	}

	/**
	 * Returns the "strings" command name.
	 * 
	 * @return the "strings" command name.
	 */
	public String getStringsCommand() {
		return this.stringsCmd;
	}

	/**
	 * Returns the command-line options for the "strings" command.
	 * 
	 * @return the command-line options for the "strings" command.
	 */
	public String getOptions() {
		// TODO
		return null;
	}

	/**
	 * Returns the maximum time (in seconds) to wait for the "strings" command
	 * to terminate.
	 * 
	 * @return the maximum time (in seconds) to wait for the "strings" command
	 *         to terminate.
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * Sets the "strings" installation folder.
	 * 
	 * @param path the "strings" installation folder.
	 */
	public void setStringsPath(String path) {
		char lastChar = path.charAt(path.length() - 1);

		if (lastChar != File.separatorChar) {
			path += File.separatorChar;
		}
		this.stringsPath = path;
	}

	/**
	 * Sets the "strings" (or strings-alternative) command name. It allows to
	 * use a strings-alternative command, if available.
	 * 
	 * @param command the "strings" (or strings-alternative) command name.
	 */
	public void setStringsCommand(String command) {
		this.stringsCmd = command;
	}

	/**
	 * Sets command-line options for the "strings" command.
	 * @param options the command-line options for the "strings" command.
	 */
	public void setOptions(String options) {
		// TODO
	}

	/**
	 * Sets the maximum time (in seconds) to wait for the "strings" command to terminate.
	 * @param timeout the maximum time (in seconds) to wait for the "strings" command to terminate.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}