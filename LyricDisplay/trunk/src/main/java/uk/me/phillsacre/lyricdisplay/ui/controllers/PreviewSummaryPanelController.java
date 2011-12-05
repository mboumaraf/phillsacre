/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.controllers;

import javax.inject.Named;

import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
@Named("previewSummaryPanelController")
public class PreviewSummaryPanelController extends SummaryPanelController
{
    public PreviewSummaryPanelController()
    {
	super(Target.PREVIEW);
    }
}
