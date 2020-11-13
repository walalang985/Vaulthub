package apc.mobprog.vaulthub;

public class Hex {
    public Hex(){}
    public String getHexString(String text){
        char[] a = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<a.length;i++) {
            sb.append(Integer.toHexString(a[i]));
        }
        return sb.toString();
    }
    public String getString(String text){
        String result = "";
        char[] a = text.toCharArray();
        for(int i = 0; i<a.length;i+=2) {
            char ch = (char) Integer.parseInt(""+a[i] + "" + a[i+1], 16);
            result = result+ch;
        }
        return result;
    }
}
