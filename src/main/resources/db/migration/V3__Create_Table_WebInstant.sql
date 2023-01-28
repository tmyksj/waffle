create table wf_web_instant
(
    id                 varchar(36) not null,
    created_date       datetime(6) not null default 0,
    last_modified_date datetime(6) not null default 0,
    version            bigint      not null,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;

create table wf_web_instant_composition
(
    wf_web_instant_id varchar(36) not null,
    id                bigint      not null,
    resource          text        not null,
    widthPx           bigint      not null,
    delayMs           bigint      not null,
    primary key (wf_web_instant_id, id)
) engine = InnoDB
  default charset utf8mb4;
