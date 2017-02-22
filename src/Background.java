import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Background extends JFrame {
	static BufferedImage image;

	public Background() {
		try {
			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 建立背景圖
		ImageIcon background = new ImageIcon(image);
		JLabel label = new JLabel(new ImageIcon(image));
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		JPanel imagePanel = (JPanel) this.getContentPane();
		imagePanel.setOpaque(false);
		imagePanel.add(new PaintSurface());
		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

		this.setSize(500, 500);
		this.setUndecorated(true);// 無標題列
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);// 最大化
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
