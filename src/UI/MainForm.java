package UI;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JScrollPane;
import trekermanager.Device;
import trekermanager.DeviceListener;
import trekermanager.ServerDb;
import trekermanager.Watcher;

public class MainForm extends javax.swing.JFrame {

    private Map DeviceList = new HashMap<String, Device>();
    private Map PanelDeviceList = new HashMap<String, PanelDevice>();
    private Map WatcherList = new HashMap<Device, Boolean>();
    public static FormDeviceParams FDP;

    public ServerDb sdb;
    private javax.swing.JPanel PanelPane;

    public MainForm() {
        initComponents();
        if (load()) {
//            System.out.println("MainForm:Load successfull");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFont = new javax.swing.JPanel();
        PanelScrolPane = new javax.swing.JScrollPane();
        AddButton = new javax.swing.JButton();
        butTrackerData = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(300, 400));
        setLocationByPlatform(true);
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });

        PanelFont.setLayout(null);

        PanelScrolPane.setPreferredSize(new java.awt.Dimension(420, 230));
        PanelFont.add(PanelScrolPane);
        PanelScrolPane.setBounds(10, 50, 420, 520);

        AddButton.setText("Add");
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        PanelFont.add(AddButton);
        AddButton.setBounds(180, 20, 100, 23);

        butTrackerData.setText("Tracker data");
        butTrackerData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butTrackerDataActionPerformed(evt);
            }
        });
        PanelFont.add(butTrackerData);
        butTrackerData.setBounds(330, 20, 100, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFont, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelFont, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void setWatcherStatus(Device d, boolean status) {
        WatcherList.put(d, status);
    }

    public boolean getWatcherStatus(Device d) {
        return (Boolean) WatcherList.get(d);
    }

    public Set<String> getDevicesKeySet() {
        Set<String> keys = DeviceList.keySet();
        return keys;
    }

    public Map getDeviceList() {
        return DeviceList;
    }

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        System.out.println("MainForm: AddButtonActionPerformed clicked");
        Point mloc = MouseInfo.getPointerInfo().getLocation();
        FDP = new FormDeviceParams(new javax.swing.JFrame(), true);
        FDP.setBounds(mloc.x, mloc.y, 220, 200);
        FDP.setName(this.getName());
        FDP.setTitle("Добавление нового устройства");
        FDP.setVisible(true);
        System.out.println("MainForm: AddButtonActionPerformed executed");
    }//GEN-LAST:event_AddButtonActionPerformed

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized
        int Hgap = this.getPreferredSize().height - this.getSize().height;
        int Wgap = this.getPreferredSize().width - this.getSize().width;
        PanelPane.setPreferredSize(new java.awt.Dimension(Start.mf.PanelPane.getPreferredSize().width + Wgap, Start.mf.PanelPane.getPreferredSize().height + Hgap));
        PanelScrolPane.setPreferredSize(new java.awt.Dimension(Start.mf.PanelScrolPane.getPreferredSize().width + Wgap, Start.mf.PanelScrolPane.getPreferredSize().height + Hgap));
        PanelPane.revalidate();
        PanelScrolPane.revalidate();

    }//GEN-LAST:event_formAncestorResized

    private void butTrackerDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butTrackerDataActionPerformed
        TrekerData td = new TrekerData();
        td.setVisible(true);
    }//GEN-LAST:event_butTrackerDataActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }

    private boolean load() {
        System.out.println("MainForm:Load Started");
        sdb = new ServerDb("jdbc:sqlserver://MAIN:1433;databaseName=UltraFiolet", "sa", "Zx3d2818!");

        testArray();
        drawPanels();
        createWatcher();
        System.out.println("MainForm: load executed");
        return true;
    }

    private void testArray() {
        for (Device dev : sdb.getDeviceList()) {
            addDevice(dev);
        }

//        Device testDevice = new Device();
//        addDevice(testDevice);
//        Device testDevice1 = new Device("100000000000001", 5602,"1234", false, false);
//        addDevice(testDevice1);
//        Device testDevice2 = new Device("100000000000002", 5603,"1234", false, false);
//        addDevice(testDevice2);
//        Device testDevice3 = new Device("100000000000003", 5604,"1234", false, false);
//        addDevice(testDevice3);
//        Device testDevice4 = new Device("100000000000004", 5605,"1234", false, false);
//        addDevice(testDevice4);
//        Device testDevice5 = new Device("100000000000005", 5606,"1234", false, false);
//        addDevice(testDevice5);
//        Device testDevice6 = new Device("100000000000006", 5607,"1234", false, false);
//        addDevice(testDevice6);
//        Device testDevice7 = new Device("100000000000007", 5608,"1234", false, false);
//        addDevice(testDevice7);
//        Device testDevice8 = new Device("100000000000008", 5609,"1234", false, false);
//        addDevice(testDevice8);
//        Device testDevice9 = new Device("100000000000009", 5610,"1234", false, false);
//        addDevice(testDevice9);
    }

    private void createPanelPane() {
        PanelPane = new javax.swing.JPanel();
        PanelPane.setPreferredSize(new java.awt.Dimension(418, 228));
        javax.swing.GroupLayout PanelPaneLayout = new javax.swing.GroupLayout(PanelPane);
        PanelPaneLayout.setHorizontalGroup(
                PanelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 418, Short.MAX_VALUE)
        );
        PanelPaneLayout.setVerticalGroup(
                PanelPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 228, Short.MAX_VALUE)
        );
        PanelPane.setLayout(PanelPaneLayout);
    }

    private void drawPanels() {
        int i = 0;
        Set<String> keys = DeviceList.keySet();
        createPanelPane();

        for (String key : keys) {
            Device device = (Device) DeviceList.get(key);
            PanelDevice pd = new PanelDevice(device);
            pd.setBounds(0, 70 * i, pd.getPreferredSize().width, 60);
            pd.setAlignmentX(PanelDevice.CENTER_ALIGNMENT);
            PanelPane.add(pd);
            pd.setVisible(true);

            i++;
            PanelPane.setPreferredSize(new java.awt.Dimension(PanelPane.getPreferredSize().width, 70 * i + 15));
            PanelDeviceList.put(device.getId(), pd);
        }
        PanelPane.revalidate();
        PanelScrolPane.setViewportView(PanelPane);
        PanelScrolPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        PanelScrolPane.revalidate();
        PanelPane.repaint();
        PanelScrolPane.repaint();
        System.out.println("MainForm: drawPanels executed");

    }
    
    private void createWatcher()
    {
      Watcher watcher = new Watcher();
        Thread t2 = new Thread(watcher);
        t2.start();
        System.out.println("MainForm: watcher created");  
    }

    public void reload() {
        PanelPane.removeAll();
        //DeviceList.clear();
        PanelDeviceList.clear();
        drawPanels();
        System.out.println("MainForm: reload executed");

    }

    public boolean addDevice(Device d) {
        System.out.println("MainForm: addDevice started");
        if (!DeviceList.containsKey(d.getId())) {
            DeviceList.put(d.getId(), d);
            WatcherList.put(d, true);
            createListener(d);
            System.out.println("MainForm: addDevice executed, Device " + d.getId() + " added, listener awaiting for connections");
            return true;
        } else {
            System.err.println("Device" + d.getId() + "exists");
            return false;
        }
    }

    /**
     *
     * @param id
     * @param connection
     */
    public void deviceConnection(String id, boolean connection) {
        Device device = (Device) Start.mf.DeviceList.get(id);
        device.setConnection(connection);
        System.out.println("MainForm: deviceConnection " + device.getId() + " executed=" + connection);
        PanelDevice pd = (PanelDevice) PanelDeviceList.get(device.getId());
        pd.redrawPanel();
    }

    public void deviceStatus(String id, boolean status) {
        Device device = (Device) DeviceList.get(id);
        device.setStatus(status);
        System.out.println("MainForm: deviceStatus" + device.getId() + "executed=" + status);
    }

    public void createListener(Device device) {
        DeviceListener DL = new DeviceListener(device);
        Thread t1 = new Thread(DL);
        t1.start();
        System.out.println("MainForm: createListener executed");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JPanel PanelFont;
    private javax.swing.JScrollPane PanelScrolPane;
    private javax.swing.JButton butTrackerData;
    // End of variables declaration//GEN-END:variables
}
