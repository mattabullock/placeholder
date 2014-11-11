package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JSeparator;

public class PostInstall extends JPanel {

    private static final long serialVersionUID = 9043469927619052834L;

    /**
     * Create the panel.
     */
    public PostInstall() {
        setBackground(new Color(245, 245, 220));
        this.setBounds(0, 0, 500, 400);
        setLayout(null);

        JLabel javaLogo = new JLabel(new ImageIcon(getClass().getResource("/javaLogoStrip.jpg")));
        javaLogo.setBackground(Color.RED);
        javaLogo.setBounds(0, 0, 500, 55);
        add(javaLogo);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(390, 330, 90, 25);
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    GUI.open(new URI("http://java.com/en/download/installed8.jsp"));
                }
                catch (URISyntaxException e1)
                {
                    e1.printStackTrace();
                }
                GUI.closeWindow();
            }
        });
        add(closeButton);

        JSeparator separator = new JSeparator();
        separator.setBounds(0, 320, 500, 2);
        add(separator);

        JLabel installedLabel = new JLabel("You have successfully installed Java");
        installedLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        installedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        installedLabel.setBounds(0, 55, 500, 45);
        add(installedLabel);

        JPanel backdrop = new JPanel();
        backdrop.setBounds(0, 100, 500, 220);
        backdrop.setBackground(Color.WHITE);
        add(backdrop);
        backdrop.setLayout(null);

        JLabel bodyText = new JLabel("<html><center>" +
                "<br>" +
                "You will be prompted when Java updates are available. " +
                "Always install updates to get the latest performance and security improvements.<br>" +
                "<br>" +
                "<br>" +
                "<br>" +
                "<br>" +
                "<br>" +
                "<br>" +
                "When you click close, your browser will be opened so you can verify that Java is working.<br>" +
                "</center></html>");
        bodyText.setVerticalAlignment(SwingConstants.TOP);
        bodyText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        bodyText.setBounds(20, 0, 460, 220);
        backdrop.add(bodyText);
        bodyText.setHorizontalAlignment(SwingConstants.CENTER);

        JButton autoUpdate = new JButton("<html><font color='000099'><U>" +
                "More about update settings</U></font></html>");
        autoUpdate.setLocation(138, 46);
        autoUpdate.setSize(224, 20);
        autoUpdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
        autoUpdate.setOpaque(false);
        autoUpdate.setBorderPainted(false);
        autoUpdate.setBackground(Color.WHITE);
        autoUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    GUI.open(new URI("http://java.com/en/download/help/java_update.xml"));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        backdrop.add(autoUpdate);
    }
}