/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.events.ChangeSetListSelectionEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.main.utils.Utils;
import uk.me.phillsacre.lyricdisplay.ui.controllers.PreviewSummaryPanelController;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class PreviewSummaryPanel extends SummaryPanel
{
    private static final long serialVersionUID = -676682403120584217L;

    public PreviewSummaryPanel()
    {
	super(App.getApplicationContext().getBean(
	        PreviewSummaryPanelController.class), "Preview");
    }

    @Override
    public JToolBar createToolBar()
    {
	JToolBar toolBar = new JToolBar();

	toolBar.add(Box.createHorizontalGlue());
	JButton liveBtn = toolBar.add(new GoLiveAction());
	liveBtn.setHorizontalTextPosition(SwingConstants.LEFT);
	liveBtn.setText("Go Live");

	return toolBar;
    }

    class GoLiveAction extends AbstractAction implements
	    EventSubscriber<ChangeSetListSelectionEvent>
    {
	private static final long serialVersionUID = 6613599619599651278L;

	private SetListItem       _item;

	public GoLiveAction()
	{
	    super("Go Live");

	    setEnabled(false);

	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/golive.png")));
	    putValue(SHORT_DESCRIPTION,
		    "Sends the current previewed song to live output");

	    EventBus.subscribe(ChangeSetListSelectionEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    EventBus.publish(new ChangeSetListSelectionEvent(Target.LIVE, _item));
	}

	@Override
	public void onEvent(ChangeSetListSelectionEvent event)
	{
	    if (event.getTarget() == Target.PREVIEW)
	    {
		_item = event.getItem();

		setEnabled(_item != null);
	    }
	}
    }
}
