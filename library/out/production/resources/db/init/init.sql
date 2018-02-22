CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

create table entity(
    id serial NOT NULL PRIMARY KEY,
    document JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON entity
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();