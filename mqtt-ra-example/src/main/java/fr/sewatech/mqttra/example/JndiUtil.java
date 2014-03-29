package fr.sewatech.mqttra.example;

import javax.naming.*;

/**
 * @author Alexis Hassler
 */
public class JndiUtil {
    public static void inspect(String root) {
        System.out.println("JNDI inspection for context " + root);
        try {
            Context context = new InitialContext();
            inspectContext((Context) context.lookup(root), "-");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void inspectContext(Context context, String prefix) throws NamingException {
        NamingEnumeration<NameClassPair> list = context.list("");
        while (list.hasMore()) {
            NameClassPair nameClassPair = list.nextElement();
            System.out.print(prefix + nameClassPair.getName());
            try {
                Object value = context.lookup(nameClassPair.getName());
                if (value instanceof Context) {
                    System.out.println();
                    inspectContext((Context) value, "-" + prefix);
                } else {
                    System.out.println("=" + value);
                }
            } catch (NameNotFoundException e) {
                System.out.println("  ** " + e.getMessage() + " **");
            }
        }
    }


}
