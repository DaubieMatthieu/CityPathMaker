package main.java.helpers;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringJoiner;

public interface googleMapsHelper {
    String address = "https://www.google.com/maps/dir/";
    HashMap<String, String> defaultParameters = new HashMap<String, String>() {{
        put("api", "1");
        put("doflg", "ptk");
        put("units", "metric");
    }};

    static URI getGooglePathURI(String origin, String destination) {
        HashMap<String, String> parameters = new HashMap<>(defaultParameters);
        parameters.put("origin", origin);
        parameters.put("destination", destination);
        parameters.put("travelmode", "transit");
        return getUri(parameters);
    }

    static URI AddressPathToURI(String[] addressPath) {
        HashMap<String, String> parameters = new HashMap<>(defaultParameters);
        String origin = addressPath[0];
        String destination = addressPath[addressPath.length - 1];
        parameters.put("origin", origin);
        parameters.put("destination", destination);
        parameters.put("travelmode", "walking");
        StringJoiner waypoints = new StringJoiner("|");
        for (int i = 1; i < addressPath.length - 1; i++) waypoints.add(addressPath[i]);
        parameters.put("waypoints", waypoints.toString());
        return getUri(parameters);
    }

    static URI getUri(HashMap<String, String> parameters) {
        StringJoiner strParameters = new StringJoiner("&", "?", "");
        for (String parameter : parameters.keySet())
            strParameters.add(encodeURI(parameter + "=" + parameters.get(parameter)));
        try {
            return new URI(address + strParameters.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    //launch url on default navigator
    static void launchUri(URI uri) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String encodeURI(String str) {
        String encoding = StandardCharsets.UTF_8.toString();
        String encoded = null;
        try {
            encoded = str.replace(" ", "+")
                    .replace("|", URLEncoder.encode("|", encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }
}
