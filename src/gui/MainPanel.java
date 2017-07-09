/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author philipp
 */
public class MainPanel extends JPanel
{

    private GroupLayout groupLayout;
    private int anz = 50;
    private JButton add;
    private LinkedList<SetPanel> panels = new LinkedList<>();

   
    public MainPanel()
    {
        super();
        iNit();
    }

    private void iNit()
    {
        groupLayout = new GroupLayout(this);
        setLayout(groupLayout);
        setPreferredSize(new Dimension(500, 500));
        panels.add(new SetPanel(panels.size()));
        add = new JButton(new ImageIcon(getClass().getResource("/images/new.png")));
        add.setOpaque(false);
        add.setContentAreaFilled(false);
        add.setBorderPainted(false);
        add.setFocusPainted(false);
        add.addActionListener((ActionEvent e) ->
        {
            panels.add(new SetPanel(panels.size()));
            panels.add(new SetPanel(panels.size()));
            panels.get(panels.size()-1).getOp().getTextArea().setText(panels.get(panels.size()-3).getOp().getTextArea().getText());
            addnewObject();
        });

        GroupLayout.ParallelGroup horizontal = groupLayout.createParallelGroup();
        horizontal.addComponent(panels.get(0), panels.get(0).getPreferredSize().width, panels.get(0).getPreferredSize().width,
                panels.get(0).getPreferredSize().width).addGroup(groupLayout.createSequentialGroup().addGap(200, 200, 200).addComponent(add));

        GroupLayout.SequentialGroup vertikal = groupLayout.createSequentialGroup();
        vertikal.addComponent(panels.get(0), panels.get(0).getPreferredSize().height, panels.get(0).getPreferredSize().height, panels.get(0).getPreferredSize().height)
                .addGap(1, 1, 1).addComponent(add, add.getPreferredSize().height, add.getPreferredSize().height,
                add.getPreferredSize().height).addGap(0, 500, Short.MAX_VALUE);

        groupLayout.setVerticalGroup(vertikal);
        groupLayout.setHorizontalGroup(horizontal);

    }

    private void addnewObject()
    {
        removeAll();
        GroupLayout.SequentialGroup vertikGroup = groupLayout.createSequentialGroup();
        GroupLayout.ParallelGroup horiGroup = groupLayout.createParallelGroup();
        for (int i = 0; i < panels.size(); i++)
        {
            ArrowPanel arrowpanel = new ArrowPanel();

            if (!(i + 1 == panels.size()))
            {
                AddButton newaddbutton = new AddButton(i + 1);
                horiGroup.addComponent(panels.get(i), panels.get(i).getPreferredSize().width, panels.get(i).getPreferredSize().width, panels.get(i).getPreferredSize().width)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(200, 200, 200)
                                .addComponent(arrowpanel, arrowpanel.getPreferredSize().width, arrowpanel.getPreferredSize().width, arrowpanel.getPreferredSize().width)
                                .addGap(260, 260, 260)
                                .addComponent(newaddbutton));
                vertikGroup.addComponent(panels.get(i), panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height)
                        .addGap(1, 1, 1)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(arrowpanel, arrowpanel.getPreferredSize().height, arrowpanel.getPreferredSize().height, arrowpanel.getPreferredSize().height)
                                .addComponent(newaddbutton))
                        .addGap(1, 1, Short.MAX_VALUE);
            } else
            {
                horiGroup.addComponent(panels.get(i), panels.get(i).getPreferredSize().width, panels.get(i).getPreferredSize().width, panels.get(i).getPreferredSize().width)
                        .addGroup(groupLayout.createSequentialGroup().addGap(200, 200, 200).addComponent(add));

                vertikGroup.addComponent(panels.get(i), panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height)
                        .addGap(1, 1, 1).addComponent(add, add.getPreferredSize().height, add.getPreferredSize().height, add.getPreferredSize().height)
                        .addGap(0, 500, Short.MAX_VALUE);
            }
        }
        groupLayout.setHorizontalGroup(horiGroup);
        groupLayout.setVerticalGroup(vertikGroup);
        setPreferredSize(new Dimension(500, panels.size() * (200 + 30) + 10));
        repaint();
        revalidate();
    }

    public void moveUp(int i)
    {
        if (!(i <= 0))
        {
            SetPanel save = panels.remove(i);
            save.setPosition(i - 1);
            panels.get(i - 1).setPosition(i);
            panels.add(i - 1, save);
            addnewObject();
        }
    }

    public void moveDown(int i)
    {
        if (!(i + 1 == panels.size()))
        {
            panels.get(i + 1).setPosition(i);
            SetPanel save = panels.remove(i);
            save.setPosition(i + 1);
            panels.add(i + 1, save);
            addnewObject();
        }
    }

    public void removePanel(int i)
    {
        if (panels.size() > 1)
        {
            if (!(i + 1 == panels.size()))
            {
                panels.get(i + 1).setPosition(i);
            }
            panels.remove(i);
            addnewObject();
        } else
        {
            panels.remove(i);
            panels.add(new SetPanel(0));
            addnewObject();
        }
    }

    public void add(int i)
    {
        for (int j = i; j < panels.size(); j++)
        {
            panels.get(j).setPosition(panels.get(j).getPosition() + 2);
        }
        panels.add(i, new SetPanel(i));
        panels.add(i+1, new SetPanel(i+1));
        panels.get(i+1).getOp().getTextArea().setText(panels.get(i-1).getOp().getTextArea().getText());
        addnewObject();
    }
    
    public LinkedList<SetPanel> getPanels()
    {
        return panels;
    }
    public void setPanels(LinkedList<SetPanel> panels)
    {
        this.panels = panels;
        addnewObject();
    }
}
