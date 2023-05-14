create table wf_web_checkpoint
(
    id                 varchar(36) not null,
    wf_web_flow_id     varchar(36) not null,
    output             varchar(36),
    state              bigint      not null,
    started_date       datetime(6)          default 0,
    completed_date     datetime(6)          default 0,
    failed_date        datetime(6)          default 0,
    created_date       datetime(6) not null default 0,
    last_modified_date datetime(6) not null default 0,
    version            bigint      not null,
    primary key (id),
    foreign key (wf_web_flow_id) references wf_web_flow (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;

create table wf_web_checkpoint_snapshot
(
    wf_web_checkpoint_id varchar(36) not null,
    idx                  bigint      not null,
    resource             text        not null,
    width_px             bigint      not null,
    screenshot           varchar(36) not null,
    primary key (wf_web_checkpoint_id, idx),
    foreign key (wf_web_checkpoint_id) references wf_web_checkpoint (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;
