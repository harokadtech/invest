package pl.hofman.projectsGmailApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

public class MessageProcessor {

    private static final String ORDER_FILLED = "filled";
    private static final String ACCOUNT_STOCK = "Account:";
    private static final String ACCOUNT_TYPE = "Type:";
    private static final String A_LINK = "//";

    public MessageProcessor() {
    }

    // finding full messages (with details like content) by id and saving them to
    // the list
    public List<Message> findMainGmailMessages(Gmail service, String user, String userQuery, int daysNumber)
            throws NullPointerException, IOException {
        List<Message> mainMessagesInThread = findMainMessages(service, user, userQuery, daysNumber);

        List<Message> mainGmailMessagesInThread = mainMessagesInThread.stream()
                .map(msg -> getMainGmailMessage(service, user, msg))
                .collect(Collectors.toList());
        System.out.printf("\nThe number of messages found: %d\n", mainGmailMessagesInThread.size());
        return mainGmailMessagesInThread;
    }

    private Message getMainGmailMessage(Gmail service, String user, Message message) {
        try {
            return service.users().messages().get(user, message.getId()).setFormat("full").execute();
        } catch (IOException e) {
            System.out.printf("\nUser message " + message.getId() + " not found");
            return message;
        }
    }

    // finding main project messages from the list (containing projects data) -
    // thread id = msg id
    // this messages has only id and thread id, no content is available/accessible
    private List<Message> findMainMessages(Gmail service, String user, String userQuery, int daysNumber)
            throws NullPointerException, IOException {
        List<Message> messages = findAllMessagesFromPages(service, user, userQuery, daysNumber);

        /*
         * List<Message> mainMessagesInThread = messages.stream()
         * .filter(msg -> msg.getId().equals(msg.getThreadId()))
         * .collect(Collectors.toList());
         */

        return messages;
    }

    private List<Message> findAllMessagesFromPages(Gmail service, String user, String userQuery, int daysNumber)
            throws IOException {

        // List of messages meeting the criteria
        ListMessagesResponse listMessagesResponse = listAllMessagesByQuery(service, user, userQuery, daysNumber);

        // Create list and add messages to it
        List<Message> messages = listMessagesResponse.getMessages();

        // System.out.println("Matching messages (page 1): " +
        // listMessagesResponse.toPrettyString());

        // check if there are more than one page available (if yes nextPageToken is
        // displayed with first results of listMessages)
        int k = 2;
        while (listMessagesResponse.getNextPageToken() != null) {

            String token = listMessagesResponse.getNextPageToken();
            listMessagesResponse = setQuery(service, user, userQuery, daysNumber).setPageToken(token).execute();
            List<Message> messages2 = listMessagesResponse.getMessages();
            if (messages2 == null) {
                System.err.println("Matching messages is null for " + k + ")");
                break;
            } else {
                System.out.println("Matching messages (page " + k + "): of " + messages2.size());
                messages.addAll(messages2);
            }
            // System.out.println("Matching messages (page " + k + "): " +
            // listMessagesResponse.toPrettyString());
            k++;
        }
        System.out.println("Total messages in last " + daysNumber + "days size =  " + messages.size());
        return messages;
    }

    private ListMessagesResponse listAllMessagesByQuery(Gmail service, String user, String userQuery, int daysNumber)
            throws IOException {
        try {
            Gmail.Users.Messages.List setQuery = setQuery(service, user, userQuery, daysNumber);
            return setQuery.execute();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private Gmail.Users.Messages.List setQuery(Gmail service, String user, String userQuery, int daysNumber)
            throws IOException {
        try {
            return service.users().messages().list(user).setQ(userQuery + daysNumber + "d");
        } catch (IOException e) {
            throw new IOException("No messages matching the query.");
        }
    }

    /*
     * Label: Label_3859003381065451893 - Akhi Souley
     * Label: Label_5703372474435783836 - Wealthsimple
     * Label: Label_8276412238729250864 - Annabella
     */
    // Displays project details from msgs
    public List<Project> processMessages(List<Message> gmailMessages) {
        List<Project> allProjects = new ArrayList<Project>();
        for (Message msg : gmailMessages) {
            String labels = String.join(",", msg.getLabelIds());
            MessagePart payload = msg.getPayload();
            String email = "KADER";
            if (labels.contains("Label_3859003381065451893")) {
                email = "RAKIA";
                // continue;
            } else if (labels.contains("Label_8276412238729250864")) {
                email = "ANNA";
            }
            String emailBody = "";
            String messageBodyOrig = StringUtils
                    .newStringUtf8(Base64.decodeBase64(payload.getParts().get(0).getBody().getData()));
            int idx = messageBodyOrig.indexOf(ORDER_FILLED);
            int MSG_SIZE = 350;
            String messageBody = messageBodyOrig.substring(idx, idx + MSG_SIZE);
            try {
                int ida = messageBody.indexOf(ACCOUNT_STOCK);
                int idt = messageBody.indexOf(ACCOUNT_TYPE);
                int idc = messageBody.indexOf(A_LINK);
                if (idc == -1){
                    idc = messageBody.length() - 1;
                }
                /* 
                List<MessagePartHeader> headers = msg.getPayload().getHeaders();
                for (MessagePartHeader header : headers) {
                    if (header.getName().equals("Date")) {
                        System.err.println(header.getName().toLowerCase() + ":" + header.getValue());
                    }
                }
                */
                if (ida < idt){
                    emailBody = messageBody.substring(ida, idc);
                   // System.err.println("messageBody body ** : " + emailBody);
                    emailBody = emailBody.replaceAll("\\*", "");
                } else {
                    emailBody = messageBody.substring(idt, idc);  
                   // System.err.println("messageBody body : " + emailBody);
                }          
                // EmailParser parser = new EmailParser(msg);
                Project project = new Project(emailBody, email);
                project.parseData();
                allProjects.add(project);
            } catch (Exception e) {
                System.err.println("messageBody body : " + emailBody);
            }

        }
        return allProjects;
    }

    public static void messagesDisplay(List<Message> messages) {
        // ArrayList<Message> msgs = mainMessagesInThread;
        for (int i = 0; i < messages.size(); i++) {

            Message message = messages.get(i);

            System.out.printf("\n" +
                    "Message %d id: %s\n" +
                    "Message id and message thread id: %s thread: %s\n" +
                    "Index 'i': %d\n",
                    i, message.getId(), message.getId(), message.getThreadId(), i);
        }
    }
}
