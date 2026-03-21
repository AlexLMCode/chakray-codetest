-- Insertar usuarios
INSERT INTO users (id, email, name, phone, password, tax_id, created_at)
VALUES
  ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'user1@mail.com', 'user1', '+1 55 555 555 55',
   '7c4a8d09ca3762af61e59520943dc26494f8941b', 'AARR990101XXX', '2026-01-01 00:00:00'),
  ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'user2@mail.com', 'user2', '+1 55 666 666 66',
   '7c4a8d09ca3762af61e59520943dc26494f8941b', 'BBRR990202YYY', '2026-01-02 00:00:00')
ON CONFLICT (id) DO NOTHING;

-- Insertar direcciones
INSERT INTO addresses (id, name, street, country_code, user_id)
OVERRIDING SYSTEM VALUE
VALUES
  (1, 'workaddress', 'street No. 1', 'UK', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
  (2, 'homeaddress', 'street No. 2', 'AU', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
  (3, 'workaddress', 'street No. 3', 'MX', 'b2c3d4e5-f6a7-8901-bcde-f12345678901')
ON CONFLICT (id) DO NOTHING;
