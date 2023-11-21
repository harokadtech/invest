package pl.hofman.projectsGmailApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.api.services.gmail.model.Message;

public class Project implements Comparable<Project> {


    private static final String TYPE_CHECK = "Type";


    public static final String SEP = "__";


    Message gmailMessage;
    String messageBody;
    String name;
    float price;
    String type;
    String orderKey;
    private Integer qty;
    private float totalCost;
    String from;
    private String account;

    long timeMillis = 0;
    private String executionTime;

    public String getOrderKey() {
        return orderKey;
    }

    public String getType() {
        return type;
    }

    public Integer getQty() {
        return qty;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public Project(Message gmailMessage) {
        this.gmailMessage = gmailMessage;
    }

    public Project(String pMessageFile) {
        String[] split = pMessageFile.split(SEP);
        this.messageBody = split[1];
        this.from = split[0];
    }

    public Project(String pMessageBody, String pFrom) {
        this.messageBody = pMessageBody;
        this.from = pFrom;
    }

    public String getFrom() {
        return from;
    }

    public String getInputData() {
        return from + SEP + messageBody;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int compareTo(Project p) {
        return orderKey.compareTo(p.getOrderKey());
    }

    public static SimpleDateFormat S_DATE_FORMAT = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM d, yyyy h:mm a z", Locale.ENGLISH);

    public void parseData() {
        String order = messageBody.replaceAll("\\n", ":");
        executionTime = order.split("Time:")[1];
        order = order.replaceAll("\\s", "");
        String[] parts = order.split(":");
        String check = parts[2];
        if (check.compareTo(TYPE_CHECK) == 0) {
            name = parts[5];
            account = parts[1];
            type = parts[3];
            qty = Integer.valueOf(parts[7]);
            price = Float.valueOf(parts[9].substring(1));
        } else {
            name = parts[3];
            account = parts[16];
            orderKey = name + "-" + from + "-" + account;
            type = parts[1];
            qty = Integer.valueOf(parts[5]);
            price = Float.valueOf(parts[7].substring(1));
        }
        type = type.toUpperCase();
        orderKey = name + "-" + from + "-" + account;
        totalCost = qty * price;
        
        timeMillis = parseTimeMillis(executionTime);
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public static long parseTimeMillis(String time) {
        try {
            time = time.trim();
            Date date = DATE_FORMAT.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String formatTimeMillis(long time) {
        return DATE_FORMAT.format(new Date(time));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static void main(String... args) {
        String emailBody = """
        Type: Limit Sell
        Symbol: LEV
        Shares: 100
        Average price: $2.55
        
        Total value: $255.00
        
        Time: March 24, 2023 3:52 PM EDT
        Account: TFSA
        
        Learn more about <a data-click-track-id=\"8252\" href=\"https://em-link.wealthsimple.com/ls/click?upn=mszNRcoJG4aE-2BioIvzGRbhmq0e8KIXlR1vH8xkZQALYAvPKSNYFksTpwqTxj2XXB8wg5HeYs4NwxXJpXxcJ-2BZaKdnQWKFDfEofga2NM504qTn2oBdCgmNxrPJtv-2FEsUu-2FC4JrwDwat9G3BSCMYAz5O3FeJXpDavkJe1pWOlaUfMjVPp4EEcTD05OOyuM-2Bd4CBjtk_PuuPDQAU5jnL7e5alXbST1nLGQ0RQpHmodlZf5QxGArlTI-2BFMVh-2BTlqWuNerbSjDA0VZPUujMlBpyfBpcNSauEy-2FDQ-2FcbEjSJYMZP0Okcf8EXfZw3v8SwrdUDb-2FU86Lm8gecVO-2FSuOxJg5NuHS-2FWjEFcRd3zJvRgF5D2XuHbS2-2Bv8SPEe1FpvvtNuQJ5XxAzdqavAIuGFYHB-2BGOb7LIgQiyGiqchAYDfI2cenU9L4autZUu4aQc3v-2B27FNWXwDDqyhM-2BtqCaz-2B8d-2BZIzpZQcD4VX0gp6PiEGNSGm4jYXIN61HuMOymoDDYRr-2Bpdf2bmAUE90uY5qcD-2BZHh6gXxrf0SM-2B8U4sELHrFJyGa4idtUvcl2ecQo2wLpj-2BxVCzrbdlWnXruyJ98Ud2foInD-2F3P9EK8IFvwv7uBq8yRyIfKaVU3Ex1Mew-2Fh80CWQHKFm-2BZGk-2Fv-2B19kXI5D29RvGjSU-2BgPklDyLMJklj8lYVmByZss0-3D style=\"color:#6c6c6c; text-decoration:underline;\">order&nbsp;fulfillment</a> in the Help Centre.
                                    """;

                    
        Project project = new Project(emailBody, "");

        emailBody = """
        Account: TFSA
        Type: Limit Buy
        Symbol: SHOP
        Shares: 20
        Average price: $61.54
        
        Total cost: $1,230.80
        
        Time: March 24, 2023 11:41 AM EDT
        
        If you have any questions, check out our Help Centre article on <a data-click-track-id=
        """;
        project = new Project(emailBody, "");
        project.parseData();
        project.toString();
    }

    public Message getGmailMessage() {
        return gmailMessage;
    }

}
