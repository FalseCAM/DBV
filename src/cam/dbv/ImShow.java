package cam.dbv;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class ImShow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Mat image;

	public ImShow(Mat image, String title) {

		this.image = image;
		BufferedImage img = null;
		img = matToImage(DBV.real(image));

		showImage(img);

		this.setTitle(title);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	public ImShow(Mat image, String title, int width, int height) {

		BufferedImage img = matToImage(image);

		showImage(img.getScaledInstance(width, height, Image.SCALE_FAST));

		this.setTitle(title);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	public ImShow(Mat image, String title, int width, int height,
			boolean normalize) {
		Mat image2 = image.clone();
		if (normalize) {
			Core.normalize(image2, image2, 0, 255, Core.NORM_MINMAX);
		}
		BufferedImage img = matToImage(image2);

		showImage(img.getScaledInstance(width, height, Image.SCALE_FAST));

		this.setTitle(title);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	BufferedImage matToImage(Mat image) {
		Mat image_tmp = image.clone();

		MatOfByte matOfByte = new MatOfByte();

		Highgui.imencode(".png", image_tmp, matOfByte);

		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try {

			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}

	void showImage(Image img) {
		setLayout(new BorderLayout());
		add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
		// add(new JScrollPane(new JTextArea(image.dump())),
		// BorderLayout.SOUTH);
		pack();
	}

}
