import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import java.util.Arrays;

public class PlayerDriver {

    public static void main(String[] args) {
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(new UrlSpaceConfigurer(Configuration.SPACE_URL));
        GigaSpace space = conf.create();

        Player bob = new Player();
        bob.setName("Bob");
        space.write(bob);

        Player anna = new Player();
        anna.setName("Anna");
        space.write(anna);

        Player players[] = space.readMultiple(new Player());
        System.out.println(Arrays.toString(players));
    }
}
