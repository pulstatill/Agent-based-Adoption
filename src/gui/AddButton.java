/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author philipp
 */
public class AddButton extends JButton
{
    private int positon;
    public AddButton(int position)
    {
        super();
        this.positon = position;
        this.setIcon(new ImageIcon(getClass().getResource("/images/new.png")));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addActionListener(new ActionListener()         
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ((MainPanel) AddButton.this.getParent()).add(AddButton.this.getPositon());
            }
        });
    }

    public int getPositon()
    {
        return positon;
    }

    public void setPositon(int positon)
    {
        this.positon = positon;
    }
    
}
