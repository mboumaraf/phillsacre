/*
 * Copyright © 2010 Clifford Thames Ltd.  All rights reserved.
 * Clifford Thames proprietary and confidential.  
 * Use is subject to license terms.
 */

package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.controller.LivePanelController;

/**
 * Live Display Panel
 * 
 * @author psacre
 * @since 15 Nov 2010
 */
public class LivePanel extends JPanel
{
    private static final long serialVersionUID = -5215907441383253818L;

    public LivePanel()
    {
	setPreferredSize(new Dimension(200, 200));

	setBorder(BorderFactory.createTitledBorder("Live Output"));

	setLayout(new GridLayout(1, 1));

	LivePanelController controller = LyricDisplay.getApplicationContext()
	        .getBean(LivePanelController.class);

	VersesList versesList = new VersesList();
	add(new JScrollPane(versesList));
    }
}
