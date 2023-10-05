import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        String plaintext = "Hello World!A";
        plaintext = "0000111";
        int [] plaintext_arr = new int[plaintext.length()];
        ArrayList<int []> plaintext_list =new ArrayList<>();
        for(int i = 0;i < plaintext.length();i++){
            int plaintext_a = (int)plaintext.charAt(i);
            String binary = Integer.toBinaryString(plaintext_a);
            StringBuilder sb = new StringBuilder(binary);
            while (sb.length() < 8) {
                sb.insert(0, '0');
            }
            int [] plaintext_i =  new int[8];
            for (int j = 0; j < 8; j++) {
                plaintext_i[j] = sb.charAt(j) - '0';
            }
            plaintext_list.add(plaintext_i);
        }

        StringBuilder result = new StringBuilder();
        for (int[] arr : plaintext_list) {
            int num = 0;
            for (int i = 0; i < 8; i++) {
                num = num * 2 + arr[i];
            }
            result.append((char) num);
        }
        String resultStr = result.toString();
        System.out.println("结果："+resultStr);


        System.out.println(Arrays.toString(plaintext_arr));
        System.out.println(Arrays.toString(plaintext_list.get(1)));
    }
}