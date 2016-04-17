/*Consumer sample 1*/
INSERT INTO `consumer` (`id`, `version`, `description`, `identifier`) VALUES (1, 1, 'Test consumer', 'test-identifier');

INSERT INTO `subscription` (`id`, `version`, `description`, `transformation_template`, `consumer_id`, `document_type`, `document_version`, `subscription_enpoint_config`) VALUES (1, '1', 'Test subscription', 'template', '1', 'vehicle', 'v1', '');
UPDATE `subscription` SET `subscription_enpoint_config`=
	' {\n    \"type\": \"RABBIT_MQ\",\n    \"exchangeName\": \"esb-subscriber-1\",\n    \"autoDelete\": true,\n    \"routingKey\": \"esb-subscriber-1-routingKey\",\n    \"queue\": \"esb-subscriber-1-queue\",\n    \"vhost\": \"vhost\",\n    \"host\": \"rabbit-host\",\n    \"port\": 1234,\n    \"username\": \"username\",\n    \"password\": \"password\"\n}' 
WHERE `id`='1';

UPDATE `subscription` SET `transformation_template`='return body;' WHERE `id`='1';

INSERT INTO `match_query` (`id`, `version`, `subscription_id`, `query`) VALUES (1, '1', '1', null);


/*Consumer sample 1*/
INSERT INTO `consumer` (`id`, `version`, `description`, `identifier`) VALUES (2, 1, 'Test consumer 2', 'test-identifier-2');

INSERT INTO `subscription` (`id`, `version`, `description`, `transformation_template`, `consumer_id`, `document_type`, `document_version`, `subscription_enpoint_config`) VALUES (2, '1', 'Test subscription', 'template', 2, 'vehicle', 'v1', '');
UPDATE `subscription` SET `subscription_enpoint_config`=
	' {\n    \"type\": \"RABBIT_MQ\",\n    \"exchangeName\": \"esb-subscriber-2\",\n    \"autoDelete\": true,\n    \"routingKey\": \"esb-subscriber-2-routingKey\",\n    \"queue\": \"esb-subscriber-2-queue\",\n    \"vhost\": \"vhost\",\n    \"host\": \"rabbit-host\",\n    \"port\": 1234,\n    \"username\": \"username\",\n    \"password\": \"password\"\n}' 
WHERE `id`='2';

UPDATE `subscription` SET `transformation_template`='return body;' WHERE `id`='2';

INSERT INTO `match_query` (`id`,  `version`, `subscription_id`, `query`) VALUES (2, '1', '2', null);
UPDATE `esb`.`match_query` 
	SET `query`='{\n  \"bool\": {\n    \"must\": {\n      \"match\": {\n        \"make\": {\n          \"query\": \"Nissan\"\n        }\n      }\n    }\n  }\n}' 
WHERE `id`='2';
