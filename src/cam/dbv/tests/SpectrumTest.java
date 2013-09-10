package cam.dbv.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import cam.dbv.DBV;
import cam.dbv.Spectrum;

public class SpectrumTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	Mat matA;
	Mat matB;
	Mat matC;
	Spectrum specA;

	@Before
	public void setUp() throws Exception {
		matA = new Mat(2, 2, CvType.CV_64F);
		matA.put(0, 0, 1, 2, 3, 4);
		matB = new Mat(2, 2, CvType.CV_64FC2);
		matB.put(0, 0, 0, 1, 0, 2, 0, 3, 0, 4);
		matC = new Mat(2, 2, CvType.CV_64FC2);
		matC.put(0, 0, 1, 1, 2, 0, 3, 2, 4, 0);

		specA = new Spectrum(matA);
	}

	@Test
	public void testAmplitude() {
		double[] expA = { 10, 2, 4, 0 };
		double[] resA = new double[(int) matC.total()];
		Mat mA = specA.getAmplitude();
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testPhase() {
		double[] expA = { 0, 3.1416, 3.1416, 0 };
		double[] resA = new double[(int) matC.total()];
		Mat mA = specA.getPhase();
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testImage() {
		double[] expA = { 1, 2, 3, 4 };
		double[] resA = new double[(int) matC.total()];
		Spectrum spec = new Spectrum(specA.getAmplitude(), specA.getPhase());

		Mat mA = spec.getImage();
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testCalcImage() {
		double[] expA = { 10, -2, -4, 0 };
		double[] resA = new double[(int) matC.total()];
		Mat amplitude = specA.getAmplitude();
		Mat phase = specA.getPhase();
		Mat image = new Mat(amplitude.size(), CvType.CV_64F);
		Mat cPhase = phase.clone();

		cPhase = DBV.complex(cPhase, false);
		cPhase = DBV.exp(cPhase);
		Mat cAmp = amplitude.clone();
		cAmp = DBV.complex(cAmp, true);
		Core.multiply(cAmp, cPhase, image, 1, CvType.CV_64FC2);
		DBV.real(image).get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

}
