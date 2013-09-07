package cam.dbv;

import org.opencv.core.Mat;

public class StructureTensor {

	private Mat image;
	private Mat Ix;
	private Mat Iy;
	private Mat gaussianFilter;

	public StructureTensor(Mat image) {
		this.image = image;
		this.gaussianFilter = Filter.getGaussianFilter(3, 1.5);
		Ix = DBV.convolve(image, Filter.DERIVATIVEx.toMat());
		Iy = DBV.convolve(image, Filter.DERIVATIVEy.toMat());

	}

	public Mat getIx2() {
		return DBV.convolve(Ix.mul(Ix), gaussianFilter);
	}

	public Mat getIy2() {
		return DBV.convolve(Iy.mul(Iy), gaussianFilter);
	}

	public Mat getIxy() {
		return DBV.convolve(Ix.mul(Iy), gaussianFilter);
	}

	public Mat getImage() {
		return image;
	}

}
