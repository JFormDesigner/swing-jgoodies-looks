package com.jgoodies.swing.fileaccess;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.util.CompoundIcon;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.lazy.Preparable;

/**
 * An extended version of the Swing <code>FileSystemView</code>. 
 * It provides much of the behavior of the 1.4 version 
 * <code>FileSystemView</code> implementation - even in 1.3 environments.
 * <p>
 * It is implemented as a look&amp;feel dependent singleton.
 * Everytime the look&amp;feel changes it reinitialzes itself.
 * 
 * @see	FileSystemView
 *
 * @author Karsten Lentzsch
 */

public final class ExtFileSystemView {

    private static final boolean CHECK_LINKS = !Utilities.IS_WINDOWS;
    private static final String   ABSENT_ICON_PATH =
        "com/jgoodies/common/images/Remove.gif";

    private static ExtFileSystemView instance;
    private static Method            isFileSystemMethod;
//	private  static Method            isTraversableMethod;

    private Icon            absentFolderIcon;
    private Icon            absentLeafIcon;
    private Icon            floppyDriveIcon;
    private Icon            hardDriveIcon;
    private Icon            homeFolderIcon;
    private Icon            leafIcon;
    private File            homeFolder;
    private JFileChooser    fileChooser;
    private FileSystemView  fileSystemView;

    /**
     * Constructs an extended file system view.
     */
    private ExtFileSystemView() {
        homeFolder = new File(System.getProperty("user.home"));
        fileChooser = new JFileChooser();
        fileSystemView = fileChooser.getFileSystemView();
        installCachedIcons();
        registerLookChangeListener();
    }

    /**
     * Returns the sole instance.
     */
    public static ExtFileSystemView getInstance() {
        if (instance == null) {
            instance = new ExtFileSystemView();
        }
        return instance;
    }

    /**
     * Registers a change listener, that listens to look changes
     * and then resets the instance, so that a new request will
     * construct a new instance and automatically refresh all
     * look-related properties.
     */
    public static void registerLookChangeListener() {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == "lookAndFeel")
                    instance = null;
            }
        });
    }

    /**
     * Returns the current directory.
     */
    public File getCurrentDirectory() {
        return fileChooser.getCurrentDirectory();
    }

    /**
     * Returns the directory name for the specified directory.
     */
    public String getDirectoryName(File directory) {
        return Utilities.IS_BEFORE_14
            ? getDirectoryName0(directory)
            : getSystemDisplayName(directory);
    }

    /**
     * Returns the directory name for the specified directory.
     */
    private String getDirectoryName0(File directory) {
        if (directory.equals(homeFolder))
            return "Home Directory";

        String name = directory.getName();
        return name.length() == 0 ? directory.getPath() : name;
    }

    /**
     * Answers an array of <code>File</code>s that are childs of 
     * the specified <code>File</code>. A child is either contained 
     * in the file system, or a <code>ShellFolder</code> child.
     */
    public File[] getFiles(File f, boolean useFileHiding) {
        return fileSystemView.getFiles(f, useFileHiding);
    }

    /**
     * Checks and answers whether the file is a link.
     * If the operating system may support links, 
     * compare the absolute path with the canonical path.
     */
    public static boolean isLink(File file) {
        return isLink(file, getCanonicalPath(file));
    }

    /**
     * Checks and answers whether the file is a link.
     * If the operating system may support links, 
     * compare the absolute path with the canonical path.
     */
    public static boolean isLink(File file, String itsCanonicalPath) {
        return CHECK_LINKS && !file.getAbsolutePath().equals(itsCanonicalPath);
    }

    /**
     * Guesses and answers the length of a link; 
     * uses the length of the file's canonical path.
     */
    public static int linkLength(File file) {
        return getCanonicalPath(file).length();
    }

    /**
     * Computes and answers the canonical path for the given file.
     */
    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * Looks up and answers an icon for the given file and selection property.
     */
    public Icon getIcon(File file, boolean isSelected) {
        if (Utilities.IS_BEFORE_14)
            return getIconBefore14(file, isSelected);
        else if (!file.exists())
            return absentFolderIcon;
        else {
            try {
                return fileChooser.getIcon(file);
            } catch (Exception e) {
                /*
                System.out.println("Can't get system icon for: " + file);
                System.out.println("isTraversable=" + isTraversable(file));
                System.out.println("isFileSystem="  + isFileSystem(file));
                System.out.println("isRoot="  + fileSystemView.isRoot(file));
                */
                return getIconBefore14(file, isSelected);
            }
        }
    }

    /**
     * Looks up and answers an icon for the given file and selection property
     * if we are in 1.3 environments.
     */
    private Icon getIconBefore14(File file, boolean isSelected) {
        if (Utilities.IS_WINDOWS && isWinFloppyDrive(file))
            return floppyDriveIcon;
        else if (fileSystemView.isRoot(file))
            return hardDriveIcon;
        else if (file.equals(homeFolder))
            return homeFolderIcon;
        else if (!file.exists())
            return absentFolderIcon;
        else if (!file.isDirectory())
            return leafIcon;
        else
            return UIManager.getIcon(
                isSelected ? "Tree.openIcon" : "Tree.closedIcon");
    }

    /**
     * Fetches and answers a leaf icon for the given file.<p>
     * 
     * If we know that the file is a leaf, we can guess
     * a better icon, in case the file is absent.
     */
    public Icon getLeafIcon(File file) {
        // Workaround for problem with "pagefile.sys" and "hyberfil.sys"
        File parent = file.getParentFile();
        if (parent != null) {
            boolean isRootChild = null == parent.getParentFile();
            String name = file.getName();
            if (isRootChild
                && (("pagefile.sys".equals(name))
                    || ("hiberfil.sys".equals(name))))
                return leafIcon;
        }

        return !file.exists() ? absentLeafIcon : getIcon(file, false);
    }

    /**
     * Looks up and answers an array of filesystem roots.
     */
    public File[] getRoots() {
        File[] roots = fileSystemView.getRoots();
        if (Utilities.IS_WINDOWS)
            return roots;

        File[] nodes = new File[roots.length + 1];
        System.arraycopy(roots, 0, nodes, 1, roots.length);
        nodes[0] = getCurrentDirectory();
        return nodes;
    }

    /**
     * Answers the system display name for the given directory.
     *[Pending:] Obsolete in 1.4
     */
    private String getSystemDisplayName(File directory) {
        try {
            Method method =
                FileSystemView.class.getMethod(
                    "getSystemDisplayName",
                    new Class[] { File.class });
            return (String)
                (method.invoke(fileSystemView, new File[] { directory }));
        } catch (NoSuchMethodException e) {} catch (InvocationTargetException e) {} catch (IllegalAccessException e) {}
        return getDirectoryName0(directory);
    }

    /**
     * Installs a set of icons that we cache to improve the performance.
     */
    private void installCachedIcons() {
        leafIcon = UIManager.getIcon("Tree.leafIcon");
        Icon folderIcon = UIManager.getIcon("Tree.closedIcon");
        Icon absentIcon = ResourceManager.readImageIcon(ABSENT_ICON_PATH);
        absentFolderIcon = new CompoundIcon(folderIcon, absentIcon);
        absentLeafIcon = new CompoundIcon(leafIcon, absentIcon);

        hardDriveIcon = UIManager.getIcon("FileView.hardDriveIcon");
        if (null == hardDriveIcon)
            hardDriveIcon = UIManager.getIcon("Tree.closedIcon");

        floppyDriveIcon = UIManager.getIcon("FileView.floppyDriveIcon");
        if (null == floppyDriveIcon)
            floppyDriveIcon = hardDriveIcon;

        homeFolderIcon = UIManager.getIcon("FileChooser.homeFolderIcon");
        if (null == homeFolderIcon)
            homeFolderIcon = UIManager.getIcon("Tree.closedIcon");
    }

    /**
     * Answers if the given file is backed up in the file system.
     * Otherwise it is a virtual file or directory, for example,
     * a shell folder like <i>Shared Documents</i> on Windows.
     */
    public boolean isFileSystem(File f) {
        if (Utilities.IS_BEFORE_14)
            return true;

        // [Pending:] Invoke #isFileSystem directly in 1.4
        if (null == isFileSystemMethod) {
            try {
                isFileSystemMethod =
                    FileSystemView.class.getMethod(
                        "isFileSystem",
                        new Class[] { File.class });
            } catch (NoSuchMethodException e) {}
        }

        if (null == isFileSystemMethod)
            return true;
        try {
            Boolean b =
                (Boolean) isFileSystemMethod.invoke(
                    fileSystemView,
                    new File[] { f });
            return b.booleanValue();
        } catch (InvocationTargetException e) {} catch (IllegalAccessException e) {}
        return true;
    }

    /**
     * Answers if the given directory is a floppy drive on Windows.
     */
    private static boolean isWinFloppyDrive(File dir) {
        String path = dir.getAbsolutePath();
        return (path != null && (path.equals("A:\\") || path.equals("B:\\")));
    }

    /**
     * Answers if the given file is traversable.
     */
    /*
    private boolean isTraversable(File f) {
    	if (Utilities.IS_BEFORE_14)
    		return true;
    	
    	// [Pending:] Invoke #isTraversable directly in 1.4
    	if (null == isTraversableMethod) {
    		try {
    			isTraversableMethod = FileSystemView.class.getMethod("isTraversable", new Class[] { File.class });
    		} catch (NoSuchMethodException e) {
    		}
    	}
    
    	if (null == isTraversableMethod)
    		return true;
    	try {
    		Boolean b =	(Boolean) isTraversableMethod.invoke(fileSystemView, new File[] { f });
    		return b.booleanValue();
    	} catch (InvocationTargetException e) {
    	} catch (IllegalAccessException e) {}
    	return true;
    }
    */

    /**
     * Answers if the given directory is a disk drive on Windows.
     */
    /*
    private static boolean isWinDrive(File dir) {
        String path = dir.getAbsolutePath();
        return (path != null) 
             && (path.length() == 3)
             && (path.substring(1, 2).equals(":\\"));
    }
    */

    /**
     * An implementation of the <code>Preparable</code> interface 
     * that instantiates and hence prepares the<code>ExtFileSystemView</code>.
     */
    public static class EagerInitializer implements Preparable {

        /**
         * Instantiates and hence prepares the <code>ExtFileSystemView</code>.
         */
        public void prepare() {
            Logger.getLogger("ExtFileSystemView").info(
                "Preparing the extended FileSytemView...");
            getInstance();
        }
    }

}