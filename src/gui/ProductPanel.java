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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Philipp
 */
public class ProductPanel extends JPanel implements GetterInterface
{

    private int position;
    private JTextField textField;
    private JTextArea textArea;
    private InformationPanel ip;
    private BufferedImage image;
    private MainPanel mainPanel;
    private boolean paneldrawn = false;

    public ProductPanel(int position)
    {
        this.position = position;
        iNit();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (!paneldrawn)
        {
            ((MainPanel) getParent().getParent()).drawPanel(image, getLocationOnScreen());
            paneldrawn = true;
        }
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
        if (textField.getText().equalsIgnoreCase("") || textField.getText().equalsIgnoreCase("P" + this.position / 2))
        {
            textField.setText("P" + position / 2);
        }
        this.position = position;
        repaint();

    }

    @Override
    public JTextField getTextField()
    {
        return textField;
    }

    @Override
    public void setTextField(String textField)
    {
        this.textField.setText(textField);
    }

    @Override
    public JTextArea getTextArea()
    {
        return textArea;
    }

    @Override
    public void setTextArea(String textArea)
    {
        this.textArea.setText(textArea);
    }

    public void iNit()
    {
        long time = -System.currentTimeMillis();
        textField = new JTextField();
        textField.setBackground(new Color(0, 0, 0, 0));
        StringBuilder b = new StringBuilder();
        b.append("P").append(position/2);
        textField.setText(b.toString());
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setBounds(4, 4, 130, 20);
        
        textArea = new JTextArea();
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setOpaque(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        ip = new InformationPanel(textArea, textField);
        ip.setPreferredSize(new Dimension(450, 200));
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
                        ((MainPanel) getParent().getParent()).removedrawedPanel(image);
                        image = info.getImage();
                        paneldrawn = false;
                        repaint();
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
                ((MainPanel) getParent().getParent()).drawPanelOnes(image, getLocationOnScreen());
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                ((MainPanel) getParent().getParent()).drawPanelOnes(null, getLocationOnScreen());
            }
        });
    }
}
