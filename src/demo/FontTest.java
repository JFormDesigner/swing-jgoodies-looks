

import java.awt.*;
import java.util.Locale;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.looks.FontSet;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;


/**
 * An application that displays the system configuration
 * and font information in a JTextArea. These information
 * may help me understand the font setup on Chinese, Japanese,
 * Korean and other non-western Windows editions.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class FontTest {
    
    private JTextComponent textArea;

    
    // Launching ************************************************************** 

    /**
     * Builds and the UI.
     */
    public static void main(String[] args) {
        FontTest instance = new FontTest();
        instance.buildInterface();
    }

    
    // Building the UI ********************************************************

    private void initComponents() {
        textArea = new JTextArea();
        textArea.setText(readConfiguration());
    }
    
    
    /**
     * Creates and configures a frame, builds the menu bar, builds the
     * content, locates the frame on the screen, and finally shows the frame.
     */
    private void buildInterface() {
        initComponents();
        
        JFrame frame = new JFrame();
        frame.setContentPane(buildContentPane());
        frame.setSize(400, 600);
        locateOnScreen(frame);
        frame.setTitle("JGoodies Looks :: FontTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    
    private JComponent buildContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }
    

    /**
     * Locates the frame on the screen center.
     */
    private void locateOnScreen(Frame frame) {
        Dimension paneSize   = frame.getSize();
        Dimension screenSize = frame.getToolkit().getScreenSize();
        frame.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (screenSize.height - paneSize.height) / 2);
    }
    
    
    // Configuration **********************************************************
    
    private String readConfiguration() {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Please copy the information below to your clipboard");
        buffer.append("\nand send them to dev@looks.dev.java.net");
        
        addSystemProperties(buffer, "Java environment:", 
                new String[]{
                    "java.vendor", 
                    "java.version", 
                    "java.runtime.version",
                    "java.vm.version",
                    "sun.desktop"});

        addSystemProperties(buffer, "Operating System:", 
                new String[]{
                    "os.name", 
                    "os.version"});
        if (LookUtils.IS_OS_WINDOWS) {
            addWindowsSettings(buffer, "Windows Settings:");
        }

        addAWTProperties(buffer, "AWT Properties:");

        addSystemProperties(buffer, "User Settings:", 
                new String[]{
                    "user.language", 
                    "user.country",
                    "user.timezone"});
        
        addDesktopProperties(buffer, "Desktop Properties:", 
                new String[]{
                "win.defaultGUI.font", 
                "win.icon.font",
                "win.menu.font", 
                "win.messagebox.font",
                "win.ansiVar.font",
                "win.ansiFixed.font",
                "win.frame.captionFont",
                "win.tooltip.font"});
        
        addInternationalizationProperties(buffer);
        
        addFontSet(buffer, "JGoodies Windows L&f:", getWindowsFontSet());
        addFontSet(buffer, "JGoodies Plastic L&fs:", getPlasticFontSet());
        
        buffer.append("\n\n");
        return buffer.toString();
    }
    
    
    private void addSystemProperties(StringBuffer buffer, String description, String[] keys) {
        buffer.append("\n\n");
        buffer.append(description);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = LookUtils.getSystemProperty(key, "n/a");
            buffer.append("\n    ");
            buffer.append(key);
            buffer.append('=');
            buffer.append(value);
        }
    }
    
    
    private void addDesktopProperties(StringBuffer buffer, String description, String[] keys) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        buffer.append("\n\n");
        buffer.append(description);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Object value = toolkit.getDesktopProperty(key);
            String printString;
            if (value == null) {
                printString = "n/a";
            } else if (value instanceof Font) {
                printString = encodeFont((Font) value);
            } else {
                printString = value.toString();
            }
            buffer.append("\n    ");
            buffer.append(key);
            buffer.append('=');
            buffer.append(printString);
        }
    }
    
    
    private void addInternationalizationProperties(StringBuffer buffer) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        buffer.append("\n\n");
        buffer.append("Internationalization:");
        Font defaultGUIFont = (Font) toolkit.getDesktopProperty("win.defaultGUI.font");
        Font iconFont = (Font) toolkit.getDesktopProperty("win.icon.font");
        Locale locale = Locale.getDefault();
        buffer.append("\n    defaultLocale.getDisplayLanguage(defaultLocale)=");
        buffer.append(locale.getDisplayLanguage(locale));
        buffer.append("\n    defaultGUI font can display localized text=");
        buffer.append(canDisplayLocalizedText(defaultGUIFont, locale));
        buffer.append("\n    icon font can display localized text=");
        buffer.append(canDisplayLocalizedText(iconFont, locale));
    }
    
    
    private void addFontSet(StringBuffer buffer, String description, FontSet fontSet) {
        buffer.append("\n\n");
        buffer.append(description);
        if (fontSet == null) {
            buffer.append("\n  n/a");
            return;
        }
        buffer.append("\n    controlFont=");
        buffer.append(encodeFont(fontSet.getControlFont()));
        buffer.append("\n    menuFont=");
        buffer.append(encodeFont(fontSet.getMenuFont()));
        buffer.append("\n    titleFont=");
        buffer.append(encodeFont(fontSet.getTitleFont()));
        buffer.append("\n    messageFont=");
        buffer.append(encodeFont(fontSet.getMessageFont()));
        buffer.append("\n    smallFont=");
        buffer.append(encodeFont(fontSet.getSmallFont()));
        buffer.append("\n    windowTitleFont=");
        buffer.append(encodeFont(fontSet.getWindowTitleFont()));
    }
    
    
    private void addWindowsSettings(StringBuffer buffer, String description) {
        buffer.append("\n\n");
        buffer.append(description);
        buffer.append("\n    Modern Windows=");
        buffer.append(LookUtils.IS_OS_WINDOWS_MODERN);
        buffer.append("\n    Windows XP=");
        buffer.append(LookUtils.IS_OS_WINDOWS_XP);
        buffer.append("\n    Windows Vista=");
        buffer.append(LookUtils.IS_OS_WINDOWS_VISTA);
        buffer.append("\n    Windows L&f XP Mode=");
        buffer.append(LookUtils.IS_LAF_WINDOWS_XP_ENABLED);
    }

    
    private void addAWTProperties(StringBuffer buffer, String description) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        buffer.append("\n\n");
        buffer.append(description);
        buffer.append("\n    awt.toolkit=");
        buffer.append(toolkit.getClass().getName());
        buffer.append("\n    screen.size=");
        buffer.append(toolkit.getScreenSize().width);
        buffer.append(" x ");
        buffer.append(toolkit.getScreenSize().height);
        buffer.append("\n    screen.resolution=");
        buffer.append(toolkit.getScreenResolution());
        buffer.append("  ");
        buffer.append(LookUtils.IS_LOW_RESOLUTION ? "(low)" : "(high)");
    }


    private String encodeFont(Font font) {
        StringBuffer buffer = new StringBuffer(font.getName());
        buffer.append('-');
        String style = font.isBold()
            ? (font.isItalic() ? "bolditalic" : "bold")
            : (font.isItalic() ? "italic" : "plain");
        buffer.append(style);
        buffer.append('-');
        buffer.append(font.getSize());
        if (!font.getName().equals(font.getFamily())) {
            buffer.append(" family=");
            buffer.append(font.getFamily());
        }
        return buffer.toString();
    }

    
    private FontSet getWindowsFontSet() {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            return WindowsLookAndFeel.getFontPolicy().getFontSet("Windows", UIManager.getDefaults());
        } catch (UnsupportedLookAndFeelException e) {
            return null;
        }
    }
    

    private FontSet getPlasticFontSet() {
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
            return PlasticLookAndFeel.getFontPolicy().getFontSet("Plastic", UIManager.getDefaults());
        } catch (UnsupportedLookAndFeelException e) {
            return null;
        }
    }
    
    
    // Helper Code ************************************************************
    
    /**
     * Checks and answers if the given font can display text
     * that is localized for the specified locale.
     * The test invokes <code>Font#canDisplayUpTo</code> on the locale's
     * name in the locale's language, e.g. "English" for English, 
     * "Deutsch" for German, etc. In a Chinese locale this test
     * will check if the font can display Chinese glyphs.
     * The international Java runtime environments seem to have this
     * name in the localized form for all supported locales. 
     *  
     * @param font     the font to be tested
     * @param locale   the locale to be used
     * @return true if the font can display the locale's text, false otherwise
     */
    private static boolean canDisplayLocalizedText(Font font, Locale locale) {
        String testString = locale.getDisplayLanguage(locale);
        int index = font.canDisplayUpTo(testString);
        return index == -1;
    }
    
    
}