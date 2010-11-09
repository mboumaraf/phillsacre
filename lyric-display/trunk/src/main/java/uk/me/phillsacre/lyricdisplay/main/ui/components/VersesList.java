/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.controller.VersesListController;
import uk.me.phillsacre.lyricdisplay.main.ui.models.SongInfoListModel;


/**
 * 
 * @author Phill
 * @since 5 Nov 2010
 */
public class VersesList extends JList
{
    private static final long serialVersionUID = 911957910035992863L;


    public VersesList()
    {
        setModel( new SongInfoListModel() );
        setFont( getFont().deriveFont( Font.PLAIN ) );
        setCellRenderer( new VersesListCellRenderer() );

        final VersesListController controller =
                                                LyricDisplay.getApplicationContext().getBean(
                                                        VersesListController.class );

        addListSelectionListener( new ListSelectionListener()
        {
            @Override
            public void valueChanged( ListSelectionEvent e )
            {
                controller.handleSelection( getSelectedIndex() );
            }
        } );
    }


    private static class VersesListCellRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent( JList list,
                                                       Object value,
                                                       int index,
                                                       boolean isSelected,
                                                       boolean cellHasFocus )
        {
            JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

            label.setBorder( BorderFactory.createEmptyBorder( 0, 0, 10, 0 ) );

            return label;
        }

    }
}
