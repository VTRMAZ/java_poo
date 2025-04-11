package com.booking.dao;

import com.booking.model.Promotion;
import java.time.LocalDate;
import java.util.List;

public interface PromotionDAO {
    Promotion getById(int id);
    List<Promotion> getAll();
    boolean add(Promotion promotion);
    boolean update(Promotion promotion);
    boolean delete(int id);
    List<Promotion> getByAccommodationId(int accommodationId);
    List<Promotion> getActivePromotions(LocalDate date);
    Promotion getByPromoCode(String promoCode);
}