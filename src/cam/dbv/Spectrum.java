package cam.dbv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class Spectrum {

	private Mat image;
	private Mat phase;
	private Mat amplitude;

	public Spectrum(Mat image) {
		this.image = image;
		calcAmplitude();
		calcPhase();
	}

	public Spectrum(Mat amplitude, Mat phase) {
		this.image = new Mat();
		this.amplitude = amplitude;
		this.phase = phase;
		calcImage();
	}

	private void calcImage() {
		Mat cPhase = phase.clone();

		cPhase = DBV.toComplexMat(cPhase, false);
		cPhase = DBV.exp(cPhase);
		Mat cAmp = amplitude.clone();
		cAmp = DBV.toComplexMat(cAmp, true);
		Core.multiply(cAmp, cPhase, this.image);
		this.image = DBV.idft(this.image);
	}

	private void calcAmplitude() {
		Mat dft = DBV.dft(image);
		this.amplitude = DBV.abs(dft);
	}

	private void calcPhase() {
		Mat dft = DBV.dft(image);
		this.phase = DBV.angle(dft);
	}

	public Mat getImage() {
		return image;
	}

	public Mat getAmplitude() {
		return amplitude;
	}

	public Mat getPhase() {
		return phase;
	}

	public Mat getVisualizeableAmplitude() {
		Mat amplitude = getAmplitude();
		amplitude.convertTo(amplitude, CvType.CV_64F);
		Core.add(amplitude, Scalar.all(1), amplitude);
		amplitude = DBV.log(amplitude);
		amplitude = DBV.dftShift(amplitude);
		Core.normalize(amplitude, amplitude, 0, 255, Core.NORM_MINMAX);
		List<Mat> chan = new ArrayList<Mat>();
		Core.split(amplitude, chan);
		return chan.get(0);
	}

	public Mat getVisualizeablePhase() {
		Mat phase = getPhase();
		phase = DBV.dftShift(phase);
		Core.normalize(phase, phase, 0, 255, Core.NORM_MINMAX);
		return phase;
	}

}
