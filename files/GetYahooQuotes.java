package com.harokad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetYahooQuotes {

    public static void main(String args[]) throws IOException {

        String cookie = null;

    try {
            // Open the URL connection
        URL url = new URL("https://finance.yahoo.com/quote/LSPD");
        URLConnection con = url.openConnection();
        String headerName = null;
        String crumb = null;
        for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {                  
                cookie = con.getHeaderField(i);
                cookie = cookie.substring(0, cookie.indexOf(";"));
                crumb = cookie.substring(cookie.indexOf("=") + 1, cookie.indexOf("&"));
                System.out.println( "Crumb = " + crumb);
            }
        }
/*
        for (Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
            if (entry.getKey() == null || !entry.getKey().equals("Set-Cookie"))
                    continue;
                for (String s : entry.getValue()) {
                    // store your cookie
                    cookie = s;
                  //  System.out.println( "Cookie = " + cookie);
                }
            }


        InputStream inStream = con.getInputStream();
        InputStreamReader irdr = new InputStreamReader(inStream);
        BufferedReader rsv = new BufferedReader(irdr);

        Pattern crumbPattern = Pattern.compile(".*\"CrumbStore\":\\{\"crumb\":\"([^\"]+)\"\\}.*");

        String line = null;
        while (crumb == null && (line = rsv.readLine()) != null) {
            Matcher matcher = crumbPattern.matcher(line);
            if (matcher.matches()) {
                crumb = matcher.group(1);
                System.out.println( "Crumb = " + crumb);
            }
        }
        rsv.close();

         */
    } catch (java.net.SocketTimeoutException e) {
        // The URL connection timed out.  Try again.
        }
    }
}