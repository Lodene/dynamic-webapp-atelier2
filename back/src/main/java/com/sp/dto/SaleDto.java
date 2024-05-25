package com.sp.dto;

import com.sp.model.Card;

public class SaleDto {
    private Long id;
    private Double price;
    private String action;
    private Card card;
    private String username;

    public SaleDto() {
        super();
    }

    public SaleDto(Long id, Double price, String action, Card card, String username) {
        super();
        this.id = id;
        this.price = price;
        this.action = action;
        this.card = card;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SaleDto{" +
                "id=" + id +
                ", price=" + price +
                ", action='" + action + '\'' +
                ", card=" + card +
                ", username='" + username + '\'' +
                '}';
    }
}
