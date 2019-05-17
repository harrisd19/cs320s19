import com.gigaspaces.annotation.pojo.SpaceId;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.springframework.jdbc.object.SqlQuery;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GamePig implements Serializable {

    private String       id;
    private List<Player> players;
    private Integer      numberPlayers;
    private Integer      round;
    private static final int MIN_NUMBER_PLAYERS = 2;
    private static final int WAITING_TIME = 5000; // 5s
    private static final int MAX_POINTS = 20;

    public GamePig() {
        players = new LinkedList<Player>();
        numberPlayers = MIN_NUMBER_PLAYERS;
        round = 0;
    }

    public GamePig(int numberPlayers) {
        players = new LinkedList<Player>();
        if (numberPlayers < MIN_NUMBER_PLAYERS)
            this.numberPlayers = MIN_NUMBER_PLAYERS;
        else
            this.numberPlayers = numberPlayers;
        round = 0;
    }

    @SpaceId (autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getPlayerByName(String name) {
        for (Player player: players)
            if (player.getName().equalsIgnoreCase(name))
                return player;
        return null;
    }

    public void removePlayerByName(String name) {
        Player player = getPlayerByName(name);
        if (player != null)
            players.remove(player);
    }

    public Integer getNumberPlayers() {
        return numberPlayers;
    }

    public void setNumberPlayers(Integer numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public boolean isRoundNew() {
        for (Player player: players)
            if (player.getRound().intValue() != round.intValue())
                return false;
        return true;
    }

    // a game is over is a new round had just started and there is one player with at least MAX_POINTS
    public boolean isGameOver() {
        for (Player player: players)
            if (player.getPoints().intValue() >= MAX_POINTS)
                return true;
        return false;
    }

    public static void main(String[] args) {
        // validate command-line parameters
        if (args.length != 2) {
            System.out.println("Use: java " + GamePig.class + " name host");
            System.exit(1);
        }
        String name = args[0];
        Player player = new Player(name);
        String host = args[1];

        // connect to XAP
        String SPACE_NAME = "game_pig";
        UrlSpaceConfigurer url = new UrlSpaceConfigurer("jini://" + host + "/./" + SPACE_NAME);
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(url);
        GigaSpace space = conf.gigaSpace();

        // search for a game that hasn't started yet; create a new one if none exists
        int round = 0;
        GamePig game = null;
        while (game == null) {
            SQLQuery<GamePig> query = new SQLQuery<GamePig>(GamePig.class, "round = ?");
            query.setParameter(1, round);
            game = space.take(query);
            if (game == null)
                space.write(new GamePig());
        }
        String gameId = game.getId(); // save the game's auto-generated id

        // add yourself to the list of players if name is unique
        List<Player> players = game.getPlayers();
        if (players.contains(player)) {
            System.out.println("Game already has a player with same name!");
            space.write(game);
            System.exit(1);
        }
        players.add(player);
        game.setPlayers(players);

        // check if you are the last player added to the game; start the game if so
        if (players.size() == game.getNumberPlayers())
            game.setRound(1);
        space.write(game);

        Scanner sc = new Scanner(System.in);
        Dice dice = new Dice();
        round = 1;
        while (game == null || !game.isGameOver() || !game.isRoundNew()) {
            SQLQuery<GamePig> query = new SQLQuery<GamePig>(GamePig.class, "id = ? and round = ?");
            query.setParameter(1, gameId);
            query.setParameter(2, round);
            game = space.take(query);
            if (game == null) {
                System.out.println("Other player(s) turn...");
                try {
                    Thread.sleep(WAITING_TIME);
                } catch (InterruptedException ex) {

                }
                continue;
            }
            System.out.println(game.getPlayers());
            player = game.getPlayerByName(name);
            if (!game.isGameOver() || !game.isRoundNew()) {
                int roundPoints = 0;
                while (true) {
                    dice.roll();
                    System.out.println("Dice: " + dice);
                    if (dice.getValue() == 1) {
                        System.out.println("You got pigged!");
                        roundPoints = 0;
                        break;
                    } else {
                        roundPoints += dice.getValue();
                        System.out.println("Current round #points: " + roundPoints);
                        System.out.print("Do you want to hold (y/n)? ");
                        String answer = sc.nextLine();
                        if (answer.startsWith("y"))
                            break;
                    }
                } // end play round
                player.setPoints(player.getPoints() + roundPoints);
                round++;
                player.setRound(round);

                // if I am the last player to play the round, increment the game's round
                System.out.println(game.getPlayers());
                boolean last = true;
                for (Player p : game.getPlayers())
                    if (p.getRound() < round) {
                        last = false;
                        break;
                    }
                if (last)
                    game.setRound(round);
            }
            space.write(game);
        } // end play game

        System.out.println("Game over!");
    } // end main
}
