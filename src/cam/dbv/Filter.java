package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Filter {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Filter filter = new Filter();

	}

	enum FilterType {
		ABLEITUNGx("Ableitung x", 1, 3, 1, 0, -1), //
		ABLEITUNGy("Ableitung y", 3, 1, 1, 0, -1), //
		ROBERTSCROSSp("Roberts cross +", 2, 2, 0, -1, 1, 0), //
		ROBERTSCROSSn("Roberts cross -", 2, 2, -1, 0, 0, 1), //
		PREWITTh("Prewitt h", 3, 3, 1, 0, -1, 1, 0, -1, 1, 0, -1), //
		PREWITTv("Prewitt v", 3, 3, 1, 0, -1, 1, 0, -1, 1, 0, -1), //
		SOBELh("Sobel h", 3, 3, 1, 0, -1, 2, 0, -2, 1, 0, -1), //
		SOBELv("Sobel v", 3, 3, 1, 2, 1, 0, 0, 0, -1, -2, -1), //
		LAPLACE1("Laplace ", 3, 3, 0, 1, 0, 1, -4, 1, 0, 1, 0), //
		LAPLACE2("Laplace ", 3, 3, 1, 1, 1, 1, -8, 1, 1, 1, 1);

		String name;
		int rows;
		int cols;
		int[] data;

		private FilterType(String name, int rows, int cols, int... filter) {
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

	}

	static String testImage = "lena.png";

	Mat image;

	public Filter() {
		image = Highgui.imread("lena.png");
		convolve();
	}

	private void convolve() {
		Mat filtered1 = new Mat();
		Mat filtered2 = new Mat();
		Imgproc.filter2D(image, filtered1, -1, FilterType.LAPLACE1.toMat());
		Imgproc.filter2D(image, filtered2, -1, FilterType.LAPLACE2.toMat());
		new ImShow("Filter 1", filtered1);
		Imgproc.cvtColor(filtered2, filtered2, Imgproc.COLOR_RGB2GRAY);
		new ImShow("Filter 2", filtered2);
		// System.out.println(filtered1.dump());

	}

}
