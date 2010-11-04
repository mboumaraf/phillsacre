/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

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

    public EditSongDialog(Song song)
    {
	super(MainFrame.getInstance(), "Edit Song", true);

	_song = song;

	setLayout(new BorderLayout());

	_titleField = new JTextField();

	add(_titleField, BorderLayout.NORTH);

	_textArea = new JTextArea();

	add(new JScrollPane(_textArea), BorderLayout.CENTER);

	setSize(300, 400);

	setLocationRelativeTo(MainFrame.getInstance());

	JPanel btnPanel = new JPanel();
	JButton okBtn = new JButton(new OKAction());
	btnPanel.add(okBtn);
	add(btnPanel, BorderLayout.SOUTH);
    }

    private void saveSong()
    {
	_song.setTitle(_titleField.getText());
	_song.setText(_textArea.getText());

    }

    private class OKAction extends AbstractAction
    {
	public OKAction()
	{
	    super("OK");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    saveSong();
	}

    }
}
