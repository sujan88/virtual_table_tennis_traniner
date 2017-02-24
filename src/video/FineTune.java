package video;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author sujan
 */
public class FineTune extends javax.swing.JFrame {

	
	public static int H;
	public static int H2;
	public static int S;
	public static int S2;
	public static int V;
	public static int V2;
    /** Creates new form FineTune */
    public FineTune() {
        initComponents();
    }

  
    private static FineTune instance=null;
    
    
    public static FineTune getInstanse(){
    	
    		instance=new FineTune();
    		
    
    	return instance; 
    }
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        saturationSlider =new javax.swing.JSlider(JSlider.HORIZONTAL,0, 255, 55);
        hueSlider = new javax.swing.JSlider(JSlider.HORIZONTAL,0, 180, 100);
        valueSlider = new javax.swing.JSlider(JSlider.HORIZONTAL,0, 255, 200);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        hueSlider2 = new javax.swing.JSlider(JSlider.HORIZONTAL,0, 180, 112);
        saturationSlider2 =new javax.swing.JSlider(JSlider.HORIZONTAL,0, 255, 84);
        valueSlider2 = new javax.swing.JSlider(JSlider.HORIZONTAL,0, 255, 252);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // handle js0 change events

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Hue");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Saturation");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Value");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Color Range");

        // handle js0 change events

        // handle js0 change events

        // handle js0 change events

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(valueSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saturationSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hueSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valueSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(saturationSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(hueSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                .addGap(47, 47, 47))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(317, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(305, 305, 305))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(hueSlider2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(hueSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saturationSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(saturationSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valueSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(valueSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

       hueSlider.setMajorTickSpacing(20);
       hueSlider.setMinorTickSpacing(10);
       hueSlider.setPaintTicks(true);
       hueSlider.setPaintLabels(true);
       
       hueSlider2.setMajorTickSpacing(20);
       hueSlider2.setMinorTickSpacing(10);
       hueSlider2.setPaintTicks(true);
       hueSlider2.setPaintLabels(true);
       
       saturationSlider.setMajorTickSpacing(30);
       saturationSlider.setMinorTickSpacing(10);
       saturationSlider.setPaintTicks(true);
       saturationSlider.setPaintLabels(true);
       
       saturationSlider2.setMajorTickSpacing(30);
       saturationSlider2.setMinorTickSpacing(10);
       saturationSlider2.setPaintTicks(true);
       saturationSlider2.setPaintLabels(true);
       
       valueSlider.setMajorTickSpacing(30);
       valueSlider.setMinorTickSpacing(10);
       valueSlider.setPaintTicks(true);
       valueSlider.setPaintLabels(true);
       
       valueSlider2.setMajorTickSpacing(30);
       valueSlider2.setMinorTickSpacing(10);
       valueSlider2.setPaintTicks(true);
       valueSlider2.setPaintLabels(true);
       
       
       hueSlider.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                  
            	   H=slider.getValue();
               }
           }
       }
   );
       hueSlider2.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                  
            	  H2=slider.getValue();
               }
           }
       }
   );
       
       saturationSlider.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                  
            	   S=slider.getValue();
               }
           }
       }
   );
       
       saturationSlider2.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                  
            	   S2=slider.getValue();
               }
           }
       }
   );
       
       valueSlider.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                  
            	   V=slider.getValue();
               }
           }
       }
   );
       
       valueSlider2.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent evt) {
               JSlider slider = (JSlider)evt.getSource();

               if (!slider.getValueIsAdjusting()) {
                 
                   V2=slider.getValue();
               }
           }
       }
   );
        pack();
    }// </editor-fold>

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
            java.util.logging.Logger.getLogger(FineTune.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FineTune.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FineTune.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FineTune.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FineTune().setVisible(true);
            }
        });
    }
    
   
    // Variables declaration - do not modify
    private javax.swing.JSlider hueSlider;
    private javax.swing.JSlider hueSlider2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider saturationSlider;
    private javax.swing.JSlider saturationSlider2;
    private javax.swing.JSlider valueSlider;
    private javax.swing.JSlider valueSlider2;
    // End of variables declaration
}
