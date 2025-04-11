package com.booking.model;

public class Accommodation {
    private int id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
    private double pricePerNight;
    private int maxGuests;
    private String accommodationType; // hotel, apartment, villa, chalet
    private boolean hasWifi;
    private boolean hasPool;
    private boolean hasParkingSpace;
    private int rating; // 1-5 stars
    
    // New properties
    private boolean hasRestaurant;
    private boolean hasAirConditioning;
    private boolean hasPetFriendly;
    private boolean hasGym;
    private double latitude;
    private double longitude;
    private String thumbnailImage;
    private String cancellationPolicy;
    
    public Accommodation() {}
    
    public Accommodation(int id, String name, String address, String city, String country, String description, 
                         double pricePerNight, int maxGuests, String accommodationType, boolean hasWifi, 
                         boolean hasPool, boolean hasParkingSpace, int rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.accommodationType = accommodationType;
        this.hasWifi = hasWifi;
        this.hasPool = hasPool;
        this.hasParkingSpace = hasParkingSpace;
        this.rating = rating;
        this.cancellationPolicy = "STANDARD"; // Default value
    }
    
    // Full constructor with all properties
    public Accommodation(int id, String name, String address, String city, String country, String description, 
                         double pricePerNight, int maxGuests, String accommodationType, boolean hasWifi, 
                         boolean hasPool, boolean hasParkingSpace, int rating, boolean hasRestaurant, 
                         boolean hasAirConditioning, boolean hasPetFriendly, boolean hasGym, double latitude, 
                         double longitude, String thumbnailImage, String cancellationPolicy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.accommodationType = accommodationType;
        this.hasWifi = hasWifi;
        this.hasPool = hasPool;
        this.hasParkingSpace = hasParkingSpace;
        this.rating = rating;
        this.hasRestaurant = hasRestaurant;
        this.hasAirConditioning = hasAirConditioning;
        this.hasPetFriendly = hasPetFriendly;
        this.hasGym = hasGym;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailImage = thumbnailImage;
        this.cancellationPolicy = cancellationPolicy;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public int getMaxGuests() {
        return maxGuests;
    }
    
    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
    
    public String getAccommodationType() {
        return accommodationType;
    }
    
    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }
    
    public boolean isHasWifi() {
        return hasWifi;
    }
    
    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }
    
    public boolean isHasPool() {
        return hasPool;
    }
    
    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }
    
    public boolean isHasParkingSpace() {
        return hasParkingSpace;
    }
    
    public void setHasParkingSpace(boolean hasParkingSpace) {
        this.hasParkingSpace = hasParkingSpace;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    // Getters and setters for new properties
    public boolean isHasRestaurant() {
        return hasRestaurant;
    }
    
    public void setHasRestaurant(boolean hasRestaurant) {
        this.hasRestaurant = hasRestaurant;
    }
    
    public boolean isHasAirConditioning() {
        return hasAirConditioning;
    }
    
    public void setHasAirConditioning(boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }
    
    public boolean isHasPetFriendly() {
        return hasPetFriendly;
    }
    
    public void setHasPetFriendly(boolean hasPetFriendly) {
        this.hasPetFriendly = hasPetFriendly;
    }
    
    public boolean isHasGym() {
        return hasGym;
    }
    
    public void setHasGym(boolean hasGym) {
        this.hasGym = hasGym;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getThumbnailImage() {
        return thumbnailImage;
    }
    
    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
    
    public String getCancellationPolicy() {
        return cancellationPolicy;
    }
    
    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }
    
    // Helper method to handle parking name difference
    public boolean isHasParking() {
        return hasParkingSpace;
    }
    
    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", maxGuests=" + maxGuests +
                ", accommodationType='" + accommodationType + '\'' +
                ", rating=" + rating +
                '}';
    }
}