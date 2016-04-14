INSERT INTO `consumer` (`id`, `version`, `description`) VALUES (1, 1, 'Test consumer');
INSERT INTO `subscription` (`id`, `version`, `description`, `transformation_template`, `consumer_id`, `document_type`, `document_version`, `subscription_enpoint_config`) VALUES (1, '1', 'Test subscription', 'template', '1', 'person', 'v1', '');
INSERT INTO `match_query` (`version`, `subscription_id`) VALUES ('1', '1');
