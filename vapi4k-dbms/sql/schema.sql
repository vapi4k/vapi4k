-- drop schema if exists vapi4k cascade;

create schema if not exists vapi4k;
create table if not exists vapi4k.messages
(
    id            serial primary key,
    created       timestamp default current_timestamp,
    message_type  text   not null,
    request_type  text   not null,
    message_jsonb jsonb  not null,
    message_json  json   not null,
    elapsed_time  bigint not null
);
