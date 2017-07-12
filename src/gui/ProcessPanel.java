/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Philipp
 */
public class ProcessPanel extends JPanel implements GetterInterface
{

    private JComboBox processBox;
    private JTextArea textArea;
    private InformationPanel ip;
    private BufferedImage image;

    public ProcessPanel()
    {
        String[][] processes;
        setPreferredSize(new Dimension(250, 100));
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
            textArea = new JTextArea();
            //processBox.setOpaque(false);
            //processBox.setBackground(new Color(0, 0, 0, 0));
            //processBox.setBorder(BorderFactory.createEmptyBorder());
            processBox.setSelectedIndex(0);
            textArea.setText(processes[processBox.getSelectedIndex()][1]);
            processBox.addActionListener((ActionEvent e) ->
            {
                textArea.setText("");
                textArea.setText(processes[processBox.getSelectedIndex()][1]);
                repaint();
            });
            processBox.setBounds(4, 4, 120, 20);
            textArea.setBackground(new Color(0, 0, 0, 0));
            textArea.setOpaque(false);
            textArea.setBorder(BorderFactory.createEmptyBorder());
        } catch (Exception exc)
        {
            Logger.getLogger(GUI_Agent.class.getName()).log(Level.SEVERE, null, exc);
        }
        ip = new InformationPanel(processBox, textArea);
        InformationFrame test = new InformationFrame(ip, false);
        test.dispose();
        image = new BufferedImage(450, 200, BufferedImage.TYPE_INT_ARGB);
        
        synchronized (ip.getTreeLock())
        {
            ip.paint(image.createGraphics());
        }
        addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                InformationFrame info = new InformationFrame(ip);
                info.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        image = info.getImage();
                    }
                    
                });
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                ((MainPanel) getParent().getParent()).drawPanel(image, getLocationOnScreen());
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                ((MainPanel) getParent().getParent()).drawPanel(null, getLocationOnScreen());
            }
        });
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
            g.fillRect(0, 0, 250, 100);
            g.drawImage(image.getScaledInstance(250, 100, Image.SCALE_DEFAULT), 0, 0, new Color(0, 0, 0, 0), null);
            g.setFont(new Font("serif", Font.CENTER_BASELINE, 20));
            g.setColor(new Color(0, 0, 0, 255));
            g.drawString((String) processBox.getSelectedItem(), 10, 50);
            
        } catch (IOException ex)
        {
            Logger.getLogger(ObjectPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public JTextField getTextField()
    {
        JTextField textField;
        textField = new JTextField();
        textField.setText((String) processBox.getSelectedItem());
        return textField;
    }

    public void setTextField(String textField)
    {
        for(int i = 0; i < processBox.getItemCount(); i++)
            {
                if(textField.equalsIgnoreCase((String)processBox.getItemAt(i)))
                    processBox.setSelectedIndex(i);
            }
    }

    public JTextArea getTextArea()
    {
        return textArea;
    }

    public void setTextArea(String textArea)
    {
        this.textArea.setText(textArea);
    }


}
