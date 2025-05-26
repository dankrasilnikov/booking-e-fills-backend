CREATE OR REPLACE FUNCTION delete_slots_after_station_delete()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM station_slots
    WHERE gas_station_id = OLD.id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_delete_slots_after_station_delete
AFTER DELETE ON gas_stations
FOR EACH ROW
EXECUTE FUNCTION delete_slots_after_station_delete();