/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import uk.me.phillsacre.lyricdisplay.ui.controllers.SummaryPanelController;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.BasePresentationComponent;
import uk.me.phillsacre.lyricdisplay.ui.presentation.display.SongPanel;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class PreviewPresentation extends BasePresentationComponent
{
    private static final long serialVersionUID = 5028904782131349264L;

    /**
     * @param blackout
     */
    public PreviewPresentation(SummaryPanelController controller)
    {
	super(false);
    }

    public void setVerse(SetListItem item, String text)
    {
	setDisplayPanel(new SongPanel(item, text));
    }

}
