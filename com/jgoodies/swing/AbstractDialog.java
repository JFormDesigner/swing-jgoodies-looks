package com.jgoodies.swing;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.layout.Grid;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.model.Trigger;
import com.jgoodies.swing.util.MySwingUtilities;

/**
 * An abstract superclass for most dialogs in the JGoodies. 
 * It provides frequently used actions, buttons, buttons bars, 
 * and access to the apply and cancel <code>Triggers</code>.<p>
 * 
 * TODO: Internationalize the button labels.
 * 
 * @see	Trigger
 *
 * @author Karsten Lentzsch
 */

public abstract class AbstractDialog extends JDialog {

    protected static final String OK_LABEL     = "OK";
    protected static final String CLOSE_LABEL  = "Close";
    protected static final String CANCEL_LABEL = "Cancel";
    protected static final String APPLY_LABEL  = "Apply";

    private final Trigger applyTrigger  = new Trigger();
    private final Trigger cancelTrigger = new Trigger();

    protected JButton okButton;

    /* Indicates whether the dialog has been packed successfully.
     * The dialog will be resizable if and only if there's a pack problem. */
    protected boolean packedSuccessfully;

    // Instance Creation ****************************************************

    /**
     * Constructs a modal <code>AbstractDialog</code> 
     * with the given <code>Frame</code> as its owner,
     * using the application's default window title.
     */
    public AbstractDialog(Frame owner) {
        this(owner, Workbench.getGlobals().getWindowTitle());
    }

    /**
     * Constructs a modal <code>AbstractDialog</code> 
     * with the given <code>Frame</code> as its owner,
     * using the given window title.
     */
    public AbstractDialog(Frame owner, String title) {
        this(owner, title, true);
    }

    /**
     * Constructs an <code>AbstractDialog</code> 
     * with the given <code>Frame</code> as its owner,
     * using the given window title, and the specified modal state.
     */
    public AbstractDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        configureWindowClosing();
    }

    /**
     * Constructs a modal <code>AbstractDialog</code> 
     * with the given <code>Dialog</code> as its owner,
     * using the application's default window title.
     */
    public AbstractDialog(Dialog owner) {
        this(owner, Workbench.getGlobals().getWindowTitle());
    }

    /**
     * Constructs a modal <code>AbstractDialog</code> 
     * with the given <code>Dialog</code> as its owner,
     * using the given window title.
     */
    public AbstractDialog(Dialog owner, String title) {
        this(owner, title, true);
    }

    /**
     * Constructs an <code>AbstractDialog</code> 
     * with the given <code>Dialog</code> as its owner,
     * using the given window title, and the specified modal state.
     */
    public AbstractDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        configureWindowClosing();
    }

    /**
     * Configures the closing behavior: invoke #doCloseWindow
     * instead of just closing the dialog.
     */
    protected void configureWindowClosing() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                doCloseWindow();
            }
        });
    }

    // Building *************************************************************

    /**
     * Builds the dialog's content pane, packs it, sets the resizable property,
     * and locates it on the screen. It then is ready to be opened.<p>
     * 
     * Subclasses should rarely override this method.
     */
    protected void build() {
        JComponent content = buildContentPane();
        resizeHook(content);
        setContentPane(content);
        // TODO: Check back in in 1.4.2, 1.5 if #pack still fails on Linux
        // pack(); 
        packedSuccessfully = MySwingUtilities.packWithWorkaround(this);
        setResizable();
        locateOnScreen();
    }

    /**
     * Builds and answers the content pane, sets the border, and puts 
     * an optional header component in the dialog's north.<p>
     * 
     * Subclasses should rarely override this method.
     */
    protected JComponent buildContentPane() {
        JComponent center = buildContent();
        center.setBorder(getDialogBorder());

        JComponent header = buildHeader();
        if (header == null)
            return center;

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds and answers the optional header component, which will be put in
     * the dialog's north by the default #buildContentPane implementation.<p>
     * 
     * A typical implementation will answer an instance of
     * <code>HeaderPane</code>, see the following code example:<p>
     * <pre>
     * protected JComponent buildHeader() {
     *     return new HeaderPanel(
     *         "My Dialog",
     *   	   "You can do click on the tabs below to ...",
     *		   ResourceManager.getIcon(MyResourceIDs.MY_DIALOG_ICON)
     *		);
     * }
     * </pre>
     */
    protected JComponent buildHeader() {
        return null;
    }

    /**
     * Resizes the specified component. This is called during the #build
     * process and enables subclasses to achieve a better aspect ratio,
     * by applying a resizer, e.g. the <code>Resizer</code>.
     */
    protected void resizeHook(JComponent component) {}

    /**
     * Answers the border that will be put around the content, which
     * has been created by the #createContent process.<p>
     * 
     * Subclasses will typically use <code>Grid.DIALOG_BORDER</code> or 
     * <code>Grid.CARD_DIALOG_BORDER</code>, which is more narrow than the
     * default.
     */
    protected Border getDialogBorder() {
        return Grid.DIALOG_BORDER;
    }

    /**
     * Sets the resizable property to true, if and only if the #pack process
     * has been successful.<p>
     * 
     * The behavior should be <code>setResizable(false)</code> 
     * but due to a bug with java.awt.Window.pack() on some platforms,
     * we take check whether #pack was successful.
     */
    protected void setResizable() {
        setResizable(!packedSuccessfully);
    }

    /**
     * Locates the dialog on the screen. The default implementation 
     * sets the location relative to the parent.<p>
     * 
     * Subclasses may choose to center the dialog, or put it in a screen
     * corner.
     */
    protected void locateOnScreen() {
        setLocationRelativeTo(getParent());
    }

    /**
     * Builds the dialog content and makes it visible.
     */
    public void open() {
        build();
        setVisible(true);
    }

    /**
     * Closes the dialog: makes it invisible and disposes it, 
     * which in turn releases all required OS resources.
     */
    public void close() {
        dispose();
    }

    // Abstract Behavior ****************************************************

    /**
     * Builds and answers the dialog's main content (with header and border).
     * Subclass must override this method.
     */
    abstract protected JComponent buildContent();

    // Common Buttons *******************************************************

    /**
     * Creates and answers an accept button for the given label and default
     * property.
     */
    protected JButton createAcceptButton(String label, boolean isDefault) {
        JButton button = new JButton(label);
        button.addActionListener(new DispatchingActionListener(ACCEPT));
        if (isDefault)
            setDefaultButton(button);
        return button;
    }

    /**
     * Creates and answers an apply button.
     */
    protected JButton createApplyButton() {
        JButton button = new JButton(APPLY_LABEL);
        button.addActionListener(new DispatchingActionListener(APPLY));
        return button;
    }

    /**
     * Creates and answers a cancel button.
     */
    protected JButton createCancelButton() {
        JButton button = new JButton(CANCEL_LABEL); // TODO: I18n.
        button.addActionListener(new DispatchingActionListener(CANCEL));
        return button;
    }

    /**
     * Creates and answers a close button.
     */
    protected JButton createCloseButton(boolean isDefault) {
        JButton button = new JButton(CLOSE_LABEL); // TODO: I18n.
        button.addActionListener(new DispatchingActionListener(CLOSE));
        if (isDefault)
            setDefaultButton(button);
        return button;
    }

    /**
     * Creates and answers an OK button.
     */
    protected JButton createOKButton(boolean isDefault) {
        okButton = createAcceptButton(OK_LABEL, isDefault); // TODO: I18n.
        return okButton;
    }

    /**
     * Sets a button as default button.
     */
    protected void setDefaultButton(JButton button) {
        getRootPane().setDefaultButton(button);
    }

    private static final String ACCEPT = "accept";
    private static final String APPLY  = "apply";
    private static final String CANCEL = "cancel";
    private static final String CLOSE  = "close";

    // A private helper class, that provides all action behavior required
    // for the default buttons created above.
    private class DispatchingActionListener implements ActionListener {

        private final String name;

        private DispatchingActionListener(String name) {
            this.name = name;
        }

        public void actionPerformed(ActionEvent e) {
            if (name.equals(ACCEPT)) {
                doAccept();
            } else if (name.equals(APPLY)) {
                doApply();
            } else if (name.equals(CANCEL)) {
                doCancel();
            } else if (name.equals(CLOSE)) {
                close();
            } else
                throw new IllegalStateException("Unknown action name" + name);
        }

        public String toString() {
            return super.toString() + ": " + name;
        }
    }

    // Common Button Bars ***************************************************

    /**
     * Builds and answers a button bar with a close button.
     */
    protected JComponent buildButtonBarWithClose() {
        JPanel bar = ButtonBarFactory.buildCloseBar(createCloseButton(true));
        bar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        return bar;
    }

    /**
     * Builds and answers a button bar with an OK and a Cancel button.
     */
    protected JComponent buildButtonBarWithOKCancel() {
        JPanel bar = ButtonBarFactory.buildOKCancelBar(
            createOKButton(true),
            createCancelButton());
        bar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        return bar;
    }

    // Common Behavior ******************************************************

    /**
     * Performs the apply action: fires the apply trigger.
     */
    public void doApply() {
        applyTrigger.setValue(Boolean.TRUE);
    }

    /**
     * Performs the accept action: performs apply, then closes the dialog.
     */
    public void doAccept() {
        doApply();
        close();
    }

    /**
     * Performs the cancel action: fires the cancel trigger, 
     * then closes the dialog.
     */
    public void doCancel() {
        cancelTrigger.setValue(Boolean.TRUE);
        close();
    }

    /**
     * Performs the close window action: performs cancel.
     */
    protected void doCloseWindow() {
        doCancel();
    }

    // Misc *****************************************************************

    /**
     * Answers the apply trigger.
     */
    protected Trigger applyTrigger() {
        return applyTrigger;
    }

    /**
     * Answers the cancel trigger.
     */
    protected Trigger cancelTrigger() {
        return cancelTrigger;
    }

    /**
     * Checks and answers whether the cancel trigger has been triggered.
     */
    public boolean hasBeenCanceled() {
        return cancelTrigger.booleanValue();
    }

    /**
     * Sets a default border for dialog components.
     * 
     * @deprecated Replaced by #setDefaultDialogBorder in 
     * class com.jgoodies.forms.builder.PanelBuilder.
     */
    protected JComponent setTabBorder(JComponent component) {
        component.setBorder(Borders.DIALOG_BORDER);
        return component;
    }
}