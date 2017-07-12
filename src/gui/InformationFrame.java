/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author philipp
 */
public class InformationFrame extends JFrame
{
    private BufferedImage image;
    public InformationFrame(InformationPanel panel)
    {
        setSize(450, 222);
        if (panel.getPointFrame() != null)
        {
            setLocation(panel.getPointFrame());
        } else
        {
            setLocationRelativeTo(null);
        }
        setLayout(null);
        panel.setBounds(0, 0, 450, 200);
        add(panel);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {

            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                panel.setPointFrame(getLocation());
                image = new BufferedImage(450, 200, BufferedImage.TYPE_INT_ARGB);

                synchronized (panel.getTreeLock())
                {
                    panel.paint(getImage().createGraphics());
                }
            }

            @Override
            public void windowClosed(WindowEvent e)
            {

            }

            @Override
            public void windowIconified(WindowEvent e)
            {

            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {

            }

            @Override
            public void windowActivated(WindowEvent e)
            {

            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {

            }
        });
    }

    public InformationFrame(InformationPanel panel, boolean visible)
    {
        setSize(450, 200);
        if (panel.getPointFrame() != null)
        {
            setLocation(panel.getPointFrame());
        } else
        {
            setLocationRelativeTo(null);
        }
        setLayout(null);
        panel.setBounds(0, 0, 450, 200);
        add(panel);
        setVisible(visible);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public BufferedImage getImage()
    {
        return image;
    }

}
