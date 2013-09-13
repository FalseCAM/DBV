package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Faces {
	Mat facesDB;
	Mat mean = new Mat();
	Mat eigenvectors = new Mat();
	Mat result = new Mat();
	int imgWidth;
	int imgHeight;
	private int facesRows;
	private int facesCols;

	public Faces(Mat faces, int facesRows, int facesCols, int numBasisVectors) {
		this.facesRows = facesRows;
		this.facesCols = facesCols;
		Imgproc.cvtColor(faces, faces, Imgproc.COLOR_RGB2GRAY);
		facesDB = loadFaces(faces, facesRows, facesCols);
		Core.PCACompute(facesDB, mean, eigenvectors, numBasisVectors);
		Core.PCAProject(facesDB, mean, eigenvectors, result);
		Core.PCABackProject(result, mean, eigenvectors, result);

	}

	public Mat loadFaces(Mat i, int rows, int cols) {
		Mat image = i.clone();
		image.convertTo(image, CvType.CV_64F);
		int w = (image.width() - cols - 1) / cols;
		imgWidth = w;
		int h = (image.height() - rows - 1) / rows;
		imgHeight = h;
		Mat ret = new Mat(rows * cols, w * h, CvType.CV_64F);
		double[] values = new double[w * h];
		int currentRow = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Mat img = image.submat(1 + r * (h + 1), 1 + r * (h + 1) + h, 1
						+ c * (w + 1), 1 + c * (w + 1) + w);
				Mat reshape = img.reshape(0);
				reshape.get(0, 0, values);
				ret.put(currentRow, 0, values);
				currentRow++;
			}
		}
		return ret;
	}

	public Mat createResultImage(Mat i, int rows, int cols) {
		Mat ret = new Mat(rows * imgHeight, cols * imgWidth, CvType.CV_64F);
		double[] values = new double[imgWidth * imgHeight];
		int currentRow = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				i.get(currentRow, 0, values);
				Mat img = new Mat(imgHeight, imgWidth, CvType.CV_64F);
				img.put(0, 0, values);
				img.copyTo(ret.submat(r * imgHeight, r * imgHeight + imgHeight,
						c * imgWidth, c * imgWidth + imgWidth));
				currentRow++;
			}
		}
		return ret;
	}

	public Mat getMean() {
		System.out.println("mean" + mean);
		return mean.reshape(0, imgHeight);
	}

	public Mat getEigenvectors() {
		System.out.println("eigenvectors" + eigenvectors);
		return createResultImage(eigenvectors, facesRows, facesCols);
	}

	public Mat getResult() {
		System.out.println("result" + result);
		return createResultImage(result, facesRows, facesCols);
	}

	public Mat getBaseReduction(Mat pcaBasis, int D) {
		return pcaBasis.submat(0, D, 0, pcaBasis.width() - 1).clone();
	}

	public Mat getPC(int i) {
		Mat pc = eigenvectors.row(i).reshape(1, imgHeight);
		return pc;
	}

	public Mat detect(Mat mat) {
		Mat image = mat.clone();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		return image;
	}

}
