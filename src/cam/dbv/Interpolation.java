package cam.dbv;

import org.opencv.core.Mat;

public class Interpolation {

	public static Mat halve(Mat image) {

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

		return newImage;
	}

	public static Mat halveNGauss(Mat image) {
		image = DBV.convolve(image, Filter.getGaussianFilter(3, 2));
		double[] imageV = new double[(int) image.total()];
		double[] newV = new double[(int) (image.total() / 4)];
		Mat newImage = new Mat(image.width() / 2, image.height() / 2,
				image.type());
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
		return newImage;
	}

	public static Mat nearestNeighbor(Mat image) {
		Mat newImage = new Mat(image.width() * 2, image.height() * 2,
				image.type());
		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {
				double[] v = new double[1];
				image.get(i, j, v);
				newImage.put(i * 2, j * 2, v);
				newImage.put(i * 2 + 1, j * 2, v);
				newImage.put(i * 2 + 1, j * 2 + 1, v);
				newImage.put(i * 2, j * 2 + 1, v);
			}
		}

		return newImage;
	}

	public static Mat bilinear(Mat image) {
		Mat newImage = new Mat(image.width() * 2, image.height() * 2,
				image.type());
		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {
				double[] v = new double[1];
				image.get(i, j, v);
				double[] vs = new double[1];
				if (i < image.width() - 1) {
					image.get(i + 1, j, vs);
				} else {
					vs[0] = 0;
				}
				double[] ve = new double[1];
				if (i < image.height() - 1) {
					image.get(i + 1, j, ve);
				} else {
					ve[0] = 0;
				}

				newImage.put(i * 2, j * 2, v);

				newImage.put(i * 2 + 1, j * 2, (v[0] + vs[0]) / 2);
				newImage.put(i * 2 + 1, j * 2 + 1, (v[0] + vs[0] + ve[0]) / 3);
				newImage.put(i * 2, j * 2 + 1, (v[0] + ve[0]) / 2);
			}
		}

		return newImage;
	}
}
