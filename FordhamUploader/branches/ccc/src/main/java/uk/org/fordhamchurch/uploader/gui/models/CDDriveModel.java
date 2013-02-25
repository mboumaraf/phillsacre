package uk.org.fordhamchurch.uploader.gui.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import uk.org.fordhamchurch.uploader.utils.PropertyUtils;


/**
 * Windows-only. Attempt to determine all the drives on the system.
 */
@SuppressWarnings( "serial" )
public class CDDriveModel extends AbstractListModel
{
    private FileSystemView _fileSystemView;
    private List<Drive>    _drives;


    public CDDriveModel()
    {
        _fileSystemView = FileSystemView.getFileSystemView();

        String myComputerName = PropertyUtils.getProperty( "mycomputer.name" );

        File[] roots = _fileSystemView.getRoots();
        File[] children = roots[ 0 ].listFiles();

        for (File file : children)
        {
            String name = _fileSystemView.getSystemDisplayName( file );

            if (name.equals( myComputerName ))
            {
                _drives = new ArrayList<Drive>();

                for (File drive : file.listFiles())
                {
                    _drives.add( new Drive( drive ) );
                }

                break;
            }
        }
    }

    public Object getElementAt( int index )
    {
        return _drives.get( index );
    }

    public int getSize()
    {
        return null == _drives ? 0 : _drives.size();
    }


    public static class Drive
    {
        private File   _file;
        private String _name;
        private Icon   _icon;


        public Drive( File f )
        {
            _file = f;

            FileSystemView fsv = FileSystemView.getFileSystemView();

            _name = fsv.getSystemDisplayName( f );
            _icon = fsv.getSystemIcon( f );
        }


        /**
         * @return the name
         */
        public String getName()
        {
            return _name;
        }

        /**
         * @return the icon
         */
        public Icon getIcon()
        {
            return _icon;
        }

        public File getFile()
        {
            return _file;
        }

        @Override
        public String toString()
        {
            return getName();
        }
    }
}
