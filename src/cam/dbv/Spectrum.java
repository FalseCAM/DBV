package cam.dbv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Spectrum {

	private Mat image;

	public Spectrum(Mat image) {
		this.image = image;
	}

	public Mat getAmplitude() {

		Mat dft = ConvertToDFT(image);
		return dft;

	}

	public Mat getPhase() {
		return image;

	}

	Mat ConvertToDFT(Mat input) {
		int N = input.cols() * 2;
		int M = input.rows() * 2;

		Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2GRAY);

		List<Mat> planes = new ArrayList<Mat>();
		Mat padded = new Mat();
		input.convertTo(input, CvType.CV_64F);
		Imgproc.copyMakeBorder(input, padded, 0, M - input.rows(), 0,
				N - input.cols(), Imgproc.BORDER_CONSTANT);
		planes.add(padded);
		Mat complexImg = new Mat();
		Core.merge(planes, complexImg);
		Mat ret = new Mat();
		Core.dft(complexImg, ret, 0, input.rows());
		ret.convertTo(ret, 0);
		return ret;
	}

}
