
package com.internship.frejaeidjmeterplugin.jmeter.visualizer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class FrejaEIDPluginVisualizerGuiPanel extends JPanel{

    private ResultPanel authResults;
    private ResultPanel signResults;

    public FrejaEIDPluginVisualizerGuiPanel() {
        initComponents();
        setForm();
    }

    public ResultPanel getAuthResults() {
        return authResults;
    }

    public ResultPanel getSignResults() {
        return signResults;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setForm() {
        this.setLayout(new GridBagLayout());
        authResults = new ResultPanel();
        signResults = new ResultPanel();
        JTabbedPane jTP = new JTabbedPane();
        jTP.add("Auth", authResults);
        jTP.add("Sign", signResults);
        GridBagConstraints myConstraints = new GridBagConstraints();
        myConstraints.ipadx = 50;
        myConstraints.ipady = 50;
        this.add(jTP, myConstraints);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}