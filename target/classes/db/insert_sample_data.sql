USE booking_db;

-- Vérifions d'abord si les tables sont vides pour éviter les erreurs de duplicats
DELETE FROM wishlist_items;
DELETE FROM wishlists;
DELETE FROM payments;
DELETE FROM reviews;
DELETE FROM promotions;
DELETE FROM bookings;
DELETE FROM accommodation_amenities;
DELETE FROM accommodation_images;
DELETE FROM accommodations;
DELETE FROM amenities;
DELETE FROM users;
DELETE FROM locations;

-- Insert sample users
INSERT INTO users (username, password, email, first_name, last_name, phone_number, is_admin, profile_image, registration_date, loyalty_points)
VALUES
    ('admin', 'admin123', 'admin@booking.com', 'Admin', 'User', '+33123456789', TRUE, 'profile_admin.jpg', '2023-01-01', 500),
    ('john_doe', 'password123', 'john.doe@example.com', 'John', 'Doe', '+33612345678', FALSE, 'profile_john.jpg', '2023-02-15', 120),
    ('jane_smith', 'password123', 'jane.smith@example.com', 'Jane', 'Smith', '+33687654321', FALSE, 'profile_jane.jpg', '2023-03-10', 85),
    ('alice_johnson', 'password123', 'alice.johnson@example.com', 'Alice', 'Johnson', '+33612345679', FALSE, 'profile_alice.jpg', '2023-04-05', 150),
    ('bob_brown', 'password123', 'bob.brown@example.com', 'Bob', 'Brown', '+33687654322', FALSE, 'profile_bob.jpg', '2023-05-20', 45),
    ('charlie_davis', 'password123', 'charlie.davis@example.com', 'Charlie', 'Davis', '+33612345670', FALSE, 'profile_charlie.jpg', '2023-06-12', 200),
    ('diana_evans', 'password123', 'diana.evans@example.com', 'Diana', 'Evans', '+33687654323', FALSE, 'profile_diana.jpg', '2023-07-08', 75),
    ('ethan_garcia', 'password123', 'ethan.garcia@example.com', 'Ethan', 'Garcia', '+33612345671', FALSE, 'profile_ethan.jpg', '2023-08-14', 110),
    ('fiona_harris', 'password123', 'fiona.harris@example.com', 'Fiona', 'Harris', '+33687654324', FALSE, 'profile_fiona.jpg', '2023-09-22', 60),
    ('george_miller', 'password123', 'george.miller@example.com', 'George', 'Miller', '+33612345672', FALSE, 'profile_george.jpg', '2023-10-30', 90);

-- Insert amenities
INSERT INTO amenities (name, icon, category)
VALUES
    ('Free WiFi', 'wifi', 'ESSENTIAL'),
    ('Swimming Pool', 'pool', 'LEISURE'),
    ('Parking', 'parking', 'CONVENIENCE'),
    ('Restaurant', 'restaurant', 'DINING'),
    ('Air Conditioning', 'ac', 'COMFORT'),
    ('Pet Friendly', 'pet', 'SPECIAL'),
    ('Gym', 'gym', 'WELLNESS'),
    ('Spa', 'spa', 'WELLNESS'),
    ('Bar', 'bar', 'DINING'),
    ('Room Service', 'service', 'COMFORT'),
    ('Business Center', 'business', 'WORK'),
    ('Free Breakfast', 'breakfast', 'DINING'),
    ('Laundry Service', 'laundry', 'CONVENIENCE'),
    ('Airport Shuttle', 'shuttle', 'TRANSPORTATION'),
    ('Concierge', 'concierge', 'SERVICE'),
    ('Beach Access', 'beach', 'LOCATION'),
    ('Kid Friendly', 'kids', 'SPECIAL'),
    ('Hot Tub', 'hottub', 'WELLNESS'),
    ('Mountain View', 'mountain', 'VIEW'),
    ('Sea View', 'sea', 'VIEW');

-- Insert sample accommodations
INSERT INTO accommodations (name, address, city, country, description, price_per_night, max_guests, accommodation_type, has_wifi, has_pool, has_parking_space, has_restaurant, has_air_conditioning, has_pet_friendly, has_gym, rating, latitude, longitude, thumbnail_image, cancellation_policy)
VALUES
    ('Luxury Hotel Paris', '123 Avenue des Champs-Élysées', 'Paris', 'France', 'Elegant 5-star hotel in the heart of Paris with stunning views of the Eiffel Tower. Experience luxury, comfort, and exceptional service in one of the world\'s most romantic cities. Our hotel features spacious rooms with modern amenities, a gourmet restaurant serving authentic French cuisine, and a rooftop terrace where you can enjoy panoramic views of the city.', 350.00, 4, 'hotel', TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, 4.8, 48.8698, 2.3075, 'hotel_paris_1.jpg', 'FLEXIBLE'),

('Cozy Apartment Montmartre', '45 Rue des Abbesses', 'Paris', 'France', 'Charming apartment in the artistic district of Montmartre, close to Sacré-Cœur. This beautifully renovated apartment offers a true Parisian experience with its vintage decor, wooden beams, and authentic charm. Situated in the heart of the artistic quarter, you\'ll find yourself surrounded by quaint cafés, art galleries, and the famous steps of Montmartre.', 120.00, 2, 'apartment', TRUE, FALSE, FALSE, FALSE, TRUE, TRUE, FALSE, 4.5, 48.8865, 2.3372, 'apartment_paris_1.jpg', 'STANDARD'),

    ('Beach Villa Nice', '67 Promenade des Anglais', 'Nice', 'France', 'Beautiful villa with private access to the beach and Mediterranean sea views. This luxurious beachfront villa offers an exclusive escape with its own garden, infinity pool overlooking the azure Mediterranean waters, and direct beach access. The property features high-end furnishings, a fully equipped gourmet kitchen, and spacious terraces perfect for al fresco dining.', 580.00, 8, 'villa', TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, FALSE, 4.9, 43.6959, 7.2654, 'villa_nice_1.jpg', 'STRICT'),

    ('Alpine Chalet Chamonix', '23 Route des Pelerins', 'Chamonix', 'France', 'Traditional wooden chalet with mountain views, perfect for skiing holidays. This authentic alpine chalet offers the perfect mountain retreat with its warm wooden interiors, stone fireplace, and breathtaking views of Mont Blanc. Located just minutes from the slopes, it provides easy access to some of the best skiing in the Alps, with a cozy retreat to return to after a day of adventure.', 220.00, 6, 'chalet', TRUE, FALSE, TRUE, FALSE, TRUE, FALSE, FALSE, 4.7, 45.9237, 6.8694, 'chalet_chamonix_1.jpg', 'STANDARD'),

    ('Boutique Hotel Lyon', '56 Rue de la République', 'Lyon', 'France', 'Stylish boutique hotel in a historic building in the center of Lyon. Set in a magnificently restored 18th-century building, this boutique hotel perfectly blends historic charm with contemporary elegance. Located in the UNESCO-listed old town, the hotel offers easy access to Lyon\'s famous gastronomic restaurants, historic sites, and cultural attractions.', 180.00, 3, 'hotel', TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, TRUE, 4.6, 45.7640, 4.8357, 'hotel_lyon_1.jpg', 'STANDARD'),

('Countryside Cottage Provence', '34 Chemin des Lavandes', 'Aix-en-Provence', 'France', 'Authentic stone cottage surrounded by lavender fields in the heart of Provence. Experience the quintessential Provence lifestyle in this lovingly restored stone cottage surrounded by fragrant lavender fields and olive groves. The property features a charming garden with a private pool, outdoor dining area, and stunning views of the Luberon mountains.', 150.00, 4, 'cottage', TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, 4.8, 43.5297, 5.4474, 'cottage_provence_1.jpg', 'MODERATE'),

('Modern Apartment Bordeaux', '78 Quai des Chartrons', 'Bordeaux', 'France', 'Contemporary apartment with river views in the trendy Chartrons district. This sleek, modern apartment offers spectacular views of the Garonne River from its floor-to-ceiling windows and private balcony. Located in the fashionable Chartrons neighborhood, you\'ll be surrounded by wine bars, antique shops, and excellent restaurants, with the historic city center just a short stroll away.', 140.00, 3, 'apartment', TRUE, FALSE, TRUE, FALSE, TRUE, FALSE, FALSE, 4.4, 44.8488, -0.5707, 'apartment_bordeaux_1.jpg', 'FLEXIBLE'),

    ('Luxury Resort Cannes', '58 Boulevard de la Croisette', 'Cannes', 'France', 'Prestigious 5-star resort overlooking the Mediterranean Sea in glamorous Cannes. This iconic resort offers the ultimate French Riviera experience with its private beach, luxurious spa, and world-class restaurants. The elegant rooms and suites feature private balconies with stunning sea views, and the hotel\'s central location on La Croisette puts you at the heart of this legendary destination.', 450.00, 4, 'hotel', TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, 4.9, 43.5507, 7.0205, 'resort_cannes_1.jpg', 'STRICT'),

('Historic Chateau Loire Valley', '15 Route des Vignobles', 'Tours', 'France', 'Magnificent 16th-century chateau set in beautiful gardens in the Loire Valley. Step back in time and experience the grandeur of the French aristocracy in this meticulously restored chateau. The property features antique furnishings, tapestries, and artwork, alongside modern comforts. The extensive grounds include formal gardens, a swimming pool, and views over the surrounding vineyards and countryside.', 680.00, 12, 'chateau', TRUE, TRUE, TRUE, FALSE, TRUE, FALSE, FALSE, 4.8, 47.3941, 0.6848, 'chateau_loire_1.jpg', 'STRICT'),

('Ski Lodge Courchevel', '42 Rue des Chenus', 'Courchevel', 'France', 'Upscale ski lodge with direct access to the slopes in the prestigious resort of Courchevel. This luxury ski-in/ski-out lodge combines alpine charm with high-end amenities. After a day on the slopes, relax in the outdoor hot tub overlooking the mountains, enjoy a meal in the gourmet restaurant, or unwind by the stone fireplace in the cozy lounge area.', 320.00, 8, 'chalet', TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, TRUE, 4.7, 45.4160, 6.6276, 'lodge_courchevel_1.jpg', 'MODERATE'),

('Modern Loft Marseille', '23 Rue de la République', 'Marseille', 'France', 'Trendy loft apartment in a converted warehouse near the Old Port of Marseille. This stylish industrial-chic loft features high ceilings, exposed brick walls, and large windows flooding the space with Mediterranean light. Located just steps from the bustling Vieux Port, you\'ll be perfectly positioned to explore Marseille\'s vibrant culinary scene, historic quarters, and beautiful coastal paths.', 130.00, 4, 'apartment', TRUE, FALSE, FALSE, FALSE, TRUE, TRUE, FALSE, 4.3, 43.2964, 5.3700, 'loft_marseille_1.jpg', 'FLEXIBLE'),

('Country House Normandy', '67 Chemin des Pommiers', 'Bayeux', 'France', 'Charming half-timbered house in the picturesque countryside of Normandy. This traditional Norman house offers a peaceful retreat surrounded by apple orchards and rolling countryside. The interior features wooden beams, a stone fireplace, and comfortable country-style furnishings. Located near historic Bayeux, it\'s the perfect base for exploring D-Day beaches, Mont Saint-Michel, and the region\'s rich heritage.', 160.00, 6, 'house', TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, 4.6, 49.2764, -0.7028, 'house_normandy_1.jpg', 'MODERATE'),

('Vineyard Guesthouse Alsace', '28 Route des Vins', 'Colmar', 'France', 'Beautiful timber-framed guesthouse on a working vineyard in the heart of Alsace. Experience the unique charm of Alsace in this family-run vineyard guesthouse. The property offers comfortable rooms with traditional decor, vineyard tours and tastings, and spectacular views over the vine-covered hills. The picturesque town of Colmar is just minutes away, and the famous Alsace Wine Route is on your doorstep.', 180.00, 4, 'guesthouse', TRUE, FALSE, TRUE, FALSE, TRUE, FALSE, FALSE, 4.8, 48.0793, 7.3623, 'guesthouse_alsace_1.jpg', 'STANDARD'),

('Modern Hotel Strasbourg', '15 Place Kléber', 'Strasbourg', 'France', 'Contemporary design hotel in the city center of Strasbourg. This cutting-edge hotel offers a sleek, modern aesthetic with thoughtfully designed spaces and state-of-the-art technology. Located on Strasbourg\'s main square, you\'ll be just steps from the magnificent cathedral, picturesque Petite France district, and excellent shopping and dining options.', 190.00, 4, 'hotel', TRUE, FALSE, TRUE, TRUE, TRUE, FALSE, TRUE, 4.5, 48.5830, 7.7442, 'hotel_strasbourg_1.jpg', 'STANDARD'),

('Beachfront Apartment Biarritz', '37 Boulevard de la Grande Plage', 'Biarritz', 'France', 'Elegant apartment with panoramic ocean views in the classic resort town of Biarritz. This refined apartment offers front-row seats to the Atlantic Ocean, with stunning views from its spacious terrace. The stylish interior features high ceilings, large windows, and elegant furnishings. Located on the main beach promenade, you\'ll be perfectly positioned to enjoy Biarritz\'s famous beaches, surf spots, and sophisticated ambiance.', 200.00, 4, 'apartment', TRUE, FALSE, TRUE, FALSE, TRUE, FALSE, FALSE, 4.7, 43.4831, -1.5586, 'apartment_biarritz_1.jpg', 'MODERATE'),

('Mountain View Cabin Annecy', '56 Chemin des Sapins', 'Annecy', 'France', 'Cozy wooden cabin with stunning views of Lake Annecy and the surrounding mountains. This intimate mountain retreat offers breathtaking panoramic views in a peaceful setting. The rustic-chic interior features wooden walls, a wood-burning stove, and comfortable furnishings. Located just a short drive from beautiful Annecy, you can easily explore the crystal-clear lake, charming old town, and excellent hiking trails.', 170.00, 3, 'cabin', TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, 4.8, 45.8992, 6.1294, 'cabin_annecy_1.jpg', 'STANDARD'),

('Riverside Cottage Dordogne', '42 Chemin du Moulin', 'Sarlat-la-Canéda', 'France', 'Picturesque stone cottage on the banks of the Dordogne River. This idyllic riverside cottage offers a tranquil setting with direct water access and beautiful views. The lovingly restored interior combines rustic charm with modern comforts. Located in the heart of the Périgord Noir region, you\'ll be surrounded by medieval villages, prehistoric caves, and the spectacular Dordogne Valley.', 140.00, 4, 'cottage', TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE, 4.6, 44.8894, 1.2157, 'cottage_dordogne_1.jpg', 'MODERATE');

-- Insert accommodation images
INSERT INTO accommodation_images (accommodation_id, image_url, is_primary, description)
VALUES
    (1, 'hotel_paris_1.jpg', TRUE, 'Hotel exterior view'),
    (1, 'hotel_paris_2.jpg', FALSE, 'Luxury room with Eiffel Tower view'),
    (1, 'hotel_paris_3.jpg', FALSE, 'Hotel lobby'),
    (1, 'hotel_paris_4.jpg', FALSE, 'Hotel restaurant'),
    (1, 'hotel_paris_5.jpg', FALSE, 'Hotel swimming pool'),
    (2, 'apartment_paris_1.jpg', TRUE, 'Apartment exterior'),
    (2, 'apartment_paris_2.jpg', FALSE, 'Living room with vintage decor'),
    (2, 'apartment_paris_3.jpg', FALSE, 'Fully equipped kitchen'),
    (2, 'apartment_paris_4.jpg', FALSE, 'Cozy bedroom'),
    (2, 'apartment_paris_5.jpg', FALSE, 'Bathroom with shower'),
    (3, 'villa_nice_1.jpg', TRUE, 'Villa with sea view'),
    (3, 'villa_nice_2.jpg', FALSE, 'Villa infinity pool'),
    (3, 'villa_nice_3.jpg', FALSE, 'Spacious living room'),
    (3, 'villa_nice_4.jpg', FALSE, 'Master bedroom with balcony'),
    (3, 'villa_nice_5.jpg', FALSE, 'Villa garden path to beach'),
    (4, 'chalet_chamonix_1.jpg', TRUE, 'Chalet exterior in winter'),
    (4, 'chalet_chamonix_2.jpg', FALSE, 'Cozy living room with fireplace'),
    (4, 'chalet_chamonix_3.jpg', FALSE, 'Dining area with mountain view'),
    (4, 'chalet_chamonix_4.jpg', FALSE, 'Bedroom with wooden features'),
    (4, 'chalet_chamonix_5.jpg', FALSE, 'Terrace with mountain view'),
    (5, 'hotel_lyon_1.jpg', TRUE, 'Hotel facade in historic building'),
    (5, 'hotel_lyon_2.jpg', FALSE, 'Elegant room interior'),
    (5, 'hotel_lyon_3.jpg', FALSE, 'Hotel courtyard'),
    (5, 'hotel_lyon_4.jpg', FALSE, 'Boutique hotel restaurant'),
    (5, 'hotel_lyon_5.jpg', FALSE, 'Hotel reception area'),
    (6, 'cottage_provence_1.jpg', TRUE, 'Cottage exterior with lavender fields'),
    (6, 'cottage_provence_2.jpg', FALSE, 'Rustic living room'),
    (6, 'cottage_provence_3.jpg', FALSE, 'Country kitchen with dining area'),
    (6, 'cottage_provence_4.jpg', FALSE, 'Garden with swimming pool'),
    (6, 'cottage_provence_5.jpg', FALSE, 'Bedroom with Provencal decor'),
    (7, 'apartment_bordeaux_1.jpg', TRUE, 'Apartment exterior with river view'),
    (7, 'apartment_bordeaux_2.jpg', FALSE, 'Modern living space'),
    (7, 'apartment_bordeaux_3.jpg', FALSE, 'Kitchen with dining area'),
    (7, 'apartment_bordeaux_4.jpg', FALSE, 'Bedroom with river view'),
    (7, 'apartment_bordeaux_5.jpg', FALSE, 'Balcony overlooking the river');

-- Add accommodation amenities
INSERT INTO accommodation_amenities (accommodation_id, amenity_id)
VALUES
-- Luxury Hotel Paris
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15),
-- Cozy Apartment Montmartre
(2, 1), (2, 5), (2, 6), (2, 13),
-- Beach Villa Nice
(3, 1), (3, 2), (3, 3), (3, 5), (3, 6), (3, 16), (3, 20),
-- Alpine Chalet Chamonix
(4, 1), (4, 3), (4, 5), (4, 18), (4, 19),
-- Boutique Hotel Lyon
(5, 1), (5, 3), (5, 4), (5, 5), (5, 7), (5, 9), (5, 10), (5, 11), (5, 15),
-- Countryside Cottage Provence
(6, 1), (6, 3), (6, 5), (6, 6), (6, 17),
-- Modern Apartment Bordeaux
(7, 1), (7, 3), (7, 5);

-- Insert locations
INSERT INTO locations (city, country, description, is_popular, image_url)
VALUES
    ('Paris', 'France', 'The City of Light, known for its art, culture, and landmarks like the Eiffel Tower.', TRUE, 'location_paris.jpg'),
    ('Nice', 'France', 'Beautiful coastal city on the French Riviera with stunning beaches and Mediterranean views.', TRUE, 'location_nice.jpg'),
    ('Chamonix', 'France', 'Alpine resort town at the base of Mont Blanc, perfect for skiing and mountain activities.', TRUE, 'location_chamonix.jpg'),
    ('Lyon', 'France', 'Historic city known for its gastronomy, Renaissance architecture, and vibrant cultural scene.', FALSE, 'location_lyon.jpg'),
    ('Aix-en-Provence', 'France', 'Elegant university town with tree-lined boulevards, thermal springs, and Provençal charm.', FALSE, 'location_aix.jpg'),
    ('Bordeaux', 'France', 'Port city famous for its wine, Gothic architecture, and vibrant riverfront.', FALSE, 'location_bordeaux.jpg'),
    ('Cannes', 'France', 'Glamorous resort town on the French Riviera, famous for its film festival and luxury hotels.', TRUE, 'location_cannes.jpg'),
    ('Tours', 'France', 'Gateway to the Loire Valley châteaux, with a well-preserved medieval center.', FALSE, 'location_tours.jpg'),
    ('Courchevel', 'France', 'Prestigious ski resort in the Three Valleys, offering world-class slopes and luxury amenities.', TRUE, 'location_courchevel.jpg'),
    ('Marseille', 'France', 'Vibrant port city with a rich maritime history, diverse neighborhoods, and beautiful calanques.', FALSE, 'location_marseille.jpg');

-- Insert sample bookings
INSERT IGNORE INTO bookings (user_id, accommodation_id, check_in_date, check_out_date, number_of_guests, total_price, status, booking_date, special_requests, promotion_code)
VALUES
(2, 1, '2024-06-15', '2024-06-20', 2, 1750.00, 'CONFIRMED', '2024-03-10', 'We would appreciate a quiet room with a view if possible.', 'SUMMER15'),
(3, 2, '2024-07-01', '2024-07-07', 2, 720.00, 'CONFIRMED', '2024-02-25', 'Early check-in requested if available.', NULL),
(4, 3, '2024-08-10', '2024-08-17', 6, 4060.00, 'CONFIRMED', '2024-01-15', 'We will be celebrating an anniversary during our stay.', 'FAMILY20'),
(5, 4, '2024-12-20', '2024-12-27', 4, 1540.00, 'CONFIRMED', '2024-04-05', 'Need baby crib and high chair for infant.', 'WINTER25'),
(6, 5, '2024-05-15', '2024-05-18', 2, 540.00, 'COMPLETED', '2024-02-10', 'Business trip, need quiet room with desk.', 'BUSINESS15'),
(7, 6, '2024-09-05', '2024-09-12', 3, 1050.00, 'CONFIRMED', '2024-03-20', 'Would like information about local wine tours.', NULL),
(2, 7, '2024-10-10', '2024-10-15', 2, 700.00, 'CONFIRMED', '2024-04-01', 'Late check-in expected around 10 PM.', 'EARLY10'),
(3, 8, '2024-07-20', '2024-07-25', 2, 2250.00, 'CONFIRMED', '2024-03-12', 'Celebrating honeymoon, any special arrangements would be appreciated.', NULL),
(4, 9, '2024-08-01', '2024-08-08', 8, 4760.00, 'CONFIRMED', '2024-02-28', 'Multiple families traveling together, would like rooms close to each other.', NULL),
(5, 10, '2024-01-15', '2024-01-22', 4, 2240.00, 'COMPLETED', '2023-10-05', 'Need ski equipment rental information.', 'WINTER25'),
(6, 11, '2024-06-20', '2024-06-25', 2, 650.00, 'CONFIRMED', '2024-04-10', 'Interested in local art galleries and museums.', NULL),
(7, 12, '2024-05-01', '2024-05-07', 4, 960.00, 'COMPLETED', '2024-01-15', 'Would like information about D-Day tours.', NULL),
(8, 13, '2024-09-15', '2024-09-20', 2, 900.00, 'CONFIRMED', '2024-05-01', 'Interested in vineyard tours and wine tastings.', NULL),
(9, 14, '2024-07-05', '2024-07-10', 3, 950.00, 'CONFIRMED', '2024-03-15', 'Would like recommendations for local restaurants.', NULL),
(10, 15, '2024-08-20', '2024-08-25', 2, 1000.00, 'CONFIRMED', '2024-04-20', 'Interested in surfing lessons.', NULL);

-- Insert sample promotions (extended)
INSERT IGNORE INTO promotions (accommodation_id, name, description, discount_percentage, start_date, end_date, promo_code, is_active, min_stay_days)
VALUES
(1, 'Summer Special', 'Get 15% off during summer months!', 15.00, '2024-06-01', '2024-09-01', 'SUMMER15', TRUE, 3),
(2, 'Weekend Getaway', 'Enjoy 10% off for weekend stays!', 10.00, '2024-05-01', '2024-12-31', 'WEEKEND10', TRUE, 2),
(3, 'Family Vacation', '20% discount for family bookings with children!', 20.00, '2024-07-01', '2024-08-31', 'FAMILY20', TRUE, 5),
(4, 'Winter Wonderland', '25% off for winter skiing holidays!', 25.00, '2024-11-01', '2025-03-31', 'WINTER25', TRUE, 4),
(5, 'Business Travel', '15% off for business travelers!', 15.00, '2024-01-01', '2024-12-31', 'BUSINESS15', TRUE, 1),
(6, 'Romantic Package', 'Special discount for couples!', 12.00, '2024-02-01', '2024-12-31', 'LOVE12', TRUE, 3),
(7, 'Early Bird', '10% off for bookings made 3 months in advance!', 10.00, '2024-01-01', '2024-12-31', 'EARLY10', TRUE, 2),
(8, 'Last Minute Deal', '18% off for bookings made within 7 days of stay!', 18.00, '2024-01-01', '2024-12-31', 'LASTMIN18', TRUE, 1),
(9, 'Extended Stay', '30% off for stays longer than 2 weeks!', 30.00, '2024-01-01', '2024-12-31', 'EXTENDED30', TRUE, 14),
(10, 'Spring Break', '15% off for stays during April and May!', 15.00, '2024-04-01', '2024-05-31', 'SPRING15', TRUE, 3),
(11, 'City Explorer', '10% off plus free city tour!', 10.00, '2024-03-01', '2024-10-31', 'CITY10', TRUE, 2),
(12, 'Countryside Escape', '15% off for rural accommodations!', 15.00, '2024-01-01', '2024-12-31', 'COUNTRY15', TRUE, 4),
(13, 'Wine Lover', '12% off plus complimentary wine tasting!', 12.00, '2024-04-01', '2024-11-30', 'WINE12', TRUE, 2),
(14, 'Urban Adventure', '10% off for city center accommodations!', 10.00, '2024-01-01', '2024-12-31', 'URBAN10', TRUE, 2),
(15, 'Beachfront Bliss', '15% off for direct beach access properties!', 15.00, '2024-05-01', '2024-09-30', 'BEACH15', TRUE, 3),
(16, 'Mountain Retreat', '20% off for mountain accommodations!', 20.00, '2024-01-01', '2024-12-31', 'MOUNTAIN20', TRUE, 3);

-- Insert sample reviews (extended with more detailed ratings)
INSERT IGNORE INTO reviews (user_id, accommodation_id, booking_id, rating, cleanliness_rating, comfort_rating, location_rating, value_rating, comment, review_date, helpful_votes)
VALUES
(2, 1, 1, 5, 5, 5, 5, 4, 'Absolutely amazing hotel with exceptional service. The view from our room was breathtaking, and the staff went above and beyond to make our stay special. The rooftop restaurant offers incredible views of Paris, and the breakfast buffet was the best I\'ve ever had at a hotel. Highly recommended for a luxury stay in Paris!', '2024-06-21', 12),

(3, 2, 2, 4, 5, 4, 5, 4, 'Lovely apartment in a great location. Very clean and comfortable. The Montmartre area is charming with many cafes and shops within walking distance. The apartment had everything we needed for our week-long stay. The only minor issue was occasional noise from the street at night, but that\'s to be expected in such a central location. The host was very helpful and responsive.', '2024-07-08', 8),

(4, 3, 3, 5, 5, 5, 5, 4, 'The villa exceeded our expectations. Perfect for our family vacation with stunning sea views. The infinity pool was amazing, and having direct beach access was incredibly convenient. The kitchen was well-equipped for preparing meals, and the villa was spacious enough that everyone had their own privacy. Would definitely return!', '2024-08-18', 15),

(5, 4, 4, 4, 4, 5, 5, 4, 'Cozy chalet with all the amenities needed. Great location for skiing. Just a short walk to the lifts, and the boot warming rack was a nice touch. The fireplace made for cozy evenings after long days on the slopes. The only improvement would be better WiFi, which was a bit slow at times.', '2024-12-28', 7),

(6, 5, 5, 4, 5, 4, 5, 3, 'Stylish hotel with friendly staff. Great central location for exploring Lyon. The historic building has been beautifully renovated with modern amenities while preserving its character. Breakfast was excellent with many local specialties. My only complaint is that the room was a bit small for the price, but that\'s typical for central European city hotels.', '2024-05-19', 9),

(7, 6, 6, 5, 5, 5, 4, 5, 'A perfect retreat in the Provence countryside. The cottage was beautiful and peaceful. Waking up to the scent of lavender and the sounds of birds was magical. The pool area was lovely for relaxing, and the kitchen had everything we needed to cook with the amazing local produce. The owner provided excellent recommendations for nearby markets and restaurants.', '2024-09-13', 11),

(2, 7, 7, 4, 4, 4, 5, 4, 'Modern apartment with beautiful river views. The location in the Chartrons district was perfect - lots of nice wine bars and restaurants nearby, and an easy walk to the city center. The balcony was our favorite spot for evening drinks while watching the sunset over the river. The apartment was well-equipped and comfortable.', '2024-10-16', 6),

(3, 8, 8, 5, 5, 5, 5, 4, 'An absolutely stunning resort with impeccable service. Our room had a breathtaking view of the Mediterranean, and the private beach area was beautifully maintained. The spa treatments were excellent, and the restaurant served some of the best seafood we\'ve ever had. Perfect for a romantic getaway.', '2024-07-26', 14),

(4, 9, 9, 5, 4, 5, 5, 5, 'Staying in this chateau was like living in a fairy tale. The historical details were fascinating, and the grounds were beautiful for morning walks. Despite the historical setting, all modern conveniences were available. Great for our large family gathering, with plenty of space for everyone. The caretaker was extremely helpful with local recommendations.', '2024-08-09', 18),

(5, 10, 10, 4, 5, 4, 5, 4, 'Excellent ski lodge with perfect slope access. Literally ski-in/ski-out as advertised. The hot tub was perfect after a day of skiing, and the lodge restaurant served hearty, delicious meals. Rooms were comfortable and warm. The boot room with heated racks was a great feature.', '2024-01-23', 10),

(6, 11, 11, 4, 4, 4, 4, 5, 'Cool, stylish loft in a great location near the Old Port. The industrial design elements were very well done, and the space was bright and airy. Comfortable bed and good shower. Great value for the price, especially considering the central location. Plenty of great restaurants and bars within walking distance.', '2024-06-26', 7),

(7, 12, 12, 5, 5, 4, 4, 5, 'Charming Norman house with beautiful surroundings. The apple orchards were in bloom during our stay, making it extra special. The house itself was cozy and authentic, with modern amenities. Great base for exploring Normandy\'s D-Day beaches and historic sites. The owner left helpful information about local attractions and markets.', '2024-05-08', 9);

-- Insert sample payments (extended with card details)
INSERT IGNORE INTO payments (booking_id, amount, payment_method, payment_date, status, transaction_id, card_last_digits, card_type)
VALUES
(1, 1750.00, 'CREDIT_CARD', '2024-03-10 14:35:22', 'COMPLETED', 'TRX-20240310-123456', '4567', 'VISA'),
(2, 720.00, 'PAYPAL', '2024-02-25 10:15:30', 'COMPLETED', 'TRX-20240225-234567', NULL, NULL),
(3, 4060.00, 'CREDIT_CARD', '2024-01-15 16:42:11', 'COMPLETED', 'TRX-20240115-345678', '7890', 'MASTERCARD'),
(4, 1540.00, 'BANK_TRANSFER', '2024-04-05 12:20:45', 'COMPLETED', 'TRX-20240405-456789', NULL, NULL),
(5, 540.00, 'CREDIT_CARD', '2024-02-10 09:05:18', 'COMPLETED', 'TRX-20240210-567890', '2345', 'AMERICAN_EXPRESS'),
(6, 1050.00, 'PAYPAL', '2024-03-20 15:30:27', 'COMPLETED', 'TRX-20240320-678901', NULL, NULL),
(7, 700.00, 'CREDIT_CARD', '2024-04-01 11:25:38', 'COMPLETED', 'TRX-20240401-789012', '3456', 'VISA'),
(8, 2250.00, 'CREDIT_CARD', '2024-03-12 13:40:15', 'COMPLETED', 'TRX-20240312-890123', '5678', 'MASTERCARD'),
(9, 4760.00, 'BANK_TRANSFER', '2024-02-28 09:15:33', 'COMPLETED', 'TRX-20240228-901234', NULL, NULL),
(10, 2240.00, 'CREDIT_CARD', '2023-10-05 10:30:22', 'COMPLETED', 'TRX-20231005-012345', '9012', 'VISA'),
(11, 650.00, 'PAYPAL', '2024-04-10 16:45:19', 'COMPLETED', 'TRX-20240410-123450', NULL, NULL),
(12, 960.00, 'CREDIT_CARD', '2024-01-15 14:20:33', 'COMPLETED', 'TRX-20240115-234561', '1234', 'MASTERCARD'),
(13, 900.00, 'CREDIT_CARD', '2024-05-01 11:05:47', 'COMPLETED', 'TRX-20240501-345672', '6789', 'VISA'),
(14, 950.00, 'PAYPAL', '2024-03-15 09:55:28', 'COMPLETED', 'TRX-20240315-456783', NULL, NULL),
(15, 1000.00, 'CREDIT_CARD', '2024-04-20 15:15:36', 'COMPLETED', 'TRX-20240420-567894', '8901', 'AMERICAN_EXPRESS');

-- Create wishlists
INSERT IGNORE INTO wishlists (user_id, name, created_date)
VALUES
(2, 'Summer Vacation Ideas', '2024-01-15'),
(3, 'Romantic Getaways', '2024-02-10'),
(4, 'Family Trip Planning', '2024-03-05'),
(5, 'Ski Trip Options', '2024-04-20'),
(6, 'Business Travel', '2024-01-30'),
(7, 'Weekend Escapes', '2024-03-15');

-- Add items to wishlists
INSERT IGNORE INTO wishlist_items (wishlist_id, accommodation_id, added_date)
VALUES
(1, 1, '2024-01-15'),
(1, 3, '2024-01-16'),
(1, 8, '2024-01-20'),
(1, 15, '2024-02-05'),
(2, 2, '2024-02-10'),
(2, 6, '2024-02-11'),
(2, 13, '2024-02-15'),
(3, 3, '2024-03-05'),
(3, 9, '2024-03-06'),
(3, 12, '2024-03-10'),
(3, 16, '2024-03-15'),
(4, 4, '2024-04-20'),
(4, 10, '2024-04-21'),
(5, 5, '2024-01-30'),
(5, 11, '2024-02-01'),
(5, 14, '2024-02-05'),
(6, 2, '2024-03-15'),
(6, 7, '2024-03-16'),
(6, 11, '2024-03-20');