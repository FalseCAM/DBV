package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

public class Flow {

	private Mat image1;
	private Mat image2;
	private Mat u;
	private Mat v;

	public Flow(Mat image1, Mat image2) {
		this.image1 = image1.clone();
		this.image2 = image2.clone();
		if (image1.channels() > 1)
			Imgproc.cvtColor(this.image1, this.image1, Imgproc.COLOR_RGB2GRAY);
		if (image2.channels() > 1)
			Imgproc.cvtColor(this.image2, this.image2, Imgproc.COLOR_RGB2GRAY);

		computeLucasKanadeFlow();
	}

	public Mat lucasKanadeFlow() {
		int MAX_CORNERS = 500;

		Mat ret = new Mat();
		return ret;
	}

	private void computeLucasKanadeFlow() {
		int window_size = 10;

		Mat filter1 = new Mat(2, 2, CvType.CV_32F);
		filter1.put(0, 0, -.25, .25, -.25, .25);
		Mat filter2 = new Mat(2, 2, CvType.CV_32F);
		filter2.put(0, 0, -.25, -.25, .25, .25);
		Mat filter3 = new Mat(2, 2, CvType.CV_32F);
		filter3.put(0, 0, -.25, -.25, -.25, -.25);
		// derivates
		Mat fx = new Mat();
		Core.add(DBV.convolve(image1, filter1), DBV.convolve(image2, filter1),
				fx);
		Mat fy = new Mat();
		Core.add(DBV.convolve(image1, filter2), DBV.convolve(image2, filter2),
				fy);
		Mat ft = new Mat();
		Core.add(DBV.convolve(image1, filter3), DBV.convolve(image2, filter3),
				ft);

		// optical flow
		int window_center = window_size / 2;
		Size image_size = image1.size();
		u = Mat.zeros(image_size, CvType.CV_32F);
		v = Mat.zeros(image_size, CvType.CV_32F);

		for (int i = window_center + 1; i < image_size.width - window_center; i++) {
			for (int j = window_center + 1; j < image_size.height
					- window_center; j++) {

			}
		}

	}

	public Mat difference() {
		Mat difference = new Mat();
		Core.subtract(image1, image2, difference);
		return difference;

	}

	public Mat quotient() {
		Mat difference = new Mat();
		Core.divide(image1, image2, difference);
		return difference;

	}

	public Mat phaseDifference() {
		Spectrum s1 = new Spectrum(image1);
		Spectrum s2 = new Spectrum(image2);
		// s1.setAmplitude(Mat.ones(image1.size(), CvType.CV_64F));
		s1.setAmplitude(s1.getAmplitude());
		s2.setAmplitude(s1.getAmplitude());

		Mat ret = new Mat();
		Core.subtract(s1.getImage(), s2.getImage(), ret);
		return ret;
	}

	public Mat getU() {
		return u;
	}

	public Mat getV() {
		return v;
	}
}
