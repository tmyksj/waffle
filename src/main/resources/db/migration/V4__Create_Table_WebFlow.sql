create table wf_web_flow
(
    id                 varchar(36) not null,
    created_date       datetime(6) not null default 0,
    last_modified_date datetime(6) not null default 0,
    version            bigint      not null,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;

create table wf_web_flow_composition
(
    wf_web_flow_id varchar(36) not null,
    idx            bigint      not null,
    resource       text        not null,
    width_px       bigint      not null,
    height_px      bigint      not null,
    delay_ms       bigint      not null,
    primary key (wf_web_flow_id, idx),
    foreign key (wf_web_flow_id) references wf_web_flow (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;
