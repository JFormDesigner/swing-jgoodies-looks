package com.jgoodies.swing.help;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * Implements a subset of the Java Help API <code>HelpSet</code> class.
 * It provides an API that is very similiar to Java Help.
 *
 * @author Karsten Lentzsch
 */

public final class HelpSet {

    private static Logger logger = Logger.getLogger("HelpSet");

    private final URL baseURL;
    private Map idsToURL;
    private String title = null;
    private URL home = null;
    private DefaultMutableTreeNode root = null;

    /**
     * Creates a <code>HelpSet</code> using the given 
     * <code>ClassLoader</code> and base <code>URL</code>.
     */
    public HelpSet(ClassLoader loader, URL baseURL) {
        this.baseURL = baseURL;
        readHelpSet(baseURL);
    }

    /**
     * Finds and answers a HelpSet URL for the given ClassLoader and path.
     */
    public static URL findHelpSet(ClassLoader loader, String helpSetPath) {
        return ResourceManager.getURL(helpSetPath, loader);
    }

    /**
     * Creates and answers a HelpBroker.
     */
    public HelpBroker createHelpBroker() throws HelpSetException {
        return new HelpBroker(this);
    }

    URL getBaseURL() {
        return baseURL;
    }
    
    URL getHome() {
        return home;
    }
    
    TreeNode getRoot() {
        return root;
    }
    
    String getTitle() {
        return title;
    }

    /**
     * Looks up and answers the help <code>URL</code> that is associated
     * with the specified help id.
     * 
     * @param helpID   the id of the help item to look for
     * @return the associated <code>URL</code>
     */
    URL idToUrl(String helpID) {
        URL url = (URL) idsToURL.get(helpID);
        if (null == url)
            logger.warning("HelpID " + helpID + " has no target URL.");
        return url;
    }


    // Private Behavior *****************************************************************	

    private DefaultMutableTreeNode createChapterNode(String name) {
        return new DefaultMutableTreeNode(HelpItem.createChapter(name));
    }

    private DefaultMutableTreeNode createTopicNode(
        String name,
        String helpID) {
        return new DefaultMutableTreeNode(
            HelpItem.createTopic(name, idToUrl(helpID)));
    }

    private URL getFullPath(String path) {
        try {
            return new URL(baseURL, path);
        } catch (java.net.MalformedURLException e) {
            logger.warning("Can't form URL" + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Reads the HelpSet's main file, the map, and the table of contents.
     */
    private void readHelpSet(URL helpSetURL) {
        ExtBufferedReader in = null;
        try {
            in =
                new ExtBufferedReader(
                    new InputStreamReader(helpSetURL.openStream()));

            // Lies Titel.
            in.skipLines(8);
            String titleLine = in.readLine().trim();
            StringTokenizer tokenizer = new StringTokenizer(titleLine, "<>");
            tokenizer.nextToken();
            title = tokenizer.nextToken();
            //System.out.println("title=" + title);

            // Lies HomeID.
            in.skipLines(3);
            String homeIDLine = in.readLine().trim();
            tokenizer = new StringTokenizer(homeIDLine, "<>");
            tokenizer.nextToken();
            String homeID = tokenizer.nextToken();
            //System.out.println("homeID=" + homeID);

            // Lies Map-Location.
            String mapLocationLine = in.readLine().trim();
            tokenizer = new StringTokenizer(mapLocationLine, "<>=\"");
            tokenizer.nextToken();
            String mapLocation = tokenizer.nextToken();
            //System.out.println("mapref location=" + mapLocation);

            // Read Map
            readMap(mapLocation);

            // Lies TOC-URL.
            in.skipLines(7);
            String tocLine = in.readLine().trim();
            tokenizer = new StringTokenizer(tocLine, "<>");
            tokenizer.nextToken();
            String tocData = tokenizer.nextToken();
            //System.out.println("toc data=" + tocData);

            // Read TOC
            readTOC(tocData);

            // Setze home	
            home = idToUrl(homeID);
        } catch (IOException e) {
            logger.log(
                Level.WARNING,
                "Can't read HelpSet from " + helpSetURL,
                e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {}
        }
    }

    /**
     * Reads the HelpSet's map, that maps ids to URLs.
     */
    private void readMap(String mapLocation) {
        idsToURL = new HashMap();

        URL mapURL = getFullPath(mapLocation);
        ExtBufferedReader in = null;
        try {
            in =
                new ExtBufferedReader(
                    new InputStreamReader(mapURL.openStream()));

            // Skip header
            in.skipLines(6);

            String line = in.readNonEmptyLine();
            while (line != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, "\"");
                tokenizer.nextToken();
                if (!tokenizer.hasMoreTokens())
                    break;
                String target = tokenizer.nextToken();
                tokenizer.nextToken();
                String url = tokenizer.nextToken();
                //System.out.println("target=" + target + " url=" + url);

                Object oldURL = idsToURL.put(target, getFullPath(url));
                if (oldURL != null)
                    logger.severe(
                        "Duplicate entry in HelpSet map "
                            + mapURL
                            + ": target="
                            + target
                            + " url="
                            + url);

                line = in.readNonEmptyLine();
            }
        } catch (IOException e) {
            logger.log(
                Level.WARNING,
                "Error while reading HelpSet map from " + mapURL,
                e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {}
        }
    }

    /**
     * Reads the HelpSet's table of contents.
     */
    private void readTOC(String tocName) {
        root = new DefaultMutableTreeNode(null);
        DefaultMutableTreeNode parent = null;

        URL tocURL = getFullPath(tocName);
        ExtBufferedReader in = null;
        try {
            in =
                new ExtBufferedReader(
                    new InputStreamReader(tocURL.openStream()));

            // Skip header
            in.skipLines(7);

            String line = in.readNonEmptyLine();
            while (line != null) {
                line = line.trim();

                if (line.equalsIgnoreCase("</tocitem>"))
                    parent = null;

                StringTokenizer tokenizer = new StringTokenizer(line, "\"");
                tokenizer.nextToken();
                if (tokenizer.hasMoreTokens()) {
                    String text = tokenizer.nextToken();
                    tokenizer.nextToken();
                    String image = tokenizer.nextToken();
                    tokenizer.nextToken();
                    String target =
                        tokenizer.hasMoreTokens()
                            ? tokenizer.nextToken()
                            : null;
                    //System.out.println("text=" + text + "; image=" + image + "; target=" + target);
                    if ("chapter".equals(image)) {
                        parent = createChapterNode(text);
                        root.add(parent);
                    } else {
                        (null == parent ? root : parent).add(
                            createTopicNode(text, target));
                    }
                }
                line = in.readNonEmptyLine();
            }

        } catch (IOException e) {
            logger.log(
                Level.WARNING,
                "Error while reading HelpSet TOC from " + tocURL,
                e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {}
        }
    }

    // A helper class that can skip empty and comment lines.
    private static class ExtBufferedReader extends BufferedReader {

        private ExtBufferedReader(Reader in) {
            super(in);
        }

        String readNonEmptyLine() throws IOException {
            String line;
            do {
                line = readLine();
            } while (
                (line != null)
                    && ((line.trim().length() == 0)
                        || (line.trim().charAt(0) == '#')));
            return line;
        }

        void skipLines(int linesToSkip) throws IOException {
            for (int i = 0; i < linesToSkip; i++)
                readLine();
        }
    }
}