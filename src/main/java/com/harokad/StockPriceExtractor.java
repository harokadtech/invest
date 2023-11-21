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

import helper.MyServiceUnavailableRetryStrategy;

/*



*/
public class StockPriceExtractor {
    private static final String API_ENDPOINT = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-EN&region=CA&corsDomain=finance.yahoo.com";
    private static Map<String, Double> stockValues = new HashMap<String, Double>();

    public static void main(final String[] args) {
        extractedValues(args);
    }


    public static Map<String, Double> extractedValues(final String[] args) {
        final long interval = Long.parseLong(args[0]);
        final StringBuilder stockSymbols = new StringBuilder();
        String symbols = Arrays.stream(args).skip(1).collect(Collectors.joining(","));
        for (int i = 1; i < args.length; i++) {
            final String[] array = args[i].split(":");
            final String symbol = array[0];
            stockSymbols.append(symbol);
            if (i < args.length - 1) {
                stockSymbols.append(",");
            }
        }
        symbols = stockSymbols.toString();
        try (CloseableHttpClient httpClient = getHttpClient(5, 10000)) {
            final HttpGet request = new HttpGet(
                    API_ENDPOINT + "&symbols=" + URLEncoder.encode(symbols, StandardCharsets.UTF_8));
            final HttpHost proxy = new HttpHost("www-proxy-brmdc.us.oracle.com", 80, "http");
            final RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
           // request.setConfig(config);
                final HttpResponse result = httpClient.execute(request);
                final String json = EntityUtils.toString(result.getEntity());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final JSONArray resultJson = jsonObject.getJSONObject("quoteResponse").getJSONArray("result");
                    for (int i = 0; i < resultJson.length(); i++) {
                        final JSONObject data = resultJson.getJSONObject(i);
                        final String symbol = data.optString("symbol");
                        final double regularMarketPrice = data.optDouble("regularMarketPrice");
                        final String shortSymbol = symbol.split("\\.")[0];
                        stockValues.put(shortSymbol, regularMarketPrice);
                    }
                } catch (final Throwable e) {
                    e.printStackTrace();
                }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return stockValues;
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
                System.out.println("Retry "+countRetry);
                if (ioe instanceof InterruptedIOException) {
                    // Timeout
                    return true;
                }

                return true;
            }
        }).setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy)
                .setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    }

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
