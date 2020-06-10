package fr.iut.tetris;

public class Log {

    public static void debug(Object sender, Object message) {
        System.out.println("[DEBUG] ["+sender.getClass()+"] "+ message);
    }
    public static void info(Object sender, Object message) {
        System.out.println("[INFO]  ["+sender.getClass()+"] "+message);
    }
    public static void warning(Object sender, Object message) {
        System.out.println("[WARN]  ["+sender.getClass()+"] "+message);
    }
    public static void error(Object sender, Object message) {
        System.out.println("[ERROR] ["+sender.getClass()+"] "+message);
    }
    public static void critical(Object sender, Object message) {
        System.out.println("[CRIT]  ["+sender.getClass()+"] "+message);
    }
}
