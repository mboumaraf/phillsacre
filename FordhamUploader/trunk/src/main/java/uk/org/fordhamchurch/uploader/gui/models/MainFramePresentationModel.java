package uk.org.fordhamchurch.uploader.gui.models;

import uk.org.fordhamchurch.uploader.entities.Upload;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;


@SuppressWarnings( "serial" )
public class MainFramePresentationModel extends PresentationModel<Upload>
{
    public MainFramePresentationModel()
    {
        super( new Upload() );
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

        if ( !ValidationUtils.isNumeric( upload.getChapter() ))
        {
            result.addError( "Chapter must be numeric" );
        }

        return result;
    }
}
