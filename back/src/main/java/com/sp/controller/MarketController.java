package com.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.dto.SaleDto;
import com.sp.service.MarketService;
import com.sp.utils.RequestUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
	MarketService marketService;

    @Autowired
    RequestUtil requestUtil;
    
    @RequestMapping(method = RequestMethod.PUT, value = "/sale")
    public void sell(HttpServletRequest request, @RequestParam String cardId, @RequestParam String price) {
		Long userId = requestUtil.getUserId(request);
        System.out.println("userId: " + userId);
		marketService.newSale(userId, Long.valueOf(cardId), Double.valueOf(price));
	}

    @RequestMapping(method = RequestMethod.GET, value = "/sales")
	public List<SaleDto> getAllSales() {
		return marketService.getAllSales();
	}

    @RequestMapping(method = RequestMethod.POST, value = "/buy")
	public void buySale(HttpServletRequest request, @RequestParam String cardId) {
        System.out.println("saleId: " + cardId);
		Long userId = requestUtil.getUserId(request);
		marketService.newPurchase(userId, Long.valueOf(cardId));
	}

}
