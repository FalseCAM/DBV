package cam.dbv.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import cam.dbv.PassFilter;

public class PassFilterTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	Mat matA;
	Mat matB;
	Mat matC;

	@Before
	public void setUp() throws Exception {
		matA = new Mat(2, 2, CvType.CV_64F);
		matA.put(0, 0, 1, 2, 3, 4);
		matB = new Mat(2, 2, CvType.CV_64FC2);
		matB.put(0, 0, 0, 1, 0, 2, 0, 3, 0, 4);
		matC = new Mat(2, 2, CvType.CV_64FC2);
		matC.put(0, 0, 1, 1, 2, 0, 3, 2, 4, 0);
	}

	@Test
	public void testGetDistances() {
		double[] expA = { 1.8, 1.5, 1.8, 1.1, 0.5, 1.1, 1.1, 0.5, 1.1, 1.8,
				1.5, 1.8 };
		Mat mA = PassFilter.getDistances(3, 4);
		double[] resA = new double[(int) mA.total()];
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testGetIdealLowpass() {
		double[] expA = { 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0 };
		Mat mA = PassFilter.getIdealLowpass(PassFilter.getDistances(3, 4), 1);
		double[] resA = new double[(int) mA.total()];
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testGetGaussianLowpass() {
		double[] expA = { 0.20, 0.32, 0.20, 0.54, 0.88, 0.54, 0.54, 0.88, 0.54,
				0.20, 0.32, 0.20 };
		Mat mA = PassFilter
				.getGaussianLowpass(PassFilter.getDistances(3, 4), 1);
		double[] resA = new double[(int) mA.total()];
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testGetGaussianHighpass() {
		double[] expA = { 0.8, 0.68, 0.8, 0.46, 0.12, 0.46, 0.46, 0.12, 0.46,
				0.8, 0.68, 0.8 };
		Mat mA = PassFilter.getGaussianHighpass(PassFilter.getDistances(3, 4),
				1);
		double[] resA = new double[(int) mA.total()];
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.1);
	}

	@Test
	public void testGetButterworthLowpass() {
		double[] expA = { 0.009, 0.038, 0.009, 0.29, 1, 0.29, 0.29, 1, 0.29,
				0.009, 0.038, 0.009 };
		Mat mA = PassFilter.getButterworthLowpass(
				PassFilter.getDistances(3, 4), 1, 4);
		double[] resA = new double[(int) mA.total()];
		mA.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.01);
	}
}
