package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public enum Filter {
	DERIVATIVEx("Derivative x", 1, 3, 1, 0, -1), //
	DERIVATIVEy("Derivative y", 3, 1, 1, 0, -1), //
	ROBERTSCROSSp("Roberts cross +", 2, 2, 0, -1, 1, 0), //
	ROBERTSCROSSn("Roberts cross -", 2, 2, -1, 0, 0, 1), //
	PREWITTh("Prewitt h", 3, 3, 1, 0, -1, 1, 0, -1, 1, 0, -1), //
	PREWITTv("Prewitt v", 3, 3, 1, 1, 1, 0, 0, 0, -1, -1, -1), //
	SOBELh("Sobel h", 3, 3, 1, 0, -1, 2, 0, -2, 1, 0, -1), //
	SOBELv("Sobel v", 3, 3, 1, 2, 1, 0, 0, 0, -1, -2, -1), //
	LAPLACE1("Laplace ", 3, 3, 0, 1, 0, 1, -4, 1, 0, 1, 0), //
	LAPLACE2("Laplace ", 3, 3, 1, 1, 1, 1, -8, 1, 1, 1, 1), //
	KIRSCH("Kirsch", 3, 3, 5, 5, 5, -3, 0, -3, -3, -3, -3);

	String name;
	int rows;
	int cols;
	int[] data;

	private Filter(String name, int rows, int cols, int... filter) {
		this.name = name;
		this.rows = rows;
		this.cols = cols;
		this.data = filter;
	}

	public Mat toMat() {
		Mat mat = new Mat(rows, cols, CvType.CV_32S);
		mat.put(0, 0, this.data);
		return mat;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(": {\n");
		sb.append(toMat().dump());
		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * 
	 * @param image
	 *            to filter and show
	 * @param filter
	 *            to use
	 */
	public static void showFiltered(Mat image, Filter filter) {
		Mat filtered = DBV.convolve(image, filter.toMat());
		new ImShow(filtered, filter.toString() + " - " + filtered.toString());
	}

	public static void showFiltered(Mat image, Filter filter, boolean rotate) {
		if (rotate) {
			Mat filtered = DBV.convolveRotate(image, filter.toMat());
			new ImShow(filtered, filter.toString() + " - "
					+ filtered.toString());
		} else {
			showFiltered(image, filter);
		}
	}

	/**
	 * 
	 * @param size
	 * @param sigma
	 * @return Gaussian Filter Matrix
	 */
	public static Mat getGaussianFilter(int size, double sigma) {
		double gaussianFilter1[] = new double[size];
		for (int i = 0; i < size; i++) {
			double dividend = -Math.pow(-(int) ((size - 1) / 2) + i, 2);
			double divisor = 2 * Math.pow(sigma, 2);
			gaussianFilter1[i] = Math.exp(dividend / divisor);
		}

		Mat gaussianFilter = new Mat(1, size, CvType.CV_32F);
		gaussianFilter.put(0, 0, gaussianFilter1);

		Mat gaussianFilter2 = gaussianFilter.t();

		Core.gemm(gaussianFilter2, gaussianFilter, 1, new Mat(), 0,
				gaussianFilter);
		Scalar value = Core.sumElems(gaussianFilter);
		Core.divide(gaussianFilter, value, gaussianFilter);
		return gaussianFilter;
	}
}
