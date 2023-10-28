    CREATE TABLE `liteflow_chain` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `application_name` varchar(32) NOT NULL COMMENT '应用名称',
                                  `chain_name` varchar(64) NOT NULL COMMENT '规则名称存储的字段名',
                                  `chain_desc` varchar(128) DEFAULT NULL,
                                  `el_data` varchar(1024) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `liteflow_chain_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则表'

CREATE TABLE `liteflow_script` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `application_name` varchar(32) NOT NULL,
                                   `script_id` varchar(64) NOT NULL,
                                   `script_name` varchar(64) NOT NULL,
                                   `script_data` varchar(1024) DEFAULT NULL,
                                   `script_type` varchar(32) DEFAULT NULL,
                                   `script_language` varchar(32) NOT NULL,
                                   `create_time` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `liteflow_script_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

