
package chatting.application;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.net.*;
import java.io.*;
public class Server  implements ActionListener {
    
    JTextField text ;
    JPanel a1 ;
     static Box vertical = Box.createVerticalBox();
     static JFrame f = new JFrame();
     Boolean typing;
     
     static ServerSocket skt;
    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    
    Server()
    {
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       
       
        JPanel p1=new JPanel();
        p1.setBackground(new Color(38,25,227));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        f.add(p1);
        
        ImageIcon i1= new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 =i1.getImage().getScaledInstance(25, 25,Image.SCALE_DEFAULT);
        ImageIcon i3= new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5,20,25,25);
        p1.add(back);
        
        back.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent ae)
            {
                System.exit(0);
            }
        });
        
        ImageIcon i4= new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image i5 =i4.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT);
        ImageIcon i6= new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);
        
        
        
        JLabel name = new JLabel("Person 1");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("POPPINS",Font.BOLD,18));
        p1.add(name);
        
        JLabel status = new JLabel("Active Now");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("POPPINS",Font.BOLD,14));
        p1.add(status);
        
             Timer t = new Timer(1, new ActionListener(){
           public void actionPerformed(ActionEvent ae){
               if(!typing){
                   status.setText("Active Now");
               }
           }
       });
        
       t.setInitialDelay(2000);
       
         a1 = new JPanel();
//        a1.setBounds(5,75,440,470);
          a1.setFont(new Font("POPPINS",Font.PLAIN,17));
//        f.add(a1);
        
        JScrollPane sp = new JScrollPane(a1);
       sp.setBounds(5, 75, 440, 470);
       sp.setBorder(BorderFactory.createEmptyBorder());
       //f1.add(sp);
       
       ScrollBarUI ui = new BasicScrollBarUI()
       {
    	   protected JButton createDecreaseButton(int orientation)
    	   {
    	   JButton button = super.createDecreaseButton(orientation);
    	   button.setBackground(new Color(189, 196, 212));
    	   button.setForeground(Color.WHITE);
    	   this.thumbColor = new Color(189, 196, 212);
    	   return button;
    	   }
       
       protected JButton createIncreaseButton(int orientation)
       {
	   JButton button = super.createIncreaseButton(orientation);
	   button.setBackground(new Color(189, 196, 212));
	   button.setForeground(Color.WHITE);
	   this.thumbColor = new Color(189, 196, 212);
	   return button;
   }
};
        sp.getVerticalScrollBar().setUI(ui);
        f.add(sp);
        
         text = new JTextField();
        text.setBounds(5,550,310,40);
        text.setFont(new Font("POPPINS",Font.PLAIN,16));
        f.add(text);
        
          text.addKeyListener(new KeyAdapter(){
           public void keyPressed(KeyEvent ke){
               status.setText("typing...");
               
               t.stop();
               
               typing = true;
           }
           
           public void keyReleased(KeyEvent ke){
               typing = false;
               
               if(!t.isRunning()){
                   t.start();
               }
           }
       });
          
        JButton send= new JButton("Send");
        send.setBounds(319,550,123,40);
        send.setBackground(new Color(38,25,227));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("POPPINS",Font.PLAIN,16));
        f.add(send);
        
         f.getContentPane().setBackground(Color.WHITE);
       f.setLayout(null);
        f.setSize(450,600);
        f.setLocation(200,50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
         f.setVisible(true);
    }
    public void actionPerformed (ActionEvent ae)
    {
        try{
      String out =text.getText();
      
      JPanel p2 = formatLabel(out);
      
      
      a1.setLayout(new BorderLayout());
     
      
      JPanel right = new JPanel(new BorderLayout());
      right.add(p2,BorderLayout.LINE_END);// right side alignment of messages
      vertical.add(right);// for messages to be placed one below the other
      vertical.add(Box.createVerticalStrut(15));
      
      a1.add(vertical,BorderLayout.PAGE_START);
      
      dout.writeUTF(out);
      
      text.setText("");
//      f.repaint();
//      f.invalidate();
//      f.validate();
    }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static JPanel formatLabel(String out)
    {
        JPanel panel =new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width:150px\">" + out +"</p><html>");
       
      
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(34,88,240));
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));
         panel.add(output);
         
         Calendar cal =  Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
         
         JLabel time = new JLabel();
         time.setText(sdf.format(cal.getTime()));
         panel.add(time);
        return panel;
    }
    public static void main(String[] args)
    {
        new Server().f.setVisible(true);
        
        String msg="";
        try
        {
             skt = new ServerSocket(6001);
            while(true)
            {
                 s= skt.accept();
                 din = new DataInputStream(s.getInputStream());
               dout = new DataOutputStream(s.getOutputStream());
                while(true)
                {
                     msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    
                    JPanel left = new JPanel( new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();// for frame refreshing 
                }
                
                
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
