package apc.mobprog.vaulthub;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
public class RSA {
    final static String TARGETDIR1 = "/sdcard/VaultHub/loginKeys";
    final static String TARGETDIR2 = "/sdcard/VaultHub/userKeys";
    public final static String privateKey = TARGETDIR1 + "/private key.key";
    public final static String publicKey = TARGETDIR1 + "/public key.key";
    public final static String privateKey1 = TARGETDIR2 + "/private key.key";
    public final static String publicKey1 = TARGETDIR2 + "/public key.key";
    public static String encrypt(String text, PrivateKey pk) throws Exception {
        Cipher cipher = Cipher.getInstance( "RSA" );
        cipher.init( Cipher.ENCRYPT_MODE, pk );
        return Base64.getEncoder().encodeToString( cipher.doFinal(text.getBytes()) );
    }
    public static String decrypt(String text, PublicKey pk) throws Exception{
        Cipher cipher = Cipher.getInstance( "RSA" );
        cipher.init( Cipher.DECRYPT_MODE, pk );
        return new String(cipher.doFinal(Base64.getDecoder().decode( text )));
    }
    public static void writeLoginKeys() throws Exception{
        KeyPairGenerator key = KeyPairGenerator.getInstance( "RSA" );
        key.initialize( 2048 );
        KeyPair keyPair = key.generateKeyPair();
        File privateK = new File( privateKey );
        File publicK = new File( publicKey );
        if(privateK.getParentFile() != null){
            privateK.getParentFile().mkdirs();
        }
        privateK.createNewFile();
        if(publicK.getParentFile() != null){
            publicK.getParentFile().mkdirs();
        }
        publicK.createNewFile();
        ObjectOutputStream publicKOut = new ObjectOutputStream( new FileOutputStream( publicKey ) ), privateKOut = new ObjectOutputStream( new FileOutputStream( privateKey ) );
        publicKOut.writeObject( keyPair.getPublic() );
        privateKOut.writeObject( keyPair.getPrivate() );
        publicKOut.close();
        privateKOut.close();
    }
    public static void writeUserInfoKeys() throws Exception{
        KeyPairGenerator key = KeyPairGenerator.getInstance( "RSA" );
        key.initialize( 2048 );
        KeyPair keyPair = key.generateKeyPair();
        File privateK = new File( privateKey1 );
        File publicK = new File( publicKey1 );
        if(privateK.getParentFile() != null){
            privateK.getParentFile().mkdirs();
        }
        privateK.createNewFile();
        if(publicK.getParentFile() != null){
            publicK.getParentFile().mkdirs();
        }
        publicK.createNewFile();
        ObjectOutputStream publicKOut = new ObjectOutputStream( new FileOutputStream( publicKey1 ) ), privateKOut = new ObjectOutputStream( new FileOutputStream( privateKey1 ) );
        publicKOut.writeObject( keyPair.getPublic() );
        privateKOut.writeObject( keyPair.getPrivate() );
        publicKOut.close();
        privateKOut.close();
    }
    public static boolean doKeysExists(){
        File[] files = new File[]{new File( privateKey ), new File( publicKey ), new File( privateKey1 ), new File( publicKey1 ) };
        return files[0].exists() && files[1].exists() && files[2].exists() && files[3].exists();
    }
}