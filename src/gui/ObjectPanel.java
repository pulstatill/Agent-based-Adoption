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
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author philipp
 */
public class ObjectPanel extends JPanel
{
    private JTextField textField;
    private JTextArea textArea;
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        BufferedImage image;
        String file = "/images/Object.png";
        InputStream inputStream = ObjectPanel.class.getResourceAsStream(file);
        
        try
        {
            int position = ((SetPanel)getParent()).getPosition();
            Color bg;
            if(position%2 == 0 || position == 0)
            {
                 bg = new Color(255, 0, 0, 255);
            }
            else
            {
                bg = new Color(0, 255, 0, 255);
            }
            image = ImageIO.read(inputStream);
            g.setColor(bg);
            g.fillRect(0, 0, 132, 26);
            g.fillRect(0, 28, 450, 172);
            g.drawImage(image.getScaledInstance(450, 200, Image.SCALE_DEFAULT), 0, 0,new Color(0, 0, 0, 0), null);
            
            
            setPreferredSize(new Dimension(450, 200));
        } catch (IOException ex)
        {
            Logger.getLogger(ObjectPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public ObjectPanel()
    {
        
        setLayout(null);
        textField = new JTextField();
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setBounds(4, 4, 130, 20);
        textArea = new JTextArea();
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(4, 28, 442, 171);
        //scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        //scrollPane.setSize(130,130);
        add(scrollPane);
        add(textField);
        setPreferredSize(new Dimension(450, 202));
    }

    public JTextField getTextField()
    {
        return textField;
    }

    public JTextArea getTextArea()
    {
        return textArea;
    }

}
