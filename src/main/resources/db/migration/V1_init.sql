CREATE EXTENSION IF NOT EXISTS btree_gist;
ALTER TABLE reservation
    ADD CONSTRAINT no_overlap
    EXCLUDE USING gist (
        connector_id WITH =,
        period WITH &&
    )
    WHERE (status IN ('HOLD','BOOKED','STARTED'));