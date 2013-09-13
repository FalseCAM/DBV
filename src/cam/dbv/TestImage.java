package cam.dbv;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public enum TestImage {
	LENA("Lena", "lena.png"), //
	TEXTSTRUCTURE("Text Structure", "textStructure.png"), //
	NAObw("Nao black&white", "nao_bw.jpg"), //
	ZEBRASbw("Zebras black&white", "zebras_bw.png"), //
	MOON("Moon", "moon_blurry.png"), //
	WHALE1("Whale 1", "rubberWhale09.png"), //
	WHALE2("Whale 2", "rubberWhale10.png"), //
	YOSEMITE1("Yosemite 1", "yosemite10.png"), //
	YOSEMITE2("Yosemite 2", "yosemite11.png"), //
	BOAT("Boat", "Boat.png"), LENAoverlay("Lena overlay", "Lena_overlay.png"), //
	TESTIMAGE("Testimage", "testImage.png"), //
	BRICKWALL("Brick wall", "brickwall.jpg"), //
	LENARAUSCHEN("Lena rauschen", "lena_rauschen.png"), //
	ELAINE("Elaine", "elaine.png"), //
	FACES20X20("Faces 20x20", "faces.png"), FACES10X5("Faces 10x5",
			"faces10x5.png"), //
	FACES20X1("Faces 20x1", "faces20x1.png");

	private String name;
	private String file;

	private TestImage(String name, String file) {
		this.name = name;
		this.file = file;
	}

	public Mat toMat() {
		return Highgui.imread(file);
	}
}
