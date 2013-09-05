package cam.dbv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Filter {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Filter filter = new Filter();

	}
	
	enum FilterType{
		
	}
	
	static String testImage = "lena.png";
	
	Mat image;
	
	public Filter(){
		image = Highgui.imread("lena.png");
		createFilter();
		convolve();
	}

	private void createFilter() {
		double m[][] = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
		//Mat filter = Mat.
		//Mat filter = Mat(3, 3, CvType.CV_64F, m).inv();
	}

	private void convolve() {
		new ImShow(image);
		
	}

}
