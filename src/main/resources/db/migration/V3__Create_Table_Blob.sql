create table wf_blob
(
    id                 varchar(36) not null,
    created_date       datetime(6) not null default 0,
    last_modified_date datetime(6) not null default 0,
    version            bigint      not null,
    primary key (id)
) engine = InnoDB
  default charset utf8mb4;

create table wf_blob_value
(
    wf_blob_id varchar(36) not null,
    value      longblob    not null,
    primary key (wf_blob_id),
    foreign key (wf_blob_id) references wf_blob (id) on delete cascade on update cascade
) engine = InnoDB
  default charset utf8mb4;
