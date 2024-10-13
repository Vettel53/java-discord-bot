package events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String messageSent;
        String messageAuthor;

        if (!event.getAuthor().isBot()) {
            messageSent = event.getMessage().getContentRaw();
            messageAuthor = event.getAuthor().getAsMention();
            event.getChannel().sendMessage("He said: " + messageSent).queue();
            event.getChannel().sendMessage("Author: " + messageAuthor).queue();
        }

        System.out.println("A message has been sent.");
    }

    public void onChannelDelete (ChannelDeleteEvent event) {
        String channelName = event.getChannel().getName();

        TextChannel general = event.getChannel().getJDA().getTextChannelById(1293823254167031861L);

        if (general != null) {
            general.sendMessage("Channel '" + channelName + "' has been deleted.").queue();
        }
    }
}
