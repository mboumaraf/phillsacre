/**
 * 
 */
package uk.me.phillsacre.lyricdisplay;

import uk.me.phillsacre.lyricdisplay.main.ui.frame.MainFrame;
import uk.me.phillsacre.lyricdisplay.presenter.controller.PresentationController;

/**
 * 
 * @author Phill
 * @since 25 Oct 2010
 */
public class LyricDisplay
{
    public static void main(String[] args)
    {
	PresentationController controller = new PresentationController();

	controller.init();

	MainFrame.getInstance().setVisible(true);
    }
}
