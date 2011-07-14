import javax.swing.UIManager;

import uk.org.fordhamchurch.uploader.gui.frames.MainFrame;



public class RunUploader
{

    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception
    {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

        MainFrame.getInstance().setVisible( true );
    }

}
