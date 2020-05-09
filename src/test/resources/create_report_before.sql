insert into reports(id, created,updated,description, name, status, decline_reason, owner_id)
values (1, '2020-03-27', '2020-03-27', 'lorem ipsum', 'queue', 'QUEUE', null, 1),
       (2, '2020-03-27', '2020-03-27', 'lorem ipsum', 'accepted', 'ACCEPTED', null, 1),
       (3, '2020-03-27', '2020-03-27', 'lorem ipsum', 'not accepted', 'NOT_ACCEPTED', 'reason', 1),
       (4, '2020-03-27', '2020-03-27', 'lorem ipsum', 'queue', 'QUEUE', null, 1);

insert into report_inspectors(report_id, usr_id) values
(4, 3),
(3, 3),
(2, 3),
(1, 3),
(4, 2),
(3, 2),
(2, 2),
(1, 2);