package secproj;

import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.*;

public class MainWindow extends javax.swing.JFrame {
    /**
     * Attributes and Members Declaration
     */
    Connection conn=null;       // conn object will be used to connect to database
    PreparedStatement pst=null; // pst  object will handle the queries
    ResultSet rs=null;          // rs   object will handle the results of queries    
    
    public void close(){
        JOptionPane.showMessageDialog(null, "Goodbye");
        WindowEvent winClosingEvent = new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
    }
    
    /**
     * Creates new form mainWindows
     */
    public MainWindow() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        signupButton = new javax.swing.JButton();
        checkButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(392, 344));
        setPreferredSize(new java.awt.Dimension(392, 324));
        getContentPane().setLayout(null);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("Welcome to SPsec");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(73, 9, 260, 30);

        jLabel1.setText("Please choose and action bellow:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(80, 50, 238, 15);

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        getContentPane().add(loginButton);
        loginButton.setBounds(63, 74, 125, 25);

        jSeparator1.setMinimumSize(new java.awt.Dimension(392, 314));
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(0, 105, 400, 10);

        jLabel5.setText("Other options");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(145, 133, 99, 15);

        signupButton.setText("SignUp");
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });
        getContentPane().add(signupButton);
        signupButton.setBounds(206, 74, 125, 25);

        checkButton.setText("Check Connection");
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkButtonActionPerformed(evt);
            }
        });
        getContentPane().add(checkButton);
        checkButton.setBounds(115, 154, 160, 25);

        aboutButton.setText("About");
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });
        getContentPane().add(aboutButton);
        aboutButton.setBounds(115, 197, 160, 25);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(exitButton);
        exitButton.setBounds(115, 240, 160, 25);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/secproj/logo.jpeg"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(290, 210, 100, 100);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/secproj/upatras_logo.png"))); // NOI18N
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 200, 340, 120);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        Login login = new Login();
        login.setVisible(true);
        dispose();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupButtonActionPerformed
        SignUp su = new SignUp();
        su.setVisible(true);
    }//GEN-LAST:event_signupButtonActionPerformed

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        AboutWin aw = new AboutWin();
        aw.setVisible(true);
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        close();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkButtonActionPerformed
        ConnectionStatus.ConnectDb();
    }//GEN-LAST:event_checkButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JButton checkButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton signupButton;
    // End of variables declaration//GEN-END:variables
}
