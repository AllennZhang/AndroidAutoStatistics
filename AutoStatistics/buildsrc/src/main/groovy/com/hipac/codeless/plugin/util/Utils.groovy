public class Utils{

    public static String path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }

    public static boolean emptyString(String str){
        return (str == null || "".equals(str))
    }

}