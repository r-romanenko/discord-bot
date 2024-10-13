package Discord;

import java.util.*;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import java.util.concurrent.TimeUnit;
import discord4j.core.object.entity.channel.MessageChannel;
import java.nio.channels.Channel;
import java.io.*;

import discord4j.core.object.reaction.Reaction;
import discord4j.core.object.reaction.ReactionEmoji;
import java.util.regex.Pattern;

import java.io.File;
import java.io.PrintStream;

public class Jerm {

    // ---------------------JERM SETUP---------------------

    String fileName = "DiscordJermData";

    //reads data into listMap from specified file
    private String open(HashMap<String, ArrayList<String>> listMap) {
        try {

            Scanner in = new Scanner(new File(fileName));
            String key = "";
            ArrayList<String> list = new ArrayList<String>();

            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] splitLine = line.split(":");


                if(splitLine[0].equals("key")) {
                    listMap.put(key, list);
                    key = line.substring(5).trim();
                    list.clear();
                }
                if(splitLine[0].equals("value")) {
                    list.add(line.substring(7).trim());
                }
            }
            in.close();
            return "All lists read from file" + fileName;

        } catch (Throwable error) {
            System.out.println("Error opening file " + fileName + " " + error.getMessage());
            return "Error opening file " + fileName + " " + error.getMessage();
        }
    }

    //saves all lists to new file with specified filename
    //format -
    //key: key
    //value: value
    private String save(HashMap<String, ArrayList<String>> listMap) {

        try {
            PrintStream out = new PrintStream(new File(fileName));

            for (String key: listMap.keySet()){
                ArrayList<String> list = listMap.get(key);
                out.println("key: " + key);
                for(String item : list) {
                    out.println("value: " + item);
                }
            }

            out.close();
            return ("All lists saved to file " + fileName);

        } catch (Throwable error) {
            System.out.println("Error opening file " + fileName + " " + error.getMessage());
            return "Error opening file " + fileName + " " + error.getMessage();
        }
    }
    // ----------------------------------------------------





    // |-----------------------------------------Methods for Programs----------------------------------------------|



    // -----------------------SETUP------------------------

    Random dice = new Random(); // dice

    Map<String, List<String>> listsTable = new HashMap<>(); // idk

    ReactionEmoji flushed = ReactionEmoji.unicode("\u2764"); // reactions
    ReactionEmoji hate = ReactionEmoji.unicode("\uD83D\uDC68\u200D\uD83E\uDDB0"); // reactions

    int counter = 0; // hateCounter
    String hatedItem = "book"; // hateCounter

    boolean PomodoroOn = false; // pomodoro timer

    String contName = ""; // lists

    int dealer = 0;                 // blackjack
    int player = 0;                 // blackjack
    int[] cardDeck = new int[14];   // blackjack
    int pickedCard;                 // blackjack

    // ----------------------------------------------------



    // --------------------MAIN METHOD---------------------
    private void totalCommands(MessageChannel channel, Message mes, String message) {

        try {
//            if (message.startsWith("!ping")) {
//                CreateMessage(channel, "Pong!");
//            }
//            else if (message.contains(":lipbite:") || message.contains("sex")) {
//                CreateMessage(channel, ":flushed:");
//            }
//
//            else if (message.contains("!me")) {
//                CreateMessage(channel, MeDown());
//            }
//            else if (message.contains("^") {
//                CreateMessage(channel, "^");
//            }
            if (message.startsWith("!commands") || message.startsWith("!help")) {
                CommandsList(channel, message);
            }
            if (message.startsWith("!ping")) {
                CreateMessage(channel, GetPingPong());
            }
            if (message.contains("^")) {
                CreateMessage(channel, Caret());
            }
            if (message.contains(":lipbite:") || message.contains("sex")) {
                CreateMessage(channel, Flushed());
            }
            if (message.contains("!me")) {
                CreateMessage(channel, MeDown());
            }
            if (message.contains("?")) {
                CreateMessage(channel, Question());
            }
            if (message.contains("o7")) {
                CreateMessage(channel, OSeven());
            }
            if (message.startsWith("!d")) {
                GetDXRollResponse(channel, message);
            }
            if (message.startsWith("!say")) {
                SayThis(channel, message);
            }
            if (message.contains("!bestie")) {
                CreateMessage(channel, Bestie());
            }
            if (message.startsWith("!8ball")) {
                CreateMessage(channel, Magic8Ball());
            }
            if (message.contains("love") || message.contains("lurv")) {
                LoveReaction(channel, mes, message);
            }
            if (message.contains("hate")) {
                HateReaction(channel, mes, message);
            }
            if (message.contains(hatedItem)) {
                HateCounter(channel, mes, message);
            }
            if (message.startsWith("!pom stop")) {
                PomodoroStop(channel);
            }
            if (message.startsWith("!pom")) {
                PomodoroEventHandler(channel, message);
            } //TODO get pomodoro stop working
            if (message.startsWith("!new list ")) {
                CreateList(channel, message);
            }
            if (message.startsWith("!print ")) {
                PrintList(channel, message);
            }
            if (message.startsWith("!add ")) {
                AddListItem(channel, message);
            }
            if (message.startsWith("!remove")) {
                ClearLists(channel, message);
            }
            if (message.startsWith("!check")) {
                RemoveListItem(channel, message);
            }
            if (message.equals("!blackjack")) {
                startBlackjack(channel, message);
            }
            if (message.startsWith("!hit")) {
                hit(channel, message);
            } //TODO make blackjack stand method
            if (message.startsWith("!stand")) {
                // incomplete
            }



        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("Erm. Error over here! :P").block();
        }

    }
    // ----------------------------------------------------



    // ----------------------ACTIVE------------------------

    // GENERAL METHOD
    private void CreateMessage(MessageChannel channel, String message) {
        channel.createMessage(message).block();
    }

    // LIST OF COMMANDS
    private void CommandsList(MessageChannel channel, String message) {
        CreateMessage(channel, """
        ```
        Here is a list of all the current Jerm Bot Commands:
        
        
        >---------------------- MISC -----------------------<
        
        ^                  - Adds its own "^" to agree with whatever you're agreeing to
        
        o7                 - Replies with o7
        
        !ping              - Replies with pong!
        
        !dx y              - rolls a die with x amount of side, add a y to roll it that many times 
        
        !me                - replies with o(-(
        
        !bestie            - says whoever is it's bestie
        
        !8ball             - add whatever prompt you want after the command to have your question answered
        
        !pom x y           - Starts a pomodoro timer with x being the worktime length and y being the breaktime (minutes)
        
        !pom stop          - Stops the currently active timer. And remember that only one timer can be on at once
        
        !say               - repeats what you said and deletes your initial message
        
        
        >---------------------- LISTS ----------------------<
        
        !new list [name]   - creates a new To Do List with a name of your choice
        
        !add [name] [item] - adds the specified item to the list specified by the name, name isn't necessary if you've just interacted with the list
        
        !print all         - prints out all existing To-Do lists
        
        !print [name]      - prints out the items in the specified To-Do list, the list is numbered
        
        !check [name] [#]  - removes the item with that number from the specified list, numbers can be seen when printing the list
        
        !remove all        - deletes all lists, try not to use this just in case you delete someone else's list
        
        !remove [name]     - deletes the specified list and all the items inside of it
        
        
        >-------------------- BLACKJACK --------------------< WORK IN PROGRESS, TYPE AT YOUR OWN RISK
        
        !blackjack         - starts a new blackjack game
        
        !hit               - get another card to increase your hand
        
        !stand             - lock in your hand and see if you've won against the dealer (computer)
        
        !ace1 / !ace11     - decides whether to make an ace count as 1 or 11
        
        ```""");
        return;
        // extra message for new commands
        // CreateMessage(channel, "");

    }

    // PING! PONG!
    private String GetPingPong() {return "Pong!";}

    // ^ SYMBOL
    private String Caret() {return "^";}

    // FLUSHED EMOJI
    private String Flushed() {return ":flushed:";}

    // O(-( GUY
    private String MeDown() {return "o(-(";}

    // ? to !
    private String Question() {return "!";}

    // o7
    private String OSeven() {return "o7";}

    // DICE ROLL
    private void GetDXRollResponse(MessageChannel channel, String input) {
        int count = 1;
        float total = 0;
        float avg = 0;
        String result = "";

        System.out.println(String.format("Received %s", input));
        try {
            String message = input.substring(2, input.length());
            Scanner inp = new Scanner(message);
            int inputNumber = Integer.parseInt(inp.next());
            System.out.println("This is inputNumber: " + inputNumber);
            if (inp.hasNext()) {
                int inputCount = Integer.parseInt(inp.next());
                count = inputCount;
            }

            for (int i = 0; i < count; i++) {

                int val = dice.nextInt(inputNumber) + 1;
                total += val;
                result += val + " ";
            }

            if (count > 1) {result += "\nTotal: " + (int) total + "\nAvg:   " + (total / count);}
            CreateMessage(channel, result);

        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            CreateMessage(channel, "I can't roll that!");
        }
    }

    // SAYS SOMETHING FOR YOU
    private void SayThis(MessageChannel channel, String input) {
        try {
            channel.getLastMessage().block().delete().block();
            channel.createMessage(input.substring(5, input.length())).block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("I'm not saying that").block();
        }
    }

    // JERM'S BESTIE
    private String Bestie() {
        int bestNumber = dice.nextInt(4) + 1;
        if (bestNumber == 1) {
            return "ruslan";
        }
        else if (bestNumber == 2) {
            return "daphne";
        }
        else if (bestNumber == 3) {
            return "anat";
        } else {
            return "I love you all equally :)";
        }
    }

    // MAGIC 8 BALL
    private String Magic8Ball() {
        int bestNumber = dice.nextInt(20) + 1;
        if (bestNumber == 1) {
            return "As I see it, yes.";
        } else if (bestNumber == 2) {
            return "Ask again later.";
        } else if (bestNumber == 3) {
            return "Better not tell you now.";
        } else if (bestNumber == 4) {
            return "Cannot predict now.";
        } else if (bestNumber == 5) {
            return "Concentrate and ask again.";
        } else if (bestNumber == 6) {
            return "Don't count on it.";
        } else if (bestNumber == 7) {
            return "It is certain.";
        } else if (bestNumber == 8) {
            return "It is decidedly so.";
        } else if (bestNumber == 9) {
            return "Most likely.";
        } else if (bestNumber == 10) {
            return "My reply is no.";
        } else if (bestNumber == 11) {
            return "My sources say no.";
        } else if (bestNumber == 12) {
            return "Outlook not so good.";
        } else if (bestNumber == 13) {
            return "Outlook good.";
        } else if (bestNumber == 14) {
            return "Reply hazy, try again.";
        } else if (bestNumber == 15) {
            return "Signs point to yes.";
        } else if (bestNumber == 16) {
            return "Very doubtful.";
        } else if (bestNumber == 17) {
            return "Without a doubt.";
        } else if (bestNumber == 18) {
            return "Yes.";
        } else if (bestNumber == 19) {
            return "Yes – definitely.";
        } else if (bestNumber == 20) {
            return "You may rely on it.";
        }
        return "Erm... try again I guess...";
    }

    // REACTIONS
    private void LoveReaction(MessageChannel channel, Message message, String input) {
        // \u2764 heart
        // \u1F633 flushed

        try {
            message.addReaction(flushed).block();
            message.addReaction(flushed).block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("Reaction Error!").block();
        }

    }
    private void HateReaction(MessageChannel channel, Message message, String input) {
        // \u2764 heart
        // \u1F633 flushed

        try {
            message.addReaction(hate).block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("Reaction Error!").block();
        }

    }

    // HATE COUNTER
    private void HateCounter(MessageChannel channel, Message message, String input) {
        counter++;
        System.out.println("HateCounter: " + counter);
        try {
            if (counter == 5) {
                CreateMessage(channel, "Okay guys no need to talk about " + hatedItem + "s so much!");
            }
            if (counter == 8) {
                CreateMessage(channel, "You know what? I'm tired of this " + hatedItem + " talk. Deleted.");
                TimeUnit.SECONDS.sleep(3);
                message.delete().block();

            }
            if (counter >= 8) {
                counter = 0;
            }
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("HateCounter Error").block();
        }

    }

    // POMODORO TIMER
    private void PomodoroEventHandler(MessageChannel channel, String message) {
        System.out.printf("Received %s%n", message);
        try {
            int indexOfSpace = message.indexOf(" ", 5);
            int worktime = Integer.parseInt(message.substring(5, indexOfSpace));
            int breaktime = Integer.parseInt(message.substring(indexOfSpace + 1, message.length()));
            CreateMessage(channel, "Pomodoro timer set with " + worktime + " worktime minutes and " + breaktime + " breaktime minutes.");
            CreateMessage(channel, "Work time has started");
            PomodoroOn = true;
            while (PomodoroOn) {
                TimeUnit.SECONDS.sleep(worktime * 60L);
                if (!PomodoroOn) {break;}
                CreateMessage(channel, "The work period is over, you may take a " + breaktime + " minute break, you've earned it!");
                TimeUnit.SECONDS.sleep(breaktime * 60L);
                if (!PomodoroOn) {break;}
                CreateMessage(channel, "Break is over, time to get back to work! See you in " + worktime + " minutes.");
            }
        } catch (Exception exc) {
            System.out.printf("Exception: %s%n", exc);
            CreateMessage(channel, "Pomodoro Timer input not valid, use !commands to see proper syntax");
        }
    }
    private void PomodoroStop(MessageChannel channel) {
        System.out.println("Received PomStop");
        PomodoroOn = false;
        CreateMessage(channel, "The pomodoro timer has stopped");
    }

    // LISTS
    private void CreateList(MessageChannel channel, String message) {
        String name = message.substring(10, message.length());
        if (listsTable.containsKey(name)) {
            channel.createMessage("There are already exists a list with the name " + name).block();
            return;
        }
        listsTable.put(name, new ArrayList<String>());
        contName = name;
        channel.createMessage("New list created called " + name).block();
    }
    private void PrintList(MessageChannel channel, String message) {
        try {
            String name = message.substring(7, message.length());
            if (message.equalsIgnoreCase("!print all")) {
                String ret = "All Lists: \n \n";
                for (String key : listsTable.keySet()) {
                    ret +=  "- " + key + "\n";
                }
                channel.createMessage(ret).block();
            } else if (!listsTable.containsKey(name)) {
                channel.createMessage("There doesn't seem to be a list with the name " + name).block();
            } else {
                String ret = "To-Do List: " + name + "\n \n";
                int i = 1;
                for (String s : listsTable.get(name)) {
                    ret += i + ")  " + s + "\n";
                    i++;
                }
                channel.createMessage(ret).block();
                contName = name;
            }
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            CreateMessage(channel, "Something went wrong in clearing a list");
        }
    }
    private void AddListItem(MessageChannel channel, String message) {
        try {
            String[] tokens = message.split(" ", 3);
            String item = "";
            String name = tokens[1];
            if (tokens.length == 2) {
                item = tokens[1];
                name = contName;
            } else if (tokens.length == 1) {
                item = "";
                name = contName;
            } else if (!listsTable.containsKey(name)) {
                item = tokens[1] + " " + tokens[2];
                name = contName;
            } else {
                item = tokens[2];
            }
            if (name.isEmpty()) {
                channel.createMessage("Please specify the To Do list you're adding to at least once").block();
                return;
            }
            listsTable.get(name).add(item);
            contName = name;
            channel.createMessage(item + " has been added to " + name).block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            CreateMessage(channel, "Something went wrong in adding the item to the list");
        }
    }
    private void RemoveListItem(MessageChannel channel, String message) {
        try {
            int indexOfSpace = message.indexOf(" ", 8);
            String name = message.substring(7, indexOfSpace);
            int index = Integer.parseInt(message.substring(indexOfSpace + 1, message.length()));
            if (!listsTable.containsKey(name)) {
                channel.createMessage("There doesn't seem to be a list called " + name).block();
                return;
            }
            listsTable.get(name).remove(index - 1);
            channel.createMessage("Item #" + index + " has been removed from " + name).block();
            return;
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            CreateMessage(channel, "Please specify a valid item number in the list");
        }
    }
    private void ClearLists(MessageChannel channel, String message) {
        try {
            String name = message.substring(8, message.length());
            if (message.equalsIgnoreCase("!remove all")) {
                listsTable.clear();
                channel.createMessage("All To-Do Lists have been deleted").block();
            } else if (!listsTable.containsKey(name)) {
                channel.createMessage("There are no lists with the name " + name).block();
            } else {
                listsTable.remove(name);
                channel.createMessage("The list named " + name + " has been removed").block();
            }
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            CreateMessage(channel, "Something went wrong in clearing a list");
        }
    }

    // BLACKJACK
    private void startBlackjack(MessageChannel channel, String message) {
        try {
            dealer = 0;
            Arrays.fill(cardDeck, 0);
            System.out.println(String.format("New Dealer"));
            while (dealer < 17) { // adds up dealer hand
                pickedCard = dice.nextInt(13) + 1;
                while (cardDeck[pickedCard] == 4) { // keeps picking a card number that hasn't been chosen 4 times
                    pickedCard = dice.nextInt(13) + 1;
                }
                cardDeck[pickedCard]++;
                if (pickedCard >= 11) { // makes kings, queens, and jacks into 10
                    pickedCard = 10;
                }
                if (pickedCard == 1) { // decides what to do with an ace card
                    if (dealer + 11 >= 17 && dealer + 11 <= 21) {
                        pickedCard = 11;
                    } else if (dealer + 11 <= 21) {
                        pickedCard = 11;
                    }
                }
                dealer += pickedCard;
            }
            CreateMessage(channel, "Blackjack has started, the dealer has a hand of " + dealer);
            player = 0;
            String givenNum = "You were given a ";

            pickedCard = dice.nextInt(13) + 1;
            while (cardDeck[pickedCard] == 4) { // keeps picking a card number that hasn't been chosen 4 times
                pickedCard = dice.nextInt(13) + 1;
            }
            cardDeck[pickedCard]++;
            if (pickedCard >= 11) { // makes kings, queens, and jacks into 10
                pickedCard = 10;
            }
            if (pickedCard == 1) { // decides what to do with an ace card

            }
            player += pickedCard;
            givenNum += pickedCard + " and a ";

            pickedCard = dice.nextInt(13) + 1;
            while (cardDeck[pickedCard] == 4) { // keeps picking a card number that hasn't been chosen 4 times
                pickedCard = dice.nextInt(13) + 1;
            }
            cardDeck[pickedCard]++;
            if (pickedCard >= 11) { // makes kings, queens, and jacks into 10
                pickedCard = 10;
            }
            if (pickedCard == 1) { // decides what to do with an ace card
                if (dealer + 11 >= 17 && dealer + 11 <= 21) {
                    pickedCard = 11;
                } else if (dealer + 11 <= 21) {
                    pickedCard = 11;
                }
            }


            CreateMessage(channel, "You were given a " + "");
        } catch (Exception exc) {
            CreateMessage(channel, "Something went wrong in building the dealer hand");
        }
    }
    private void hit(MessageChannel channel, String message) {
        try {
            if (dealer < 17) {
                CreateMessage(channel, "The dealer hasn't gotten their hand yet, please type !blackjack to begin a new game");
                return;
            }
            while (dealer < 17) { // adds up dealer hand
                pickedCard = dice.nextInt(13) + 1;
                while (cardDeck[pickedCard] == 4) { // keeps picking a card number that hasn't been chosen 4 times
                    pickedCard = dice.nextInt(13) + 1;
                }
                cardDeck[pickedCard]++;
                if (pickedCard >= 11) { // makes kings, queens, and jacks into 10
                    pickedCard = 10;
                }
                if (pickedCard == 1) { // decides what to do with an ace card
                    if (dealer + 11 >= 17 && dealer + 11 <= 21) {
                        pickedCard = 11;
                    } else if (dealer + 11 <= 21) {
                        pickedCard = 11;
                    }
                }
                dealer += pickedCard;
            }


            CreateMessage(channel, "Blackjack has started, the dealer has a hand of " + dealer);
        } catch (Exception exc) {
            CreateMessage(channel, "Something went wrong in building the dealer hand");
        }
    }
    private int ace1(MessageChannel channel, String message) {
        try {
            return 1;
        } catch (Exception exc) {
            CreateMessage(channel, "Something went wrong with the Ace command!");
        }
        return -1;
    }

    // ----------------------------------------------------





    // ----------------------DISABLED-----------------------

    /*
    private void SaveSayThis(MessageChannel channel, String input) {
        try {
            // channel.getLastMessage().block().delete().block();
            channel.createMessage("saved: \"" + input.substring(6, input.length()) + "\" to JermBook.txt").block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("I'm not saving that").block();
        }
    }
     */ // Save Say This

    /*
    private void HiImDad(MessageChannel channel, String input) {
        try {
            int indexHi = 0;
            String lowerCase = input.toLowerCase();
            if (lowerCase.contains("im ")) {
                indexHi = lowerCase.indexOf("im ") + 3;
            } else if (lowerCase.contains("i'm ")) {
                indexHi = lowerCase.indexOf("i'm") + 4;
            } else {

            }

            String hi = lowerCase.substring(indexHi);

            channel.createMessage("Hi " + hi + ", I'm dad!").block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("I'm.....").block();
        }
    }
     */ // Hi I'm Dad

    /*
    private void ExclamationPoint(MessageChannel channel, String input) {
        try {
            // channel.getLastMessage().block().delete().block();
            channel.createMessage("!").block();
        } catch (Exception exc) {
            System.out.println(String.format("Exception: %s", exc));
            channel.createMessage("I'm not saying that").block();
        }
    }
    */ // Exclamation Point

    // ----------------------------------------------------





    public static void main(String[] args) {


        // ----------------------------------------------NECESSARY-----------------------------------------------

        GatewayDiscordClient client = DiscordClientBuilder.create
                ("API_KEY_HERE")
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.println(String.format(
                            "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
                    ));
                });

        Jerm jerm = new Jerm();

        // REFORMATTED
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(message -> jerm.totalCommands(message.getChannel().block(), message, message.getContent().toLowerCase(Locale.ROOT)));

        // -----------------------------------------------------------------------------------------------------



        // TESTING USING REACTIONS

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!test"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("reaction? :eyes:"))
                //.flatMap(message -> message.addReaction("💩"))
                .subscribe();



        // ----------------IMPROPER FORMATTING-----------------

        /*
        // bestie checker
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.totalCommands(message.getContent()))
                        .block());
*/ // bestie

        /*
        // commands list
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!commands");
                })
                .subscribe(message -> jerm.CommandsList(message.getChannel().block(), message.getContent()));
         */ // commands list

        /*
        // ping pong
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.GetPingPong())
                        .block());

         */ // ping pong

        /*
        // flushed
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().contains(":lipbite:"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.Flushed())
                        .block());

         */ // flushed

        /*
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().contains("sex"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.Flushed())
                        .block());

         */ // flushed

        /*
        // caret
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("^"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("^"))
                .subscribe();

         */ // caret

        /*
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().contains("o7"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("o7"))
                .subscribe();
        */ // o7

        /*
        // dX roll
        client.getEventDispatcher().on(MessageCreateEvent.class)
                // subscribe is like block, in that it will *request* for action
                // to be done, but instead of blocking the thread, waiting for it
                // to finish, it will just execute the results asynchronously.
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().startsWith("!d"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.GetDXRollResponse(message.getContent()))
                        .block());

         */ // dice roll

        /*
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().startsWith("!d"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(jerm.GetDXRollResponse(channel.getLastMessage().toString())))
                .subscribe();
         */ // dice roll

        /*
        // boolean ifSay = false;
        // says for you
        // replies with o(-(
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    // ifSay = true;
                    return msg.startsWith("!me");
                })
                .subscribe(message -> jerm.MeDown(message.getChannel().block(), message.getContent()));
         */ // o(-(

        /*
        // "hi _____, I'm dad!"
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    // ifSay = true;
                    return msg.contains("im ");
                })
                .subscribe(message -> jerm.HiImDad(message.getChannel().block(), message.getContent()));

        // "hi _____, I'm dad!"
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    // ifSay = true;
                    return msg.contains("i'm ");
                })
                .subscribe(message -> jerm.HiImDad(message.getChannel().block(), message.getContent()));

*/ // Hi I'm Dad

        /*
        // replies with o(-(
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    // ifSay = true;
                    return msg.contains("?");
                })
                .subscribe(message -> jerm.ExclamationPoint(message.getChannel().block(), message.getContent()));


         */ // ? -> !

        /*
        // Magic 8 Ball
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().startsWith("!8ball"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.Magic8Ball())
                        .block());
         */ // 8ball

        /*
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    // ifSay = true;
                    return msg.startsWith("!say");
                })
                .subscribe(message -> jerm.SayThis(message.getChannel().block(), message.getContent()));
         */ // say this

        /*
        // pomodoro timer
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!pom") && !msg.equals("!pom stop");
                })
                .subscribe(message -> jerm.PomodoroEventHandler(message.getChannel().block(), message.getContent()));
         */ // pomodoro timer

        /*
        // pomodoro stopper
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().startsWith("!pom stop"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.PomodoroStop())
                        .block());
         */ // pomodoro stopper

        /*
        // bestie checker
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!bestie"))
                .subscribe(message -> message.getChannel()
                        .block()
                        .createMessage(jerm.Bestie())
                        .block());
         */ // bestie

        /*
        // new ArrayList
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!new list ");
                })
                .subscribe(message -> jerm.CreateList(message.getChannel().block(), message.getContent()));
         */ // new arraylist

        /*
        // print name of list
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!print ");
                })
                .subscribe(message -> jerm.PrintList(message.getChannel().block(), message.getContent()));
         */ // print list name

        /*
        // adds an item to a specific list
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!add ");
                })
                .subscribe(message -> jerm.AddListItem(message.getChannel().block(), message.getContent()));
         */ // add item to list

        /*
        // clears all lists or a specified list
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!remove");
                })
                .subscribe(message -> jerm.ClearLists(message.getChannel().block(), message.getContent()));
         */ // clears list(s)

        /*
        // checks off a specific task in a list
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!check");
                })
                .subscribe(message -> jerm.RemoveListItem(message.getChannel().block(), message.getContent()));
         */ // checks off task in list

        /*
        // blackjack start
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!blackjack");
                })
                .subscribe(message -> jerm.startBlackjack(message.getChannel().block(), message.getContent()));
         */ // blackjack start

        /*
        // blackjack hit
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!hit");
                })
                .subscribe(message -> jerm.hit(message.getChannel().block(), message.getContent()));
         */ // blackjack hit

        /*
        // blackjack stand
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> {
                    String msg = message.getContent().toLowerCase();
                    return msg.startsWith("!stand");
                })
                .subscribe(message -> jerm.ClearLists(message.getChannel().block(), message.getContent()));
         */ // blackjack stand

        // ----------------------------------------------------



        // -------------------DON'T DELETE!--------------------

        // listsTable reference
        /*

        // clear entire table
        listsTable.clear();
        // does table have a row with this name
        listsTable.containsKey(listName);
        // add new row to the table (with empty ArrayList)
        listsTable.put(listName, new ArrayList<String>());
        // get row's ArrayList
        listsTable.get(listName);
        listsTable.get(listName).add(listItem);
        // delete a row from the table
        listsTable.remove(listName);

*/

        // ----------------------------------------------------


        client.onDisconnect().block();
    }

}
