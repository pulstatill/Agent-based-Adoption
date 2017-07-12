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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Philipp
 */
public class ProductPanel extends JPanel
{

    private int position;

    public ProductPanel(int position)
    {
        this.position = position;
        addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                
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
                
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        BufferedImage image;
        String file = "/images/product.png";
        InputStream inputStream = ObjectPanel.class.getResourceAsStream(file);

        try
        {
            image = ImageIO.read(inputStream);
            g.drawImage(image.getScaledInstance(50, 50, Image.SCALE_DEFAULT), 0, 0, new Color(0, 0, 0, 0), null);
            g.setFont(new Font("serif", Font.BOLD, 25));
                g.drawString("P" + position / 2, 10, 35);

            setPreferredSize(new Dimension(50, 50));
        } catch (IOException ex)
        {
            Logger.getLogger(ObjectPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        System.out.println(this.position);
        this.position = position;
        
    }

}
