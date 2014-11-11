package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ClientConnection {

  public CipherInputStream cin;
  public CipherOutputStream cout;

  public ClientConnection(SecretKeySpec secretKeySpec, InputStream in, OutputStream out, String cipherImplementation) {
    Cipher decrypt, encrypt;
    try {
      decrypt = Cipher.getInstance(cipherImplementation);
      decrypt.init(Cipher.DECRYPT_MODE, secretKeySpec);
      cin = new CipherInputStream(in, decrypt);
      
      encrypt = Cipher.getInstance(cipherImplementation);
      encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      cout = new CipherOutputStream(out, encrypt);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }
  }
}
