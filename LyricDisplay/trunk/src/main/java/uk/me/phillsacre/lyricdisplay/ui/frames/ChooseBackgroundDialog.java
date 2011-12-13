/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.ui.presentation.BasePresentationComponent;
import uk.me.phillsacre.lyricdisplay.ui.presentation.DisplayPanel;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.GradientBackground;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.ImageBackground;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.TextRenderer;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author Phill
 * @since 8 Dec 2011
 */
public class ChooseBackgroundDialog extends JDialog
{
    private static final long         serialVersionUID = -2052506736832992498L;

    private BasePresentationComponent _presentation;
    private Background                _background;
    private JButton                   _colour1;
    private JButton                   _colour2;
    private Color                     _topColor        = Color.blue;
    private Color                     _bottomColor     = Color.black;
    
    private static File               _directory;

    public ChooseBackgroundDialog(Background background)
    {
	super(App.getMainFrame(), "Choose Background", true);

	_background = (background != null) ? background
	        : new GradientBackground(_topColor, _bottomColor);

	setSize(300, 350);
	setResizable(false);
	setLocationRelativeTo(App.getMainFrame());

	setLayout(new BorderLayout());

	JPanel container = new JPanel();
	container.setLayout(new BorderLayout());
	container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	_presentation = new BasePresentationComponent(false);
	_presentation.setBorder(BorderFactory.createEtchedBorder());
	_presentation.setPreferredSize(new Dimension(0, 150));
	_presentation.setDisplayPanel(createDisplayPanel());
	container.add(_presentation, BorderLayout.NORTH);

	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.add("Gradient", createGradientTab());
	tabbedPane.add("Image", createImageTab());

	container.add(tabbedPane, BorderLayout.CENTER);

	ButtonBarBuilder2 builder = new ButtonBarBuilder2();
	builder.addGlue();
	builder.addButton(new OKAction());
	builder.addButton(new CancelAction());

	container.add(builder.getPanel(), BorderLayout.SOUTH);

	add(container, BorderLayout.CENTER);

	pack();
    }

    public Background getSelectedBackground()
    {
	return _background;
    }

    private DisplayPanel createDisplayPanel()
    {
	return new TestDisplayPanel();
    }

    private JPanel createGradientTab()
    {
	FormLayout layout = new FormLayout("p, 3dlu, p");
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);

	_colour1 = new JButton(new ChooseColourAction());
	_colour1.setBackground(_topColor);
	_colour2 = new JButton(new ChooseColourAction());
	_colour2.setBackground(_bottomColor);

	_colour1.addPropertyChangeListener("background",
	        new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt)
		    {
		        _topColor = (Color) evt.getNewValue();
		        refreshGradient();
		    }
	        });
	_colour2.addPropertyChangeListener("background",
	        new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt)
		    {
		        _bottomColor = (Color) evt.getNewValue();
		        refreshGradient();
		    }
	        });

	builder.append("Colour 1", _colour1);
	builder.append("Colour 2", _colour2);

	return builder.getPanel();
    }

    private JPanel createImageTab()
    {
	JPanel panel = new JPanel();

	panel.add(new JButton(new SelectFileAction()));

	return panel;
    }

    private void refreshGradient()
    {
	_background = new GradientBackground(_topColor, _bottomColor);
	_presentation.repaint();

    }

    private class OKAction extends AbstractAction
    {
	private static final long serialVersionUID = -3032239641908809468L;

	public OKAction()
	{
	    super("OK");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    dispose();
	}
    }

    private class CancelAction extends AbstractAction
    {
	private static final long serialVersionUID = -7075592258274306839L;

	public CancelAction()
	{
	    super("Cancel");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    _background = null;
	    dispose();
	}

    }

    private class ChooseColourAction extends AbstractAction
    {
	private static final long serialVersionUID = 1147957024315011074L;

	public ChooseColourAction()
	{
	    putValue(SHORT_DESCRIPTION, "Click to choose colour");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    JButton button = (JButton) e.getSource();
	    Color background = button.getBackground();

	    Color newColor = JColorChooser.showDialog(
		    ChooseBackgroundDialog.this, "Choose Colour", background);

	    if (null != newColor)
	    {
		button.setBackground(newColor);
	    }
	}
    }

    private class SelectFileAction extends AbstractAction
    {
	private static final long serialVersionUID = -4074733498473622229L;

	public SelectFileAction()
	{
	    super("Select File");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    final JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(_directory);
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.setMultiSelectionEnabled(false);
	    chooser.setFileFilter(new FileFilter() {
		@Override
		public boolean accept(File f)
		{
		    if (f.isDirectory())
		    {
			return true;
		    }
		    String filename = f.getName().toLowerCase();
		    String extension = filename.indexOf('.') > -1 ? filename
			    .substring(filename.indexOf('.') + 1) : filename;

		    return extension.equals("png") || extension.equals("jpg")
			    || extension.equals("bmp");
		}

		@Override
		public String getDescription()
		{
		    return "Images only";
		}
	    });

	    int retVal = chooser.showDialog(ChooseBackgroundDialog.this, "OK");

	    if (retVal == JFileChooser.APPROVE_OPTION)
	    {
		_directory = chooser.getCurrentDirectory();
		try
		{
		    _background = new ImageBackground(chooser.getSelectedFile()
			    .toURI().toURL(), Color.black);
		    _presentation.repaint();
		}
		catch (MalformedURLException e1)
		{
		    throw new RuntimeException("Could not load image", e1);
		}
	    }
	}
    }

    private class TestDisplayPanel implements DisplayPanel
    {
	private TextRenderer _textRenderer = App.getApplicationContext()
	                                           .getBean(TextRenderer.class);

	private String       _text         = "Verse 1, line 1\nVerse one, line two\nVerse 1\nAnother line of verse 1\nFouth line.";

	@Override
	public void render(Graphics2D g2d, int width, int height)
	{
	    int marginW = (int) Math.round((float) width * 0.1);
	    int marginH = (int) Math.round((float) height * 0.1);

	    final int availableWidth = width - (marginW * 2);
	    final int availableHeight = height - (marginH * 2);

	    _background.renderBackground(g2d, width, height);

	    int left = (width - availableWidth) / 2;
	    int top = (height - availableHeight) / 2;

	    final Rectangle bounds = new Rectangle(left, top, availableWidth,
		    availableHeight);

	    String[] text = _text.split("\n");
	    float size = _textRenderer.getSize(g2d, bounds, text);
	    _textRenderer.renderText(g2d, text, size, bounds);
	}

	@Override
	public void addRepaintListener(RepaintListener repaintListener)
	{

	}
    }

}
