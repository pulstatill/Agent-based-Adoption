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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author philipp
 */
public class ArrowPanel extends JPanel
{

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);  //To change body of generated methods, choose Tools | Templates.
        BufferedImage image;
        String file = "/images/arrow.png";
        InputStream inputStream = ObjectPanel.class.getResourceAsStream(file);

        try
        {
            image = ImageIO.read(inputStream);

            g.drawImage(image.getScaledInstance(10, 30, Image.SCALE_DEFAULT), 0, 0, new Color(0, 0, 0, 0), null);

        } catch (IOException ex)
        {
            Logger.getLogger(ObjectPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrowPanel()
    {
        setPreferredSize(new Dimension(10, 30));
    }

}
