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

	private ImShow() {

	}

	static BufferedImage matToImage(Mat image) {
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

	public static void show(Mat img) {
		show(img, "");

	}

	public static void show(Mat img, String title) {
		show(img, title, img.width(), img.height());
	}

	public static void show(Mat img, String title, boolean normalize) {
		show(img, title, img.width(), img.height(), normalize);
	}

	public static void showDoubled(Mat img, String title, boolean normalize) {
		show(img, title, img.width() * 2, img.height() * 2, normalize);
	}

	public static void show(Mat img, String title, int width, int height) {
		show(img, title, width, height, false);
	}

	public static void show(Mat img, String title, int width, int height,
			boolean normalize) {
		Mat image = img.clone();
		if (normalize) {
			Core.normalize(image, image, 0, 255, Core.NORM_MINMAX);
		}

		BufferedImage bimg = matToImage(image);

		ImShow window = new ImShow();
		window.setTitle(title);
		window.showImage(bimg
				.getScaledInstance(width, height, Image.SCALE_FAST));
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);

	}

}
