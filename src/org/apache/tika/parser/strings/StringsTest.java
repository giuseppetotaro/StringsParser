package org.apache.tika.parser.strings;

import it.cnr.rm.iac.util.Timer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 * Simple class to test the StringsParser.
 * @author gtotaro
 *
 */
public class StringsTest {

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Usage: " + StringsParser.class.getName() + " /path/to/input /path/to/output");
			System.exit(1);
		}
		
		File input = new File(args[0]);
		File output = new File(args[1]);
		FileOutputStream outputStream = new FileOutputStream(output);
		
		// Checks if the MIME type matches with "application/octet-stream"
		Tika tika = new Tika();
		String mimetype = tika.detect(input);
		if (!"application/octet-stream".equals(new Tika().detect(input))) {
			System.out.println("This application runs over application/octet-stream mediatype only!");
			System.out.println("MIME type detected: " + mimetype);
			System.exit(0);
		}
		
		System.out.println("Input file : " + input.getPath());
		System.out.println("MIME type  : application/octet-stream");
		
		// Parsing
		Parser parser = new StringsParser();
		
		ContentHandler handler = new BodyContentHandler(outputStream);
		FileInputStream inputStream = new FileInputStream(input);
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		
		StringsConfig config = new StringsConfig();
		context.set(StringsConfig.class, config);

		Timer.start();
		
		try {
			parser.parse(inputStream, handler, metadata, context);
		} catch(Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			inputStream.close();
			outputStream.close();
		}
		
		Timer.end();
		
		// Report
		System.out.println("Parsing completed in " + Timer.getTimeInSeconds() + " seconds.");
		System.out.println("Output file: " + output.getName());

		Timer.reset();
	}
}
