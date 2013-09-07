package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Aufgaben {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Aufgaben a = new Aufgaben();
		a.aufgabeSpectra();

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
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		Spectrum spectrum = new Spectrum(image);
		Mat amplitude = spectrum.getAmplitude();
		new ImShow(amplitude, "Amplitude", 512, 512);
	}

}
