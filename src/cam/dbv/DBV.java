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
		Mat r = real(mat);
		Mat i = imag(mat);
		// r^2
		Core.multiply(r, r, r);
		Core.multiply(i, i, i);
		Core.add(r, i, temp);
		Core.sqrt(temp, temp);

		// Core.absdiff(mat, Scalar.all(0), temp);
		return temp;
	}

	/**
	 * 
	 * @param mat
	 * @return
	 */
	public static Mat angle(Mat mat) {
		Mat temp = new Mat(mat.size(), CvType.CV_64F);
		double[] im = new double[(int) mat.total()];
		double[] re = new double[(int) mat.total()];
		double[] value = new double[(int) mat.total()];

		real(mat).get(0, 0, re);
		imag(mat).get(0, 0, im);
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

	/**
	 * 
	 * @param mat
	 * @return e^mat
	 */
	public static Mat exp(Mat mat) {
		Mat ret = new Mat();
		// complex: e^z = e^x*(cis(y)), where z = x + i*y
		if (mat.channels() == 2) {
			Mat x = new Mat();
			Mat y = new Mat();
			Core.extractChannel(mat, x, 0);
			Core.extractChannel(mat, y, 1);
			Mat cis = cis(y);
			Mat e_x = new Mat();
			Core.exp(x, e_x);

			Mat r = new Mat();
			Mat i = new Mat();
			Core.extractChannel(cis, r, 0);
			Core.extractChannel(cis, i, 1);
			Core.multiply(e_x, r, r);
			Core.multiply(e_x, i, i);

			List<Mat> planes = new ArrayList<Mat>();
			planes.add(r);
			planes.add(i);
			Mat e_z = new Mat();
			Core.merge(planes, e_z);
			return e_z;
		} else {
			Core.exp(mat, ret);
		}
		return ret;
	}

	/**
	 * cis = cos + i*sin
	 * 
	 * @param mat
	 * @return
	 */
	public static Mat cis(Mat mat) {
		Mat cos = new Mat(mat.size(), CvType.CV_64F);
		Mat sin = new Mat(mat.size(), CvType.CV_64F);

		double[] c = new double[(int) mat.total()];
		double[] s = new double[(int) mat.total()];
		double[] value = new double[(int) mat.total()];
		mat.get(0, 0, value);
		for (int i = 0; i < mat.total(); i++) {
			c[i] = Math.cos(value[i]);
			s[i] = Math.sin(value[i]);
		}
		cos.put(0, 0, c);
		sin.put(0, 0, s);

		List<Mat> planes = new ArrayList<Mat>();
		planes.add(cos);
		planes.add(sin);
		Mat complexImg = new Mat();
		Core.merge(planes, complexImg);
		return complexImg;
	}

	public static Mat dft(Mat img) {
		return dft(img, true);
	}

	public static Mat dft(Mat img, boolean scale) {
		Mat input = img.clone();
		if (input.channels() == 3) {
			Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY);
		}
		input.convertTo(input, CvType.CV_64F);
		Mat fourierTransform = new Mat();
		if (scale) {
			Core.dft(input, fourierTransform, Core.DFT_SCALE
					| Core.DFT_COMPLEX_OUTPUT, 0);
		} else {
			Core.dft(input, fourierTransform, Core.DFT_COMPLEX_OUTPUT, 0);
		}
		return fourierTransform;

	}

	public static Mat idft(Mat img) {
		Mat input = img.clone();
		Mat ret = new Mat();
		Core.dft(input, ret, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT, 0);
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

	/**
	 * 
	 * @param input
	 * @return real values of a complex matrix
	 */
	public static Mat real(Mat input) {
		if (input.channels() == 2) {
			List<Mat> chan = new ArrayList<Mat>();
			Core.split(input, chan);
			Mat ret = chan.get(0);
			return ret;
		} else {
			return input;
		}
	}

	/**
	 * 
	 * @param input
	 * @return complex values of a complex matrix
	 */
	public static Mat imag(Mat input) {
		if (input.channels() == 2) {
			List<Mat> chan = new ArrayList<Mat>();
			Core.split(input, chan);
			Mat ret = chan.get(1);
			// ret.convertTo(ret, CvType.CV_64F);
			return ret;
		} else {
			return input;
		}
	}

	public static Mat toComplexMat(Mat input, boolean realInput) {
		List<Mat> planes = new ArrayList<Mat>();

		if (realInput) {
			Mat padded = Mat.zeros(input.size(), input.type());
			planes.add(input.clone());
			planes.add(padded);
		} else {
			Mat padded = Mat.zeros(input.size(), input.type());
			planes.add(padded);
			planes.add(input.clone());
		}
		Mat complexImg = new Mat();
		Core.merge(planes, complexImg);
		return complexImg;
	}

}
