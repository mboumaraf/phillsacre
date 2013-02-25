package uk.org.fordhamchurch.uploader.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to help build an executable command (i.e. which will be run as
 * a {@link Process}).
 * 
 * @author psacre
 */
public class CommandBuilder
{
    private static final Log _log = LogFactory.getLog(CommandBuilder.class);

    private String           _command;
    private List<String>     _arguments;
    private File             _workingDir;

    public CommandBuilder(String command)
    {
	_command = command;
	_arguments = new ArrayList<String>();

	_workingDir = new File(System.getProperty("java.io.tmpdir"));
    }

    public void addArgument(String arg)
    {
	_arguments.add(arg);
    }

    /**
     * Adds an argument which contains replacement placeholders. The string and
     * parameters will be passed into {@link String#format}.
     * 
     * @param arg
     * @param params
     */
    public void addArgument(String arg, Object param)
    {
	if (null == param)
	{
	    return;
	}
	_arguments.add(arg);
	_arguments.add(param.toString());
    }

    public void setWorkingDirectory(File directory)
    {
	_workingDir = directory;
    }

    public ProcessBuilder getProcess()
    {
	String command = _command;

	if (Utils.isOSWindows())
	{
	    String path = PropertyUtils.getProperty("tools.dir");
	    command = path + File.separator + command;
	}

	_log.debug("Command is: " + command);

	List<String> args = new ArrayList<String>();
	args.add(command);
	args.addAll(_arguments);

	_log.debug("Number of arguments: " + _arguments.size());

	ProcessBuilder builder = new ProcessBuilder(args);

	if (null != _workingDir)
	{
	    builder.directory(_workingDir);

	    _log.debug("Working directory: " + _workingDir);
	}

	return builder;
    }
}
