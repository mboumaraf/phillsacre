package uk.org.fordhamchurch.uploader.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import uk.org.fordhamchurch.uploader.entities.Upload;
import uk.org.fordhamchurch.uploader.gui.models.ProgressPresentationModel;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


@SuppressWarnings( "serial" )
public class ProgressDialog extends JDialog
{
    private JProgressBar              _progressBar;
    private JLabel                    _statusLabel;
    private ProgressPresentationModel _pModel;


    public ProgressDialog( JFrame parent, Upload upload )
    {
        super( parent, true );

        _pModel = new ProgressPresentationModel( upload );

        initGUI();

        pack();

        setResizable( false );
        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        setLocationRelativeTo( parent );

        addWindowListener( new WindowAdapter()
        {
            /**
             * @param e
             */
            @Override
            public void windowClosing( WindowEvent e )
            {
                if (_pModel.isComplete())
                {
                    dispose();
                }
            }
        } );

        _pModel.startUpload();
    }

    /**
     * Initialise the GUI components
     */
    private void initGUI()
    {
        setTitle( "Progress" );
        setLayout( new BorderLayout() );

        BeanAdapter<ProgressPresentationModel> adapter = new BeanAdapter<ProgressPresentationModel>( _pModel, true );

        FormLayout layout = new FormLayout( "p, 3dlu, fill:100dlu:grow" );
        DefaultFormBuilder builder = new DefaultFormBuilder( layout );
        builder.setDefaultDialogBorder();

        _progressBar = new JProgressBar( 0, 100 );
        _progressBar.setValue( 0 );

        builder.appendSeparator( "Progress" );
        builder.append( _progressBar, 3 );

        _statusLabel = new JLabel( _pModel.getStatus() );

        Bindings.bind( _statusLabel, adapter.getValueModel( "status" ) );
        Bindings.bind( _progressBar, "value", adapter.getValueModel( "percentage" ) );

        builder.append( "Status: ", _statusLabel );

        ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
        btnBar.addGlue();
        btnBar.addButton( new CloseAction() );
        btnBar.addButton( new CancelAction() );

        builder.appendUnrelatedComponentsGapRow();
        builder.nextRow();

        builder.append( btnBar.getPanel(), 3 );

        add( builder.getPanel(), BorderLayout.CENTER );
    }


    private class CloseAction extends AbstractAction
    {
        public CloseAction()
        {
            super( "Close" );

            setEnabled( false );

            PropertyConnector.connect( this, "enabled", _pModel, "complete" );
        }

        /**
         * @param e
         */
        public void actionPerformed( ActionEvent e )
        {
            dispose();
        }
    }


    private class CancelAction extends AbstractAction
    {
        public CancelAction()
        {
            super( "Cancel" );

            setEnabled( true );

            PropertyConnector.connect( this, "enabled", _pModel, "cancelEnabled" );
        }

        public void actionPerformed( ActionEvent e )
        {
            _pModel.cancel();
        }
    }
}
