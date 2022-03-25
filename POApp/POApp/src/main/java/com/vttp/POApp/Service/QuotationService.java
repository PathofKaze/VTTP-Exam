package com.vttp.POApp.Service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import com.vttp.POApp.Model.Quotation;

@Service
public class QuotationService {
    private static final Logger logger = LoggerFactory.getLogger(QuotationService.class);

    private static final String URL = "https://quotation.chuklee.com/";
    private final String QUOTE = "/quotation";
    
    public Optional<Quotation> getQuotations(List<String> items) {

        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        items.stream()
            .forEach((String i) -> {
                jsonArray.add(i);
            });
        
            ResponseEntity<String> resp = null;
            if(resp.getStatusCodeValue() >= 400)
            return Optional.empty();
            logger.error(">>> RestClientException >= 400");
        
        String quoteString = resp.getBody();
        System.out.println(">>> Server Response: " + quoteString);

        InputStream is = new ByteArrayInputStream(quoteString.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject quoteobj = reader.readObject();

        Quotation quote = new Quotation();
        quote.setQuoteId(quoteobj.getString("quoteId"));

        JsonArray quotationsArray = quoteobj.getJsonArray("quotations");
        for(int i = 0; i < quotationsArray.size(); i++) {
            JsonObject item = quotationsArray.getJsonObject(i);
            String itemName = item.getString("item");
            Double unitPrice = item.getJsonNumber("unitPrice").doubleValue();
            quote.addQuotation(itemName, unitPrice.floatValue());
        }

        return Optional.of(quote);
   
}
}
