package uk.org.fordhamchurch.uploader.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.entities.Book;
import uk.org.fordhamchurch.uploader.entities.Speaker;
import uk.org.fordhamchurch.uploader.entities.Upload;
import uk.org.fordhamchurch.uploader.gui.components.FileChooser;
import uk.org.fordhamchurch.uploader.gui.models.BibleBooksModel;
import uk.org.fordhamchurch.uploader.gui.models.MainFramePresentationModel;
import uk.org.fordhamchurch.uploader.gui.models.SpeakersModel;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;


@SuppressWarnings( "serial" )
public class MainFrame extends JFrame
{
    private static final MainFrame     INSTANCE = new MainFrame();
    private static final Log           _log     = LogFactory.getLog( MainFrame.class );

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
        setLocationRelativeTo( null );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }

    public static MainFrame getInstance()
    {
        return INSTANCE;
    }

    private void initGUI()
    {
        setLayout( new BorderLayout() );
        setTitle( "Phripp: Fordham Sermon Uploader" );

        FormLayout layout = new FormLayout( "5dlu, p, 3dlu, fill:150dlu:grow", "" );
        DefaultFormBuilder builder = new DefaultFormBuilder( layout );

        builder.setDefaultDialogBorder();
        builder.setLeadingColumnOffset( 1 );

        builder.appendSeparator( "Sermon Details" );

        FileChooser chooser = new FileChooser();
        Bindings.bind( chooser, "file", _pModel.getModel( "file" ) );
        builder.append( "File", chooser );

        _sermonTitleField = BasicComponentFactory.createTextField( _pModel.getModel( "title" ) );
        builder.append( "Title", _sermonTitleField );

        ListModel speakersModel = new SpeakersModel();
        ComboBoxAdapter<Speaker> speakerAdapter =
                                                  new ComboBoxAdapter<Speaker>(
                                                          speakersModel, _pModel.getModel( "speaker" ) );
        _speakerComboBox = new JComboBox( speakerAdapter );
        builder.append( "Speaker", _speakerComboBox );

        ListModel booksModel = new BibleBooksModel();
        ComboBoxAdapter<Book> bookAdapter = new ComboBoxAdapter<Book>( booksModel, _pModel.getModel( "book" ) );
        _bibleBookComboBox = new JComboBox( bookAdapter );
        builder.append( "Bible Book", _bibleBookComboBox );

        _chapterField = BasicComponentFactory.createTextField( _pModel.getModel( "chapter" ) );
        builder.append( "Chapter", _chapterField );

        _dateField = BasicComponentFactory.createFormattedTextField( _pModel.getModel( "date" ), "##-##-####" );
        builder.append( "Date [dd-MM-yyyy]", _dateField );

        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
        _pModel.getBean().setDate( sdf.format( new Date() ) );

        ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
        btnBar.addGlue();
        btnBar.addButton( new UploadAction() );

        builder.appendRelatedComponentsGapRow();
        builder.nextRow();

        builder.append( btnBar.getPanel(), 3 );

        add( builder.getPanel(), BorderLayout.CENTER );
    }


    private class UploadAction extends AbstractAction
    {
        public UploadAction()
        {
            super( "Upload" );
        }

        /**
         * @param e
         */
        public void actionPerformed( ActionEvent e )
        {
            ValidationResult result = _pModel.validate();

            if (result.isEmpty())
            {
                Upload upload = _pModel.getBean();

                _log.debug( "Starting upload" );
                ProgressDialog dialog = new ProgressDialog( MainFrame.this, upload );
                dialog.setVisible( true );
            }
            else
            {
                JOptionPane.showMessageDialog(
                        MainFrame.this, result.getMessagesText(), "Validation Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
