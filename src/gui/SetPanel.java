/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author philipp
 */
public class SetPanel extends JPanel
{

    private JButton arrowup, arrowdown, delete;
    private ProcessPanel processPanel;
    private ProductPanel productPanel;
    private GroupLayout gp;
    private int position;

    public SetPanel(int position)
    {
        this.position = position;

        gp = new GroupLayout(this);
        setOpaque(false);
        if (position % 2 == 0)
        {
            productPanel = new ProductPanel(position);
            GroupLayout.SequentialGroup horizontset = gp.createSequentialGroup();
            horizontset.addGap(125, 125, 125).addComponent(productPanel);

            GroupLayout.ParallelGroup vertikset = gp.createParallelGroup();
            vertikset.addComponent(productPanel);

            gp.setVerticalGroup(vertikset);
            gp.setHorizontalGroup(horizontset);
            setPreferredSize(new Dimension(175, 50));
        } else
        {
            arrowup = new JButton(new ImageIcon(getClass().getResource("/images/arrowup.png")));
            arrowup.setOpaque(false);
            arrowup.setContentAreaFilled(false);
            arrowup.setBorderPainted(false);
            arrowup.setFocusPainted(false);

            arrowup.addActionListener((ActionEvent e) ->
            {
                ((MainPanel) this.getParent()).moveUp(this.position);
            });

            arrowdown = new JButton(new ImageIcon(getClass().getResource("/images/arrowdown.png")));
            arrowdown.setOpaque(false);
            arrowdown.setContentAreaFilled(false);
            arrowdown.setBorderPainted(false);
            arrowdown.setFocusPainted(false);

            arrowdown.addActionListener((ActionEvent e) ->
            {
                ((MainPanel) this.getParent()).moveDown(this.position);
            });

            delete = new JButton(new ImageIcon(getClass().getResource("/images/delete.png")));
            delete.setOpaque(false);
            delete.setContentAreaFilled(false);
            delete.setBorderPainted(false);
            delete.setFocusPainted(false);

            delete.addActionListener((ActionEvent e) ->
            {
                ((MainPanel) this.getParent()).removePanel(this.position);
            });

            processPanel = new ProcessPanel();

            GroupLayout.SequentialGroup vertikbuttons = gp.createSequentialGroup();
            vertikbuttons.addComponent(arrowup).addComponent(delete).addComponent(arrowdown);

            GroupLayout.ParallelGroup horizontbuttons = gp.createParallelGroup();
            horizontbuttons.addComponent(arrowup).addComponent(delete).addComponent(arrowdown);

            GroupLayout.SequentialGroup horizontset = gp.createSequentialGroup();
            horizontset.addComponent(processPanel, processPanel.getPreferredSize().width, processPanel.getPreferredSize().width, processPanel.getPreferredSize().width)
                    .addGap(5, 5, 5).addGroup(horizontbuttons);

            GroupLayout.ParallelGroup vertikset = gp.createParallelGroup();
            vertikset.addComponent(processPanel).addGroup(gp.createSequentialGroup().addGap(7, 7, 7).addGroup(vertikbuttons));

            gp.setVerticalGroup(vertikset);
            gp.setHorizontalGroup(horizontset);
            setPreferredSize(new Dimension(350, 100));
        }

        setLayout(gp);
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
        if(productPanel != null)
        {
            productPanel.setPosition(position);
        }
        //this.op.setPosition(position);
    }

    public GetterInterface getallText()
    {
        if(productPanel!=null)
            return productPanel;
        else
            return processPanel;
    }
}
