package uk.org.fordhamchurch.uploader.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.fordhamchurch.uploader.entities.Upload;
import uk.org.fordhamchurch.uploader.gui.components.FileChooser;
import uk.org.fordhamchurch.uploader.gui.models.MainFramePresentationModel;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
    private static final MainFrame     INSTANCE = new MainFrame();
    private static final Logger        LOG      = LoggerFactory
	                                                .getLogger(MainFrame.class);

    private MainFramePresentationModel _pModel;

    private JTextField                 _sermonTitleField;
    private JComboBox                  _speakerComboBox;
    private JComboBox                  _bibleBookComboBox;
    private JTextField                 _chapterField;
    private JTextField                 _dateField;

    private MainFrame()
    {
	_pModel = new MainFramePresentationModel();

	initGUI();

	pack();
	setLocationRelativeTo(null);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static MainFrame getInstance()
    {
	return INSTANCE;
    }

    private void initGUI()
    {
	BufferedImage image = null;
	try
	{
	    image = ImageIO.read(MainFrame.class.getClassLoader().getResource(
		    "icon.png"));
	}
	catch (IOException e)
	{
	    throw new RuntimeException("Could not load icon", e);
	}
	setIconImage(image);

	setLayout(new BorderLayout());
	setTitle("Phripp: Sermon Ripper");

	FormLayout layout = new FormLayout("5dlu, p, 3dlu, fill:150dlu:grow",
	        "");
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);

	builder.setDefaultDialogBorder();
	builder.setLeadingColumnOffset(1);

	builder.appendSeparator("Sermon Details");

	FileChooser chooser = new FileChooser();
	Bindings.bind(chooser, "file", _pModel.getModel("file"));
	builder.append("File [Leave Blank for CD]", chooser);
	chooser.setToolTipText("Select the MP3 or WAV file the sermon was recorded to. If you do not select a file, the uploader will look for a CD to rip.");

	FileChooser outputChooser = new FileChooser();
	Bindings.bind(outputChooser, "file", _pModel.getModel("outputFile"));
	builder.append("Output File", outputChooser);

	_sermonTitleField = BasicComponentFactory.createTextField(_pModel
	        .getModel("title"));
	_sermonTitleField
	        .setToolTipText("The title of the sermon, include speaker name, for example: Aella Gage - Ephesians 5:1-15");
	builder.append("Title", _sermonTitleField);
	//
	// final ListModel speakersModel = _pModel.getSpeakersModel();
	// ComboBoxAdapter<Speaker> speakerAdapter =
	// new ComboBoxAdapter<Speaker>(
	// speakersModel, _pModel.getModel( "speaker" ) );
	// _speakerComboBox = new JComboBox( speakerAdapter );
	// _speakerComboBox
	// .setToolTipText(
	// "The name of the speaker. 'Others' is a general term for anyone other than one of the regular speakers."
	// );
	// builder.append( "Speaker", _speakerComboBox );
	//
	// final ListModel booksModel = _pModel.getBibleBooksModel();
	// ComboBoxAdapter<Book> bookAdapter = new ComboBoxAdapter<Book>(
	// booksModel, _pModel.getModel( "book" ) );
	// _bibleBookComboBox = new JComboBox( bookAdapter );
	// _bibleBookComboBox
	// .setToolTipText(
	// "The bible book for the sermon. You may only enter one per sermon. If there is no clear Bible passage, use 'Various'."
	// );
	// builder.append( "Bible Book", _bibleBookComboBox );
	//
	// _chapterField = BasicComponentFactory.createTextField(
	// _pModel.getModel( "chapter" ) );
	// _chapterField
	// .setToolTipText(
	// "The chapter of the Bible book. Leave blank if there is no specific chapter. Note that you can only enter ONE chapter in numeric format."
	// );
	// builder.append( "Chapter", _chapterField );
	//
	// _dateField = BasicComponentFactory.createFormattedTextField(
	// _pModel.getModel( "date" ), "##-##-####" );
	// _dateField.setToolTipText(
	// "Please enter the date of the sermon in the format dd-MM-yyyy, e.g. 17-07-2011"
	// );
	// builder.append( "Date [dd-MM-yyyy]", _dateField );
	//
	// SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
	// _pModel.getBean().setDate( sdf.format( new Date() ) );

	ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
	btnBar.addGlue();
	btnBar.addButton(new UploadAction());

	builder.appendRelatedComponentsGapRow();
	builder.nextRow();

	builder.append(btnBar.getPanel(), 3);

	add(builder.getPanel(), BorderLayout.CENTER);
    }

    private class UploadAction extends AbstractAction
    {
	public UploadAction()
	{
	    super("Convert");
	}

	/**
	 * @param e
	 */
	public void actionPerformed(ActionEvent e)
	{
	    ValidationResult result = _pModel.validate();

	    if (result.isEmpty())
	    {
		Upload upload = _pModel.getBean();

		if (null != upload.getOutputFile()
		        && upload.getOutputFile().exists())
		{
		    int overwrite = JOptionPane
			    .showConfirmDialog(
			            MainFrame.this,
			            String.format(
			                    "Are you sure you want to overwrite \"%s\"?",
			                    upload.getOutputFile().getName()),
			            "Confirm Overwrite",
			            JOptionPane.YES_NO_OPTION);

		    if (overwrite == JOptionPane.NO_OPTION)
		    {
			return;
		    }
		}

		LOG.debug("Starting upload");
		ProgressDialog dialog = new ProgressDialog(MainFrame.this,
		        upload);
		dialog.setVisible(true);
	    }
	    else
	    {
		JOptionPane.showMessageDialog(MainFrame.this,
		        result.getMessagesText(), "Validation Error",
		        JOptionPane.ERROR_MESSAGE);
	    }
	}
    }
}
