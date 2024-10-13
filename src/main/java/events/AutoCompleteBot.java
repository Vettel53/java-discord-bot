package events;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoCompleteBot extends ListenerAdapter {

//    private final String [] platforms = new String [] { "steam", "epic games", "psn", "xbox", "switch"};
//
//
//    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteraction event) {
//        if (event.getName().equals("steam") && event.getFocusedOption().getName().equals("steam")) {
//
//            List<Command.Choice> options = Stream.of(platforms)
//                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
//                    .map(word -> new Command.Choice(word, word))
//                    .collect(Collectors.toList());
//            event.replyChoices(options).queue();
//        }
//    } TODO: Autocomplete  -- Grabbed from JDA Wiki
}
