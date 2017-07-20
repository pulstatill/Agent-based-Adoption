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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        long time = -System.currentTimeMillis();
        mainPanel = new MainPanel();
        jsp = new JScrollPane(mainPanel);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        this.add(jsp, BorderLayout.CENTER);
        JButton sendbutton = new JButton("send");
        sendbutton.addActionListener((e) ->
        {
            myAgent.sendoffer(mainPanel.getPanels());
        });
        this.add(sendbutton, BorderLayout.AFTER_LAST_LINE);
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        menuBar = new JMenuBar();
        menu = new JMenu("Product Request Creator");
        menuItem = new JMenuItem("New Product Request"); 
        menuItem.addActionListener((ActionEvent e) ->
        {
            jsp.getViewport().remove(mainPanel);
            mainPanel = new MainPanel();
            jsp.getViewport().add(mainPanel);
            repaint();
            revalidate();
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
            filename = fd.getDirectory() + filename;
            if (null != filename)
            {
                StringBuilder data = new StringBuilder();
                for (SetPanel panel : mainPanel.getPanels())
                {
                    data.append(panel.getallText().getTextField().getText());
                    data.append("\n");
                    data.append(panel.getallText().getTextArea().getText());
                    data.append(";").append("\n");
                }
                
                try (BufferedWriter out = new BufferedWriter(new FileWriter(filename)))
                {
                    String savedata = data.toString();
                    out.write(savedata);
                    out.close();
                } catch (Exception exc)
                {
                    Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, exc);
                }
            }
        })));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem);
        menuItem = new JMenuItem("Load Product Request");
        menuItem.addActionListener((((ActionEvent e) ->
        {
            FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
            fd.setFile("*.txt");
            fd.setVisible(true);
            String filename = fd.getFile();
            filename = fd.getDirectory() + filename;
            if (null != filename)
            {
                try (BufferedReader in = new BufferedReader(new FileReader(filename)))
                {
                    StringBuilder data = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null)
                    {
                        data.append(line).append("\n");
                    }
                    in.close();
                    String savedData = data.toString();
                    String[] objects = savedData.split(";");
                    LinkedList<SetPanel> panels = new LinkedList<>();
                    
                    String[] nameandprefs = objects[0].split("\n");
                    SetPanel panel = new SetPanel(0);
                    GetterInterface obpanel = panel.getallText();
                    obpanel.setTextField(nameandprefs[0]);
                    StringBuilder textarea = new StringBuilder();
                    for (int j = 1; j < nameandprefs.length; j++)
                    {
                        textarea.append(nameandprefs[j]).append("\n");
                    }
                    obpanel.setTextArea(textarea.toString());
                    panels.addLast(panel);
                    
                    for (int i = 1; i < objects.length - 1; i++)
                    {
                        if (objects[i] != null || objects[i].equalsIgnoreCase(""))
                        {
                            nameandprefs = objects[i].split("\n");
                            panel = new SetPanel(i);
                            obpanel = panel.getallText();
                            obpanel.setTextField(nameandprefs[1]);
                            textarea = new StringBuilder();
                            for (int j = 2; j < nameandprefs.length; j++)
                            {
                                textarea.append(nameandprefs[j]).append("\n");
                            }
                            obpanel.setTextArea(textarea.toString());
                            panels.addLast(panel);
                        }
                    }
                    mainPanel.setPanels(panels);
                } catch (Exception exc)
                {
                    Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, exc);
                }
            }
        })));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.setLayout(new BorderLayout());

    }

}
