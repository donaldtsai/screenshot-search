import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@SuppressWarnings("serial")
public class PaintSurface extends JComponent {
	static int x1, y1, width, height;
	Point startDrag, endDrag;

	public PaintSurface() {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				PopUp menu = new PopUp();
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				endDrag = new Point(e.getX(), e.getY());
				repaint();
			}
		});
	}

	public void paint(Graphics g) {
		if (startDrag != null && endDrag != null) {
			x1 = Math.min(startDrag.x, endDrag.x);
			y1 = Math.min(startDrag.y, endDrag.y);
			width = Math.abs(startDrag.x - endDrag.x);
			height = Math.abs(startDrag.y - endDrag.y);
			g.drawRect(x1, y1, width, height);
		}
	}

	class PopUp extends JPopupMenu implements ActionListener {
		public PopUp() {
			BufferedImage img = cutBufferedImage(Background.image, x1, y1, width, height);
			JMenuItem saveItem = new JMenuItem("Save");
			JMenuItem copyItem = new JMenuItem("Copy");
			JMenuItem gsItem = new JMenuItem("Google search");
			saveItem.addActionListener(e -> {
				save(img);
				SysTray.bg.dispose();
			});
			copyItem.addActionListener(e -> {
				copy(img);
				SysTray.bg.dispose();
			});
			gsItem.addActionListener(e -> {
				search(img);
				SysTray.bg.dispose();
			});
			add(saveItem);
			add(copyItem);
			add(gsItem);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	public BufferedImage cutBufferedImage(BufferedImage srcBfImg, int x, int y, int width, int height) {
		BufferedImage cutedImage = null;
		CropImageFilter cropFilter = new CropImageFilter(x, y, width, height);
		Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcBfImg.getSource(), cropFilter));
		cutedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = cutedImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return cutedImage;
	}

	public void save(BufferedImage img) {
		try {
			Calendar c = Calendar.getInstance();
			ImageIO.write(img, "png", new File(c.getTimeInMillis() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copy(BufferedImage img) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		ImageTransferable transferable = new ImageTransferable(img);
		clipboard.setContents(transferable, new ClipboardOwner() {
			public void lostOwnership(Clipboard clipboard, Transferable contents) {

			}
		});
	}

	public void search(BufferedImage img) {
		try {
			ImageIO.write(img, "png", new File(System.getProperty("java.io.tmpdir") + "temp.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Runnable r = () -> {
			WebDriver driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get("https://www.google.com.tw/imghp");
			((JavascriptExecutor) driver).executeScript("google.load('qi', function() {window.google.qb.tp()})");
			driver.findElement(By.id("qbfile")).sendKeys(System.getProperty("java.io.tmpdir") + "temp.png");
		};
		new Thread(r).start();
	}
}
