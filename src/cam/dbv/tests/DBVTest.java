package cam.dbv.tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import cam.dbv.DBV;

public class DBVTest {

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
	public void testAbs() {
		fail("Not yet implemented");
	}

	@Test
	public void testAngle() {
		fail("Not yet implemented");
	}

	@Test
	public void testLog() {
		double[] expA = { 0, 0.6931, 1.0986, 1.3863 };
		double[] resA = new double[(int) matA.total()];
		Mat log = DBV.log(matA);
		log.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.01);

		double[] expB = { 0, 1.5708, 0.6931, 1.5708, 1.0986, 1.5708, 1.3863,
				1.5708 };
		double[] resB = new double[(int) matB.total() * 2];
		Mat logB = DBV.log(matB);
		logB.get(0, 0, resB);
		Assert.assertArrayEquals(expB, resB, 0.01);
	}

	@Test
	public void testDFT() {
		double[] expC = { 10, 3, -2, 3, -4, -1, 0, -1 };
		double[] resC = new double[(int) matC.total() * 2];
		Mat mC = DBV.dft(matC, false);
		mC.get(0, 0, resC);
		System.out.println(mC.dump());
		Assert.assertArrayEquals(expC, resC, 0.01);
	}

	@Test
	public void testExp() {
		double[] expA = { 2.7183, 7.3891, 20.0855, 54.5982 };
		double[] resA = new double[(int) matA.total()];
		Mat log = DBV.exp(matA);
		log.get(0, 0, resA);
		Assert.assertArrayEquals(expA, resA, 0.01);

		double[] expC = { 1.4687, 2.2874, 7.3891, 0, -8.3585, 18.2637, 54.5982,
				0 };
		double[] resC = new double[(int) matC.total() * 2];
		Mat mC = DBV.exp(matC);
		mC.get(0, 0, resC);
		Assert.assertArrayEquals(expC, resC, 0.01);

		Mat iPi = new Mat(1, 1, CvType.CV_64FC2);
		iPi.put(0, 0, 0, 3.1415926);
		Mat rPi = DBV.exp(iPi);
		double[] resPi = new double[2];
		rPi.get(0, 0, resPi);
		Assert.assertArrayEquals(new double[] { -1, 0 }, resPi, 0.001);
	}

	@Test
	public void testCis() {
		Mat y = new Mat(1, 1, CvType.CV_64F);
		y.put(0, 0, new double[] { 2 });
		double[] expB = { -0.4161, 0.9093 };
		double[] resB = new double[2];
		Mat resMat = DBV.cis(y);
		resMat.get(0, 0, resB);
		Assert.assertArrayEquals(expB, resB, 0.01);

		y = new Mat(2, 2, CvType.CV_64F);
		y.put(0, 0, new double[] { 2, 3 });
		double[] expY = { -0.4161, 0.9093, -0.9900, 0.1411 };
		double[] resY = new double[4];
		resMat = DBV.cis(y);
		resMat.get(0, 0, resY);
		Assert.assertArrayEquals(expY, resY, 0.01);
	}

	@Test
	public void testToComplexMat() {

		double[] resR = new double[(int) matA.total() * 2];
		DBV.toComplexMat(matA, true).get(0, 0, resR);
		double[] expR = { 1, 0, 2, 0, 3, 0, 4, 0 };
		double[] resI = new double[(int) matA.total() * 2];
		DBV.toComplexMat(matA, false).get(0, 0, resI);
		double[] expI = { 0, 1, 0, 2, 0, 3, 0, 4 };
		Assert.assertArrayEquals(expR, resR, 0.01);
		Assert.assertArrayEquals(expI, resI, 0.01);
	}

}
