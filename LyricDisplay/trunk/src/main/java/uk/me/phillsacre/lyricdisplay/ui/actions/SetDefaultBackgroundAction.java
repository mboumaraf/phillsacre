/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.events.DefaultBackgroundSelectedEvent;
import uk.me.phillsacre.lyricdisplay.main.utils.BackgroundUtils;
import uk.me.phillsacre.lyricdisplay.ui.frames.ChooseBackgroundDialog;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;

/**
 * 
 * @author Phill
 * @since 8 Dec 2011
 */
public class SetDefaultBackgroundAction extends AbstractAction
{
    private static final long serialVersionUID = -3600468173673758663L;

    public SetDefaultBackgroundAction()
    {
	super("Set default background...");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	BackgroundUtils utils = App.getApplicationContext().getBean(
	        BackgroundUtils.class);

	ChooseBackgroundDialog dialog = new ChooseBackgroundDialog(
	        utils.getDefaultBackground());
	dialog.setVisible(true);

	final Background background = dialog.getSelectedBackground();

	if (null != background)
	{
	    EventBus.publish(new DefaultBackgroundSelectedEvent(background));
	}
    }

}
