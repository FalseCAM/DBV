package cam.dbv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Rect;

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

	/**
	 * 
	 * @param mat
	 * @return elementwise absolute value of a matrix
	 */
	public static Mat abs(Mat mat) {
		Mat temp = new Mat();
		Core.absdiff(mat, Scalar.all(0), temp);
		return temp;
	}

	public static Mat angle(Mat mat) {
		Mat temp = new Mat(mat.size(), CvType.CV_32F);

		List<Mat> chan = new ArrayList<Mat>();
		Core.split(mat, chan);
		double[] im = new double[(int) mat.total()];
		double[] re = new double[(int) mat.total()];
		double[] value = new double[(int) mat.total()];

		chan.get(0).get(0, 0, re);
		chan.get(1).get(0, 0, im);
		for (int i = 0; i < mat.total(); i++) {
			value[i] = Math.atan2(im[i], re[i]);
		}

		temp.put(0, 0, value);

		return temp;
	}

	/**
	 * 
	 * @param mat
	 * @return elementwise logarithm value of a matrix
	 */
	public static Mat log(Mat mat) {
		Mat temp = new Mat();
		Core.log(mat, temp);
		return temp;
	}

	public static Mat dft(Mat img) {
		Mat input = img.clone();
		int M = Core.getOptimalDFTSize(input.rows());
		int N = Core.getOptimalDFTSize(input.cols());

		Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);

		List<Mat> planes = new ArrayList<Mat>();
		Mat padded = new Mat();
		input.convertTo(input, CvType.CV_64F);
		Imgproc.copyMakeBorder(input, padded, 0, M - input.rows(), 0,
				N - input.cols(), Imgproc.BORDER_CONSTANT);
		planes.add(input.clone());
		planes.add(padded);
		Mat complexImg = new Mat();
		Core.merge(planes, complexImg);
		Mat ret = new Mat();
		Core.dft(complexImg, ret, 0, input.rows());
		return ret;
	}

	public static Mat dftShift(Mat input) {
		Mat temp = Mat.zeros(input.size(), input.type());
		Size size = input.size();
		int cx = (int) (size.width / 2);
		int cy = (int) (size.height / 2); // image center
		Rect q1 = new Rect(cx, 0, cx, cy);
		Rect q2 = new Rect(0, 0, cx, cy);
		Rect q3 = new Rect(0, cy, cx, cy);
		Rect q4 = new Rect(cx, cy, cx, cy);

		input.submat(q1).copyTo(temp.submat(q3));
		input.submat(q2).copyTo(temp.submat(q4));
		input.submat(q3).copyTo(temp.submat(q1));
		input.submat(q4).copyTo(temp.submat(q2));
		return temp;
	}

}
