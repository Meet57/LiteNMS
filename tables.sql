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


insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Shekhar ssh', 'ssh', '10.20.42.142', 'shekhar', 'Mind@123', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('Shekhar ping', 'ping', '10.20.42.142', 0);
insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Rahil ssh', 'ssh', '10.20.40.204', 'rahil', 'Mind@123', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('Rahil ping', 'ping', '10.20.42.204', 0);
insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Smit ssh', 'ssh', '10.20.40.113', 'smit', 'Mind@123', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('Smit ping', 'ping', '10.20.40.113', 0);
insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Server 1 ssh', 'ssh', '172.16.10.183', 'root', 'motadata', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('server 1 ping', 'ping', '172.16.10.183', 0);
insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Server 2 ssh', 'ssh', '172.16.10.193', 'root', 'motadata', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('server 2 ping', 'ping', '172.16.10.193', 0);
insert into tbl_devices (deviceName, type, ip, username, password, provision) values ('Server 3 ssh', 'ssh', '172.16.10.195', 'root', 'motadata', 0);
insert into tbl_devices (deviceName, type, ip, provision) values ('server 3 ping', 'ping', '172.16.10.195', 0);
INSERT INTO `tbl_devices` VALUES (67,'smit ssh','10.20.40.158','ssh','smit','mind@1682',1),(74,'smit','10.20.40.158','ping',NULL,NULL,1));


create table users (id INT AUTO_INCREMENT PRIMARY KEY,username VARCHAR(50),password VARCHAR(50));

insert into users (id, username, password) values (1, 'shekhar', 'admin');
insert into users (id, username, password) values (2, 'pavan', 'admin');
insert into users (id, username, password) values (3, 'rahil', 'admin');
insert into users (id, username, password) values (4, 'meet', 'admin');
insert into users (id, username, password) values (5, 'smit', 'admin');

create table metrics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    timestamp TIMESTAMP,
    ip VARCHAR(30),
    type VARCHAR(5),
    avaibility VARCHAR(5),
    packet_loss int,
    rtt VARCHAR(7),
    cpu int,
    mem int,
    total_mem int,
    disk int,
    tdisk int
);