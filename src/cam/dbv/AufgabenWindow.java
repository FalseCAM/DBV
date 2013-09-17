package cam.dbv;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import org.opencv.core.Core;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class AufgabenWindow extends JFrame {

	private JPanel contentPane;
	private Aufgaben aufgaben = new Aufgaben();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AufgabenWindow frame = new AufgabenWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AufgabenWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton filterButton = new JButton("Filter");
		filterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aufgaben.filter();
			}
		});
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(filterButton);

		JButton filterNeighbourButton = new JButton("Filter Neighbour");
		filterNeighbourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.filterNeighbour();
			}
		});
		contentPane.add(filterNeighbourButton);

		JButton filterRotateButton = new JButton("Filter Rotate");
		filterRotateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.filterRotate();
			}
		});
		contentPane.add(filterRotateButton);

		JButton sharpenButton = new JButton("Sharpen");
		sharpenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.sharpen();
			}
		});
		contentPane.add(sharpenButton);

		JButton harrisButton = new JButton("Harris Points");
		harrisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.harris();
			}
		});
		contentPane.add(harrisButton);

		JButton spectraButton = new JButton("Spectrum");
		spectraButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.spectra();
			}
		});
		contentPane.add(spectraButton);

		JButton spectra2Button = new JButton("Spectrum 2");
		spectra2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.spectra2();
			}
		});
		contentPane.add(spectra2Button);

		JButton reshapedSpectraButton = new JButton("Reshaped Spectra");
		reshapedSpectraButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.reshapedSpectra();
			}
		});
		contentPane.add(reshapedSpectraButton);

		JButton differentiatonTheoremButton = new JButton(
				"Differentiations Theorem");
		differentiatonTheoremButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.differentiationsTheorem();
			}
		});
		contentPane.add(differentiatonTheoremButton);

		JButton dumpSpectraButton = new JButton("Dump Spectrum");
		dumpSpectraButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.dumpSpectra();
			}
		});
		contentPane.add(dumpSpectraButton);

		JButton lowPassButton = new JButton("Low Pass Filter");
		lowPassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.lowpass();
			}
		});
		contentPane.add(lowPassButton);

		JButton highPassButton = new JButton("High Pass Filter");
		highPassButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.highpass();
			}
		});
		contentPane.add(highPassButton);

		JButton flowButton = new JButton("Flow");
		flowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.flow();
			}
		});
		contentPane.add(flowButton);

		JButton halbBildButton = new JButton("Halb Bild");
		halbBildButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.halbbild();
			}
		});
		contentPane.add(halbBildButton);

		JButton interpolationButton = new JButton("Interpolation");
		interpolationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.interpolation();
			}
		});
		contentPane.add(interpolationButton);

		JButton facesButton = new JButton("Faces");
		facesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aufgaben.faces();
			}
		});
		contentPane.add(facesButton);
	}

}
