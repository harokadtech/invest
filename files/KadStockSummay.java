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

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import helper.ConsoleColors;
import helper.MyServiceUnavailableRetryStrategy;

/*



*/
public class KadStockSummay {
    private static final String API_ENDPOINT = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-CA&region=CA&corsDomain=finance.yahoo.com";
    private static final String SUMMARY_ENDPOINT = "https://query1.finance.yahoo.com/v11/finance/quoteSummary/XXX?modules=financialData";

    private static int count = 0;
    private static Map<String, Double> oldValues = new HashMap<String, Double>();
    private static Map<String, Double> lastNotifMap = new HashMap<String, Double>(1);
    private static Map<String, List<Double>> alertLimits = new HashMap<String, List<Double>>();
    public static Map<String, Double> volatilityMap = new HashMap<String, Double>();
    private static long lastBeep = System.currentTimeMillis();

    public static void main(final String[] args) {
        final long interval = Long.parseLong(args[0]);
        final StringBuilder stockSymbols = new StringBuilder();
        String symbols = Arrays.stream(args).skip(1).collect(Collectors.joining(","));
        symbols = stockSymbols.toString();
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
        // System.exit(0);
        try (CloseableHttpClient httpClient = getHttpClient(5, 10000)) {
            final String[] stockS = stockSymbols.toString().split(",");
            while (true) {
                for (String ticker : stockS) {
                    final HttpGet request = new HttpGet(
                            SUMMARY_ENDPOINT.replace("XXX", ticker));
                    final HttpResponse result = httpClient.execute(request);
                    count++;
                    final String json = EntityUtils.toString(result.getEntity());
                    try {
                        printSummary(new JSONObject(json));
                        Thread.sleep(interval / 10);
                        // java.awt.Toolkit.getDefaultToolkit().beep();
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    } catch (final Throwable e) {
                        // e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
                try {
                    Thread.sleep(interval);
                    // java.awt.Toolkit.getDefaultToolkit().beep();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
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
        final InputStream inputStream = KadStockSummay.class.getClassLoader().getResourceAsStream("files/" + soundFile);
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

    public static CloseableHttpClient getHttpClient(final int executionCount, final int retryInterval) {
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
                .setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    }

    private static void printSummary(final JSONObject jsonObject) {
        final JSONArray result = jsonObject.getJSONObject("quoteResponse").getJSONArray("result");
        // result = sort(result);

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
            double vol = KadStockSummay.volatilityMap.get(symbol);
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

    private static void print(final JSONObject jsonObject) {
        final JSONArray result = jsonObject.getJSONObject("quoteResponse").getJSONArray("result");
        // result = sort(result);

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
            double vol = KadStockSummay.volatilityMap.get(symbol);
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
    // https://query1.finance.yahoo.com/v7/finance/LSPD.TO?period1=1647601200&period2=1679137200&interval=1d&events=history&crumb=yEVCsOtsKVM

    static String payload = """
        {"quoteSummary":{"result":[{"financialData":{"maxAge":86400,"currentPrice":{"raw":18.66,"fmt":"18.66"},"targetHighPrice":{"raw":55.01,"fmt":"55.01"},"targetLowPrice":{"raw":21.43,"fmt":"21.43"},"targetMeanPrice":{"raw":31.52,"fmt":"31.52"},"targetMedianPrice":{"raw":28.56,"fmt":"28.56"},"recommendationMean":{"raw":2.4,"fmt":"2.40"},"recommendationKey":"buy","numberOfAnalystOpinions":{"raw":16,"fmt":"16","longFmt":"16"},"totalCash":{"raw":838118016,"fmt":"838.12M","longFmt":"838,118,016"},"totalCashPerShare":{"raw":5.559,"fmt":"5.56"},"ebitda":{"raw":-243548000,"fmt":"-243.55M","longFmt":"-243,548,000"},"totalDebt":{"raw":26403000,"fmt":"26.4M","longFmt":"26,403,000"},"quickRatio":{"raw":6.147,"fmt":"6.15"},"currentRatio":{"raw":6.56,"fmt":"6.56"},"totalRevenue":{"raw":692835968,"fmt":"692.84M","longFmt":"692,835,968"},"debtToEquity":{"raw":1.04,"fmt":"1.04"},"revenuePerShare":{"raw":4.632,"fmt":"4.63"},"returnOnAssets":{"raw":-0.061929997,"fmt":"-6.19%"},"returnOnEquity":{"raw":-0.37050998,"fmt":"-37.05%"},"grossProfits":{"raw":271173000,"fmt":"271.17M","longFmt":"271,173,000"},"freeCashflow":{"raw":39440000,"fmt":"39.44M","longFmt":"39,440,000"},"operatingCashflow":{"raw":-95039000,"fmt":"-95.04M","longFmt":"-95,039,000"},"earningsGrowth":{},"revenueGrowth":{"raw":0.236,"fmt":"23.60%"},"grossMargins":{"raw":0.45415002,"fmt":"45.42%"},"ebitdaMargins":{"raw":-0.35152,"fmt":"-35.15%"},"operatingMargins":{"raw":-0.45529997,"fmt":"-45.53%"},"profitMargins":{"raw":-1.6021899,"fmt":"-160.22%"},"financialCurrency":"USD"}}],"error":null}}
            """;
}
