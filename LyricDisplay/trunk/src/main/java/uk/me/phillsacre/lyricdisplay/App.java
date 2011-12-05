package uk.me.phillsacre.lyricdisplay;

import javax.swing.UIManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.me.phillsacre.lyricdisplay.ui.frames.MainFrame;

/**
 * Hello world!
 * 
 */
public class App
{
    private static ApplicationContext APP_CONTEXT;
    private static MainFrame          MAIN_FRAME;

    public static void main(String[] args) throws Exception
    {
	APP_CONTEXT = new ClassPathXmlApplicationContext(
	        "spring/spring-main.xml");

	UIManager
	        .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

	MAIN_FRAME = new MainFrame();
	MAIN_FRAME.init();
	MAIN_FRAME.setVisible(true);
    }

    public static ApplicationContext getApplicationContext()
    {
	return APP_CONTEXT;
    }

    public static MainFrame getMainFrame()
    {
	return MAIN_FRAME;
    }
}
