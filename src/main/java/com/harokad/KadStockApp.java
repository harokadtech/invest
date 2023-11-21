package com.harokad;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import helper.ConsoleColors;
import helper.MyServiceUnavailableRetryStrategy;

/*



*/
public class KadStockApp {
    private static final String API_ENDPOINT = "https://query2.finance.yahoo.com/v7/finance/quote?lang=en-CA&region=CA&corsDomain=finance.yahoo.com";
    private static int count = 0;
    private static Map<String, Double> oldValues = new HashMap<String, Double>();
    private static Map<String, Double> lastNotifMap = new HashMap<String, Double>(1);
    private static Map<String, List<Double>> alertLimits = new HashMap<String, List<Double>>();
    public static Map<String, Double> volatilityMap = new HashMap<String, Double>();
    private static long lastBeep = System.currentTimeMillis();
    private static final String JAVA_ARGS_INPUT_FILE = "D:\\devCode\\invest\\.vscode\\args.txt";
    private static int READ_ARGS_INT_MIN = 30000;
    public static void main(final String[] args) {
       
        final StringBuilder stockSymbols = new StringBuilder();
        String symbols = Arrays.stream(args).skip(1).collect(Collectors.joining(","));
        for (int i = 1; i < args.length; i++) {
            final String[] array = args[i].split(":");
            final String symbol = array[0];
            final List<Double> limits = new ArrayList<Double>(2);
            if (array.length > 1) {
                final Double limit1 = Double.parseDouble(array[1]);
                limits.add(limit1);
            }
            if (array.length > 2) {
                final Double limit2 = Double.parseDouble(array[1]);
                limits.add(limit2);
            }
            Double vol = 1.0;
            if (array.length > 3) {
                vol = Double.parseDouble(array[1]);
            }
            volatilityMap.put(symbol, vol);
            alertLimits.put(symbol, limits);
            stockSymbols.append(symbol);
            if (i < args.length - 1) {
                stockSymbols.append(",");
            }
        }
        symbols = stockSymbols.toString();
        final BasicCookieStore cookieStore = new BasicCookieStore();
        final BasicClientCookie cookie = new BasicClientCookie("A3", "d%3DAQABBM3Bf2QCEB1DsYprueJhbOE1e8gSmUEFEgEBAQETgWSJZCXcxyMA_eMAAA%26S%3DAQAAAqRnaQwVpYgcAwxD6-WOrxk");
        cookie.setDomain("yahoo.com");
        cookie.setSecure(true);
        cookie.setAttribute("domain", "true");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        final String EXCLUDES = "AQN.TO,HR.UN.TO,GOOG.NE,RNW.TO,LAV.TO,ENB.TO,.TO"; // ,SRU.UN
        try (CloseableHttpClient httpClient = getHttpClient(5, 10000, cookieStore)) {
              String crumb = "ysJOboVsWKV";
              
            // + "&crumb="+crumb
            HttpGet request = new HttpGet(
                    API_ENDPOINT + "&crumb="+crumb + "&symbols=" + URLEncoder.encode(symbols, StandardCharsets.UTF_8));
            final HttpHost proxy = new HttpHost("www-proxy-brmdc.us.oracle.com", 80, "http");
            final RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            request.setHeader("Cookie", "A3=d=AQABBM3Bf2QCEB1DsYprueJhbOE1e8gSmUEFEgEBAQETgWSJZCXcxyMA_eMAAA&S=AQAAAqRnaQwVpYgcAwxD6-WOrxk; Expires=Thu, 6 Jun 2024 05:31:26 GMT; Max-Age=31557600; Domain=.yahoo.com; Path=/; SameSite=None; Secure; HttpOnly");
            // request.setConfig(config);

            // System.exit(0);
            int readArgsInt = 0;
            int interval = Integer.parseInt(args[0]);
            while (true) {
                if(readArgsInt > READ_ARGS_INT_MIN){
                    String newArgs = GmailInvestExtract.readFromInputStream(JAVA_ARGS_INPUT_FILE);
                  //  System.out.println("Using new Args "+ newArgs);
                    readArgsInt = 0;
                    interval =  Integer.parseInt(newArgs.trim());
                }
                try {
                    Thread.sleep(interval);
                    readArgsInt = readArgsInt + interval;
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                final HttpResponse result = httpClient.execute(request);
                final String json = EntityUtils.toString(result.getEntity());
              /*  if(json.indexOf("quoteResponse") < 0){
                    request = new HttpGet(
                    API_ENDPOINT.replace("com/v7", "com/v6") + "&symbols=" + URLEncoder.encode(symbols, StandardCharsets.UTF_8));
                    System.out.println("Using new URL "+ request.getURI());
                    continue;
                }*/ 
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray quoteResponse = jsonObject.getJSONObject("quoteResponse").getJSONArray("result");
                    // quoteResponse = sort(quoteResponse);
                    printSummary(quoteResponse);
                } catch (final Throwable e) {
                    // e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        } catch (final IOException e) {
            try {
                playSound("beep-a.wav");
            } catch (final Exception e1) {
            }
            e.printStackTrace();
        }
    }

    static void playSound(final String soundFile) throws Exception {
        // File f = new File("files/" + soundFile);
        final InputStream inputStream = KadStockApp.class.getClassLoader().getResourceAsStream("files/" + soundFile);
        // AudioInputStream audioIn1 =
        // AudioSystem.getAudioInputStream(f.getAbsoluteFile());
        final AudioInputStream audioIn = AudioSystem.getAudioInputStream(inputStream);
        final Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        // Plays audio once
        clip.start();
        // Plays the audio in a loop
        // clip.loop(Clip.LOOP_CONTINUOUSLY);
        try {
            Thread.sleep(1000);
            // java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Stop the audio
            clip.stop();
            // optional
            clip.close();
        }

    }

    public static CloseableHttpClient getHttpClient(final int executionCount, final int retryInterval, CookieStore cookieStore) {
        final ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new MyServiceUnavailableRetryStrategy.Builder()
                .executionCount(executionCount).retryInterval(retryInterval).build();
        return HttpClientBuilder.create().setRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(final IOException ioe, final int countRetry, final HttpContext context) {
                if (countRetry >= executionCount) {
                    // Do not retry if over max retry count
                    return false;
                }
                System.out.println("Retry " + countRetry);
                if (ioe instanceof InterruptedIOException) {
                    // Timeout
                    return true;
                }

                return true;
            }
        }).setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy)
             // .setDefaultCookieStore(cookieStore)
                .setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    }

    private static void printSummary(final  JSONArray result) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\033[H\033[2J");
        // "%-15s%10s%10s%11s%10s%15s%12s %-20s\033[0m\n"
        sb.append(String.format(ConsoleColors.CYAN_UNDERLINED + "%8s%6s%6s%8s\033[0m\n", "Symbol " + (count),
                "Price", "Diff", "Percent", "Delay"));// , "MarketState", "Long Name"));

        final long currentTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < result.length(); i++) {
            final JSONObject data = result.getJSONObject(i);

            String shortName = data.optString("shortName");
            shortName = shortName.length() > 14 ? shortName.substring(0, 14) : shortName;

            final String longName = data.optString("longName", shortName);
            final String symbol = data.optString("symbol");
            final String marketState = data.optString("marketState");
            final long regularMarketTime = data.optLong("regularMarketTime");

            final double regularMarketPrice = data.optDouble("regularMarketPrice");
            final double regularMarketDayHigh = data.optDouble("regularMarketDayHigh");
            final double regularMarketDayLow = data.optDouble("regularMarketDayLow");
            checkAlertLimit(symbol, regularMarketPrice, regularMarketDayLow, regularMarketDayHigh);
            final double regularMarketChange = data.optDouble("regularMarketChange");
            final double regularMarketChangePercent = data.optDouble("regularMarketChangePercent");

            final String color = regularMarketChange == 0 ? ""
                    : regularMarketChange > 0 ? ConsoleColors.GREEN_BOLD_BRIGHT : ConsoleColors.RED_BOLD_BRIGHT;

            final String shortSymbol = symbol.split("\\.")[0];
            if (lastNotifMap.get(symbol) != null) {
                sb.append(String.format("%7s", shortSymbol + "$"));
            } else {
                sb.append(String.format("%6s", shortSymbol + " "));
            }
            if (regularMarketDayHigh == regularMarketPrice || regularMarketDayLow == regularMarketPrice) {
                sb.append(String.format(ConsoleColors.WHITE_BOLD + color + "%.2f" + ConsoleColors.RESET,
                        regularMarketPrice));
            } else {
                sb.append(String.format(ConsoleColors.WHITE_BOLD + "%.2f" + ConsoleColors.RESET, regularMarketPrice));
            }

            sb.append(String.format(color + "%6s" + ConsoleColors.RESET,
                    " " + String.format("%.2f", regularMarketChange) + " "));// +(regularMarketChange>0?"▲":regularMarketChange<0?"▼":"-")
            sb.append(String.format(color + "%5s" + ConsoleColors.RESET,
                    String.format("%.2f%%", regularMarketChangePercent)));
            // sb.append(String.format("%10s\n", prettyTime(currentTimeMillis -
            // (regularMarketTime * 1000))));
            if (regularMarketDayLow < 5000) {
                sb.append(String.format(color + "%12s\n" + ConsoleColors.RESET,
                        String.format("%.2f", regularMarketDayLow) + "-"
                                + String.format("%.2f", regularMarketDayHigh)));
            } else {
                sb.append(String.format(color + "%12s\n" + ConsoleColors.RESET,
                        String.format("%.2f", regularMarketDayLow)));
            }
            double vol = KadStockApp.volatilityMap.get(symbol);
            double down = regularMarketPrice - regularMarketPrice * vol * 0.02;
            double up = regularMarketPrice + regularMarketPrice * vol * 0.02;
            // sb.append(" " + String.format("%.2f", down) + " - " + String.format("%.2f",
            // up) );
            sb.append("\n");
            /*
             * sb.append
             * LEV.TO:2.76:2.88(String.format("%12s", marketState));
             * sb.append(String.format(" %-20s\n", longName));
             */
            if ("SHOP.TO".compareTo(symbol) == 0) {
                sb.append("\n\n");
            }
        }
        System.out.print(sb);
    }

    private static void print(JSONArray result) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\033[H\033[2J");
        // "%-15s%10s%10s%11s%10s%15s%12s %-20s\033[0m\n"
        sb.append(String.format(ConsoleColors.CYAN_UNDERLINED + "%8s%6s%6s%8s\033[0m\n", "Symbol " + (count),
                "Price", "Diff", "Percent", "Delay"));// , "MarketState", "Long Name"));

        final long currentTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < result.length(); i++) {
            final JSONObject data = result.getJSONObject(i);

            String shortName = data.optString("shortName");
            shortName = shortName.length() > 14 ? shortName.substring(0, 14) : shortName;

            final String longName = data.optString("longName", shortName);
            final String symbol = data.optString("symbol");
            final String marketState = data.optString("marketState");
            final long regularMarketTime = data.optLong("regularMarketTime");

            final double regularMarketPrice = data.optDouble("regularMarketPrice");
            final double regularMarketDayHigh = data.optDouble("regularMarketDayHigh");
            final double regularMarketDayLow = data.optDouble("regularMarketDayLow");
            checkAlertLimit(symbol, regularMarketPrice, regularMarketDayLow, regularMarketDayHigh);
            final double regularMarketChange = data.optDouble("regularMarketChange");
            final double regularMarketChangePercent = data.optDouble("regularMarketChangePercent");

            final String color = regularMarketChange == 0 ? ""
                    : regularMarketChange > 0 ? ConsoleColors.GREEN_BOLD_BRIGHT : ConsoleColors.RED_BOLD_BRIGHT;

            final String shortSymbol = symbol.split("\\.")[0];
            if (lastNotifMap.get(symbol) != null) {
                sb.append(String.format("%7s", shortSymbol + "$"));
            } else {
                sb.append(String.format("%6s", shortSymbol + " "));
            }
            if (regularMarketDayHigh == regularMarketPrice || regularMarketDayLow == regularMarketPrice) {
                sb.append(String.format(ConsoleColors.WHITE_BOLD + color + "%.2f" + ConsoleColors.RESET,
                        regularMarketPrice));
            } else {
                sb.append(String.format(ConsoleColors.WHITE_BOLD + "%.2f" + ConsoleColors.RESET, regularMarketPrice));
            }

            sb.append(String.format(color + "%6s" + ConsoleColors.RESET,
                    " " + String.format("%.2f", regularMarketChange) + " "));// +(regularMarketChange>0?"▲":regularMarketChange<0?"▼":"-")
            sb.append(String.format(color + "%5s" + ConsoleColors.RESET,
                    String.format("%.2f%%", regularMarketChangePercent)));
            // sb.append(String.format("%10s\n", prettyTime(currentTimeMillis -
            // (regularMarketTime * 1000))));
            if (regularMarketDayLow < 5000) {
                sb.append(String.format(color + "%12s\n" + ConsoleColors.RESET,
                        String.format("%.2f", regularMarketDayLow) + "-"
                                + String.format("%.2f", regularMarketDayHigh)));
            } else {
                sb.append(String.format(color + "%12s\n" + ConsoleColors.RESET,
                        String.format("%.2f", regularMarketDayLow)));
            }
            double vol = KadStockApp.volatilityMap.get(symbol);
            double down = regularMarketPrice - regularMarketPrice * vol * 0.02;
            double up = regularMarketPrice + regularMarketPrice * vol * 0.02;
            // sb.append(" " + String.format("%.2f", down) + " - " + String.format("%.2f",
            // up) );
            sb.append("\n");
            /*
             * sb.append
             * LEV.TO:2.76:2.88(String.format("%12s", marketState));
             * sb.append(String.format(" %-20s\n", longName));
             */
            if ("SHOP.TO".compareTo(symbol) == 0) {
                sb.append("\n\n");
            }
        }
        System.out.print(sb);
    }

    private static void checkAlertLimit(final String symbol, final double regularMarketPrice, final double dayLowPrice,
            final double dayHighPrice) {
        final Double lastPrice = oldValues.get(symbol);
        boolean playSoundFlag = false;
        oldValues.put(symbol, regularMarketPrice);
        if (lastPrice == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (now - lastBeep < 1000 * 60 * 1) {
            return;
        }
        if (lastNotifMap.size() >= 2) {
            lastNotifMap.clear();
        }
        final List<Double> limits = alertLimits.get(symbol);
        for (final Double limitPrice : limits) {
            final Double low = lastPrice < regularMarketPrice ? lastPrice : regularMarketPrice;
            final Double high = lastPrice > regularMarketPrice ? lastPrice : regularMarketPrice;
            if (limitPrice > low && limitPrice <= high) {
                lastNotifMap.put(symbol, regularMarketPrice);
                playSoundFlag = true;
            }
        }
        if (regularMarketPrice < dayLowPrice || regularMarketPrice > dayHighPrice) {
            lastNotifMap.put(symbol, regularMarketPrice);
            playSoundFlag = true;
        }
        if (playSoundFlag) {
            try {
                lastBeep = now;
                playSound("beep-a.wav");
            } catch (final Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    private static String prettyTime(final long millis) {
        return String.format("%dm, %ds",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static JSONArray sort(final JSONArray result) {
        final JSONArray sortedJsonArray = new JSONArray();

        final List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            jsonValues.add(result.getJSONObject(i));
        }

        jsonValues.sort((a, b) -> {
            final double valA = a.optDouble("regularMarketChangePercent");
            final double valB = b.optDouble("regularMarketChangePercent");
            return Double.compare(valB, valA);
        });

        for (final JSONObject jsonValue : jsonValues) {
            sortedJsonArray.put(jsonValue);
        }

        return sortedJsonArray;
    }
    // https://query2.finance.yahoo.com/v7/finance/LSPD.TO?period1=1647601200&period2=1679137200&interval=1d&events=history&crumb=yEVCsOtsKVM

    static String payload = """
            quoteResponse: {
                result: [
                    {
                        language: en-US,
                        region: CA,
                        quoteType: EQUITY,
                        typeDisp: Equity,
                        quoteSourceName: Free Realtime Quote,
                        triggerable: true,
                        customPriceAlertConfidence: HIGH,
                        earningsTimestamp: 1667476800,
                        earningsTimestampStart: 1675198800,
                        earningsTimestampEnd: 1675717200,
                        trailingAnnualDividendRate: 0.0,
                        trailingAnnualDividendYield: 0.0,
                        epsTrailingTwelveMonths: -2.645,
                        epsForward: 0.0,
                        epsCurrentYear: -0.35,
                        priceEpsCurrentYear: -62.114285,
                        sharesOutstanding: 150374000,
                        bookValue: 21.983,
                        fiftyDayAverage: 24.2236,
                        fiftyDayAverageChange: -2.4836006,
                        fiftyDayAverageChangePercent: -0.10252814,
                        twoHundredDayAverage: 29.20155,
                        twoHundredDayAverageChange: -7.4615498,
                        twoHundredDayAverageChangePercent: -0.25551897,
                        marketCap: 3269130752,
                        priceToBook: 0.988946,
                        sourceInterval: 15,
                        exchangeDataDelayedBy: 15,
                        prevName: Lightspeed POS Inc.,
                        nameChangeDate: 2022-11-09,
                        averageAnalystRating: 2.1 - Buy,
                        tradeable: false,
                        cryptoTradeable: false,
                        currency: CAD,
                        exchange: TOR,
                        shortName: LIGHTSPEED COMMERCE INC,
                        longName: Lightspeed Commerce Inc.,
                        messageBoardId: finmb_105664036,
                        exchangeTimezoneName: America/Toronto,
                        exchangeTimezoneShortName: EST,
                        gmtOffSetMilliseconds: -18000000,
                        market: ca_market,
                        esgPopulated: false,
                        regularMarketChangePercent: 13.524805,
                        regularMarketPrice: 21.74,
                        askSize: 0,
                        fullExchangeName: Toronto,
                        financialCurrency: USD,
                        regularMarketOpen: 20.44,
                        averageDailyVolume3Month: 1363580,
                        averageDailyVolume10Day: 1984580,
                        fiftyTwoWeekLowChange: 2.8999996,
                        fiftyTwoWeekLowChangePercent: 0.15392779,
                        fiftyTwoWeekRange: 18.84 - 89.79,
                        fiftyTwoWeekHighChange: -68.05,
                        fiftyTwoWeekHighChangePercent: -0.7578795,
                        fiftyTwoWeekLow: 18.84,
                        fiftyTwoWeekHigh: 89.79,
                        marketState: REGULAR,
                        firstTradeDateMilliseconds: 1552055400000,
                        priceHint: 2,
                        regularMarketChange: 2.5900002,
                        regularMarketTime: 1668109366,
                        regularMarketDayHigh: 21.89,
                        regularMarketDayRange: 20.44 - 21.89,
                        regularMarketDayLow: 20.44,
                        regularMarketVolume: 2258551,
                        regularMarketPreviousClose: 19.15,
                        bid: 21.74,
                        ask: 21.75,
                        bidSize: 0,
                        symbol: LSPD.TO
                    }
                ],
            """;
}
