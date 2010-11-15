/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.controller.VersesListController;
import uk.me.phillsacre.lyricdisplay.main.controller.VersesListController.VersesListUI;
import uk.me.phillsacre.lyricdisplay.presenter.ui.PresentationPanel;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;


/**
 * 
 * @author Phill
 * @since 5 Nov 2010
 */
public class VersesList extends JList implements VersesListUI
{
    private static final long serialVersionUID = 911957910035992863L;


    public VersesList()
    {
        setFont( getFont().deriveFont( Font.PLAIN ) );
        setCellRenderer( new VersesListCellRenderer() );

        final VersesListController controller =
                                                LyricDisplay.getApplicationContext().getBean(
                                                        VersesListController.class );
        controller.setUI( this );

        setModel( controller.getListModel() );

        addListSelectionListener( new ListSelectionListener()
        {
            @Override
            public void valueChanged( ListSelectionEvent e )
            {
                controller.handleSelection( getSelectedIndex() );
            }
        } );
    }


    private class VersesListCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent( JList list,
                                                       Object value,
                                                       int index,
                                                       boolean isSelected,
                                                       boolean cellHasFocus )
        {
            Slide slide = (Slide) value;

            int width = VersesList.this.getWidth() - 10;
            int height = (int) Math.round( width * 0.8 );

            PresentationPanel panel = new PresentationPanel();
            panel.setSlide( slide, index );
            panel.setBorder( BorderFactory.createEtchedBorder() );

            JPanel container = new JPanel();
            container.setLayout( new GridLayout( 1, 1 ) );
            container.add( panel );
            container.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
            container.setPreferredSize( new Dimension( width, height ) );

            if (isSelected)
            {
                container.setBackground( SystemColor.textHighlight );
            }

            return container;
        }
    }


    @Override
    public void resetSelection()
    {
        setSelectedIndex( 0 );
    }
}
