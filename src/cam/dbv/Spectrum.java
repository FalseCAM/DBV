package cam.dbv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class Spectrum {

	private Mat image;
	private Mat spectrum;
	private Mat phase;
	private Mat amplitude;

	public Spectrum(Mat image) {
		setImage(image);
	}

	public Spectrum(Mat amplitude, Mat phase) {
		this.image = new Mat();
		this.amplitude = amplitude.clone();
		this.phase = phase.clone();
		if (amplitude.type() != phase.type()) {
			this.amplitude.convertTo(amplitude, CvType.CV_64F);
			this.phase.convertTo(phase, CvType.CV_64F);
		}
		calcImage();
	}

	// amplitude * exp(j*phase)
	private void calcImage() {
		Mat image = new Mat(amplitude.size(), CvType.CV_64F);
		Mat cPhase = phase.clone();

		cPhase = DBV.complex(cPhase, false);
		cPhase = DBV.exp(cPhase);
		Mat cAmp = amplitude.clone();
		cAmp = DBV.complex(cAmp, true);
		image = DBV.multiply(cAmp, cPhase);
		image = DBV.idft(image);
		this.image = DBV.real(image);
	}

	private void calcSpectrum() {
		this.spectrum = DBV.dft(this.image);
	}

	private void calcAmplitude() {
		Mat dft = DBV.dft(image);
		this.amplitude = DBV.abs(dft);
	}

	private void calcPhase() {
		Mat dft = DBV.dft(image);
		this.phase = DBV.angle(dft);
	}

	public void setImage(Mat image) {
		this.image = image.clone();
		calcSpectrum();
		calcAmplitude();
		calcPhase();
	}

	public Mat getImage() {
		return image;
	}

	public void setSpectrum(Mat spectrum) {
		this.spectrum = spectrum;
		setImage(DBV.idft(spectrum));
	}

	public Mat getSpectrum() {
		return spectrum;
	}

	public void setAmplitude(Mat amplitude) {
		this.amplitude = amplitude.clone();
		calcImage();
		calcSpectrum();
	}

	public Mat getAmplitude() {
		return amplitude;
	}

	public void setPhase(Mat phase) {
		this.phase = phase.clone();
		calcImage();
		calcSpectrum();
	}

	public Mat getPhase() {
		return phase;
	}

	public Mat getVisualizeableAmplitude() {
		Mat amplitude = getAmplitude().clone();
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

	public void reshapeAmplitude(Rect rect) {
		Mat tempAmp = DBV.dftShift(this.amplitude);
		Mat reshaped = Mat.zeros(this.amplitude.size(), this.amplitude.type());
		tempAmp.submat(rect).copyTo(reshaped.submat(rect));
		setAmplitude(DBV.dftShift(reshaped));
	}

}
