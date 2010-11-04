/**
 * 
 */
package uk.me.phillsacre.lyricdisplay;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.me.phillsacre.lyricdisplay.main.ui.frame.MainFrame;

/**
 * 
 * @author Phill
 * @since 25 Oct 2010
 */
public class LyricDisplay
{
    private static ApplicationContext APP_CONTEXT;

    public static void main(String[] args)
    {
	// Initialise the application context
	APP_CONTEXT = new ClassPathXmlApplicationContext(
	        "spring/spring-main.xml");

	MainFrame.getInstance().setVisible(true);
    }

    public static ApplicationContext getApplicationContext()
    {
	return APP_CONTEXT;
    }
}
