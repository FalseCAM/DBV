package cam.dbv;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImShow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BufferedImage bufImage = null;

	public ImShow(String title, Mat image) {
		Mat image_tmp = image;

		MatOfByte matOfByte = new MatOfByte();

		Highgui.imencode(".png", image_tmp, matOfByte);

		byte[] byteArray = matOfByte.toArray();

		try {

			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new JLabel(new ImageIcon(bufImage)));
		this.setTitle(title + " - " + image.toString());
		pack();
		this.setVisible(true);
	}

}
