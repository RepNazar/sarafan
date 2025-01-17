package letscode.sarafan.service;

import letscode.sarafan.domain.Message;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.domain.Views;
import letscode.sarafan.dto.EventType;
import letscode.sarafan.dto.MessagePageDto;
import letscode.sarafan.dto.MetaDto;
import letscode.sarafan.dto.ObjectType;
import letscode.sarafan.repo.MessageRepo;
import letscode.sarafan.repo.UserSubscriptionRepo;
import letscode.sarafan.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private final static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private final static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private final static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepo messageRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(MessageRepo messageRepo, UserSubscriptionRepo userSubscriptionRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    public MessagePageDto findAllForUser(Pageable pageable, User user) {
        List<User> channels = userSubscriptionRepo.findAllBySubscriber(user)
                .stream()
                .filter(UserSubscription::isActive)
                .map(UserSubscription::getChannel)
                .collect(Collectors.toList());
        channels.add(user);

        Page<Message> page = messageRepo.findAllByAuthorIn(channels, pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public Message create(Message message, User user) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);

        Message updatedMessage = messageRepo.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    public Message update(Message messageFromDb, Message message, User currentUser) throws IOException {
        if (!messageFromDb.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException();
        }

        messageFromDb.setText(message.getText());
        fillMeta(messageFromDb);

        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    public void delete(Message message, User currentUser) {
        if (!message.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException();
        }
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);

            message.setLink(url);
            if (matcher.find()) {
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);

                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
                message.setLinkCover(meta.getCover());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }
}
