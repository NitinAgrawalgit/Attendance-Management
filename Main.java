import com.mysql.cj.protocol.ResultStreamer;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class InvalidData extends JFrame{
    JFrame jf;
    JPanel jp;
    JLabel l;

    InvalidData(){
        jf = new JFrame("Error!");
        jp = new JPanel();
        jf.add(jp);

        l = new JLabel("Invalid Username / Password");
        l.setBounds(20, 20, 250, 30);
        jp.add(l);

        jp.setLayout(null);
        jf.setSize(250, 100);
        jf.setVisible(true);
    }
}

class AttendanceMarked extends JFrame{
    JFrame jf;
    JPanel jp;
    JLabel l;

    AttendanceMarked(){
        jf = new JFrame("Message");
        jp = new JPanel();
        jf.add(jp);

        l = new JLabel("Attendance Marked !");
        l.setBounds(50, 30, 250, 30);
        jp.add(l);

        jp.setLayout(null);
        jf.setSize(250, 130);
        jf.setVisible(true);
    }
}

class ViewAttendance extends JFrame implements ActionListener{
    JFrame jf;
    JPanel jp;

    JLabel []jl;
    JProgressBar []pb;
    JButton []jb;
    JButton menu;
    int totalRecords;
    int totalValue;

    ViewAttendance() {
        jf = new JFrame("Attendance Manager");
        jp = new JPanel();
        jf.add(jp);

        jl = new JLabel[7];
        pb = new JProgressBar[7];
        jb = new JButton[7];
        int []values = new int[7];
        totalValue = 0;

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Operating Systems");
        labels.add("Wireless Communication");
        labels.add("Compiler Design");
        labels.add("Information Theory");
        labels.add("Computer Graphics");
        labels.add("Analysis of Algorithms");
        labels.add("TOTAL");

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "root");

            PreparedStatement psmt = con.prepareStatement("SELECT SUM(os) FROM attendsheet");
            ResultSet rs = psmt.executeQuery();
            if(rs.next()){
                values[0] = rs.getInt(1);
            }
            totalValue += values[0];

            PreparedStatement psmt1 = con.prepareStatement("SELECT SUM(wc) FROM attendsheet");
            ResultSet rs1 = psmt1.executeQuery();
            if(rs1.next()){
                values[1] = rs1.getInt(1);
            }
            totalValue += values[1];

            PreparedStatement psmt2 = con.prepareStatement("SELECT SUM(cd) FROM attendsheet");
            ResultSet rs2 = psmt2.executeQuery();
            if(rs2.next()){
                values[2] = rs2.getInt(1);
            }
            totalValue += values[2];

            PreparedStatement psmt3 = con.prepareStatement("SELECT SUM(itc) FROM attendsheet");
            ResultSet rs3 = psmt3.executeQuery();
            if(rs3.next()){
                values[3] = rs3.getInt(1);
            }
            totalValue += values[3];

            PreparedStatement psmt4 = con.prepareStatement("SELECT SUM(cgm) FROM attendsheet");
            ResultSet rs4 = psmt4.executeQuery();
            if(rs4.next()){
                values[4] = rs4.getInt(1);
            }
            totalValue += values[4];

            PreparedStatement psmt5 = con.prepareStatement("SELECT SUM(aoa) FROM attendsheet");
            ResultSet rs5 = psmt5.executeQuery();
            if(rs5.next()){
                values[5] = rs5.getInt(1);
            }
            totalValue += values[5];

            values[6] = totalValue;
            PreparedStatement psmt6 = con.prepareStatement("SELECT COUNT(date) FROM attendsheet");
            ResultSet rs6 = psmt6.executeQuery();
            if(rs6.next()){
                totalRecords = rs6.getInt(1);
            }

            for(int i = 0; i < 7; i++){
                jl[i] = new JLabel(labels.get(i));
                jl[i].setBounds(20, 30 + (50 * i), 150, 25);
                jp.add(jl[i]);

                pb[i] = new JProgressBar(0, 100);
                pb[i].setBounds(200, 30 + (50 * i), 300, 25);
                pb[i].setValue((values[i] * 100) / totalRecords);
                jp.add(pb[i]);

                jb[i] = new JButton();
                jb[i].setBounds(520, 30 + (50 * i), 80, 25);
                jb[i].setText("" + values[i] + " / " + totalRecords);
                jp.add(jb[i]);
            }
            jb[6].setText("" + values[6] + " / " + (totalRecords*6));
            pb[6].setValue((values[6] * 100) / (totalRecords*6));

            jl[6].setBounds(20, 30 + (50 * 7), 150, 25);
            pb[6].setBounds(200, 30 + (50 * 7), 300, 25);
            jb[6].setBounds(520, 30 + (50 * 7), 80, 25);

        }catch(Exception e){
            System.out.println("Exception occured: " + e);
        }

        menu = new JButton("Menu");
        menu.setBounds(220, 520, 200, 25);
        menu.addActionListener(this);
        jp.add(menu);

        jp.setLayout(null);
        jf.setSize(650, 600);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == menu){
            new Menu();
            jf.dispose();
        }
    }
}

class MarkAttendance extends JFrame implements ActionListener{
    JFrame jf;
    JPanel jp;

    JButton b1, mark;
    JTextField t1;
    JCheckBox []subjects;
    String dateString;

    MarkAttendance(){
        jf = new JFrame("Attendance Manager");
        jp = new JPanel();
        jf.add(jp);

        subjects = new JCheckBox[6];
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Operating Systems");
        labels.add("Wireless Communication");
        labels.add("Compiler Design");
        labels.add("Information Theory");
        labels.add("Computer Graphics");
        labels.add("Analysis of Algorithms");

        b1 = new JButton("Date");
        b1.setBounds(20, 30, 80, 25);
        jp.add(b1);

        t1 = new JTextField();
        t1.setBounds(120, 30, 150, 25);
        dateString = java.time.LocalDate.now().toString();
        t1.setText(dateString);
        jp.add(t1);

        for(int i = 0; i < 6; i++){
            subjects[i] = new JCheckBox(labels.get(i));
            subjects[i].setBounds(20, 100 + (40 * i), 200, 25);
            jp.add(subjects[i]);
        }

        mark = new JButton("Mark Attendance");
        mark.setBounds(20, 350, 290, 25);
        jp.add(mark);
        mark.addActionListener(this);

        jp.setLayout(null);
        jf.setSize(350, 450);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "root");

            dateString = t1.getText();

            PreparedStatement psmt = con.prepareStatement("INSERT INTO attendsheet VALUES (?, ?, ?, ?, ?, ?, ?)");

            psmt.setString(1, dateString);
            for(int i = 1; i <= 6; i++){
                if(subjects[i-1].isSelected()){
                    psmt.setInt(i+1, 1);
                }else {
                    psmt.setInt(i+1, 0);
                }
            }

            psmt.execute();


        } catch(Exception e){
            System.out.println("Exception: " + e);
        }

        new Menu();
        new AttendanceMarked();
        jf.dispose();
    }
}

class Menu extends JFrame implements ActionListener{
    JFrame jf;
    JPanel jp;
    JButton b1, b2, b3;

    Menu(){
        jf = new JFrame("Attendance Manager");
        jp = new JPanel();
        jf.add(jp);

        b1 = new JButton("Mark Attendance");
        b1.setBounds(100, 80, 200, 30);
        jp.add(b1);
        b1.addActionListener(this);

        b2 = new JButton("View Attendance");
        b2.setBounds(100, 130, 200, 30);
        jp.add(b2);
        b2.addActionListener(this);

        b3 = new JButton("Sign Out");
        b3.setBounds(100, 230, 200, 30);
        jp.add(b3);
        b3.addActionListener(this);

        jp.setLayout(null);
        jf.setSize(400, 330);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b2){
            new ViewAttendance();
            jf.dispose();
        }else if(ae.getSource() == b1){
            new MarkAttendance();
            jf.dispose();
        }else if(ae.getSource() == b3){
            new LoginPage();
            jf.dispose();
        }
    }
}

class LoginPage extends JFrame implements ActionListener{
    JFrame jf;
    JPanel jp;
    JLabel l1, l2, name;
    JTextField t1;
    JPasswordField p1;
    JButton b1;

    LoginPage(){
        jf = new JFrame("Attendance Manager");
        jp = new JPanel();
        jf.add(jp);

        l1 = new JLabel("Username");
        l1.setBounds(20, 20, 80, 25);
        jp.add(l1);

        l2 = new JLabel("Password");
        l2.setBounds(20, 60, 80, 25);
        jp.add(l2);

        t1 = new JTextField(20);
        t1.setBounds(100, 20, 240, 25);
        jp.add(t1);

        p1 = new JPasswordField(20);
        p1.setBounds(100, 60, 240, 25);
        jp.add(p1);

        b1 = new JButton("Log In");
        b1.setBounds(110, 130, 80, 25);
        b1.addActionListener(this);
        jp.add(b1);

        name = new JLabel("Made By:  NITIN AGRAWAL");
        name.setBounds(220, 240, 200, 25);
        jp.add(name);

        jp.setLayout(null);
        jf.setSize(400, 300);
        //jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try{
            String userName, pswd;
            userName = t1.getText();
            pswd = p1.getText();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendancedb", "root", "root");

            PreparedStatement psmt = con.prepareStatement("SELECT * FROM logininfo WHERE username=?");
            psmt.setString(1, userName);

            ResultSet rs = psmt.executeQuery();
            if(rs.next()){
                if(rs.getString(2).equals(pswd)){
                    new Menu();
                    jf.dispose();
                }else {
                    new InvalidData();
                }
            }else {
                new InvalidData();
            }

        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        LoginPage obj1 = new LoginPage();
    }
}