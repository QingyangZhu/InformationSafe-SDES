import java.util.Arrays;

public class Test_ {
    public static void main(String[] args) {
        SDES_ sdes = new SDES_();
        //int [] key ={1,0,1,0,0,0,0,0,1,0};
        int [] key = {1,0,1,0,1,0,1,0,1,0};
        int [] plaintext = {1, 1, 1, 1, 1, 1, 1, 1};
        //int [] plaintext = {1, 0, 0, 1, 1, 0, 1, 0};
        //int [] ciphertext = {0,1,1,0,0,1,1,0};
        /*System.out.println(Arrays.toString(sdes.P_BOX_substitute(key,SDES_.P10)));
        System.out.println(Arrays.toString(sdes.LeftShift(sdes.P_BOX_substitute(key,SDES_.P10),SDES_.LeftShift1)));
        System.out.println(Arrays.toString(sdes.LeftShift(sdes.P_BOX_substitute(key,SDES_.P10),SDES_.LeftShift2)));
        int [] k1 = sdes.P_BOX_substitute(sdes.LeftShift(sdes.P_BOX_substitute(key,SDES_.P10),SDES_.LeftShift1),SDES_.P8);
        System.out.println(Arrays.toString(k1));
        System.out.println(Arrays.toString(sdes.P_BOX_substitute(key,SDES_.P8)));*/
        int  [] ciphertext = sdes.encrypt(key,plaintext);
        System.out.println(Arrays.toString(ciphertext));
        int [] decrypted = sdes.decrypt(key,ciphertext);
        System.out.println(Arrays.toString(decrypted));
    }
}
