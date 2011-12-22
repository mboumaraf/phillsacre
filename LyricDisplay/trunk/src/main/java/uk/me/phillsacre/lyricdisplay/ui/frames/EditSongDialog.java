/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.ui.controllers.EditSongController;
import uk.me.phillsacre.lyricdisplay.ui.controllers.EditSongController.EditSongUI;
import uk.me.phillsacre.lyricdisplay.ui.utils.UpperCaseDocumentFilter;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class EditSongDialog extends JDialog implements EditSongUI
{
    private static final long  serialVersionUID = -5763139971684394379L;

    private EditSongController _controller;

    public EditSongDialog(JFrame parent, Song song)
    {
	super(parent, "Edit Song", true);

	_controller = new EditSongController(this, song);

	FormLayout layout = new FormLayout(
	        "p, 3dlu, fill:30dlu:grow, 3dlu, p, 3dlu, fill:30dlu:grow", "");
	layout.setColumnGroups(new int[][] { { 1, 5 }, { 3, 7 } });
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.setDefaultDialogBorder();
	builder.setDefaultRowSpec(RowSpec.decode("p"));

	builder.append("Title", BasicComponentFactory
	        .createTextField(_controller.getModel("title")), 5);

	builder.append("Publisher", BasicComponentFactory
	        .createTextField(_controller.getModel("publisher")));

	builder.append("Author", BasicComponentFactory
	        .createTextField(_controller.getModel("author")));

	NumberFormat nf = NumberFormat.getInstance();
	nf.setGroupingUsed(false);

	builder.append(
	        "Year",
	        BasicComponentFactory.createIntegerField(
	                _controller.getModel("year"), nf));

	builder.append("Copyright", BasicComponentFactory
	        .createTextField(_controller.getModel("copyright")));

	final JTextField songOrderField = BasicComponentFactory
	        .createTextField(_controller.getModel("songOrder"));

	UpperCaseDocumentFilter filter = new UpperCaseDocumentFilter();
	filter.installFilter(songOrderField);

	builder.append("Song Order", songOrderField, 5);

	builder.appendRow(builder.getLineGapSpec());
	builder.nextLine(2);

	JTextArea textArea = BasicComponentFactory.createTextArea(_controller
	        .getModel("text"));
	textArea.setText(song.getText());
	textArea.setCaretPosition(0);

	CellConstraints cc = new CellConstraints();

	builder.appendRow(RowSpec.decode("top:50dlu:grow"));
	builder.append("Text");
	builder.add(new JScrollPane(textArea), cc.xywh(builder.getColumn(),
	        builder.getRow(), 5, 1, "fill, fill"));
	builder.nextLine();

	setSize(600, 500);

	setLocationRelativeTo(parent);

	ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
	btnBar.addGlue();
	btnBar.addButton(new SaveAction());
	btnBar.addRelatedGap();
	btnBar.addButton(new CancelAction());

	builder.append(btnBar.getPanel(), 7);

	setLayout(new BorderLayout());
	add(builder.getPanel(), BorderLayout.CENTER);
    }

    private class SaveAction extends AbstractAction
    {
	private static final long serialVersionUID = 1L;

	public SaveAction()
	{
	    super("Save");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    _controller.saveSong();
	}
    }

    private class CancelAction extends AbstractAction
    {
	private static final long serialVersionUID = 1L;

	public CancelAction()
	{
	    super("Cancel");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    _controller.cancel();
	}
    }

    @Override
    public void close()
    {
	dispose();
    }
}
