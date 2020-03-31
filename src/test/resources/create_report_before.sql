insert into reports(id, created,updated,description, name, status, usr_id, inspector_id, decline_reason)
values (1, '2020-03-27', '2020-03-27', 'lorem ipsum', 'queue', 'QUEUE',
        1, 2, null),
       (2, '2020-03-27', '2020-03-27', 'lorem ipsum', 'accepted', 'ACCEPTED',
        1,2, null),
       (3, '2020-03-27', '2020-03-27', 'lorem ipsum', 'not accepted', 'NOT_ACCEPTED',
        1, 2, 'reason'),
        (4, '2020-03-27', '2020-03-27', 'lorem ipsum', 'queue', 'QUEUE',
        1, 2, null);