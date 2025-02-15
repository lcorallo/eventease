-- Insert for EventService

INSERT INTO event_service (id, activity, start_datetime, estimated_end_datetime, end_datetime, status, note, location, created_at, update_at, code, supplier, num_availability)
VALUES 
  ('1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '4d2b8459-5e0f-4145-b234-bd3f23c1e795', '2024-07-02T08:00:00Z', '2024-07-02T10:00:00Z', '2024-07-02T09:00:00Z', 'DRAFT', 'Sample note 1', '7b63d8d3-c5cf-4516-8b8d-743a8fc3b5c7', '2024-07-02T07:00:00Z', '2024-07-02T07:30:00Z', 'CODE123', '5d8a421c-f93a-44e1-9fc5-19e3516c9830', 100),
  ('8c9d8f7b-5a4d-498f-844e-6b125837b0b1', 'f1a1c5d6-4903-4e9c-8d42-cd7d8e2d779d', '2024-07-03T09:00:00Z', '2024-07-03T11:00:00Z', '2024-07-03T10:00:00Z', 'COMPLETED', 'Sample note 2', '64db6ef9-087e-43bc-8f4b-8efc9a8f5911', '2024-07-03T08:00:00Z', '2024-07-03T08:30:00Z', 'CODE456', 'a5c34c45-99b1-4d37-93d2-48f8c2387467', 200), 
  ('5a8d6c3f-7b4e-4f9b-8b24-1dbe8a741cfc', '9b6d43e0-8ecf-4517-939d-122caa9cb28b', '2024-07-04T10:00:00Z', '2024-07-04T12:00:00Z', '2024-07-04T11:00:00Z', 'DRAFT', 'Draft event', 'a4a4cb68-cefd-453d-945f-8cee8af75c35', '2024-07-04T09:00:00Z', '2024-07-04T09:30:00Z', 'CODE789', 'd824aa28-266e-4778-bd44-c4684215553f', 50),
  ('45d6e7a2-f9b7-49f0-8a5c-c2b6a0b8aeaf', '8e9f8c3b-06f4-4fb9-a95c-104d597f7f91', '2024-07-05T11:00:00Z', '2024-07-05T13:00:00Z', '2024-07-05T12:00:00Z', 'PUBLISHED', 'Published event', 'ef38ea57-3585-44eb-bd12-7c4b6f43c67c', '2024-07-05T10:00:00Z', '2024-07-05T10:30:00Z', 'CODE012', '1e126624-02ac-4730-8e58-b6874b3be859', 75),
  ('cdf7a3d7-ebe3-42e9-986d-5d5c5b750446', '61e8f2a4-0a2a-4679-8232-923eeb4a1a8f', '2024-07-06T12:00:00Z', '2024-07-06T14:00:00Z', '2024-07-06T13:00:00Z', 'CANCELLED', 'Bookable event', '3dce5cb5-2f14-4b57-b3eb-4faba3963bbd', '2024-07-06T11:00:00Z', '2024-07-06T11:30:00Z', 'CODE345', '0007864f-b3b1-4766-9064-ca653afaba61', 150),
  ('0bfea64f-e1a6-432d-978a-5425e2b065bc', 'aa647fc7-cad1-4e96-bc3c-dc86b2c9e6d8', '2024-07-07T13:00:00Z', '2024-07-07T15:00:00Z', '2024-07-07T14:00:00Z', 'IN_PROGRESS', 'In progress event', 'f589a3e8-648a-44f8-b56a-b27d08bb421d', '2024-07-07T12:00:00Z', '2024-07-07T12:30:00Z', 'CODE678', 'fa24ed79-1354-434e-b1e4-02bb2ed3b561', 125),
  ('14a0c3da-5db1-47f4-a85a-8e5c0b6e68cc', 'c0dbe8e9-3f0f-4b7c-bc50-fce27c43a34f', '2024-07-08T14:00:00Z', '2024-07-08T16:00:00Z', '2024-07-08T15:00:00Z', 'COMPLETED', 'Completed event', '5e25da37-8db8-4bcb-9f50-f0328e37faba', '2024-07-08T13:00:00Z', '2024-07-08T13:30:00Z', 'CODE901', '14c68e0c-01de-4f8c-a791-eb1b9f4b43c2', 80),
  ('5e0f4c3b-3c9e-45b9-8f7e-0f5f7d2c7f65', '48c9efeb-78c3-418d-88f8-b92fa7a78135', '2024-07-09T15:00:00Z', '2024-07-09T17:00:00Z', '2024-07-09T16:00:00Z', 'CANCELLED', 'CANCELLED event', '6e4bd54f-450a-4fb9-bf37-ff5ad95a0f76', '2024-07-09T14:00:00Z', '2024-07-09T14:30:00Z', 'CODE234', 'b78c9890-f9ca-42c6-97f0-e77aba191ad4', 60),
  ('708eac9b-7e2b-49bc-866d-c73b4e858a16', 'b2c68b5f-7c7a-46e1-b98b-890bdf86a799', '2024-07-10T16:00:00Z', '2024-07-10T18:00:00Z', '2024-07-10T17:00:00Z', 'CLOSED', 'Closed event', 'bb0e3463-3b22-4e1c-bd92-f0420c38e85d', '2024-07-10T15:00:00Z', '2024-07-10T15:30:00Z', 'CODE567', '06d85be9-192e-4286-b2fe-701d517e02ff', 30);

INSERT INTO booking (id, event_service_id, consumer, status, created_at, update_at)
VALUES 
(1,'1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', 'b1d9291e-5776-4d3e-8e74-30d5c90d1b1d', 'PENDING', '2024-07-01 10:00:00', '2024-07-01 10:00:00'),
(2,'8c9d8f7b-5a4d-498f-844e-6b125837b0b1', 'c2e2793d-f0f5-4a0a-8a56-7f8b9a4b4c5d', 'CONFIRMED', '2024-07-02 11:00:00', '2024-07-02 11:00:00'),
(3,'5a8d6c3f-7b4e-4f9b-8b24-1dbe8a741cfc', 'd3f4814e-1234-4c8e-8a76-8f0c5b5c6d7e', 'CANCELLED', '2024-07-03 12:00:00', '2024-07-03 12:00:00'),
(4,'45d6e7a2-f9b7-49f0-8a5c-c2b6a0b8aeaf', 'e4a5835f-3456-4d3e-8b87-9a1d7c6e8f8f', 'PENDING', '2024-07-04 13:00:00', '2024-07-04 13:00:00'),
(5,'cdf7a3d7-ebe3-42e9-986d-5d5c5b750446', 'f5b6956f-5678-4a4a-8c98-0b2e8d7f9a9f', 'CONFIRMED', '2024-07-05 14:00:00', '2024-07-05 14:00:00'),
(6,'0bfea64f-e1a6-432d-978a-5425e2b065bc', '02b2ed6d-628c-4b7c-8fe7-cd139238518f', 'CANCELLED', '2024-07-06 15:00:00', '2024-07-06 15:00:00'),
(7,'14a0c3da-5db1-47f4-a85a-8e5c0b6e68cc', 'dc281f9f-f46e-41bd-af60-80c0db0b3d98', 'PENDING', '2024-07-07 16:00:00', '2024-07-07 16:00:00'),
(8,'5e0f4c3b-3c9e-45b9-8f7e-0f5f7d2c7f65', '806a56ad-64c6-47e9-bf0e-a23369323a6f', 'CONFIRMED', '2024-07-08 17:00:00', '2024-07-08 17:00:00'),
(9,'708eac9b-7e2b-49bc-866d-c73b4e858a16', 'a00c141b-1fd3-4964-9392-3b7b2fba707d', 'CANCELLED', '2024-07-09 18:00:00', '2024-07-09 18:00:00'),
(10,'1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45','91ce98f8-7933-4a25-bae9-884ba780fb70', 'PENDING', '2024-07-10 19:00:00', '2024-07-10 19:00:00'),
(11,'8c9d8f7b-5a4d-498f-844e-6b125837b0b1','378ab423-a928-4fab-b210-af9aecaf6230', 'CONFIRMED', '2024-07-11 20:00:00', '2024-07-11 20:00:00'),
(12,'5a8d6c3f-7b4e-4f9b-8b24-1dbe8a741cfc','a992d345-6b8a-4bcf-ab29-3b4f00877fee', 'CANCELLED', '2024-07-12 21:00:00', '2024-07-12 21:00:00'),
(13,'45d6e7a2-f9b7-49f0-8a5c-c2b6a0b8aeaf','5644095c-df28-471b-8b4e-1307fcbe6983', 'PENDING', '2024-07-13 22:00:00', '2024-07-13 22:00:00'),
(14,'cdf7a3d7-ebe3-42e9-986d-5d5c5b750446','f7c9948d-bbc8-460f-8ddb-7873e64c9d43', 'CONFIRMED', '2024-07-14 23:00:00', '2024-07-14 23:00:00'),
(15,'0bfea64f-e1a6-432d-978a-5425e2b065bc','575bc176-6a2f-4b31-bfac-121466f39d9c', 'CANCELLED', '2024-07-15 00:00:00', '2024-07-15 00:00:00');
SELECT setval('booking_id_seq', 15, true);

-- Inserting 5 records into the event_operation table
INSERT INTO event_operation (id, activity, start_datetime, estimated_end_datetime, end_datetime, status, note, location, created_at, update_at, event_service_id, operator, num_partecipants)
VALUES
    ('1d6d7fb0-32f1-4bdc-b1d4-d51b2a68fd8e', 'c41f83e3-37d2-489b-aea3-33ab09b9dfe1', '2024-07-01T08:00:00Z', '2024-07-01T10:00:00Z', '2024-07-01T09:30:00Z', 'COMPLETED', 'Event note 1', 'c8d1f96b-2d9e-4dbb-9f4f-7e2a1c3fb6f4', '2024-07-01T07:00:00Z', '2024-07-01T07:30:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '33d33c37-c214-4ea1-9d9e-dc9d7f2e9881', 100),
    ('2e7e12b5-5022-4566-a1c9-78e6b0e212e4', '2a516c0f-43f8-4e65-bf2b-52b516c0f43f', '2024-07-02T09:00:00Z', '2024-07-02T11:00:00Z', '2024-07-02T10:30:00Z', 'CANCELLED', 'Event note 2', '2d8c4b6c-d7a9-44a2-a9a1-f4f9a8c0d9f4', '2024-07-02T08:00:00Z', '2024-07-02T08:30:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '4bcee70a-925f-4e4e-b93e-37c4b5b0d6f1', 150),
    ('3f7f13b6-6133-4757-b1e9-89e7c0e323d5', '3a516d0e-54f9-4f76-af3c-63b527d1f64a', '2024-07-03T10:00:00Z', '2024-07-03T12:00:00Z', '2024-07-03T11:30:00Z', 'CANCELLED', 'Event note 3', '3e9d5b7d-d8b0-55a3-bab2-04fb9b1a0a10', '2024-07-03T09:00:00Z', '2024-07-03T09:30:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '5cfee80b-a236-4f5f-c9f1-48d5b6c1e7f2', 200),
    ('1cac7c77-5d47-49eb-89d5-5c9ceb626e96', '4b617e1f-65fa-5f87-bf4d-74c638e2e75b', '2024-07-04T11:00:00Z', '2024-07-04T13:00:00Z', '2024-07-04T12:30:00Z', 'IN_PROGRESS', 'Event note 4', '4f1d6c8e-e9c1-66b4-bbc3-15fcad1b2b21', '2024-07-04T10:00:00Z', '2024-07-04T10:30:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '92c27a07-79d5-4bdb-95ac-1f52d7b0aa39', 250),
    ('f357035d-2f80-421b-898b-e5637e37f50e', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'DRAFT', 'Event note 5', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-05T11:00:00Z', '2024-07-05T11:30:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300),
    ('24562280-f5ab-4400-8389-f60892b04447', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'PUBLISHED', 'Event note 6', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-30T07:02:00Z', '2024-07-30T07:07:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300),
    ('1fdd48b2-9d47-4fe2-b7de-cdd1c2cd9e8f', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'PUBLISHED', 'Event note 7', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-30T07:03:00Z', '2024-07-30T07:08:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300),
    ('510b4297-f7de-49e9-ab45-7242acad2b9d', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'PUBLISHED', 'Event note 8', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-30T07:04:00Z', '2024-07-30T07:09:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300),
    ('f645b772-7a55-45ac-ab7c-323e8a9ce7cd', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'PUBLISHED', 'Event note 9', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-30T07:05:00Z', '2024-07-30T07:00:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300),
    ('46d3cf3c-0cef-4b1c-8510-fc9790b55654', '3e1a10b5-704e-488b-a5d0-afe91f82c6d9', '2024-07-05T12:00:00Z', '2024-07-05T14:00:00Z', '2024-07-05T13:30:00Z', 'PUBLISHED', 'Event note 10', '4d70dbcc-730c-4c8f-829b-973b421b0ecb', '2024-07-30T07:06:00Z', '2024-07-30T07:01:00Z', '1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45', '0ef3b5f9-f8e0-4c4b-90af-d35c8f664207', 300);
