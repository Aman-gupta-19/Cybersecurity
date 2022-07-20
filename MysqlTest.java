import java.security.*;
import java.security.spec.KeySpec;
import java.sql.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;


import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
public class MysqlTest {
    private static SecretKeySpec secretKey;
    private static byte[] key;

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

    public static String encrypt(final String strToEncrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    public static void main(String args[]){
        final String secretKey = "aes_key";

        String originalString = "yahoooo";
        String encryptedString = MysqlTest.encrypt(originalString, secretKey) ;
        String decryptedString = MysqlTest.decrypt(encryptedString, secretKey) ;

        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);

        System.out.println("BQRSfo5D3hbyHdpKprDXgA==");
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection con=DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/sap_user","root","root");
////here sonoo is database name, root is username and password
//            Statement stmt=con.createStatement();
//
//            //insert query
////            stmt.executeUpdate("INSERT INTO user " + "VALUES (11, 'aes-done', HEX( AES_ENCRYPT( 'yahoooo', 'aes_key' )))" + "AS encString");
////            stmt.executeUpdate("INSERT INTO aes_user " + "VALUES ('"+1+"','"+my_data+"','"+"yoooo"+"')");
//            stmt.executeUpdate("INSERT INTO user_table_test3 (user_name,pass,u_name,pn_no,Address,Personal_id)" + "VALUES (\"Aman_patel\",\"Ag@1234\",\"Aman\",1234567890,\"abc,on,canada\",\"r1266thy\")");
//
////            ResultSet rs=stmt.executeQuery("select * from user");
//            ResultSet rs=stmt.executeQuery("select * from user_table_test3");
//
//            while(rs.next())
////                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
//                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5)+"  "+rs.getString(6));
//
//            ResultSet match = stmt.executeQuery("select * from user where user_name='md5' and pass=md5('Pqr@123')");
//            if(!match.next())
//            {
//                System.out.println("no match found");
//            }
//            else System.out.println("valid cred");
//            con.close();
//        }catch(Exception e){ System.out.println(e);}
    }
}
