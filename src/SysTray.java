import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SysTray {
	private TrayIcon trayIcon;
	private SystemTray tray = null;
	private final Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png"));
	//private final Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
	private PopupMenu popup = new PopupMenu();
	public static Background bg;

	public SysTray() {
		// set icon
		tray = SystemTray.getSystemTray();
		trayIcon = new TrayIcon(image, "snapshot", popup);
		trayIcon.setImageAutoSize(true);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		setMenu();
	}

	private void setMenu() {
		MenuItem openItem = new MenuItem("screen shot");
		MenuItem optionItem = new MenuItem("option");
		MenuItem aboutItem = new MenuItem("about");
		MenuItem exitItem = new MenuItem("exit");

		popup.add(openItem);
		popup.add(optionItem);
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);
		MouseListener mouseListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					bg = new Background();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
		};

		openItem.addActionListener(e -> {
			bg = new Background();
		});
		optionItem.addActionListener(e -> {
		});
		aboutItem.addActionListener(e -> {
		});
		exitItem.addActionListener(e -> {
			System.exit(0);
		});
		trayIcon.addMouseListener(mouseListener);
	}
}