package Discord;
import java.util.ArrayList;

public class ToDoList {
    ArrayList<String> list = new ArrayList<>();
    String _name;

    public ToDoList(String name) {
        _name = name;
    }

    public void add(String input) {
        list.add(input);
    }

    public String printable() {
        String ret = "To-Do List: " + _name + "\n";
        for (String s : list) {
            ret += "- " + s + "\n";
        }
        return ret;
    }

}
