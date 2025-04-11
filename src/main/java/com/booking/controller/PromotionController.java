package com.booking.controller;

import com.booking.dao.PromotionDAO;
import com.booking.dao.impl.PromotionDAOImpl;
import com.booking.model.Promotion;

import java.time.LocalDate;
import java.util.List;

public class PromotionController {
    private final PromotionDAO promotionDAO;
    
    public PromotionController() {
        this.promotionDAO = new PromotionDAOImpl();
    }
    
    public Promotion getPromotionById(int id) {
        return promotionDAO.getById(id);
    }
    
    public List<Promotion> getAllPromotions() {
        return promotionDAO.getAll();
    }
    
    public boolean addPromotion(Promotion promotion) {
        return promotionDAO.add(promotion);
    }
    
    public boolean updatePromotion(Promotion promotion) {
        return promotionDAO.update(promotion);
    }
    
    public boolean deletePromotion(int id) {
        return promotionDAO.delete(id);
    }
    
    public List<Promotion> getPromotionsByAccommodationId(int accommodationId) {
        return promotionDAO.getByAccommodationId(accommodationId);
    }
    
    public List<Promotion> getActivePromotions() {
        return promotionDAO.getActivePromotions(LocalDate.now());
    }
    
    public List<Promotion> getActivePromotionsForDate(LocalDate date) {
        return promotionDAO.getActivePromotions(date);
    }
    
    public Promotion getPromotionByPromoCode(String promoCode) {
        return promotionDAO.getByPromoCode(promoCode);
    }
    
    public boolean createPromotion(int accommodationId, String name, String description, 
                                double discountPercentage, LocalDate startDate, LocalDate endDate, 
                                String promoCode) {
        Promotion promotion = new Promotion();
        promotion.setAccommodationId(accommodationId);
        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setDiscountPercentage(discountPercentage);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromoCode(promoCode);
        
        return promotionDAO.add(promotion);
    }
    
    public Double getPromotionDiscount(String promoCode) {
        Promotion promotion = getPromotionByPromoCode(promoCode);
        if (promotion != null && isPromotionValid(promotion)) {
            return promotion.getDiscountPercentage();
        }
        return 0.0;
    }
    
    private boolean isPromotionValid(Promotion promotion) {
        LocalDate today = LocalDate.now();
        return !today.isBefore(promotion.getStartDate()) && !today.isAfter(promotion.getEndDate());
    }
}