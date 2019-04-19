import com.gigaspaces.query.ISpaceQuery;
import com.j_spaces.core.client.SQLQuery;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.SqlQuery;

import java.util.Map;
import java.util.Scanner;

public class AuctionMain {

    static final String SPACE_HOST = "localhost";
    static final String SPACE_NAME = "auction";
    static final String SPACE_URL  = "jini://" + SPACE_HOST + "/./" + SPACE_NAME + "?locators=" + SPACE_HOST;
    static final long   WAIT_TIME  = 60000 * 5; // 5 minutes
    private int role;
    static final int BUYER_ROLE  = 0;
    static final int SELLER_ROLE = 1;
    private GigaSpace space;

    AuctionMain(int role) {
        this.role = role;

        // JavaSpaces specifics
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(new UrlSpaceConfigurer(SPACE_URL));
        this.space = conf.gigaSpace();
    }

    void run() {
        Scanner sc = new Scanner(System.in);
        if (role == BUYER_ROLE) {
            System.out.println("Buyer role");
            System.out.print("Item type? ");
            String itemType = sc.nextLine().toLowerCase();
            if (!itemType.equals("car") && !itemType.equals("scooter")) {
                System.out.println("Unknown item type...");
                System.exit(1);
            }
            System.out.print("Item description? ");
            String decription = sc.nextLine();
            System.out.print("Price? ");
            double price = Double.parseDouble(sc.nextLine());
            Item item;
            if (itemType.equals("car")) {
                System.out.print("Year? ");
                int year = Integer.parseInt(sc.nextLine());
                item = new Car(decription, price, year);
            }
            else {
                System.out.print("Electric? ");
                boolean electric = Boolean.parseBoolean(sc.nextLine());
                item = new Scooter(decription, price, electric);
            }
            space.write(item);
            while (true) {
                System.out.println("Waiting for bids on " + item);
                Bid bid = space.read(new Bid(), WAIT_TIME);
                if (bid.getItem().getId().equals(item.getId())) {
                    System.out.println("Bid received: " + bid);
                    if (bid.getOfferedPrice() <= item.getPrice()) {
                        System.out.println("Bid accepted!!!");
                        // TODO: remove ALL bid for the item that the buyer is interested in
                        SQLQuery<Bid> query = new SQLQuery<Bid>(Bid.class, "item.id = ?")
                                .setParameter(1, item.getId());
                        Bid bids[] = space.takeMultiple(query);
                        System.out.println(bids.length + " bids to the same item removed!");
                        // TODO: remove the buyer's item
                        space.takeById(Item.class, item.getId()); // remove the item
                        break;
                    }
                }
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {

                }
            }
        } else {
            System.out.println("Seller role");
            System.out.print("Name? ");
            String seller = sc.nextLine();
            System.out.print("Item type? ");
            String itemType = sc.nextLine().toLowerCase();
            if (!itemType.equals("car") && !itemType.equals("scooter")) {
                System.out.println("Unknown item type...");
                System.exit(1);
            }
            System.out.println("Waiting for buyers...");
            while (true) {
                Item item;
                if (itemType.equals("car"))
                    item = space.read(new Car(), WAIT_TIME);
                else
                    item = space.read(new Scooter(), WAIT_TIME);
                System.out.println("A buyer is interested in this item: " + item);
                System.out.print("Wanna bid (y|n)? ");
                String answer = sc.nextLine();
                if (answer.equals("y")) {
                    System.out.print("Offered price? ");
                    double offeredPrice = Double.parseDouble(sc.nextLine());
                    Bid bid = new Bid(item, seller, offeredPrice);
                    space.write(bid);
                    System.out.println("Bid was placed: " + bid);
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0 || args[0].split("=").length != 2) {
            System.out.println("Use: java " + AuctionMain.class + " role=[0|1]");
            System.out.println("0: buyer; 1: seller");
            System.exit(1);
        }

        int role = BUYER_ROLE;
        try {
            role = Integer.parseInt(args[0].split("=")[1]);
            if (role < 0 || role > 1)
                throw new Exception();
        }
        catch (Exception ex) {
            System.out.println("Couldn't identify process role!");
            System.exit(1);
        }

        AuctionMain auctionMain = new AuctionMain(role);
        auctionMain.run();
    }
}
