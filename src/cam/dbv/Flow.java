package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Flow {

	private Mat image1;
	private Mat image2;
	private Mat U;
	private Mat V;

	public Flow(Mat image1, Mat image2) {
		this.image1 = image1.clone();
		this.image2 = image2.clone();
		if (image1.channels() > 1)
			Imgproc.cvtColor(this.image1, this.image1, Imgproc.COLOR_RGB2GRAY);
		if (image2.channels() > 1)
			Imgproc.cvtColor(this.image2, this.image2, Imgproc.COLOR_RGB2GRAY);

		computeLucasKanadeFlow();
	}

	private void computeLucasKanadeFlow() {

		Mat It = difference();

		Mat Ixt = new Mat();
		Mat Iyt = new Mat();

		Mat IxIt = new Mat();
		Mat IyIt = new Mat();

		Mat gaussianFilter = Filter.getGaussianFilter(11, 2);

		Mat GIxIt = DBV.convolve(IxIt, gaussianFilter);
		Mat GIyIt = DBV.convolve(IyIt, gaussianFilter);

		StructureTensor sTI1 = new StructureTensor(image1);

		double alpha = 0.0001;
		double[] AValue = new double[image1.width() * image1.height()];
		for (int i = 0; i < image1.height(); i++) {
			for (int j = 0; j < image1.width(); j++) {

			}
		}

	}

	public Mat difference() {
		Mat difference = new Mat();
		Core.subtract(image1, image2, difference);
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
		return U;
	}

	public Mat getV() {
		return V;
	}
}
