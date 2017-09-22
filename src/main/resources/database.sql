CREATE TABLE `files` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `path` varchar(250) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8

CREATE TABLE `images` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `path` varchar(250) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8


CREATE TABLE `roles` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `name` varchar(100) NOT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8



CREATE TABLE `users` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `username` varchar(255) NOT NULL,
   `password` varchar(255) NOT NULL,
   `email` varchar(100) DEFAULT NULL,
   `image_id` int(11) DEFAULT NULL,
   `about` varchar(100) DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `users_images_id_fk` (`image_id`),
   CONSTRAINT `users_images_id_fk` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8


CREATE TABLE `user_roles` (
   `user_id` int(11) NOT NULL,
   `role_id` int(11) NOT NULL,
   UNIQUE KEY `user_id` (`user_id`,`role_id`),
   KEY `role_id` (`role_id`),
   CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
   CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `dialog_config` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `user1` varchar(45) DEFAULT NULL,
   `user2` varchar(45) DEFAULT NULL,
   `file_id` int(11) DEFAULT '1',
   PRIMARY KEY (`id`),
   KEY `dialog_config_files_id_fk` (`file_id`),
   CONSTRAINT `dialog_config_files_id_fk` FOREIGN KEY (`file_id`) REFERENCES `files` (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8