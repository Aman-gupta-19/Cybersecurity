import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.lang.Exception;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Base64;


class CreateLoginForm extends JFrame implements ActionListener {

    JButton b1, b2, b3;
    JPanel newPanel;
    JLabel userLabel, passLabel, p1;
    final JTextField userfield, passfield;

    JLabel l_username, l_password, l_uname, l_contact, l_email, l_address;
    final JTextField username, password, uname, contact, email, address;
    int x=0;

    CreateLoginForm() {
        userLabel = new JLabel();
        userLabel.setText("Username");

        p1 = new JLabel();
        p1.setText("");
        userfield = new JTextField(15);

        passLabel = new JLabel();
        passLabel.setText("Password");

        passfield = new JPasswordField(15);


        b1 = new JButton("Submit");
        b2 = new JButton("Sign up");
        b3 = new JButton("new user?");



        newPanel = new JPanel(new GridLayout(10, 1));
        newPanel.add(userLabel);
        newPanel.add(userfield);
        newPanel.add(passLabel);
        newPanel.add(passfield);
        newPanel.add(b1);
        newPanel.add(b3);

        add(newPanel, BorderLayout.CENTER);
        b1.addActionListener(this);
        setTitle("LOGIN FORM");

        l_username = new JLabel();
        l_username.setText("Username");
        l_password = new JLabel();
        l_password.setText("Password");
        l_uname = new JLabel();
        l_uname.setText("Name");
        l_contact = new JLabel();
        l_contact.setText("Contact");
        l_email = new JLabel();
        l_email.setText("Email");
        l_address = new JLabel();
        l_address.setText("Address");

        username = new JTextField(15);
        password = new JPasswordField(15);
        uname = new JTextField(15);
        contact = new JTextField(15);
        email = new JTextField(15);
        address = new JTextField(20);

        newPanel.add(l_username).setVisible((false));
        newPanel.add(username).setVisible((false));
        newPanel.add(l_password).setVisible((false));
        newPanel.add(password).setVisible((false));
        newPanel.add(l_uname).setVisible((false));
        newPanel.add(uname).setVisible((false));
        newPanel.add(l_contact).setVisible((false));
        newPanel.add(contact).setVisible((false));
        newPanel.add(l_email).setVisible((false));
        newPanel.add(email).setVisible((false));
        newPanel.add(l_address).setVisible((false));
        newPanel.add(address).setVisible((false));
        newPanel.add(b2).setVisible((false));
        b3.addActionListener(e ->
        {
            newPanel.add(l_username).setVisible((true));
            newPanel.add(username).setVisible((true));
            newPanel.add(l_password).setVisible((true));
            newPanel.add(password).setVisible((true));
            newPanel.add(l_uname).setVisible((true));
            newPanel.add(uname).setVisible((true));
            newPanel.add(l_contact).setVisible((true));
            newPanel.add(contact).setVisible((true));
            newPanel.add(l_email).setVisible((true));
            newPanel.add(email).setVisible((true));
            newPanel.add(l_address).setVisible((true));
            newPanel.add(address).setVisible((true));
            newPanel.add(b2).setVisible((true));
        });

        b2.addActionListener(e ->
        {
            String userValue = userfield.getText();
            String passValue = passfield.getText();
            String usern = username.getText();
            String passw = password.getText();
            String u_name = uname.getText();
            String u_contact = contact.getText();
            String u_email = email.getText();
            String u_address = address.getText();
            
            newPanel.add(l_username).setVisible((false));
            newPanel.add(username).setVisible((false));
            newPanel.add(l_password).setVisible((false));
            newPanel.add(password).setVisible((false));
            newPanel.add(l_uname).setVisible((false));
            newPanel.add(uname).setVisible((false));
            newPanel.add(l_contact).setVisible((false));
            newPanel.add(contact).setVisible((false));
            newPanel.add(l_email).setVisible((false));
            newPanel.add(email).setVisible((false));
            newPanel.add(l_address).setVisible((false));
            newPanel.add(address).setVisible((false));
            newPanel.add(b2).setVisible((false));

            AES.encrypt(usern,passw,u_name,u_contact,u_email,u_address);
        });
    }

    public void actionPerformed(ActionEvent ae)
    {
        String userValue = userfield.getText();
        String passValue = passfield.getText();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sap_user", "root", "root");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from user_table_test3");

            while (rs.next()) {
//
            }

            ResultSet match = stmt.executeQuery("select * from app_user_table where username='" + userValue + "' and pass=md5('"+passValue+"')");
            if (!match.next()) {
                WelcomePage page = new WelcomePage();
                page.setVisible(true);
                JLabel lbl = new JLabel("Incorrect Credentials Please Try Again Later");
                page.getContentPane().add(lbl);
                System.out.println("no match found");
            } else {
                System.out.println("valid cred");
                AES.decrypt(match.getString(3), match.getString(4), match.getString(5), match.getString(6));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class MainPage
{
    public static void main(String arg[])
    {
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try
        {
            CreateLoginForm form = new CreateLoginForm();
            form.setSize(500,900);
            form.setVisible(true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
