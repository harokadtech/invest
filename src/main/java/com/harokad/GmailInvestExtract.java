package com.harokad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import pl.hofman.projectsGmailApi.MessageProcessor;
import pl.hofman.projectsGmailApi.Project;

/* class to demonstrate use of Gmail list labels API */
public class GmailInvestExtract {
  /**
   *
   */
  private static final String STOCK_SEP = "###\n";
  /**
   *
   */
  private static final String STOCK_INPUT_FILE = "D:\\devCode\\invest\\files\\StockInput.txt";
  public static final String DESJ_STOCK_INPUT_FILE = "D:\\devCode\\invest\\files\\DesjStockInputMay.txt";
  private static final String STOCK_OUTPUT_FILE = "D:\\devCode\\invest\\files\\AA_DAILY_TRADEs.as";
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Gmail API Invest";
  /**
   * Global instance of the JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  /**
   * Directory to store authorization tokens for this application.
   */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   * GmailScopes.GMAIL_LABELS
   */
  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = GmailInvestExtract.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    // returns an authorized Credential object.
    return credential;
  }
 
  private static final String EXCLUDES = "AQN"; // GOOG,SRU.UN
  public static long fromTime = Project.parseTimeMillis("June 15, 2023 02:30 AM EDT"); //  June May
  private static long now = (new Date()).getTime();
  private static long fromToday = now - (8 + 24*0) * 3600 * 1000;
  private static long fromfewDays = now - (8 + 24*2) * 3600 * 1000;
  private static double deltaPerc = 0.0002;
  public static Map<String, String> ACCOUNT_TYPES = new HashMap<String, String>();
  static {
      ACCOUNT_TYPES.put("5KVXER7", "REEE"); //REEE
      ACCOUNT_TYPES.put("5KVXEZ7", "TFSA");
      ACCOUNT_TYPES.put("5KVXEY9", "RRSP");
  }
  public static void main(String... args) throws IOException, GeneralSecurityException {
    // Ask user to give number of days in the past (including today) to check mails
    // from
    int daysNumber = 1;// askUserForNumberOfDays(); true false February March
    double daysDiff =  (((new Date()).getTime() - fromTime) / (24 * 3600 * 1000));
    daysNumber = (int) daysDiff;
    long Jan15th2023Time = 1673764100000L;
    boolean fromFIle = false;
    Map<String, Double> stockValues = new HashMap<>();
    if (!fromFIle) {
     // StockPriceExtractor.extractedValues(args);
    } 
    // boolean fromFIle
    // boolean merge = false;

    List<Project> allProjects = new ArrayList<Project>(); 
    if (fromTime < Jan15th2023Time) {
      // allProjects =
      // fetchStockFromFIle("D:\\devCode\\invest\\files\\StockInputNov1st202215Janv2023.txt");
    }
    daysNumber = (int) daysDiff + 1;
    if (daysNumber < 51) {
      List<Project> fecthprojects = fromFIle ? fetchStockFromFIle(STOCK_INPUT_FILE) : fetchStockMessages(daysNumber);
      allProjects.addAll(fecthprojects);
    } else {
      System.err.println("Could not fecth more than " + daysNumber + " days");
    }
    if (!fromFIle) {
      allProjects.addAll(DailyDesjTrades.parseCurrentProjects());
    }
    StringBuilder strb = new StringBuilder();
    if(daysNumber > 1){
     strb.append(extracted(fromToday, true, false, allProjects, stockValues)); 
    }
     if(daysNumber > 2){
    //  strb.append(extracted(fromfewDays, true, true, allProjects, stockValues));
      strb.append(extracted(fromfewDays, true, false, allProjects, stockValues)); 
     }
    // strb.append(extracted(fromTime, true, true, allProjects, stockValues));
     strb.append(extracted(fromTime, false, false, allProjects, stockValues));
    try {
      writeStringtoFile(STOCK_OUTPUT_FILE, strb.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String extracted(long fromTime, boolean fromFIle, boolean merge, List<Project> allProjects,
      Map<String, Double> stockValues) {
    long today = (new Date()).getTime() - 9 * 3600 * 1000;
    StringBuilder strInput = new StringBuilder();
    DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
    decimalFormatSymbols.setDecimalSeparator('.');
    DecimalFormat df = new DecimalFormat("0.00", decimalFormatSymbols);

    HashMap<String, List<Project>> stocksMap = new HashMap<String, List<Project>>();
    String key = "";
    List<String> prints = new ArrayList<String>();
    int projectCount = 0;
    for (Project project : allProjects) {
      if (fromTime > project.getTimeMillis() || EXCLUDES.contains(project.getName())) {
        // System.out.print("SKIP --" + project.getExecutionTime());
        continue;
      }    
      key = project.getOrderKey();
      if (merge) {
        key = project.getName() + "_" + project.getAccount();
      }
      if(project.getTimeMillis() < today){
        strInput.append(project.getInputData() + "###\n");  
      }

      projectCount++;

      List<Project> projects = stocksMap.get(key);
      if (projects == null) {
        projects = new ArrayList<Project>();
        projects.add(project);
      } else {
        projects.add(project);
      }
      stocksMap.put(key, projects);
    }
    StringBuilder strLaunch = new StringBuilder();
    StringBuilder strActions = new StringBuilder();
    stocksMap.forEach((key1, stockList) -> {
      int bCount = 0, sCount = 0, lowBCount = 0, upSCount = 0;
      float bCost = 0, sCost = 0;
      String name = "", from = "", account = "";
      StringBuilder strb = new StringBuilder();

      for (Project stock : stockList) {
        String type = stock.getType();
        name = stock.getName();
        from = stock.getFrom();
        account = stock.getAccount();
        double price = stock.getPrice();
        Double currPrice = stockValues.get(name);
        if (currPrice == null) {
          currPrice = price;
        }
        double delta = Math.abs((price - currPrice) / currPrice);
        if (type.contains("BUY")) {
          bCount = bCount + stock.getQty();
          bCost = bCost + stock.getTotalCost();
          if (price > currPrice && delta > deltaPerc) {
            lowBCount = lowBCount + stock.getQty();
          }
        } else {
          sCount = sCount + stock.getQty();
          sCost = sCost + stock.getTotalCost();
          if (price < currPrice && delta > deltaPerc) {
            upSCount = upSCount + stock.getQty();
          }

        }
      }
      String from2 = from + account + "-";
      if (merge) {
        from = "";
        account = "";
        from2 = "";
      }
      boolean append = false;
      if (bCount > 0) {
        float bAverage = bCost / bCount;
        strb.append(key1 + " B= " + bCount + " x " +
            df.format(bAverage) + " - " + lowBCount);
        append = true;
      }
      if (sCount > 0) {
        float sAverage = sCost / sCount;
        if (!append) {
          strb.append(key1);
        }
        strb.append(" S= " + sCount + " x " + df.format(sAverage) + " - " + upSCount);

        // SHOP-- B= 159 x 45.37 S= 234 x 47.85
        // LSPD-- B= 622 x 21.58 S= 253 x 21.87 369
        float sTotalAverage = sAverage;
        if (sCount - bCount != 0) {
          sTotalAverage = Math.abs(sCost - bCost) / Math.abs(sCount - bCount);
        }
        /*
         * Double vol = KadStockApp.volatilityMap.get(name);
         * double down = sTotalAverage * vol * 0.02;
         * double up = sTotalAverage * vol * 0.02;
         */
        strLaunch.append(
            from2 + name + ".TO:" + df.format(sTotalAverage * 0.97) + ":" + df.format(sTotalAverage * 0.99) + " ");
      }
      strb.append(" \n");
      prints.add(strb.toString());
    });
    Collections.sort(prints);

    StringBuilder strPrint = new StringBuilder(
        "Total projects: " + projectCount + " after " + Project.S_DATE_FORMAT.format(new Date(fromTime)));
    strPrint.append(" \n");
    for (String stockLine : prints) {
      strPrint.append(stockLine);
    }
    strPrint.append(" \n");
    if (!fromFIle) {
      try {
        writeStringtoFile(STOCK_INPUT_FILE, strInput.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //System.out.println(strPrint.toString());
    // System.out.println(strLaunch.toString());
    return strPrint.toString();
  }

  private static List<Project> fetchStockFromFIle(String fileName) throws IOException {
    String inputStr = readFromInputStream(STOCK_INPUT_FILE);
    String[] inputs = inputStr.split(STOCK_SEP);
    List<Project> allProjects = new ArrayList<Project>();
    for (String messageFile : inputs) {
      Project project = new Project(messageFile);
      project.parseData();
      allProjects.add(project);
    }
    if (allProjects.size() < 200) {
      System.err.println("Could not fecth more all data days size= " + allProjects.size());
      // System.exit(-1);
    } else {
      System.out.println("All data fetched  size= " + allProjects.size());
      // System.exit(-1);
    }
    return allProjects;
  }

  public static List<Project> fetchStockMessages(int daysNumber) throws GeneralSecurityException, IOException {
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    // Print the labels in the user's account.
    String user = "me";
    /*
     * ListLabelsResponse listResponse =
     * service.users().labels().list(user).execute();
     * List<Label> labels = listResponse.getLabels();
     * if (labels.isEmpty()) {
     * System.out.println("No labels found.");
     * } else {
     * System.out.println("Labels:");
     * for (Label label : labels) {
     * System.out.printf(" Label: %s - %s\n", label.getId(), label.getName());
     * }
     * }
     */

    String userQuery = "label:Wealthsimple AND subject:Your order has been filled AND newer_than:";

    MessageProcessor messageProcessor = new MessageProcessor();
    List<Message> mainGmailMessages = messageProcessor.findMainGmailMessages(service, user, userQuery, daysNumber);

    return messageProcessor.processMessages(mainGmailMessages);
  }

  public static void writeStringtoFile(String fileName, String data)
      throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    writer.write(data);

    writer.close();
  }

  public static String readFromInputStream(String file)
      throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    // InputStream inputStream =
    // GmailInvestExtract.class.getResourceAsStream(STOCK_INPUT_FILE);
    InputStream inputStream = new FileInputStream(file);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }

  private static int askUserForNumberOfDays() {

    System.out.println("Enter the number of days you want to check the news (including today)");
    int daysNumber = 0;
    boolean ifNumber = true;

    while (ifNumber) {
      try {
        // Scanner scanner = new Scanner(System.in);
        // daysNumber = scanner.nextInt();
        ifNumber = false;
      } catch (InputMismatchException e) {
        System.out.println("Zły format danych, wprowadź liczbę.");
      }
    }
    return daysNumber;
  }
}