create user if not exists 'waffle'@'%' identified by 'my-secret-pw';
create database if not exists `waffle_test`;
grant all on `waffle_test`.* to 'waffle'@'%';
