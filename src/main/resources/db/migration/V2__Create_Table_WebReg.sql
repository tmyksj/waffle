create table wf_web_reg
(
    id                 varchar(36) not null,
    result             longblob,
    state              bigint      not null,
    started_date       datetime(6) default 0,
    completed_date     datetime(6) default 0,
    failed_date        datetime(6) default 0,
    created_date       datetime(6) not null default 0,
    last_modified_date datetime(6) not null default 0,
    version            bigint      not null,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;

create table wf_web_reg_case
(
    wf_web_reg_id varchar(36) not null,
    id            bigint      not null,
    expected      text        not null,
    actual        text        not null,
    primary key (wf_web_reg_id, id)
) engine = InnoDB
  default charset utf8mb4;
