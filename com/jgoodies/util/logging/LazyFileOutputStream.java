package com.jgoodies.util.logging;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Unlike <code>FileOutputStream</code> this class deferres the file 
 * creation until output is written the first time.
 *
 * All output is delegated to an underlying <code>FileOutputStream</code>.
 * 
 * @author Karsten Lentzsch
 */

final class LazyFileOutputStream extends OutputStream {

	private final File     file;
	private final boolean writeHeader;

	private OutputStream    delegate = null;
	private boolean        canCreateDelegate = true;
	

	public LazyFileOutputStream(File file, boolean writeHeader) {
		this.file = file;
		this.writeHeader = writeHeader;
	}


	public void close() throws IOException {
		if (delegate != null)
			delegate.close();
	}


	private void createDelegate() {
		if (!file.exists()) file.getParentFile().mkdirs();
		
		try {
			delegate = new FileOutputStream(file);
			if (writeHeader)
				writeHeader();
		} catch (IOException e) {
			canCreateDelegate = false;
			Logger.getLogger("LazyFileOutputStream").warning("Can't create file " + file.getPath() + ".");
		}
	}


	private void ensureDelegateCreation() {
		if (canCreateDelegate && (null == delegate))
			createDelegate();
	}


	public void flush() throws IOException {
		if (delegate != null)
			delegate.flush();
	}


	public void write(byte b[]) throws IOException {
		ensureDelegateCreation();
		if (delegate != null)
			delegate.write(b);
	}


	public void write(byte b[], int off, int len) throws IOException {
		ensureDelegateCreation();
		if (delegate != null)
			delegate.write(b, off, len);
	}


	public void write(int b) throws java.io.IOException {
		ensureDelegateCreation();
		if (delegate != null)
			delegate.write(b);
	}


	private void writeHeader() {
		PrintStream stream = new PrintStream(this);
		String[] basicKeys =
			{
				"application.name",
				"application.fullversion",
				"java.vendor",
				"java.version",
				"os.name",
				"os.version",
				"os.arch" };
		for (int i = 0; i < basicKeys.length; i++) {
			String key = basicKeys[i];
			stream.println(key + '=' + System.getProperty(key));
		}
		stream.println();
	}
}