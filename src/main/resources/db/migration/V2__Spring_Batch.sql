# https://docs.spring.io/spring-batch/docs/current/reference/html/index-single.html#metaDataSchema
# https://github.com/spring-projects/spring-batch/tree/main/spring-batch-core/src/main/resources/org/springframework/batch/core

create sequence BATCH_STEP_EXECUTION_SEQ
    engine = InnoDB
    default charset utf8mb4;

create sequence BATCH_JOB_EXECUTION_SEQ
    engine = InnoDB
    default charset utf8mb4;

create sequence BATCH_JOB_SEQ
    engine = InnoDB
    default charset utf8mb4;

create table BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID bigint       not null,
    VERSION         bigint,
    JOB_NAME        varchar(100) not null,
    JOB_KEY         varchar(32)  not null,
    primary key (JOB_INSTANCE_ID),
    unique key (JOB_NAME, JOB_KEY)
) engine = InnoDB
  default charset utf8mb4;

create table BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID bigint      not null,
    VERSION          bigint,
    JOB_INSTANCE_ID  bigint      not null,
    CREATE_TIME      datetime(6) not null,
    START_TIME       datetime(6) default null,
    END_TIME         datetime(6) default null,
    STATUS           varchar(10),
    EXIT_CODE        varchar(2500),
    EXIT_MESSAGE     varchar(2500),
    LAST_UPDATED     datetime(6),
    primary key (JOB_EXECUTION_ID),
    foreign key (JOB_INSTANCE_ID) references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
) engine = InnoDB
  default charset utf8mb4;

create table BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   bigint        not null,
    SHORT_CONTEXT      varchar(2500) not null,
    SERIALIZED_CONTEXT longtext,
    primary key (JOB_EXECUTION_ID),
    foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) engine = InnoDB
  default charset utf8mb4;

create table BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID bigint       not null,
    PARAMETER_NAME   varchar(100) not null,
    PARAMETER_TYPE   varchar(100) not null,
    PARAMETER_VALUE  varchar(2500),
    IDENTIFYING      char(1)      not null,
    foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) engine = InnoDB
  default charset utf8mb4;

create table BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  bigint       not null,
    VERSION            bigint       not null,
    STEP_NAME          varchar(100) not null,
    JOB_EXECUTION_ID   bigint       not null,
    CREATE_TIME        datetime(6)  not null,
    START_TIME         datetime(6) default null,
    END_TIME           datetime(6) default null,
    STATUS             varchar(10),
    COMMIT_COUNT       bigint,
    READ_COUNT         bigint,
    FILTER_COUNT       bigint,
    WRITE_COUNT        bigint,
    READ_SKIP_COUNT    bigint,
    WRITE_SKIP_COUNT   bigint,
    PROCESS_SKIP_COUNT bigint,
    ROLLBACK_COUNT     bigint,
    EXIT_CODE          varchar(2500),
    EXIT_MESSAGE       varchar(2500),
    LAST_UPDATED       datetime(6),
    primary key (STEP_EXECUTION_ID),
    foreign key (JOB_EXECUTION_ID) references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
) engine = InnoDB
  default charset utf8mb4;

create table BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  bigint        not null,
    SHORT_CONTEXT      varchar(2500) not null,
    SERIALIZED_CONTEXT longtext,
    primary key (STEP_EXECUTION_ID),
    foreign key (STEP_EXECUTION_ID) references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
) engine = InnoDB
  default charset utf8mb4;
