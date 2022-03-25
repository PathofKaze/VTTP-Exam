package com.vttp.POApp.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

import com.vttp.POApp.Model.Quotation;
import com.vttp.POApp.Service.QuotationService;

@RestController
@RequestMapping(path="/api")
public class PurchaseOrderRestController {

    @Autowired
    private QuotationService quotationSvc;

    @PostMapping(path = "/purchaseorder",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> postPurchaseOrder(@RequestBody String payload) {

    JsonObjectBuilder builder;
    try (InputStream is = new ByteArrayInputStream(payload.getBytes()));
    JsonReader r = Json.createReader(is);
    JsonObject PurchaseOrderObj = r.readObject();
    builder = Json.createObjectBuilder();
    
    JsonArray jsonItems = PurchaseOrderObj.getJsonArray("lineItems");
    
    List<String> itemname = new ArrayList<>();
    List<Integer> itemquantity = new ArrayList<>();

    if (jsonItems != null) {
        for (int i=0; i<jsonItems.size(); i++) {
            itemname.add(jsonItems.getJsonObject(i).getString("item"));
            itemquantity.add(jsonItems.getJsonObject(i).getInt("quantity"));
        }
    }
    
    Optional<Quotation> QuoteOpt = quotationSvc.getQuotations(itemname);
    Quotation quotes = QuoteOpt.get();
    
    String invoiceId = quotes.getQuoteId();
    List<Float> quoteP = new ArrayList<>();
    
    Float Total = 0f;

    for (int i=0 ; i<itemname.size();i++) {
    Float Price = quoteP.get(i);
    Integer Quantity = itemquantity.get(i);
    Float Cost = Price * Quantity;
    Total = Total + Cost;
    }
    
    System.out.printf(">>> Total Cost: %s\n", Total);
    

    catch (IOException ex){
        ex.printStackTrace();
        throw ex;
    }
    catch (Exception ex){
        JsonObject result = Json.createObjectBuilder()
        .add("","")
        .build();
        return ResponseEntity.status(400).body(result.toString());
    }
    return ResponseEntity.ok(builder.build().toString());

    }