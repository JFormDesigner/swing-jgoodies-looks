package com.jgoodies.util.logging;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;

/** 
 * An implementation of the Handler interface that writes
 * log messages to a lazily created file.
 * 
 * @author Karsten Lentzsch
 */

public final class FileHandler extends ConsoleHandler /*implements Handler*/ {

	private final PrintStream outputStream;

	public FileHandler(String pattern) {
		File file = generate(pattern, 0, 0);
		outputStream =
			new PrintStream(new BufferedOutputStream(new LazyFileOutputStream(file, true)), true);
	}

	public void log(Level level, String catalogName, String msg, Throwable thrown) {
		super.log(level, catalogName, msg, thrown);
		
		outputStream.print(new Date(System.currentTimeMillis()));
		outputStream.print(' ');
		outputStream.println(catalogName);
		outputStream.print(level);
		outputStream.print(':');
		outputStream.println(msg);
		if (thrown != null) {
			thrown.printStackTrace(System.out);
		}
	}

	public void flush() {
		outputStream.flush();
	}
	
	
	// Generate a file from a pattern.
	private File generate(String pattern, int generation, int unique) {
		File file = null;
		String word = "";
		int ix = 0;
		boolean sawu = false;
		while (ix < pattern.length()) {
			char ch = pattern.charAt(ix);
			ix++;
			char ch2 = 0;
			if (ix < pattern.length()) {
				ch2 = Character.toLowerCase(pattern.charAt(ix));
			}
			if (ch == '/') {
				if (file == null) {
					file = new File(word);
				} else {
					file = new File(file, word);
				}
				word = "";
				continue;
			} else if (ch == '%') {
				if (ch2 == 't') {
					String tmpDir = System.getProperty("java.io.tmpdir");
					if (tmpDir == null) {
						tmpDir = System.getProperty("user.home");
					}
					file = new File(tmpDir);
					ix++;
					word = "";
					continue;
				} else if (ch2 == 'h') {
					file = new File(System.getProperty("user.home"));
					ix++;
					word = "";
					continue;
				} else if (ch2 == 'g') {
					word = word + generation;
					ix++;
					continue;
				} else if (ch2 == 'u') {
					word = word + unique;
					sawu = true;
					ix++;
					continue;
				} else if (ch2 == '%') {
					word = word + "%";
					ix++;
					continue;
				}
			}
			word = word + ch;
		}
		if (unique > 0 && !sawu) {
			word = word + "." + unique;
		}
		if (word.length() > 0) {
			if (file == null) {
				file = new File(word);
			} else {
				file = new File(file, word);
			}
		}
		return file;
	}

}