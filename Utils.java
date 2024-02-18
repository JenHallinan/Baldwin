public class Utils {
    // unit testing
    public static void main(String[] args){
        Utils u = new Utils();
        System.out.println(u.convertFromBaseToBase("5", 10, 4));
    }

    // convert bases
    public String convertFromBaseToBase(String str, int fromBase, int toBase) {
        return Integer.toString(Integer.parseInt(str, fromBase), toBase);
    }

    // deep copy a host

}
