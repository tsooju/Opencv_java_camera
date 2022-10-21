package com.opencvcamera;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class Camera extends JFrame {

	// camera screen
	private JLabel cameraScreen;

	private JButton btnCapture;

	private VideoCapture capture;
	private Mat image;

	private boolean clicked = false;

	public Camera() {
		// design UI
		setLayout(null);
		cameraScreen = new JLabel();
//		cameraScreen.setBounds(0, 0, 1200, 800);
		cameraScreen.setBounds(0, 0, 640, 480);
		add(cameraScreen);

		btnCapture = new JButton("찍기");
		btnCapture.setBounds(300, 480, 80, 40);
		add(btnCapture);

		btnCapture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clicked = true;

			}

		});
		
		setSize(new Dimension(640, 560));
//		setSize(new Dimension(1200, 800));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// create camera
	public void startCamera() {
		capture = new VideoCapture(0);
		image = new Mat();
		byte[] imageData;

		ImageIcon icon;

		while (true) {
			// read image to matrix
			capture.read(image);

			// convert matrix to byte
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", image, buf);

			imageData = buf.toArray();

			// add to JLabel
			icon = new ImageIcon(imageData);
			cameraScreen.setIcon(icon);

			// capture and save to file
			if (clicked) {
				// prompt for enter image name
				String name = JOptionPane.showInputDialog(this, "Enter image name");
				if (name == null) {
					name = new SimpleDateFormat("yyy-mm-dd-hh-mm-ss").format(new Date());
				}
				// write to file
				Imgcodecs.imwrite("images/" + name + ".jpg", image);
				clicked = false;
			}
		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		System.out.println("load success");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Camera camera = new Camera();
				// start camera in thread
				new Thread(new Runnable() {

					@Override
					public void run() {
						camera.startCamera();
					}

				}).start();
			}
		});
	}
}
