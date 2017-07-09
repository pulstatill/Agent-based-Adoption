/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import main.Debugger;

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
    private JScrollPane jsp;
    private MainPanel mainPanel;

    public MainFrame(GUI_Agent myAgent)
    {
        super(myAgent.getLocalName());
        this.myAgent = myAgent;
        iNit();
        this.myFrame = this;
    }

    public MainFrame()
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
            throw new UnsupportedOperationException("Not supported yet.");
        });
        menuItem.getAccessibleContext().setAccessibleDescription("Create a new Product Request");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem);
        menuItem = new JMenuItem("Save Product Request");
        menuItem.addActionListener((((ActionEvent e) ->
        {
            FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.SAVE);
            fd.setFile("*.txt");
            fd.setVisible(true);
            String filename = fd.getFile();
            if (null != filename)
            {
                StringBuilder data = new StringBuilder();
                for (SetPanel panel : mainPanel.getPanels())
                {
                    data.append(panel.getOp().getTextField().getText());
                    data.append("\n");
                    data.append(panel.getOp().getTextArea().getText());
                    data.append(";");
                }
                try (BufferedWriter out = new BufferedWriter(new FileWriter(filename)))
                {
                    String savedata = data.toString();
                    out.write(savedata);
                } catch (Exception exc)
                {
                    Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, exc);
                }
            }
        })));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.setLayout(new BorderLayout());
        mainPanel = new MainPanel();
        jsp = new JScrollPane(mainPanel);
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
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
