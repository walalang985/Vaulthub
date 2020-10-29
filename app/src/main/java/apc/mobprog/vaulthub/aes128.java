package apc.mobprog.vaulthub;
import android.util.Log;
import androidx.annotation.NonNull;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.util.*;
public class aes128 {
    private static final String KEY = "x/A?D(G+KbPeShVk";
    private static final String ENCODING = "UTF-8";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final String ALGO ="AES";
    public static String encrypt(@NonNull String txt){
        String res = "";
        try {
            Cipher a = Cipher.getInstance( TRANSFORMATION );
            byte[] b = KEY.getBytes(ENCODING);
            SecretKeySpec sks = new SecretKeySpec( b, ALGO );
            IvParameterSpec ivs = new IvParameterSpec( b );
            a.init(Cipher.ENCRYPT_MODE, sks, ivs);
            byte[] cipher = a.doFinal(txt.getBytes("UTF-8"));
            Base64.Encoder enc = Base64.getEncoder();
            res = enc.encodeToString( cipher );
        }catch (Exception e){
            Log.e(e.toString(), "Error encrypting text");
        }
        return res;
    }
    public static String decrypt(@NonNull String txt){
        String res = "";
        try {
            Cipher a = Cipher.getInstance( TRANSFORMATION );
            byte[] b = KEY.getBytes(ENCODING);
            SecretKeySpec sks = new SecretKeySpec( b, ALGO );
            IvParameterSpec ivs = new IvParameterSpec( b );
            a.init(Cipher.ENCRYPT_MODE, sks, ivs);
            Base64.Decoder dec = Base64.getDecoder();
            byte[] cipher = dec.decode( txt.getBytes("UTF-8") );
            res = new String(a.doFinal(cipher), "UTF-8");
        }catch (Exception e){
            Log.e( e.toString(), "Error decrypting text" );
        }
        return res;
    }
}