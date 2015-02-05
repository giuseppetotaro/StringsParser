package org.apache.tika.parser.strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.external.ExternalParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Parser that uses the "strings" (or strings-alternative) command to find the
 * printable strings in a object, or other binary, file
 * (application/octet-stream). Useful as "best-effort" parser for files detected
 * as application/octet-stream.
 * 
 * @author gtotaro
 *
 */
public class StringsParser extends AbstractParser {
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 802566634661575025L;

	private static final Set<MediaType> SUPPORTED_TYPES = Collections
			.singleton(MediaType.OCTET_STREAM);

	private static final StringsConfig DEFAULT_CONFIG = new StringsConfig();

	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	@Override
	public void parse(InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context) throws IOException,
			SAXException, TikaException {
		StringsConfig config = context.get(StringsConfig.class, DEFAULT_CONFIG);

		if (!hasStrings(config)) {
			return;
		}

		TikaInputStream tis = TikaInputStream.get(stream);
		File input = tis.getFile();

		// Metadata
		metadata.set(Metadata.CONTENT_TYPE, "application/octet-stream");
		metadata.set("strings:command", config.getStringsCommand());
		metadata.set("strings:options", config.getOptions());
		metadata.set("strings:file_output", doFile(input));

		int totalBytes = 0;

		// Content
		XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);

		xhtml.startDocument();

		totalBytes = doStrings(input, config, xhtml);

		xhtml.endDocument();

		// Metadata
		metadata.set("strings:length", "" + totalBytes);
	}

	/**
	 * Checks if the "strings" command is supported.
	 * 
	 * @param config
	 *            {@see StringsConfig} object used for testing the strings
	 *            command.
	 * @return Returns returns {@code true} if the strings command is supported.
	 */
	private boolean hasStrings(StringsConfig config) {
		String stringsProg = config.getStringsPath()
				+ config.getStringsCommand();
		String[] checkCmd = { stringsProg, "--version" };

		boolean hasStrings = ExternalParser.check(checkCmd);

		return hasStrings;
	}

	/**
	 * Runs the "strings" command on the given file.
	 * 
	 * @param input
	 *            {@see File} object that represents the file to parse.
	 * @param config
	 *            {@see StringsConfig} object including the strings
	 *            configuration.
	 * @param xhtml
	 *            {@see XHTMLContentHandler} object.
	 * @return the total number of bytes read using the strings command.
	 * @throws IOException
	 *             if any I/O error occurs.
	 * @throws TikaException
	 *             if the parsing process has been interrupted.
	 * @throws SAXException
	 */
	private int doStrings(File input, StringsConfig config,
			XHTMLContentHandler xhtml) throws IOException, TikaException,
			SAXException {
		String[] cmd = { config.getStringsPath() + config.getStringsCommand(),
				input.getPath() };

		ProcessBuilder pb = new ProcessBuilder(cmd);
		final Process process = pb.start();

		InputStream out = process.getInputStream();

		FutureTask<Integer> waitTask = new FutureTask<Integer>(
				new Callable<Integer>() {
					public Integer call() throws Exception {
						return process.waitFor();
					}
				});

		Thread waitThread = new Thread(waitTask);
		waitThread.start();

		// Reads content printed out by "strings" command
		int totalBytes = 0;
		totalBytes = extractOutput(out, xhtml);

		try {
			waitTask.get(config.getTimeout(), TimeUnit.SECONDS);

		} catch (InterruptedException ie) {
			waitThread.interrupt();
			process.destroy();
			Thread.currentThread().interrupt();
			throw new TikaException(StringsParser.class.getName()
					+ " interrupted", ie);

		} catch (ExecutionException ee) {
			// should not be thrown

		} catch (TimeoutException te) {
			waitThread.interrupt();
			process.destroy();
			throw new TikaException(StringsParser.class.getName() + " timeout",
					te);
		}

		return totalBytes;
	}

	/**
	 * Extracts ASCII strings using the "strings" command.
	 * 
	 * @param stream
	 *            {@see InputStream} object used for reading the binary file.
	 * @param xhtml
	 *            {@see XHTMLContentHandler} object.
	 * @return the total number of bytes read using the "strings" command.
	 * @throws SAXException
	 *             if the content element could not be written.
	 * @throws IOException
	 *             if any I/O error occurs.
	 */
	private int extractOutput(InputStream stream, XHTMLContentHandler xhtml)
			throws SAXException, IOException {

		char[] buffer = new char[1024];
		BufferedReader reader = null;
		int totalBytes = 0;

		try {
			reader = new BufferedReader(new InputStreamReader(stream));

			int n = 0;
			while ((n = reader.read(buffer)) != -1) {
				if (n > 0) {
					xhtml.characters(buffer, 0, n);
				}
				totalBytes += n;
			}

		} finally {
			reader.close();
		}

		return totalBytes;
	}

	/**
	 * Runs the "file" command on the given file that aims at providing an
	 * alternative way to determine the file type.
	 * 
	 * @param input
	 *            {@see File} object that represents the file to detect.
	 * @return the file type provided by the "file" command using the "-b"
	 *         option (it stands for "brief mode").
	 * @throws IOException if any I/O error occurs.
	 */
	private String doFile(File input) throws IOException {
		String[] cmd = { "file", "-b", input.getPath() };

		ProcessBuilder pb = new ProcessBuilder(cmd);
		final Process process = pb.start();

		InputStream out = process.getInputStream();

		BufferedReader reader = null;
		String fileOutput = null;

		try {
			reader = new BufferedReader(new InputStreamReader(out));
			fileOutput = reader.readLine();

		} catch (IOException ioe) {
			// TODO
			System.err
					.println("An error occurred in reading output of the file command: "
							+ ioe.getMessage());
		} finally {
			reader.close();
		}

		return fileOutput;
	}
}
