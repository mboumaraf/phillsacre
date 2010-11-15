/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SaveSongEvent;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class EditSongDialog extends JDialog
{
    private static final long serialVersionUID = -5763139971684394379L;

    private Song              _song;
    private JTextField        _titleField;
    private JTextArea         _textArea;


    public EditSongDialog( Song song )
    {
        super( MainFrame.getInstance(), "Edit Song", true );

        _song = song;

        FormLayout layout = new FormLayout( "p, 3dlu, fill:p:grow", "" );
        DefaultFormBuilder builder = new DefaultFormBuilder( layout );
        builder.setDefaultDialogBorder();
        builder.setDefaultRowSpec( RowSpec.decode( "p" ) );

        _titleField = new JTextField();
        _titleField.setText( _song.getTitle() );

        builder.append( "Title", _titleField );
        builder.appendRow( builder.getLineGapSpec() );
        builder.nextLine( 2 );

        _textArea = new JTextArea();
        _textArea.setText( _song.getText() );
        _textArea.setCaretPosition( 0 );

        CellConstraints cc = new CellConstraints();

        builder.appendRow( RowSpec.decode( "top:50dlu:grow" ) );
        builder.append( "Text" );
        builder.add( new JScrollPane( _textArea ), cc.xy( builder.getColumn(), builder.getRow(), "fill, fill" ) );
        builder.nextLine();

        setSize( 600, 500 );

        setLocationRelativeTo( MainFrame.getInstance() );

        ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
        btnBar.addGlue();
        btnBar.addButton( new SaveAction() );
        btnBar.addRelatedGap();
        btnBar.addButton( new CancelAction() );

        builder.append( btnBar.getPanel(), 3 );

        setLayout( new BorderLayout() );
        add( builder.getPanel(), BorderLayout.CENTER );
    }

    private void saveSong()
    {
        _song.setTitle( _titleField.getText() );
        _song.setText( _textArea.getText() );

        EventBus.publish( new SaveSongEvent( _song ) );
    }


    private class SaveAction extends AbstractAction
    {
        public SaveAction()
        {
            super( "Save" );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveSong();
            dispose();
        }

    }


    private class CancelAction extends AbstractAction
    {
        public CancelAction()
        {
            super( "Cancel" );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            dispose();
        }
    }
}
