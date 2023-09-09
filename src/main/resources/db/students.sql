TRUNCATE groups RESTART IDENTITY CASCADE ;
insert into groups(name) values ('group A');
insert into groups(name) values ('group B');

TRUNCATE students RESTART IDENTITY CASCADE;
insert into students(first_name, last_name, group_id) values ('Aliaksandr', 'Hanchar', 1);
insert into students(first_name, last_name, group_id) values ('Giorgi', 'Khvadadze', 1);
insert into students(first_name, last_name, group_id) values ('Ilya', 'Gospodarev', 1);
insert into students(first_name, last_name, group_id) values ('Daniil', 'Fedorenko', 1);
insert into students(first_name, last_name, group_id) values ('Gleb', 'Ivashyn', 1);
insert into students(first_name, last_name, group_id) values ('Pavel', 'Bukin', 2);
insert into students(first_name, last_name, group_id) values ('Alexandr', 'Matyushenko', 2);
insert into students(first_name, last_name, group_id) values ('Stepan', 'Dulka', 2);
insert into students(first_name, last_name, group_id) values ('Yusufbek', 'Sulaymonov', 2);

update groups set students_amount = 5, teamlead_id = 1 where id = 1;
update groups set students_amount = 4, teamlead_id = 6 where id = 1;