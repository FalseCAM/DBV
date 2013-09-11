package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Aufgaben {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Aufgaben a = new Aufgaben();
		a.halbbild();
	}

	public Aufgaben() {

	}

	public void filter() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		// Gauss
		// image = DBV.convolve(image, Filter.KIRSCH.toMat());

		Mat gaussian = Filter.getGaussianFilter(9, 1.8);
		Mat filtered = DBV.convolve(image, gaussian);
		new ImShow(filtered, "Gaussian Filter - " + filtered.toString());

		Mat gaussian2 = Filter.getGaussianFilter(9, 500);
		Mat filtered2 = DBV.convolve(image, gaussian2);
		new ImShow(filtered2, "Gaussian Filter - " + filtered2.toString());
		// others

		Filter.showFiltered(image, Filter.GLAETTUNG);
		// Filter.showFiltered(image, Filter.DERIVATIVEx);
		// Filter.showFiltered(image, Filter.DERIVATIVEy);
		// Filter.showFiltered(image, Filter.KIRSCH);
		// Filter.showFiltered(image, Filter.LAPLACE1);
		// Filter.showFiltered(image, Filter.LAPLACE2);
		// Filter.showFiltered(image, Filter.PREWITTh);
		// Filter.showFiltered(image, Filter.PREWITTv);
		// Filter.showFiltered(image, Filter.ROBERTSCROSSn);
		// Filter.showFiltered(image, Filter.ROBERTSCROSSp);
		// Filter.showFiltered(image, Filter.SOBELh);
		// Filter.showFiltered(image, Filter.SOBELv);
		// Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		// Filter.showFiltered(image, Filter.SOBELh);
	}

	public void filterNeighbour() {
		Mat image = TestImage.TEXTSTRUCTURE.toMat();
		new ImShow(image, "Original");

		Filter.showFiltered(image, Filter.DERIVATIVEx);
		Filter.showFiltered(image, Filter.DERIVATIVEy);
		Filter.showFiltered(image, Filter.ROBERTSCROSSn);
		Filter.showFiltered(image, Filter.ROBERTSCROSSp);
		System.out.println(Filter.ROBERTSCROSSn.toMat().dump());
		System.out.println(Filter.ROBERTSCROSSp.toMat().dump());
	}

	public void filterRotate() {
		Mat image = TestImage.TEXTSTRUCTURE.toMat();
		new ImShow(image, "Original");
		Filter.showFiltered(image, Filter.KIRSCH);
		Filter.showFiltered(image, Filter.KIRSCH, true);
		// Filter.showFiltered(image, Filter.LAPLACE1, true);
	}

	public void sharpen() {
		Mat image = TestImage.MOON.toMat();
		new ImShow(image, "Original");
		Mat imageFiltered = DBV.convolve(image, Filter.LAPLACE2.toMat());
		new ImShow(imageFiltered, "Image filtered");
		Mat imageSharpened = new Mat();
		Core.subtract(image, imageFiltered, imageSharpened);
		new ImShow(imageSharpened, "Image sharpened");
	}

	public void harris() {
		Mat image = TestImage.LENA.toMat();
		new ImShow(image, "Original");
		Mat harris = DBV.harrisPoints(image, 0.125);
		new ImShow(harris, "Harris Points");
	}

	public void spectra() {
		Mat image = TestImage.BRICKWALL.toMat();
		new ImShow(image, "Original", 512, 512);
		Spectrum spectrum = new Spectrum(image);
		Mat amplitude = spectrum.getVisualizeableAmplitude();
		new ImShow(amplitude, "Amplitude", 512, 512);
		Mat phase = spectrum.getVisualizeablePhase();
		new ImShow(phase, "Phase", 512, 512);
	}

	public void test2() {
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

	public void spectra2() {
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

	public void reshapedSpectra() {
		Mat img1 = TestImage.LENA.toMat();
		new ImShow(img1, "Original1");
		Spectrum s1 = new Spectrum(img1);
		int w = (int) img1.size().width;
		int h = (int) img1.size().height;

		new ImShow(s1.getVisualizeableAmplitude(), "Amplitude", 512, 512);

		s1.reshapeAmplitude(new Rect(w / 4, h / 4, w / 2, h / 2));

		new ImShow(s1.getVisualizeableAmplitude(), "reshaped Amplitude", 512,
				512);
		Spectrum a1p1 = new Spectrum(s1.getAmplitude(), s1.getPhase());

		new ImShow(a1p1.getImage(), "Result", 512, 512);

	}

	public void dumpSpectra() {
		Mat img1 = TestImage.LENAoverlay.toMat();

		Point specP1 = new Point(247, 241);
		Point specP2 = new Point(267, 273);
		Point specP3 = new Point(241, 257);
		Point specP4 = new Point(273, 257);

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
		new ImShow(a1p1.getImage(), "Result filtered", 512, 512);

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

	public void lowpass() {
		int cutoff = 50;
		int order = 5;
		Mat img = TestImage.TESTIMAGE.toMat();
		img = DBV.padded(img, img.width() / 2);
		img = DBV.dftShift(DBV.dft(img));
		img.convertTo(img, CvType.CV_64F);
		Mat distances = PassFilter.getDistances(img.width(), img.height());

		Mat idealLF = PassFilter.getIdealLowpass(distances, cutoff);
		Mat imgILF = DBV.multiply(img, idealLF);
		imgILF = DBV.real(DBV.idft(DBV.dftShift(imgILF)));
		new ImShow(imgILF, "Ideal Lowpass Filtered", 512, 512);

		Mat gaussianLF = PassFilter.getGaussianLowpass(distances, cutoff);
		Mat imgGLF = DBV.multiply(img, gaussianLF);
		imgGLF = DBV.real(DBV.idft(DBV.dftShift(imgGLF)));
		new ImShow(imgGLF, "Gaussian Lowpass Filtered", 512, 512);

		Mat butterworthLF = PassFilter.getButterworthLowpass(distances, cutoff,
				order);
		Mat imgBLF = DBV.multiply(img, butterworthLF);
		imgBLF = DBV.real(DBV.idft(DBV.dftShift(imgBLF)));
		new ImShow(imgBLF, "Butterworth Lowpass Filtered", 512, 512);

		Spectrum s = new Spectrum(imgGLF);
		new ImShow(s.getVisualizeableAmplitude(), "Amplitude filtered, cutoff="
				+ cutoff, 512, 512);
	}

	public void highpass() {
		int cutoff = 50;
		int order = 5;
		Mat img = TestImage.TESTIMAGE.toMat();
		img = DBV.padded(img, img.width() / 2);
		img = DBV.dftShift(DBV.dft(img));
		// img.convertTo(img, CvType.CV_64F);
		Mat distances = PassFilter.getDistances(img.width(), img.height());

		Mat idealLF = PassFilter.getIdealHighpass(distances, cutoff);
		Mat imgILF = DBV.multiply(img, idealLF);
		imgILF = DBV.real(DBV.idft(DBV.dftShift(imgILF)));
		new ImShow(imgILF, "Ideal Highpass Filtered", 512, 512, true);

		Mat gaussianLF = PassFilter.getGaussianHighpass(distances, cutoff);
		Mat imgGLF = DBV.multiply(img, gaussianLF);
		imgGLF = DBV.real(DBV.idft(DBV.dftShift(imgGLF)));
		new ImShow(imgGLF, "Gaussian Highpass Filtered", 512, 512, true);

		Mat butterworthLF = PassFilter.getButterworthHighpass(distances,
				cutoff, order);
		Mat imgBLF = DBV.multiply(img, butterworthLF);
		imgBLF = DBV.real(DBV.idft(DBV.dftShift(imgBLF)));
		new ImShow(imgBLF, "Butterworth Highpass Filtered", 512, 512, true);

		Spectrum s1 = new Spectrum(imgILF);
		new ImShow(s1.getVisualizeableAmplitude(),
				"Amplitude ideal filtered, cutoff=" + cutoff, 512, 512);
		Spectrum s2 = new Spectrum(imgGLF);
		new ImShow(s2.getVisualizeableAmplitude(),
				"Amplitude gauss filtered, cutoff=" + cutoff, 512, 512);
		Spectrum s3 = new Spectrum(imgBLF);
		new ImShow(s3.getVisualizeableAmplitude(),
				"Amplitude butterworth filtered, cutoff=" + cutoff, 512, 512);

	}

	public void flow() {
		Mat image1 = TestImage.WHALE1.toMat();
		Mat image2 = TestImage.WHALE2.toMat();
		Mat image2b = image2.clone();
		Core.multiply(image2b, Scalar.all(1.5), image2b);
		Mat image3 = TestImage.YOSEMITE1.toMat();
		Mat image4 = TestImage.YOSEMITE2.toMat();

		Flow flow12 = new Flow(image1, image2);
		Flow flow12b = new Flow(image1, image2b);
		Flow flow34 = new Flow(image3, image4);

		new ImShow(flow12.difference(), "Flow of 1+2 (difference)");
		new ImShow(flow12b.difference(), "Flow of 1+2b (difference)");
		new ImShow(flow12b.phaseDifference(),
				"Flow of 1+2b (difference without amplitude, phase only)");

		new ImShow(flow34.difference(), "Flow of 3+4 (difference)");

	}

	public void halbbild() {
		Mat image = TestImage.LENA.toMat();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		image.convertTo(image, CvType.CV_64F);
		new ImShow(image, "Original");

		double[] imageV = new double[(int) image.total()];
		image.get(0, 0, imageV);
		double[] newV = new double[(int) (image.total() / 4)];
		Mat newImage = new Mat(image.width() / 2, image.height() / 2,
				image.type());
		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					newV[(i / 2) * newImage.width() + j / 2] = imageV[i
							* image.width() + j];
				}
			}
		}

		newImage.put(0, 0, newV);
		new ImShow(newImage, "new image", 512, 512);
		image = DBV.convolve(image, Filter.getGaussianFilter(3, 2));
		image.get(0, 0, imageV);
		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					newV[(i / 2) * newImage.width() + j / 2] = imageV[i
							* image.width() + j];
				}
			}
		}
		newImage.put(0, 0, newV);
		new ImShow(newImage, "new image, gaussian shaped before", 512, 512);
	}

}
