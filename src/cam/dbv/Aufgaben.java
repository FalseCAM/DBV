package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Aufgaben {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Aufgaben a = new Aufgaben();
		a.aufgabeSpectra2();
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
		Mat image = TestImage.BOAT.toMat();
		new ImShow(image, "Original", 512, 512);
		Spectrum spectrum = new Spectrum(image);
		Mat amplitude = spectrum.getVisualizeableAmplitude();
		System.out.println(spectrum.getAmplitude().dump());
		new ImShow(amplitude, "Amplitude", 512, 512);
		Mat phase = spectrum.getVisualizeablePhase();
		System.out.println(spectrum.getPhase().dump());
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

}
