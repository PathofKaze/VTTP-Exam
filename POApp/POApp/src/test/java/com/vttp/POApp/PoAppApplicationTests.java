package com.vttp.POApp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vttp.POApp.Model.Quotation;
import com.vttp.POApp.Service.QuotationService;

@SpringBootTest
class PoAppApplicationTests {

	@Autowired
	private QuotationService QuotationSvc;
	@Test
	void contextLoads() {
		List<String> items = new ArrayList<>();
		items.add("durian");
		items.add("plum");
		items.add("pear");

	Optional<Quotation> QuotationOpt = QuotationSvc.getQuotations(items);

	//QuotationOpt should be empty as the js main file only has apple","durian","grapes","orange","pear".

	Assertions.assertFalse(QuotationOpt.isPresent());
	}

}
