package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class PassFilter {

	public static Mat getDistances(int x, int y) {
		double u = (x - 1) / 2f;
		double v = (y - 1) / 2f;
		double[] D = new double[x * y];
		int n = 0;
		for (double i = -v; i <= v; i++) {

			for (double j = -u; j <= u; j++) {

				D[n] = Math.sqrt(i * i + j * j);
				n++;
			}
		}
		Mat ret = new Mat(y, x, CvType.CV_64F);
		ret.put(0, 0, D);
		return ret;
	}

	public static Mat getIdealLowpass(Mat distances, int cutoffFreq) {
		Mat ret = distances.clone();
		double[] d = new double[(int) distances.total()];
		distances.get(0, 0, d);
		for (int i = 0; i < d.length; i++) {
			d[i] = d[i] <= cutoffFreq ? 1 : 0;
		}
		ret.put(0, 0, d);
		return ret;
	}

	public static Mat getIdealHighpass(Mat distances, int cutoffFreq) {
		Mat d = getIdealLowpass(distances, cutoffFreq);
		Core.multiply(d, Scalar.all(-1), d);
		Core.add(d, Scalar.all(1), d);
		return d;
	}

	public static Mat getGaussianLowpass(Mat distances, int cutoffFreq) {
		double[] d = new double[(int) distances.total()];
		Mat ret = distances.clone();
		distances.get(0, 0, d);
		for (int i = 0; i < d.length; i++) {
			d[i] = Math.exp((-Math.pow(d[i], 2))
					/ (2.0 * Math.pow(cutoffFreq, 2)));
		}
		ret.put(0, 0, d);
		return ret;
	}

	public static Mat getGaussianHighpass(Mat distances, int cutoffFreq) {
		Mat d = getGaussianLowpass(distances, cutoffFreq);
		Core.multiply(d, Scalar.all(-1), d);
		Core.add(d, Scalar.all(1), d);
		return d;
	}

	public static Mat getButterworthLowpass(Mat distances, int cutoffFreq,
			int order) {
		Mat ret = distances.clone();
		double[] d = new double[(int) distances.total()];
		distances.get(0, 0, d);
		for (int i = 0; i < d.length; i++) {
			d[i] = 1.0 / (1.0 + Math.pow((d[i] / cutoffFreq), (2.0 * order)));
		}
		ret.put(0, 0, d);
		return ret;
	}

	public static Mat getButterworthHighpass(Mat distances, int cutoffFreq,
			int order) {
		Mat d = getButterworthLowpass(distances, cutoffFreq, order);
		Core.multiply(d, Scalar.all(-1), d);
		Core.add(d, Scalar.all(1), d);
		return d;
	}

}
