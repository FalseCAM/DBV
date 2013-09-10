package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Aufgaben {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Aufgaben a = new Aufgaben();
		a.aufgabeFilterRotate();
	}

	public Aufgaben() {

	}

	public void aufgabeFilter() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		// Gauss
		Mat gaussian = Filter.getGaussianFilter(5, 1.8);
		Mat filtered = DBV.convolve(image, gaussian);
		new ImShow(filtered, "Gaussian Filter - " + filtered.toString());

		Mat gaussian2 = Filter.getGaussianFilter(5, 6.8);
		Mat filtered2 = DBV.convolve(image, gaussian2);
		new ImShow(filtered2, "Gaussian Filter - " + filtered2.toString());
		// others

		Filter.showFiltered(image, Filter.LAPLACE1);
		Filter.showFiltered(image, Filter.PREWITTv);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		Filter.showFiltered(image, Filter.SOBELh);
	}

	public void aufgabeFilterNeighbour() {
		Mat image = TestImage.TEXTSTRUCTURE.toMat();
		new ImShow(image, "Original");

		Filter.showFiltered(image, Filter.DERIVATIVEx);
		Filter.showFiltered(image, Filter.DERIVATIVEy);
		Filter.showFiltered(image, Filter.ROBERTSCROSSn);
		Filter.showFiltered(image, Filter.ROBERTSCROSSp);
		System.out.println(Filter.ROBERTSCROSSn.toMat().dump());
		System.out.println(Filter.ROBERTSCROSSp.toMat().dump());
	}

	public void aufgabeFilterRotate() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		Filter.showFiltered(image, Filter.KIRSCH);
		Filter.showFiltered(image, Filter.KIRSCH, true);
		Filter.showFiltered(image, Filter.LAPLACE1, true);
	}

	public void aufgabeSharpen() {
		Mat image = TestImage.MOON.toMat();
		new ImShow(image, "Original");
		Mat imageFiltered = DBV.convolve(image, Filter.LAPLACE2.toMat());
		new ImShow(imageFiltered, "Image filtered");
		Mat imageSharpened = new Mat();
		Core.subtract(image, imageFiltered, imageSharpened);
		new ImShow(imageSharpened, "Image sharpened");
	}

	public void aufgabeHarris() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		Mat harris = DBV.harrisPoints(image, 0.125);
		new ImShow(harris, "Harris Points");
	}

	public void aufgabeSpectra() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original", 512, 512);
		Spectrum spectrum = new Spectrum(image);
		Mat amplitude = spectrum.getVisualizeableAmplitude();
		new ImShow(amplitude, "Amplitude", 512, 512);
		Mat phase = spectrum.getVisualizeablePhase();
		new ImShow(phase, "Phase", 512, 512);
	}

	public void aufgabeTest2() {
		Mat image = new Mat(4, 4, CvType.CV_64F);
		image.put(0, 0, 1, 2, 3, 4, 2, 3, 4, 5, 3, 4, 5, 6, 4, 5, 6, 7);
		Mat amp = new Mat(2, 2, CvType.CV_64F);
		amp.put(0, 0, 1, 2, 3, 4);
		Mat ph = new Mat(2, 2, CvType.CV_64F);
		ph.put(0, 0, 2, 3, 4, 5);
		Spectrum spectrum = new Spectrum(image);
		Mat amplitude = spectrum.getVisualizeableAmplitude();
		new ImShow(amplitude, "Amplitude", 512, 512);
		Mat phase = spectrum.getVisualizeablePhase();
		new ImShow(phase, "Phase", 512, 512);
		Spectrum a1p1 = new Spectrum(amp, ph);

		new ImShow(a1p1.getImage(), "Amplitude Image 1, Phase Image 1", 512,
				512);
	}

	public void aufgabeSpectra2() {
		Mat img1 = TestImage.LENA.toMat();
		Mat img2 = TestImage.BOAT.toMat();
		new ImShow(img1, "Original1");
		new ImShow(img2, "Original2");
		Spectrum s1 = new Spectrum(img1);
		Spectrum s2 = new Spectrum(img2);

		Spectrum a1p1 = new Spectrum(s1.getAmplitude(), s1.getPhase());
		new ImShow(a1p1.getImage(), "Amplitude Image 1, Phase Image 1", 512,
				512);

		Spectrum a1p2 = new Spectrum(s1.getAmplitude(), s2.getPhase());
		new ImShow(a1p2.getImage(), "Amplitude Image 1, Phase Image 2", 512,
				512);
		Spectrum a2p1 = new Spectrum(s2.getAmplitude(), s1.getPhase());
		new ImShow(a2p1.getImage(), "Amplitude Image 2, Phase Image 1", 512,
				512);

	}

	public void aufgabeReshapedSpectra() {
		Mat img1 = TestImage.LENA.toMat();
		new ImShow(img1, "Original1");
		Spectrum s1 = new Spectrum(img1);
		int w = (int) img1.size().width;
		int h = (int) img1.size().height;

		new ImShow(s1.getVisualizeableAmplitude(), "Amplitude", 512, 512);

		s1.reshapeAmplitude(new Rect(w / 8, h / 8, 6 * w / 8, 6 * h / 8));

		new ImShow(s1.getVisualizeableAmplitude(), "reshaped Amplitude", 512,
				512);
		Spectrum a1p1 = new Spectrum(s1.getAmplitude(), s1.getPhase());

		new ImShow(a1p1.getImage(), "Result", 512, 512);

	}

	public void aufgabeDumpSpectra() {
		Mat img1 = TestImage.LENAoverlay.toMat();
		// new ImShow(img1, "Original1");
		Spectrum s1 = new Spectrum(img1);
		int w = (int) img1.size().width;
		int h = (int) img1.size().height;
		Spectrum a2p2 = new Spectrum(s1.getAmplitude(), s1.getPhase());
		Mat amp = s1.getAmplitude();
		new ImShow(s1.getVisualizeableAmplitude(), "Amplitude", 1024, 1024);
		Mat gaussian = Filter.getGaussianFilter(15, 2.5);
		Mat filtered = DBV.convolve(amp, gaussian);
		Spectrum a1p1 = new Spectrum(filtered, s1.getPhase());
		// new ImShow(a1p1.getImage(), "Result filtered", 512, 512);

		Mat sAmp = s1.getAmplitude();
		Core.MinMaxLocResult res = Core.minMaxLoc(sAmp);
		System.out.println("Amplitude max Value " + res.maxVal
				+ " at Position " + res.maxLoc + " (not shifted)");
		Mat sMax = new Mat();
		Core.min(sAmp, Scalar.all(120), sMax);
		sMax = sAmp;
		sMax.convertTo(sAmp, CvType.CV_32F);

		// // ???????? ///

		// Imgproc.threshold(sMax, sMax, 80, 80, Imgproc.THRESH_TRUNC);
		Spectrum a3p3 = new Spectrum(sMax, s1.getPhase());
		new ImShow(a3p3.getVisualizeableAmplitude(), "Amplitude cropped", 1024,
				1024);
		new ImShow(a3p3.getImage(), "Result cropped", 512, 512);

	}

}
