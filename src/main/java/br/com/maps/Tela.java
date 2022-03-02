/*
 * Created by JFormDesigner on Thu Feb 24 13:08:58 BRT 2022
 */

package br.com.maps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefLoadHandlerAdapter;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

public class Tela extends JFrame {
	
	private CefApp cefApp_;
	private CefClient client_;
	private CefBrowser browser_;
	private Component browerUI_;
	private boolean load = false;
    
    private String API_KEY = "your_api_key_google_maps";
	
	public Tela() {
		initComponents();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		lblMaps = new JLabel();
		txtLocal = new JTextField();
		btnBuscar = new JButton();
		pnlMapa = new JPanel();
		btnRecarregar = new JButton();

		//======== this ========
		var contentPane = getContentPane();
		contentPane.setLayout(null);

		//======== panel1 ========
		{
			panel1.setBackground(Color.white);
			panel1.setLayout(null);

			//---- lblMaps ----
			lblMaps.setText("Maps");
			lblMaps.setFont(new Font("Segoe UI Black", Font.PLAIN, 24));
			panel1.add(lblMaps);
			lblMaps.setBounds(new Rectangle(new Point(10, 6), lblMaps.getPreferredSize()));
			panel1.add(txtLocal);
			txtLocal.setBounds(85, 11, 440, txtLocal.getPreferredSize().height);

			//---- btnBuscar ----
			btnBuscar.setText("Buscar");
			btnBuscar.addActionListener(e -> btnBuscarActionPerformed(e));
			panel1.add(btnBuscar);
			btnBuscar.setBounds(530, 10, 129, 25);

			//======== pnlMapa ========
			{
				pnlMapa.setLayout(new BorderLayout());
			}
			panel1.add(pnlMapa);
			pnlMapa.setBounds(2, 38, 792, 489);

			//---- btnRecarregar ----
			btnRecarregar.setText("Recarregar");
			btnRecarregar.addActionListener(e -> btnRecarregarActionPerformed(e));
			panel1.add(btnRecarregar);
			btnRecarregar.setBounds(665, 10, 129, 25);

			{
				// compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < panel1.getComponentCount(); i++) {
					Rectangle bounds = panel1.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = panel1.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				panel1.setMinimumSize(preferredSize);
				panel1.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(panel1);
		panel1.setBounds(1, 2, 796, 530);

		{
			// compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	
		open_site();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel lblMaps;
	private JTextField txtLocal;
	private JButton btnBuscar;
	private JPanel pnlMapa;
	private JButton btnRecarregar;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Tela tela = new Tela();
				tela.setVisible(true);
			}
		});
	}
	
	private void btnRecarregarActionPerformed(ActionEvent e) {
		browser_.reload(); 
		
		try {
			Thread.sleep(100);
		}catch (Exception err) {
			// TODO: handle exception
		}
		
		loadMap();
		
	}
	
	private void btnBuscarActionPerformed(ActionEvent e) {        
        browser_.executeJavaScript("geocodeAddress2('" + txtLocal.getText() + "');", "", 0);
	}
	
	private void open_site() {
		
		try {
	        CefAppBuilder builder = new CefAppBuilder();
	        
	        builder.getCefSettings().windowless_rendering_enabled = false;
	        
	        builder.setAppHandler(new MavenCefAppHandlerAdapter() {
	            @Override
	            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
	                // Shutdown the app if the native CEF part is terminated
	                if (state == CefAppState.TERMINATED) System.exit(0);
	            }
	        });
	
	        cefApp_ = builder.build();
	
	        client_ = cefApp_.createClient();
	        
	        client_.addLoadHandler(new CefLoadHandlerAdapter() {
				@Override
				public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
					if(!load) {
						loadMap();
						load = true;
					}
				}
			});
	
	        CefMessageRouter msgRouter = CefMessageRouter.create();
	        client_.addMessageRouter(msgRouter);
	        
	        String userDir = System.getProperty("user.dir");
	        String url = userDir + "\\HTMLGMaps\\simplemap\\simple_map.html";
	
	        browser_ = client_.createBrowser(url, false, false);
	        browerUI_ = browser_.getUIComponent();
	
	        pnlMapa.add(browerUI_, BorderLayout.CENTER);
	        
		}catch (Exception err) {
			err.printStackTrace();
		}
    }

	private void loadMap() {
		browser_.executeJavaScript("loadMap('" + API_KEY + "');", "", 0);
	}
	
}
