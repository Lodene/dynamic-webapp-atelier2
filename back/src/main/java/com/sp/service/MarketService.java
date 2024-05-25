package com.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sp.dto.SaleDto;
import com.sp.model.Card;
import com.sp.model.Market;
import com.sp.model.User;
import com.sp.repo.CardRepository;
import com.sp.repo.MarketRepository;
import com.sp.repo.UserRepository;

@Service
public class MarketService {

    @Autowired
	CardRepository cardRepository;

    @Autowired
	MarketRepository marketRepository;

    @Autowired
    UserRepository userRepository;
    
    public void newSale(Long userId, Long cardId, Double price) {
		Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        System.out.println("card: " + card);
		if (!card.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		if (price < 0.0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		Market market = new Market(card, price,"SELL");
		marketRepository.save(market);
	}

    public List<SaleDto> getAllSales() {
        //return marketRepository.findByAction("SELL");
        Iterable<Market> sales = marketRepository.findByAction("SELL");
        List<SaleDto> salesDto = new ArrayList<>();
        for (Market sale : sales) {
            //salesDto.add(new SaleDto(sale));
            SaleDto saleDto = new SaleDto();
            saleDto.setId(sale.getId());
            saleDto.setAction(sale.getAction());
            saleDto.setPrice(sale.getPrice());
            saleDto.setCard(sale.getCard());
            saleDto.setUsername(sale.getCard().getUser().getUsername());
            salesDto.add(saleDto);

        }
        return salesDto;

    }

    public void newPurchase(Long userId, Long cardId) {
        Market sale = marketRepository.findByCardId(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (sale.getAction().equals("BUY")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (sale.getCard().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User buyer = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User seller = sale.getCard().getUser();
        double price = sale.getPrice();

        System.out.println("price of sale: " + sale.getPrice());

        // Check if the buyer has enough money
        if (price > buyer.getMoney()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // Update the buyer's and seller's money
        buyer.setMoney(buyer.getMoney() - price);
        seller.setMoney(seller.getMoney() + price);

        // Transfer the card ownership
        Card card = sale.getCard();
        card.setUser(buyer);

        // Save all changes in the correct order
        userRepository.save(buyer);
        userRepository.save(seller);
        cardRepository.save(card);
        marketRepository.delete(sale);

        // Log the transaction
        System.out.println("Buyer new balance: " + buyer.getMoney());
        System.out.println("Seller new balance: " + seller.getMoney());
    }
}
