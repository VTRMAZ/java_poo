

-- Create database
CREATE DATABASE IF NOT EXISTS booking_db;
USE booking_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    is_admin BOOLEAN DEFAULT FALSE,
    profile_image VARCHAR(255),
    registration_date DATE NOT NULL,
    loyalty_points INT DEFAULT 0
    );

-- Accommodations table
CREATE TABLE IF NOT EXISTS accommodations (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    description TEXT,
    price_per_night DECIMAL(10, 2) NOT NULL,
    max_guests INT NOT NULL,
    accommodation_type VARCHAR(20) NOT NULL,
    has_wifi BOOLEAN DEFAULT FALSE,
    has_pool BOOLEAN DEFAULT FALSE,
    has_parking_space BOOLEAN DEFAULT FALSE,
    has_restaurant BOOLEAN DEFAULT FALSE,
    has_air_conditioning BOOLEAN DEFAULT FALSE,
    has_pet_friendly BOOLEAN DEFAULT FALSE,
    has_gym BOOLEAN DEFAULT FALSE,
    rating DECIMAL(3, 1) DEFAULT 0,
    latitude DECIMAL(10, 6),
    longitude DECIMAL(10, 6),
    thumbnail_image VARCHAR(255),
    cancellation_policy VARCHAR(50) DEFAULT 'STANDARD'
    );

-- Accommodation images table (new)
CREATE TABLE IF NOT EXISTS accommodation_images (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    accommodation_id INT NOT NULL,
                                                    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    description VARCHAR(255),
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE
    );

-- Amenities table (new)
CREATE TABLE IF NOT EXISTS amenities (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(50) NOT NULL,
    icon VARCHAR(50),
    category VARCHAR(30)
    );

-- Accommodation amenities mapping (new)
CREATE TABLE IF NOT EXISTS accommodation_amenities (
                                                       accommodation_id INT NOT NULL,
                                                       amenity_id INT NOT NULL,
                                                       PRIMARY KEY (accommodation_id, amenity_id),
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES amenities(id) ON DELETE CASCADE
    );

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        user_id INT NOT NULL,
                                        accommodation_id INT NOT NULL,
                                        check_in_date DATE NOT NULL,
                                        check_out_date DATE NOT NULL,
                                        number_of_guests INT NOT NULL,
                                        total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    booking_date DATE NOT NULL,
    special_requests TEXT,
    promotion_code VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE
    );

-- Promotions table
CREATE TABLE IF NOT EXISTS promotions (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          accommodation_id INT NOT NULL,
                                          name VARCHAR(100) NOT NULL,
    description TEXT,
    discount_percentage DECIMAL(5, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    promo_code VARCHAR(20) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    min_stay_days INT DEFAULT 1,
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE
    );

-- Reviews table
CREATE TABLE IF NOT EXISTS reviews (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL,
                                       accommodation_id INT NOT NULL,
                                       booking_id INT NOT NULL,
                                       rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    cleanliness_rating INT CHECK (cleanliness_rating BETWEEN 1 AND 5),
    comfort_rating INT CHECK (comfort_rating BETWEEN 1 AND 5),
    location_rating INT CHECK (location_rating BETWEEN 1 AND 5),
    value_rating INT CHECK (value_rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE NOT NULL,
    helpful_votes INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
    );

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        booking_id INT NOT NULL,
                                        amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    card_last_digits VARCHAR(4),
    card_type VARCHAR(20),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
    );

-- Locations table (new)
CREATE TABLE IF NOT EXISTS locations (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    description TEXT,
    is_popular BOOLEAN DEFAULT FALSE,
    image_url VARCHAR(255)
    );

-- Wishlists table (new)
CREATE TABLE IF NOT EXISTS wishlists (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         name VARCHAR(50) NOT NULL,
    created_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Wishlist items table (new)
CREATE TABLE IF NOT EXISTS wishlist_items (
                                              wishlist_id INT NOT NULL,
                                              accommodation_id INT NOT NULL,
                                              added_date DATE NOT NULL,
                                              PRIMARY KEY (wishlist_id, accommodation_id),
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id) ON DELETE CASCADE,
    FOREIGN KEY (accommodation_id) REFERENCES accommodations(id) ON DELETE CASCADE
    );
