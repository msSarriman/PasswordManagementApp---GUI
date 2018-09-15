package secproj;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.util.Scanner;
import java.util.Random;

public class AesEncryption {
    public String saltAes=null;    
    public String passwordInput=null; 
    public String encData=null;
    public String decData=null;
    public String passAes=null;
    public String saltAesDec=null;
    public String passCipher=null;
    //public String passwordOutput=null;     
    private static final String ALGO = "AES";
    private byte[] keyValue;
    public byte[] key = new byte[16];
    private int keyLengthGl=0;
    static Random rand=new Random();
    static Charset UTF8 = Charset.forName("UTF-8");
    
    public AesEncryption(String key){
        keyValue = key.getBytes();    
    }
    
    /**
     * This constructor will create the following values:
     * @passAes         Password to be used in AES
     * @saltAes         Salt to be used after AES
     * @saltAesDec      Salt without AES
     * @encData         Input '@param password' after encryption
     * @decData         Input '@param password' after decryption
     * @param conNum    Random @param that can define the use of this constructor
     * @param password  User's password that will be hashed and AESed.
     */
    public AesEncryption(int conNum,String password){
        passwordInput=password;
        pass16Corr(passwordInput);                      // here the key is created
        passAes = new String(key, UTF8); 
        saltAes = saltCreation(passAes);   // here the salt is created
        try{            
            AesEncryption aes = new AesEncryption(passAes); //key here must be 16 bytes
            encData = aes.encrypt(passwordInput);
            saltAes = aes.encrypt(saltAes);
            decData = aes.decrypt(encData);
            saltAesDec = aes.decrypt(saltAes);
            System.lineSeparator();
        } catch (Exception ex){
            Logger.getLogger(AesEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AesEncryption(String passTry, String passRetr){     
        pass16Corr(passTry);                      // here the key is created
        passAes = new String(key, UTF8); 
        try{            
            AesEncryption aes = new AesEncryption(passAes); //key here must be 16 bytes
            decData = aes.decrypt(passRetr);
            System.lineSeparator();
        } catch (Exception ex){
            Logger.getLogger(AesEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public AesEncryption(int x, String pass, String passToEnc){     
        pass16Corr(pass);                      // here the key is created
        passAes = new String(key, UTF8);
        passwordInput=pass;
        try{            
            AesEncryption aes = new AesEncryption(passAes); //key here must be 16 bytes
            encData = aes.encrypt(passToEnc);
            passCipher=aes.encrypt(pass);
            decData = aes.decrypt(passToEnc);
            System.lineSeparator();
        } catch (Exception ex){
            Logger.getLogger(AesEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String encrypt(String Data) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }
    
    public String decrypt(String encryptedData) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    
    private Key generateKey() throws Exception{
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public void pushBackKey(byte bt){
        key[keyLengthGl++]=bt;
    }    
    
    /**
     * 
     * @param pass
     * @return 
     * This function takes user's password, and always converts it to 16bytes 
     * through deterministic manners.
     * That algorithm is necessary, in order to use an input key in AES.
     * Once the key gets to 16 bytes, it gets hashed in order to not used as it 
     * is in the AES encryption.
     */
    public void pass16Corr(String pass){
        char[] passC = new char[16];
        byte[][] hashMatrix = new byte[4][4];
        int[][] hashMatrixPointer = new int[5][5];        
        int len=pass.length();
        int k=len;
        System.out.println("k is: <"+k+">");
        System.out.println("length is is: <"+len+">");  

        if(len<16){
            //passC=pass.toCharArray();
            for(int i=0;i<len;i++){
                passC[i]=pass.charAt(i);
            }
            int l=1;
            for(int i=0;i<16-len;i++){
                passC[i+len]=pass.charAt(len-l++);
                //System.out.println("debug line");
            }
        }else if(len>16){
            for(int i=0;i<8;i++){
                passC[i]=pass.charAt(i);
            }
            for(int i=0;i<8;i++){           
                passC[i+8]=pass.charAt(len-i-1);
            }
        }else{
            passC=pass.toCharArray();
        }
        // So far the key always expands or srhrinks to 16 characters.

        int simpleCounter=0; //used to access the chars int passC
        for(int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                hashMatrixPointer[i][j]=(int)passC[simpleCounter++]%16;
                hashMatrixPointer[i][4]+=(int)hashMatrixPointer[i][j]; //sum creation for j=5
                hashMatrixPointer[4][j]+=(int)hashMatrixPointer[i][j];
            }
        }
        simpleCounter=0;
        for(int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                hashMatrix[i][j]=(byte)(passC[simpleCounter]^passC[hashMatrixPointer[i][j]]);  
                hashMatrix[i][j]=(byte)(hashMatrix[i][j]^hashMatrixPointer[i][4]);
                hashMatrix[i][j]=(byte)(hashMatrix[i][j]^hashMatrixPointer[4][j]);              
                hashMatrix[i][j]=(byte)(hashMatrix[i][j]^passC[simpleCounter++]);
                pushBackKey(hashMatrix[i][j]); 
            }            
        }
    }
    
    
    /**
     * 
     * @param passC
     * @return salt
     * This function creates the salt, necessary for the AES encryption to avoid patterns.
     * It is based on a rand generator inside the ascii limits, and adds (int)33 to the
     * result if it is not 33+, which means its not a representable symbol. 
     */
    public String saltCreation(String passC){
        System.out.println(passC);
        char[] salt = new char[16];
        for(int i=0;i<16;i++){
            int j=rand.nextInt(128)+1;
            salt[i]=(char)(key[i]^(byte)j);
            if((int)salt[i]<33)
                salt[i]+=33;
            if((int)salt[i]==127)
                salt[i]-=15;
        }
        passC = new String(salt);
        return passC;
        
    }
    
    public static void main(String[] args) {
        /*Scanner sc = new Scanner(System.in);
        String passwordInput=sc.nextLine();*/
        /*pass16Corr(passwordInput);                      // here the key is created
        saltAes = saltCreation(Arrays.toString(key));   // here the salt is created*/
        /*Charset UTF8 = Charset.forName("UTF-8");
        String passAes = new String(key, UTF8);
        try{
        AesEncryption aes = new AesEncryption(passAes); //key here must be 16 bytes
        encData = aes.encrypt("diaman");
        //System.out.println("Encrypted data - " + encData);
        decData = aes.decrypt(encData);
        //System.out.println("Decrypted Data - " + decData);
        } catch (Exception ex){
        Logger.getLogger(AesEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }*/      
    }    
}