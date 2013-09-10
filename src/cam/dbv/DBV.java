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

	public static Mat convolveRotate(Mat image, Mat filter) {
		int rotations = 7;
		if (filter.width() != 3 || filter.height() != 3) {
			return image;
		}
		//Mat ret = Mat.zeros(image.size(), image.type());
		Mat ret = image.clone();
		for (int i = 0; i < rotations; i++) {
			Mat f = rotate3x3Mat(filter, i);
			//Core.add(ret, convolve(image, f), ret);
			ret = convolve(ret, f);
		}
		//Core.divide(ret, Scalar.all(rotations), ret);
		return ret;
	}

	public static Mat rotate3x3Mat(Mat mat, int step) {
		Mat copy = mat.clone();
		copy.convertTo(copy, CvType.CV_64F);
		Mat ret = new Mat(3, 3, CvType.CV_64F);
		double[] matV = new double[9];
		copy.get(0, 0, matV);
		double[] v = matV;
		for (int i = 0; i < step; i++) {

			v = new double[] { v[3], v[0], v[1], v[6], v[4], v[2], v[7], v[8],
					v[5] };
		}
		ret.put(0, 0, v);
		return ret;
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
		Mat ret = new Mat();

		// log(z) = log(abs(z)) + 1i*atan2(y,a)
		if (mat.channels() == 2) {
			Mat x = new Mat();
			Mat y = new Mat();
			Core.extractChannel(mat, x, 0);
			Core.extractChannel(mat, y, 1);

			Mat absz = abs(mat);
			Core.log(absz, absz);

			Mat atan = angle(mat);

			List<Mat> planes = new ArrayList<Mat>();
			planes.add(absz);
			planes.add(atan);
			Core.merge(planes, ret);
		} else {
			Core.log(mat, ret);
		}
		return ret;

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

	public static Mat multiply(Mat source1, Mat source2) {
		Mat src1 = source1.clone();
		Mat src2 = source2.clone();
		Mat ret = new Mat();
		if (src1.channels() == 2) {
			if (src2.channels() == 1) {
				List<Mat> planes = new ArrayList<Mat>();
				planes.add(src2);
				planes.add(src2);
				Core.merge(planes, src2);
			}
			Core.mulSpectrums(src1, src2, ret, 0);
		} else {
			Core.multiply(src1, src2, ret);
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
		return idft(img, false);
	}

	public static Mat idft(Mat img, boolean scale) {
		Mat input = img.clone();
		Mat ret = new Mat();

		if (scale) {
			Core.dft(input, ret, Core.DFT_INVERSE | Core.DFT_SCALE
					| Core.DFT_REAL_OUTPUT, 0);
		} else {
			Core.dft(input, ret, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT, 0);
		}

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
			Mat ret = new Mat();
			Core.extractChannel(input, ret, 0);
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
			Mat ret = new Mat();
			Core.extractChannel(input, ret, 1);
			return ret;
		} else {
			return input;
		}
	}

	public static Mat complex(Mat input, boolean realInput) {
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

	public static Mat padded(Mat input, int size) {
		Mat ret = input.clone();
		Imgproc.copyMakeBorder(ret, ret, size, size, size, size,
				Imgproc.BORDER_CONSTANT);
		return ret;
	}

}
