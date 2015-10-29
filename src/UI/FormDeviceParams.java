/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import trekermanager.Device;

/**
 *
 * @author RusTe
 */
public class FormDeviceParams extends javax.swing.JDialog {

    /**
     * Creates new form FormDeviceParams
     *
     * @param parent
     * @param modal
     */
    public FormDeviceParams(javax.swing.JFrame parent, boolean modal) {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        LabelId = new javax.swing.JLabel();
        LabelPort = new javax.swing.JLabel();
        TextFieldId = new javax.swing.JTextField();
        TextFieldPort = new javax.swing.JTextField();
        TextFieldPass1 = new javax.swing.JTextField();
        LabelPass = new javax.swing.JLabel();
        ButtonOk = new javax.swing.JButton();
        ButtonDiscard = new javax.swing.JButton();

        setLocationByPlatform(true);

        jPanel1.setMaximumSize(new java.awt.Dimension(220, 301));
        jPanel1.setMinimumSize(new java.awt.Dimension(220, 301));
        jPanel1.setPreferredSize(new java.awt.Dimension(220, 301));
        jPanel1.setLayout(null);

        LabelId.setText("ID устройства (15 символов)");
        jPanel1.add(LabelId);
        LabelId.setBounds(10, 10, 170, 14);

        LabelPort.setText("Порт");
        jPanel1.add(LabelPort);
        LabelPort.setBounds(10, 70, 150, 14);
        jPanel1.add(TextFieldId);
        TextFieldId.setBounds(10, 30, 160, 30);
        jPanel1.add(TextFieldPort);
        TextFieldPort.setBounds(10, 90, 160, 30);
        jPanel1.add(TextFieldPass1);
        TextFieldPass1.setBounds(10, 150, 160, 30);

        LabelPass.setText("Пароль");
        jPanel1.add(LabelPass);
        LabelPass.setBounds(10, 130, 150, 14);

        ButtonOk.setText("Ок");
        ButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonOkActionPerformed(evt);
            }
        });

        ButtonDiscard.setText("Отмена");
        ButtonDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonDiscardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ButtonOk, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(ButtonDiscard, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonOk)
                    .addComponent(ButtonDiscard))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonDiscardActionPerformed
        this.dispose();
    }//GEN-LAST:event_ButtonDiscardActionPerformed

    private void ButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonOkActionPerformed
        if (TextFieldId.getText().matches("\\d{15}")) {
            if (Integer.parseInt(TextFieldPort.getText()) != 0) {
                Device device = new Device(TextFieldId.getText(), Integer.parseInt(TextFieldPort.getText()),TextFieldPass1.getText());
                System.out.println("FormDeviceParams: Device" + TextFieldId.getText() + " Added");
                if (Start.mf.addDevice(device)) {
                    Start.mf.sdb.setDevice(device);
                    System.out.println("FormDeviceParams: Device Added");
                    Start.mf.reload();
                    this.dispose();
                } else {
                    System.err.println("Device exists");
                }
            }
        } else {
            System.err.println("incorrect input");
        }

    }//GEN-LAST:event_ButtonOkActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            FormDeviceParams dialog = new FormDeviceParams(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonDiscard;
    private javax.swing.JButton ButtonOk;
    private javax.swing.JLabel LabelId;
    private javax.swing.JLabel LabelPass;
    private javax.swing.JLabel LabelPort;
    private javax.swing.JTextField TextFieldId;
    private javax.swing.JTextField TextFieldPass1;
    private javax.swing.JTextField TextFieldPort;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
