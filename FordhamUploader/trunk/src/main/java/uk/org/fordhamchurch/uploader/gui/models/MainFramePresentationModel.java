package uk.org.fordhamchurch.uploader.gui.models;

import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.fordhamchurch.uploader.dao.FordhamDAO;
import uk.org.fordhamchurch.uploader.entities.Book;
import uk.org.fordhamchurch.uploader.entities.Speaker;
import uk.org.fordhamchurch.uploader.entities.Upload;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;


@SuppressWarnings( "serial" )
public class MainFramePresentationModel extends PresentationModel<Upload>
{
    private static final Logger   LOG = LoggerFactory.getLogger( MainFramePresentationModel.class );

    private final SpeakersModel   _speakersModel;
    private final BibleBooksModel _bibleBooksModel;


    public MainFramePresentationModel()
    {
        super( new Upload() );

        _speakersModel = new SpeakersModel();
        _bibleBooksModel = new BibleBooksModel();

        loadDataAsynchronously();
    }

    public SpeakersModel getSpeakersModel()
    {
        return _speakersModel;
    }

    public BibleBooksModel getBibleBooksModel()
    {
        return _bibleBooksModel;
    }

    public ValidationResult validate()
    {
        ValidationResult result = new ValidationResult();

        Upload upload = getBean();

        if (ValidationUtils.isEmpty( upload.getTitle() ))
        {
            result.addError( "You must specify a Title" );
        }

        if (null == upload.getSpeaker())
        {
            result.addError( "You must specify a Speaker" );
        }

        if (null == upload.getBook())
        {
            result.addError( "You must specify a Bible book" );
        }

        if (ValidationUtils.isEmpty( upload.getDate() ))
        {
            result.addError( "You must specify a Date" );
        }

        if (StringUtils.isNotBlank( upload.getChapter() ) && !ValidationUtils.isNumeric( upload.getChapter() ))
        {
            result.addError( "Chapter must be numeric" );
        }

        return result;
    }

    private void loadDataAsynchronously()
    {
        new DataLoader().execute();
    }


    private class DataLoader extends SwingWorker<Void, Void>
    {
        private List<Speaker> _speakers;
        private List<Book>    _books;


        @Override
        protected Void doInBackground() throws Exception
        {
            _speakers = FordhamDAO.getInstance().getSpeakers();
            _books = FordhamDAO.getInstance().getBooks();

            return null;
        }

        @Override
        protected void done()
        {
            LOG.debug( "Got data: {} books, {} speakers", _books.size(), _speakers.size() );

            _speakersModel.addAll( _speakers );
            _bibleBooksModel.addAll( _books );
        }
    }
}
