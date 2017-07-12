/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author philipp
 */
public class InformationPanel extends JPanel
{

    private JComboBox processBox;
    private JTextArea textArea;
    private JTextField textField;
    private Point pointFrame;
    private boolean transparent;

    public InformationPanel(JComboBox processBox, JTextArea textArea)
    {
        setLayout(null);
        this.processBox = processBox;
        this.textArea = textArea;
        add(this.processBox);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(4, 28, 442, 171);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        add(scrollPane);
    }

    public InformationPanel(JTextArea textArea, JTextField textField)
    {
        setLayout(null);
        this.textArea = textArea;
        this.textField = textField;
        add(this.textField);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(4, 28, 442, 171);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        add(scrollPane);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        try
        {
            
            super.paintComponent(g);   
            BufferedImage image;
            String file = "/images/information.png";
            InputStream inputStream = ObjectPanel.class.getResourceAsStream(file);
            image = ImageIO.read(inputStream);
            g.drawImage(image.getScaledInstance(450, 200, Image.SCALE_DEFAULT), 0, 0, new Color(0, 0, 0, 0), null);
            setPreferredSize(new Dimension(450, 200));
        } catch (IOException ex)
        {
            Logger.getLogger(InformationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Point getPointFrame()
    {
        return pointFrame;
    }

    public void setPointFrame(Point pointFrame)
    {
        this.pointFrame = pointFrame;
    }

}
