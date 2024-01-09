package finger.jetty;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;

import jakarta.annotation.PostConstruct;

public class MyTrayIcon extends TrayIcon {

    private static final String IMAGE_PATH = "/fingerprint.png";
    private static final String TOOLTIP = "Fingerprint desktop";

    private PopupMenu popup;
    private SystemTray tray;

    public MyTrayIcon(){
        super(createImage(IMAGE_PATH,TOOLTIP),TOOLTIP);
        popup = new PopupMenu();
        tray = SystemTray.getSystemTray();
        adicionarOpcaoSair();
    }
    
    public PopupMenu getPopup() {
    	return popup;
    }
    public SystemTray getTray() {
    	return tray;
    }
    
    public void iniciarTray()throws AWTException{
    	setPopupMenu(popup);
        tray.add(this);
    }

    protected static Image createImage(String path, String description){
        URL imageURL = MyTrayIcon.class.getResource(path);
        if(imageURL == null){
            System.err.println("Failed Creating Image. Resource not found: "+path);
            return null;
        }else {
            return new ImageIcon(imageURL,description).getImage();
        }
    }
    
    public void adicionarOpcaoSair() {
        MenuItem sairItem = new MenuItem("Sair");
        sairItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
        popup.add(sairItem);
    }
}