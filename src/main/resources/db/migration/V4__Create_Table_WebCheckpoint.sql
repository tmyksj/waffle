create table wf_web_checkpoint
(
    id                 varchar(36) not null,
    wf_web_flow_id     varchar(36) not null,
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

create table wf_web_checkpoint_snapshot
(
    wf_web_checkpoint_id varchar(36) not null,
    id                   bigint      not null,
    resource             text        not null,
    width_px             bigint      not null,
    screenshot           longblob    not null,
    primary key (wf_web_checkpoint_id, id)
) engine = InnoDB
  default charset utf8mb4;
