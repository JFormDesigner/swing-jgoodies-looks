/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.convenience;


import java.awt.Frame;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import com.jgoodies.swing.application.Workbench;
import com.jgoodies.util.logging.Handler;
import com.jgoodies.util.logging.Level;

/**
 * This class pops up message dialogs to log messages with
 * a level greater or equal to Level.WARNING. In addition, 
 * a <code>ConsoleHandler</code> is used to write
 * all messages to the console, too.
 *
 * @see	com.jgoodies.util.logging.ConsoleHandler
 * 
 * @author Karsten Lentzsch
 */

public final class MessageLogHandler implements Handler {

    private static final String OK_LABEL = "OK";
    private static final String FEEDBACK_LABEL = "Send Feedback...";
    private static final Object[] OPTIONS =
        new Object[] { OK_LABEL, FEEDBACK_LABEL };

    private final Handler outputHandler;
    private final String feedbackReceiver;

    public MessageLogHandler(Handler outputHandler, String feedbackReceiver) {
        this.outputHandler = outputHandler;
        this.feedbackReceiver = feedbackReceiver;
    }

    public void log(
        Level level,
        String catalogName,
        String msg,
        Throwable thrown) {
        outputHandler.log(level, catalogName, msg, thrown);

        // Don't pop up Windows for Level < WARNING.
        if (level.intValue() < Level.WARNING.intValue())
            return;

        if (null == thrown)
            JOptionPane.showMessageDialog(
                owner(),
                msg,
                getTitle(level),
                getMessageType(level));
        else {
            showOptionWithFeedbackDialog(level, msg, thrown);
        }
    }

    public void flush() {
    }

    private int getMessageType(Level level) {
        if (Level.SEVERE.equals(level))
            return JOptionPane.ERROR_MESSAGE;
        else if (Level.WARNING.equals(level))
            return JOptionPane.WARNING_MESSAGE;
        else
            return JOptionPane.INFORMATION_MESSAGE;
    }

    private String getSubject(Level level) {
        return "Execution " + getTitle(level);
    }

    private String getTitle(Level level) {
        if (Level.SEVERE.equals(level))
            return "Error";
        else if (Level.WARNING.equals(level))
            return "Warning";
        else
            return "Message";
    }

    private void sendFeedback(Level level, String msg, Throwable thrown) {
        StringWriter out = new StringWriter();
        out.write(msg);

        out.write("\n");
        writeSystemProperties(
            out,
            new String[] {
                "os.name",
                "os.version",
                "java.vm.vendor",
                "java.vm.version",
                "application.fullversion" });

        if (thrown != null) {
            out.write("\n\n");
            thrown.printStackTrace(new PrintWriter(out));
        }
        new SendFeedbackDialog(
            owner(),
            feedbackReceiver,
            getSubject(level),
            out.toString())
            .open();
    }

    private void writeSystemProperties(StringWriter out, String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = System.getProperty(key);
            if (value != null) {
                out.write("\n");
                out.write(key);
                out.write("=");
                out.write(value);
            }
        }
    }

    private void showOptionWithFeedbackDialog(
        Level level,
        String msg,
        Throwable thrown) {
        int messageType = getMessageType(level);
        String title = getTitle(level);
        String fullMessage = msg + "\n" + thrown.getLocalizedMessage();

        int choice =
            JOptionPane.showOptionDialog(
                owner(),
                fullMessage,
                title,
                -1,
                messageType,
                null,
                OPTIONS,
                OK_LABEL);
        if (choice == 1)
            sendFeedback(level, msg, thrown);
    }

    private Frame owner() {
        Frame frame = Workbench.getMainFrame();
        return frame == null ? new Frame() : frame;
    }
}