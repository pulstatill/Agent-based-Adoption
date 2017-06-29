/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 *
 * @author philipp
 */
public class MainFrame extends JFrame
{
    private GUI_Agent myAgent;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    private JFrame myFrame;
    
    public MainFrame(GUI_Agent myAgent)
    {
     super(myAgent.getLocalName());
     this.myAgent = myAgent;
     iNit();
     this.myFrame = this;
    }
    public MainFrame ()
    {
        super("Hallo");
        iNit();
    }
    private void iNit()     
    {
        menuBar = new JMenuBar();
        menu = new JMenu("Product Request Creator");
        menuItem = new JMenuItem("New Product Request");
        menuItem.addActionListener((ActionEvent e) ->
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
        menuItem.getAccessibleContext().setAccessibleDescription("Create a new Product Request");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.setLayout(new BorderLayout());
        MainPanel mainPanel = new MainPanel();
        JScrollPane jsp = new JScrollPane(mainPanel);
        this.add(jsp, BorderLayout.CENTER);
        JButton sendbutton = new JButton("send");
        sendbutton.setPreferredSize(new Dimension(50, 20));
        sendbutton.setMinimumSize(new Dimension(50, 20));
        sendbutton.setMaximumSize(new Dimension(50, 20));
        sendbutton.setSize(50, 20);
        sendbutton.addActionListener((e) ->
        {
            myAgent.sendoffer(mainPanel.getPanels());
        });
        this.add(sendbutton, BorderLayout.AFTER_LAST_LINE);
        this.setSize(600, 600);
        this.setVisible(true);
        Application macApplication = Application.getApplication();
        macApplication.setAboutHandler(new AboutHandler()
        {
            @Override
            public void handleAbout(AppEvent.AboutEvent ae)
            {
                JOptionPane.showMessageDialog(myFrame, "Hallo Test", "Titel", JOptionPane.PLAIN_MESSAGE);
            }
        });
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
}
