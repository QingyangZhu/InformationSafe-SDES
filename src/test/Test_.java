package test;

import src.SDES_;

import java.util.Arrays;

public class Test_ {
    public static void main(String[] args) {
        SDES_ sdes = new SDES_();
        //int [] key ={1,0,1,0,0,0,0,0,1,0};
        int [] key = {0, 0, 1, 1, 0, 1, 1, 0, 1, 0};
        int [] plaintext = {1, 1, 1, 1, 1, 1, 1, 1};
        int  [] ciphertext = sdes.encrypt(key,plaintext);
        System.out.println(Arrays.toString(ciphertext));
        int [] decrypted = sdes.decrypt(key,ciphertext);
        System.out.println(Arrays.toString(decrypted));

        String plaintext_ = "yuanshen";
        String key_ = "11111111111";
        String ciphertext_ = sdes.encrypt(key_,plaintext_);
        System.out.println(ciphertext_);
        String decrypted_ = sdes.decrypt(key_,"yF\u009CSÆË\u0019S");
        System.out.println(decrypted_);
    }
}
