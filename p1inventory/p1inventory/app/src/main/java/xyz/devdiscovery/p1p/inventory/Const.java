package xyz.devdiscovery.p1p.inventory;




public class Const {

    private static final String url = "jdbc:mysql://192.168.1.201:3306/p1posffx";
    private static final String user = "p1posff";
    private static final String pass = "(p1posff)";


    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}
