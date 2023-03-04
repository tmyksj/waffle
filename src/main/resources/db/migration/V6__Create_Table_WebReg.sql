create table wf_web_reg
(
    id                     varchar(36) not null,
    wf_web_checkpoint_id_a varchar(36) not null,
    wf_web_checkpoint_id_b varchar(36) not null,
    result                 varchar(36),
    state                  bigint      not null,
    started_date           datetime(6)          default 0,
    completed_date         datetime(6)          default 0,
    failed_date            datetime(6)          default 0,
    created_date           datetime(6) not null default 0,
    last_modified_date     datetime(6) not null default 0,
    version                bigint      not null,
    primary key (id),
    foreign key (wf_web_checkpoint_id_a) references wf_web_checkpoint (id) on delete cascade on update cascade,
    foreign key (wf_web_checkpoint_id_b) references wf_web_checkpoint (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;
