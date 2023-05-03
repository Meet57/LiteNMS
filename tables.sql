create table tbl_devices
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    deviceName VARCHAR(50),
    type       VARCHAR(4),
    ip         VARCHAR(20),
    username   VARCHAR(50),
    password   VARCHAR(50),
    provision  INT default 0
);

create table tbl_monitor_devices (id INT AUTO_INCREMENT PRIMARY KEY, deviceName VARCHAR(50),ip VARCHAR(20), type VARCHAR(4), username VARCHAR(50), password VARCHAR(50) )

create table users (id INT AUTO_INCREMENT PRIMARY KEY,username VARCHAR(50),password VARCHAR(50));

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
    disk varchar(30),
    tdisk int,
    status int,
    device_id int
);
