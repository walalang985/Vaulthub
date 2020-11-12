package apc.mobprog.vaulthub;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import androidx.annotation.NonNull;
public class RSA {
    String res;
    PrivateKey privKey;
    PublicKey pubKey;
    final String dir = "/sdcard/Android/data/apc.mobprog.vaulthub/keySet1";
    final String[] privaKey = {dir + "/loginKeys/privateKey.key", dir + "/userKeys/privateKey.key"};
    final String[] publKey = {dir + "/loginKeys/publicKey.key", dir + "/userKeys/publicKey.key"};
    //constructor that uses the PrivateKey
    public RSA(PrivateKey privateKey){
        this.privKey = privateKey;
    }
    //constructor that uses the PublicKey
    public RSA(PublicKey publicKey){
        this.pubKey = publicKey;
    }
    public RSA(){
        //empty constructor
    }
    public String getPrivateLoginKeys(){
        return this.privaKey[0];
    }
    public String getPublicLoginKeys(){
        return this.publKey[0];
    }
    public String getPrivateUserKeys(){
        return this.privaKey[1];
    }
    public String getPublicUserKeys(){
        return this.publKey[1];
    }
    public String encrypt(@NonNull String text){
        try{
            Cipher cipher = Cipher.getInstance( "RSA" );
            cipher.init( Cipher.ENCRYPT_MODE, privKey );
            this.res = Base64.getEncoder().encodeToString( cipher.doFinal(text.getBytes()) );
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public String decrypt(@NonNull String text){
        try{
            Cipher cipher = Cipher.getInstance( "RSA" );
            cipher.init( Cipher.DECRYPT_MODE, pubKey );
            this.res = new String(cipher.doFinal(Base64.getDecoder().decode( text )));
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public void generateKeys(){
        if(!this.doKeysExists()){
            try{
                KeyPairGenerator keygen0 = KeyPairGenerator.getInstance( "RSA" );
                KeyPairGenerator keygen1 = KeyPairGenerator.getInstance( "RSA" );
                keygen0.initialize(2048);
                keygen1.initialize(2048);
                KeyPair kp0 = keygen0.generateKeyPair();
                KeyPair kp1 = keygen1.generateKeyPair();
                File[] files = {new File( privaKey[0] ), new File( privaKey[1] ), new File( publKey[0] ), new File( publKey[1] )};
                if(files[0]!=null && files[1]!=null&&files[2]!=null&&files[3]!=null){
                    for(int i = 0; i < files.length;i++){
                        files[i].getParentFile().mkdirs();
                    }
                }
                for (int i = 0; i < files.length;i++){
                    files[i].createNewFile();
                }
                ObjectOutputStream pub1,pub2,priv1,priv2;
                priv1 = new ObjectOutputStream( new FileOutputStream( files[0] ) );
                pub1 = new ObjectOutputStream( new FileOutputStream( files[2] ) );
                priv2 = new ObjectOutputStream( new FileOutputStream( files[1] ) );
                pub2 = new ObjectOutputStream( new FileOutputStream( files[3] ) );
                priv1.writeObject( kp0.getPrivate() );
                pub1.writeObject( kp0.getPublic() );
                priv2.writeObject( kp1.getPrivate() );
                pub2.writeObject( kp1.getPublic() );
                pub1.close();
                priv1.close();
                pub2.close();
                priv2.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public boolean doKeysExists(){
        File[] files = {new File( privaKey[0] ), new File( privaKey[1] ), new File( publKey[0] ), new File( publKey[1] )};
        Log.d("tag", Boolean.toString( files[0].exists() && files[1].exists() && files[2].exists() && files[3].exists() ) );
        return files[0].exists() && files[1].exists() && files[2].exists() && files[3].exists();
    }
}
