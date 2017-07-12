/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
            //panels.get(panels.size()-1).getOp().getTextArea().setText(panels.get(panels.size()-3).getOp().getTextArea().getText());
            addnewObject();
        });

        GroupLayout.ParallelGroup horizontal = groupLayout.createParallelGroup();
        horizontal.addComponent(panels.get(0), panels.get(0).getPreferredSize().width, panels.get(0).getPreferredSize().width,
                panels.get(0).getPreferredSize().width).addGroup(groupLayout.createSequentialGroup().addGap(139, 139, 139).addComponent(add));

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
                                .addGap(145, 145, 145)
                                .addComponent(arrowpanel, arrowpanel.getPreferredSize().width, arrowpanel.getPreferredSize().width, arrowpanel.getPreferredSize().width)
                                .addGap(80, 80, 80)
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
                        .addGroup(groupLayout.createSequentialGroup().addGap(139, 139, 139).addComponent(add));

                vertikGroup.addComponent(panels.get(i), panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height, panels.get(i).getPreferredSize().height)
                        .addGap(1, 1, 1).addComponent(add, add.getPreferredSize().height, add.getPreferredSize().height, add.getPreferredSize().height)
                        .addGap(0, 500, Short.MAX_VALUE);
            }
        }
        groupLayout.setHorizontalGroup(horiGroup);
        groupLayout.setVerticalGroup(vertikGroup);
        setPreferredSize(new Dimension(450, 50+20+ ((panels.size()-1)/2)*(100+64+50)));
        repaint();
        revalidate();
    }

    public void moveUp(int i)
    {
        if (!(i - 1 <= 0))
        {
            SetPanel putuppd = panels.remove(i + 1);
            SetPanel putuppr = panels.remove(i);
            SetPanel putdownpd = panels.remove(i - 1);
            SetPanel putdownpr = panels.remove(i - 2);
            putdownpr.setPosition(i);
            putdownpd.setPosition(i + 1);
            putuppr.setPosition(i - 2);
            putuppd.setPosition(i - 1);
            panels.add(i - 2, putuppr);
            panels.add(i - 1, putuppd);
            panels.add(i, putdownpr);
            panels.add(i + 1, putdownpd);
            addnewObject();
        }
    }

    public void moveDown(int i)
    {
        if (!(i + 1 >= panels.size()))
        {
            SetPanel putuppd = panels.remove(i + 3);
            SetPanel putuppr = panels.remove(i + 2);
            SetPanel putdownpd = panels.remove(i + 1);
            SetPanel putdownpr = panels.remove(i);

            putdownpr.setPosition(i + 2);
            putdownpd.setPosition(i + 3);
            putuppd.setPosition(i + 1);
            putuppr.setPosition(i);

            panels.add(i, putuppr);
            panels.add(i + 1, putuppd);
            panels.add(i + 2, putdownpr);
            panels.add(i + 3, putdownpd);
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
                panels.get(i + 2).setPosition(i + 1);
            }

            panels.remove(i + 1);
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
        panels.add(i + 1, new SetPanel(i + 1));
        //anels.get(i+1).getOp().getTextArea().setText(panels.get(i-1).getOp().getTextArea().getText());
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
