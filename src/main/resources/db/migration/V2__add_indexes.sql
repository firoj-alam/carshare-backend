-- V2: Indexes on columns used by repository queries

CREATE INDEX idx_car_owner_id ON car (owner_id);
CREATE INDEX idx_car_status_available ON car (status, available);
CREATE INDEX idx_booking_car_id ON booking (car_id);
CREATE INDEX idx_booking_driver_id ON booking (driver_id);