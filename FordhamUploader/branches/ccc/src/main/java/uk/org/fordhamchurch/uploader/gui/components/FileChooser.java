/**
 * 
 */
package uk.org.fordhamchurch.uploader.gui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
 * 
 * @author Phill
 * @since 1 Mar 2011
 */
public class FileChooser extends JComponent
{
    private static final long     serialVersionUID = 5223551285153552243L;

    private File                  _file;
    private JTextField            _textField;
    private PropertyChangeSupport _changeSupport   = new PropertyChangeSupport( this );


    public FileChooser()
    {
        setLayout( new BorderLayout() );

        _textField = new JTextField();
        _textField.setEditable( false );

        add( _textField, BorderLayout.CENTER );

        JButton button = new JButton( new ChooseFileAction() );

        add( button, BorderLayout.EAST );
    }

    public File getFile()
    {
        return _file;
    }

    public void setFile( File file )
    {
        File oldFile = _file;

        _file = file;
        _textField.setText( _file.getAbsolutePath() );

        _changeSupport.firePropertyChange( "file", oldFile, _file );
    }

    @Override
    public void setToolTipText( String text )
    {
        _textField.setToolTipText( text );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        _changeSupport.addPropertyChangeListener( propertyName, listener );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        _changeSupport.removePropertyChangeListener( propertyName, listener );
    }

    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        _changeSupport.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        _changeSupport.removePropertyChangeListener( listener );
    }


    private class ChooseFileAction extends AbstractAction
    {
        private static final long serialVersionUID = 7341663808313774659L;


        public ChooseFileAction()
        {
            super( "..." );
        }

        public void actionPerformed( ActionEvent e )
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter( new FileFilter()
            {
                @Override
                public boolean accept( File pathname )
                {
                    String filename = pathname.getName().toLowerCase();

                    return pathname.isDirectory() || filename.endsWith( ".mp3" ) || filename.endsWith( ".wav" );
                }

                @Override
                public String getDescription()
                {
                    return "WAV and MP3 files";
                }
            } );

            int retVal = chooser.showOpenDialog( FileChooser.this.getParent() );

            if (retVal == JFileChooser.APPROVE_OPTION)
            {
                setFile( chooser.getSelectedFile() );
            }
        }

    }
}
