create user if not exists 'waffle'@'%' identified by 'my-secret-pw';
create database if not exists `waffle_main`;
grant all on `waffle_main`.* to 'waffle'@'%';
