import java.util.Arrays;

public class SDES_ {
    //分组及密文长度
    private static final int BLOCK_LENGTH = 8;
    private static final int KEY_LENGTH = 10;
    //密钥拓展盒，包括直接置换盒、压缩置换盒、移位变化1位、移位变化2位
    public static final int [] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int [] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    public static final int [] LeftShift1 = {2, 3, 4, 5, 1};
    public static final int [] LeftShift2 = {3, 4, 5, 1, 2};
    //初始置换盒
    private static final int [] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    //最终置换盒
    private static final int [] IP_1 = {4, 1, 3, 5, 7, 2, 8, 6};
    //拓展置换
    private static final int [] EP_BOX = {4, 1, 2, 3, 2, 3, 4, 1};
    //替换盒
    private static final int [][] S_BOX1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2}};
    private static final int [][] S_BOX2 = {{0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3}};
    //直接置换
    private static final int [] SP_BOX = {2, 4, 3, 1};

    //根据P_BOX对密钥进行置换,也可以用于初始置换和最终置换
    public int[] P_BOX_substitute(int[] key, int [] P_BOX) {

        int[] result = new int[P_BOX.length];
        for (int i = 0; i < P_BOX.length; i++) {
            result[i] = key[P_BOX[i]-1];
        }
        //System.out.println("P_BOX_substitute:"+Arrays.toString(result));
        return result;
    }
    //根据移位变换盒进行移位
    public int[] LeftShift(int[] key,int[] LeftShift) {
        int shift_count = LeftShift[0]-1;
        int[] result = new int[key.length];
        for (int i = 0; i < key.length/2; i++) {
            result[i] = key[(i+shift_count)%(key.length/2)];
        }
        for (int i = key.length/2; i < key.length; i++) {
            if (i+shift_count < key.length) result[i] = key[(i+shift_count)];
            else result[i] = key[i+shift_count-key.length/2];
        }
        //result[key.length-1] = key[key.length/2];
        //System.out.println("LeftShift:"+Arrays.toString(result));
        return result;
    }
    //S-DES的核心部分：
    //密钥在这个阶段会被分成左右两个部分，左部分先保留，
    //右部分会先进行拓展置换，再与拓展密钥进行异或运算，再根据替换盒S-BOX进行压缩
    //最后根据SP-BOX对右部分进行直接置换
    //然后将得到的右部分与左部分进行异或，得到新的密钥


    //根据EP_BOX对经过了初始置换的密钥进行拓展置换，输入为一个四位的密钥（即完整密钥的一半）
    public int [] expand_substitute(int [] right_key){
        int [] res = new int [8];
        for (int i = 0; i < 8; i++) {
            res[i] = right_key[EP_BOX[i]-1];
        }
        //System.out.println("expand_substitute:"+Arrays.toString(res));
        return res;
    }
    //加轮密钥，将拓展置换后得到的新密钥与ki进行异或运算
    public int [] round_key(int [] e_key,int [] k_i){
        int [] res = new int[e_key.length];
        //int [] k1 = P_BOX_substitute(LeftShift(P_BOX_substitute(key,P10),SDES_.LeftShift1),P8);
        for (int i = 0; i < e_key.length; i++) {
            res[i] = e_key[i] ^ k_i[i];
        }
        //System.out.println("round_key:"+Arrays.toString(res));
        return res;
    }
    //根据替换盒S-BOX1 S-BOX2 对经过异或运算后的密钥进行压缩置换
    public int [] compress_substitute(int [] r_key){
        int [] res = new int[4];
        int x1 = r_key[0]*2+r_key[3];
        int y1 = r_key[1]*2+r_key[2];
        int x2 = r_key[4]*2+r_key[7];
        int y2 = r_key[5]*2+r_key[6];
        res[0] = S_BOX1[x1][y1]/2;
        res[1] = S_BOX1[x1][y1]%2;
        res[2] = S_BOX2[x2][y2]/2;
        res[3] = S_BOX2[x2][y2]%2;
       // System.out.println("compress_substitute:"+Arrays.toString(res));
        return res;
    }
    //根据替换盒SP-BOX进行直接置换,此时可以直接将函数P_BOX_substitute调用

    //然后将上述步骤中得到的右部分与左部分进行异或，得到新的密钥
    //然后将新的密钥左右部分交换，再重复上面的核心步骤得到新的密钥，只不过需要注意的是此时要加入的是k2
    //最后将这个密钥进行IP_1进行置换，完成加密过程
    public int [] encrypt(int [] key,int [] plaintext){
        int [] ip = P_BOX_substitute(plaintext,IP);
        int [] k1 = P_BOX_substitute(LeftShift(P_BOX_substitute(key,P10),SDES_.LeftShift1),P8);
        int [] k2 = P_BOX_substitute(LeftShift(P_BOX_substitute(key,P10),SDES_.LeftShift2),P8);
        int [] left_ip = Arrays.copyOfRange(ip,0,4);
        int [] right_ip = Arrays.copyOfRange(ip,4,8);
        left_ip = round_key(left_ip,P_BOX_substitute(compress_substitute(round_key(expand_substitute(right_ip),k1)),SP_BOX));
        right_ip = round_key(right_ip,P_BOX_substitute(compress_substitute(round_key(expand_substitute(left_ip),k2)),SP_BOX));
        int [] res = new int[8];
        System.arraycopy(right_ip,0,res,0,4);
        System.arraycopy(left_ip,0,res,4,4);
        res = P_BOX_substitute(res,IP_1);
        return res;
    }
    //解密过程是加密过程的逆运算
    public int [] decrypt(int [] key,int [] ciphertext){
        int [] ip = P_BOX_substitute(ciphertext,IP);
        int [] k1 = P_BOX_substitute(LeftShift(P_BOX_substitute(key,P10),SDES_.LeftShift1),P8);
        int [] k2 = P_BOX_substitute(LeftShift(P_BOX_substitute(key,P10),SDES_.LeftShift2),P8);
        int [] left_ip = Arrays.copyOfRange(ip,0,4);
        int [] right_ip = Arrays.copyOfRange(ip,4,8);
        left_ip = round_key(left_ip,P_BOX_substitute(compress_substitute(round_key(expand_substitute(right_ip),k2)),SP_BOX));
        right_ip = round_key(right_ip,P_BOX_substitute(compress_substitute(round_key(expand_substitute(left_ip),k1)),SP_BOX));
        int [] res = new int[8];
        System.arraycopy(right_ip,0,res,0,4);
        System.arraycopy(left_ip,0,res,4,4);
        res = P_BOX_substitute(res,IP_1);
        return res;
    }
}
