package parser;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import subscription.SingleSubscription;
import subscription.Subscription;

public class SubscriptionParser {
    public Subscription setSubscription(JsonNode node) {

        Subscription subscriptions = new Subscription();

        for (final JsonNode item : node.get("subscriptions")) {
            String url = item.get("url").asText();
            String type = item.get("urlType").asText();

            List<String> urlParams = new ArrayList<String>();
            JsonNode paramsNodeAux = item.get("urlParams");

            for (final JsonNode param : paramsNodeAux) {
                urlParams.add(param.asText());
            }

            SingleSubscription subscription = new SingleSubscription(url, urlParams, type);
            subscriptions.addSingleSubscription(subscription);
        }

        return subscriptions;
    }
}
