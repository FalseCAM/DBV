package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class DBV {

	/**
	 * 
	 * @param image
	 *            The image to filter
	 * @param filter
	 *            The filter to use
	 * @return a filtered image
	 */
	public static Mat convolve(Mat image, Mat filter) {
		Mat filtered = new Mat();
		Imgproc.filter2D(image, filtered, -1, filter);
		return filtered;
	}

	public static Mat harrisPoints(Mat image, double k) {
		StructureTensor sT = new StructureTensor(image);
		Mat detA = sT.getIx2().mul(sT.getIy2());
		Core.subtract(detA, sT.getIxy().mul(sT.getIxy()), detA);
		Mat K = new Mat();
		Core.add(sT.getIx2(), sT.getIy2(), K);
		Core.multiply(K, K, K, k);

		Mat R = new Mat();
		Core.subtract(detA, K, R);
		return R;
	}

}
