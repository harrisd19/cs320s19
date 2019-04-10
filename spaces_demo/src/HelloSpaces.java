import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

public class HelloSpaces {

   static final String SPACE_HOST = "localhost";
   static final String SPACE_NAME = "test";
   static final String SPACE_URL  = "jini://" + SPACE_HOST + "/./" + SPACE_NAME;

   public static void main(String[] args) {

       UrlSpaceConfigurer url = new UrlSpaceConfigurer(SPACE_URL);
       GigaSpaceConfigurer conf = new GigaSpaceConfigurer(url);
       GigaSpace space = conf.gigaSpace();

       // creating an object in the space
       Message msg = new Message();
       msg.setId(1);
       msg.setText("Hello");
       space.write(msg);

       // read the object
       msg = space.readById(Message.class, 1);
       System.out.println(msg);
   }
}
