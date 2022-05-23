ALTER TABLE movies 
  ALTER COLUMN description TYPE TEXT USING description::TEXT; 