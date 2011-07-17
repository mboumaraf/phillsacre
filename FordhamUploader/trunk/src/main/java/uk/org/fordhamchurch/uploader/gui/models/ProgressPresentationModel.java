package uk.org.fordhamchurch.uploader.gui.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.entities.Upload;
import uk.org.fordhamchurch.uploader.utils.UploadRunnerWorker;

import com.jgoodies.binding.beans.PropertyConnector;

public class ProgressPresentationModel
{
    private static final Log      LOG            = LogFactory
	                                                 .getLog(ProgressPresentationModel.class);

    private boolean               _complete      = false;
    private String                _status;
    private int                   _percentage    = 0;
    private PropertyChangeSupport _changeSupport = new PropertyChangeSupport(
	                                                 this);
    private Upload                _upload;
    private UploadRunnerWorker    _worker;

    public ProgressPresentationModel(Upload upload)
    {
	_upload = upload;
	_status = "Initialising...";
    }

    public void startUpload()
    {
	if (null != _worker)
	{
	    cancel();
	    _worker = null;
	}

	LOG.debug("Starting upload...");

	_worker = new UploadRunnerWorker(this, _upload);
	PropertyConnector.connect(this, "percentage", _worker, "progress");
	_worker.execute();
    }

    public int getPercentage()
    {
	return _percentage;
    }

    public void setPercentage(int percentage)
    {
	int old = _percentage;
	_percentage = percentage;

	_changeSupport.firePropertyChange("percentage", old, percentage);
    }

    public String getStatus()
    {
	return _status;
    }

    public void setStatus(String newStatus)
    {
	String oldStatus = _status;
	_status = newStatus;

	_changeSupport.firePropertyChange("status", oldStatus, newStatus);
    }

    public boolean isCancelEnabled()
    {
	return !_complete;
    }

    public boolean isComplete()
    {
	return _complete;
    }

    public void setComplete(boolean complete)
    {
	_complete = complete;

	_changeSupport.firePropertyChange("complete", !complete, complete);
	_changeSupport.firePropertyChange("cancelEnabled", complete, !complete);
    }

    public void cancel()
    {
	LOG.debug("Cancelling worker thread");
	_worker.cancel(true);

	setComplete(true);
    }

    /* === Property Change Listener support =============================== */

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
	_changeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String property,
	    PropertyChangeListener l)
    {
	_changeSupport.addPropertyChangeListener(property, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
	_changeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
	    PropertyChangeListener l)
    {
	_changeSupport.removePropertyChangeListener(propertyName, l);
    }
}
