CREATE OR REPLACE FUNCTION create_slots_after_station_insert()
RETURNS TRIGGER AS $$
DECLARE
    current_hour TIMESTAMP;
    end_of_day TIMESTAMP;
BEGIN
    current_hour := date_trunc('hour', NOW()) + interval '1 hour';  -- ближайший час
    end_of_day := date_trunc('day', NOW()) + interval '1 day'; -- конец текущих суток

    WHILE current_hour < end_of_day LOOP
        INSERT INTO station_slots (gas_station_id, slot_time, available_slots)
        VALUES (NEW.id, current_hour, 2);
        current_hour := current_hour + interval '1 hour';
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_create_slots_after_station_insert
AFTER INSERT ON gas_stations
FOR EACH ROW
EXECUTE FUNCTION create_slots_after_station_insert();