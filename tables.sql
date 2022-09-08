create table tbl_devices
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    deviceName VARCHAR(50),
    type       VARCHAR(4),
    ip         VARCHAR(20),
    username   VARCHAR(50),
    password   VARCHAR(50),
    provision  INT default 0;
);


insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (1, 'Zaam-Dox', 'ssh', '191.216.73.67', 'LiveZ', 'lpoI5A0vT', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (2, 'Flowdesk', 'ssh', '1.105.58.204', 'Fliptune', 'bDkXZ1VuZ1bM', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (3, 'Stringtough', 'ping', '107.165.37.42', 'Mynte', 'HEiwLqd', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (4, 'Overhold', 'ping', '242.90.231.216', 'Dynazzy', 'IaILHQ', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (5, 'It', 'ping', '229.217.51.218', 'Blogtag', 'RIa5LubSf', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (6, 'Otcom', 'ssh', '74.148.122.69', 'Voonder', 'q0YX6M', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (7, 'Zamit', 'ssh', '39.186.208.210', 'Topiclounge', 'a3f5tc', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (8, 'Domainer', 'ping', '178.20.206.237', 'Twitterwire', '6r9Y3aC', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (9, 'Regrant', 'ping', '152.111.248.255', 'Rhycero', 'P8QFuy', 0);
insert into tbl_devices (id, deviceName, type, ip, username, password, provision) values (10, 'Daltfresh', 'ping', '14.208.221.122', 'Voonyx', 'YkOZ6qLRf9', 0);

create table users (id INT AUTO_INCREMENT PRIMARY KEY,username VARCHAR(50),password VARCHAR(50));

insert into users (id, username, password) values (1, 'shekhar', 'admin');
insert into users (id, username, password) values (2, 'pavan', 'admin');
insert into users (id, username, password) values (3, 'rahil', 'admin');
insert into users (id, username, password) values (4, 'meet', 'admin');
insert into users (id, username, password) values (5, 'smit', 'admin');