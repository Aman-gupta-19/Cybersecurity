import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Base64;

import static java.sql.Types.NULL;

public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    public String temp;

    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(final String username, final String password, final String name, final String contact, final String email, final String address) {
        try {
            setKey("aes_key");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            String usern = username;
            String passw = password;
            String uname = Base64.getEncoder().encodeToString(cipher.doFinal(name.getBytes("UTF-8")));
            String ucontact = Base64.getEncoder().encodeToString(cipher.doFinal(contact.getBytes("UTF-8")));
            String uemail = Base64.getEncoder().encodeToString(cipher.doFinal(email.getBytes("UTF-8")));
            String uaddress = Base64.getEncoder().encodeToString(cipher.doFinal(address.getBytes("UTF-8")));

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/sap_user","root","root");
                Statement stmt=con.createStatement();

                stmt.executeUpdate("INSERT INTO app_user_table " + "VALUES ('"+usern+"',md5('"+passw+"'),'"+uname+"','"+ucontact+"','"+uemail+"','"+uaddress+"')");

                con.close();
            }
            catch(Exception e){
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(final String name, final String contact, final String email, final String address) {
        try {
            setKey("aes_key");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            String uname =  new String(cipher.doFinal(Base64.getDecoder().decode(name)));
            String ucontact =  new String(cipher.doFinal(Base64.getDecoder().decode(contact)));
            String uemail =  new String(cipher.doFinal(Base64.getDecoder().decode(email)));
            String uaddress =  new String(cipher.doFinal(Base64.getDecoder().decode(address)));

            WelcomePage page = new WelcomePage();
            page.setVisible(true);

            JTextArea t1 = new JTextArea("Welcome: " +uname+ "\nBelow are your personal data safe with us\n\ncontact: " +ucontact+ "\n\nEmail: " +uemail+ "\n\nAddress: " +uaddress);
            Font font = new Font("Segoe Script", Font.BOLD, 30);
            t1.setFont(font);
//            page.getContentPane().add(wel_label);
            page.getContentPane().add(t1);

        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
