/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Philipp
 */
public class ProcessPanel extends JPanel
{
    private JComboBox processBox;
    private JTextArea textArea;
    
    public ProcessPanel()
    {
        String[][] processes;
            try (BufferedReader in = new BufferedReader(new FileReader("./ressources/processes.txt")))
            {
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null)
                {
                    data.append(line).append("\n");
                }
                in.close();

                String saveddata = data.toString();
                String[] proc = saveddata.split(";");
                processes = new String[proc.length - 1][2];

                String[] nameandprefs = proc[0].split("\n");
                processes[0][0] = nameandprefs[0];
                StringBuilder prefs = new StringBuilder();
                for (int j = 2; j < nameandprefs.length; j++)
                {
                    prefs.append(nameandprefs[j]).append("\n");
                }
                processes[0][1] = prefs.toString();

                for (int i = 1; i < proc.length - 1; i++)
                {
                    nameandprefs = proc[i].split("\n");
                    processes[i][0] = nameandprefs[1];
                    prefs = new StringBuilder();
                    for (int j = 2; j < nameandprefs.length; j++)
                    {
                        prefs.append(nameandprefs[j]).append("\n");
                    }
                    processes[i][1] = prefs.toString();
                }
                String[] items = new String[processes.length];
                for (int i = 0; i < processes.length; i++)
                {
                    items[i] = processes[i][0];
                }
                processBox = new JComboBox(items);
                //processBox.setOpaque(false);
                //processBox.setBackground(new Color(0, 0, 0, 0));
                //processBox.setBorder(BorderFactory.createEmptyBorder());
                processBox.setSelectedIndex(0);
                textArea.setText(processes[processBox.getSelectedIndex()][1]);
                processBox.addActionListener((ActionEvent e) ->
                {
                    textArea.setText("");
                    textArea.setText(processes[processBox.getSelectedIndex()][1]);
                });
                processBox.setBounds(4, 4, 120, 20);
                add(processBox);
            } catch (Exception exc)
            {
                Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
    

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        BufferedImage image;
        String file = "/images/Object.png";
        InputStream inputStream = ObjectPanel.class.getResourceAsStream(file);

        try
        {
            Color bg = new Color(0, 255, 0);
            image = ImageIO.read(inputStream);
            g.setColor(bg);
            //g.fillRect(0, 0, 132, 26);
            g.fillRect(0, 0, 300, 100);
            g.drawImage(image.getScaledInstance(300, 100, Image.SCALE_DEFAULT), 0, 0, new Color(0, 0, 0, 0), null);

            setPreferredSize(new Dimension(450, 200));
        } catch (IOException ex)
        {
            Logger.getLogger(ObjectPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
