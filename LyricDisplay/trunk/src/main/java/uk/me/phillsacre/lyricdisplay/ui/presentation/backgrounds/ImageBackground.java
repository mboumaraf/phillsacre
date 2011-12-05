/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
public class ImageBackground implements Background
{
    private BufferedImage _image;
    private Color         _background;

    public ImageBackground(URL url, Color background)
    {
	_background = background;

	try
	{
	    _image = ImageIO.read(url);
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }

    public void renderBackground(Graphics2D g, int width, int height)
    {
	BufferedImage scaled = Scalr.resize(_image, width, height);
	g.drawImage(scaled, 0, 0, _background, null);
    }
}
