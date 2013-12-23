CREATE
    TABLE rd4_role(
        role_name VARCHAR(100) PRIMARY KEY
        ,description VARCHAR2(500)
    );

    CREATE
    TABLE rd4_system(
        system_name VARCHAR(100) PRIMARY KEY
        ,url VARCHAR(100)
        ,description VARCHAR(500)
        ,war_path VARCHAR(100)
        ,duration VARCHAR(100)
    );

    create table rd4_authority(
	authority_name VARCHAR2(100) primary key
	);

	 create table rd4_upload_file(
	id VARCHAR2(50) primary key
	,uploader_name VARCHAR2(100)
	,file_path VARCHAR2(500)
	,file_name VARCHAR2(500)
	);

CREATE
    TABLE rd4_user(
        user_name VARCHAR(100) PRIMARY KEY
        ,password VARCHAR(100)
        ,role_name VARCHAR(100) references rd4_role(role_name) on delete cascade
        ,expire_time number(30,0)
        ,real_name varchar(100)
		,identity_number varchar(100)
		,description varchar(500)
		,department varchar(100)
    );

CREATE
	TABLE rd4_system_admin(
		id VARCHAR2(50)
		,user_name VARCHAR(100) NOT null references rd4_user(user_name) on delete cascade
		,system_name VARCHAR(100) NOT null
		);

CREATE
    TABLE rd4_main_authority(
        id VARCHAR(50) PRIMARY KEY
        ,user_name VARCHAR(100) NOT null references rd4_user(user_name) on delete cascade
        ,authority_name VARCHAR(100) NOT null references rd4_authority(authority_name) on delete cascade
    );



create table rd4_task(
	id VARCHAR2(50) primary key
	,creator_name VARCHAR2(100)
	,owner_name VARCHAR2(100)
	,type VARCHAR2(100)
	,title VARCHAR2(100)
	,description VARCHAR2(500)
	,start_time  NUMBER(20,0)
	,end_time  NUMBER(20,0)
	,statue NUMBER(10,0)
	,data VARCHAR2(100)
	,file_id VARCHAR2(50)
	);


create table rd4_mission(
	id VARCHAR2(50) primary key
	,creator_name VARCHAR2(100)
	,leader_name VARCHAR2(100)
	,title VARCHAR2(100)
	,description VARCHAR2(500)
	,progress NUMBER(10,0)
	);


create table rd4_mission_partition(
	id VARCHAR2(50) primary key
	,mission_id VARCHAR2(50) references rd4_mission(id) on delete cascade
	,leader_name VARCHAR2(100)
	,title VARCHAR2(100)
	,description VARCHAR2(500)
	,progress NUMBER(10,0)
	);

create table rd4_mission_unit(
	id VARCHAR2(50) primary key
	,partition_id VARCHAR2(50) references rd4_mission_partition(id) on delete cascade
	,leader_name VARCHAR2(100)
	,title VARCHAR2(100)
	,description VARCHAR2(500)
	,progress NUMBER(10,0)
	);



create table rd4_message(
	id VARCHAR2(50) primary key
	,sender_name VARCHAR2(100)
	,reciever_name VARCHAR2(100)
	,title VARCHAR2(100)
	,body VARCHAR2(1000)
	,time  NUMBER(20,0)
	,file_id VARCHAR2(50)
	,statue NUMBER(10,0)
	);



create table rd4_notification(
	id VARCHAR2(50) primary key
	,reciever_name VARCHAR2(100)
	,type NUMBER(10,0)
	,title VARCHAR2(100)
	,body VARCHAR2(1000)
	,data VARCHAR2(200)
	,time  NUMBER(20,0)
	,file_id VARCHAR2(50)
	,statue NUMBER(10,0)
	);



create table rd4_application(
	id VARCHAR2(50) primary key
	,sender_name VARCHAR2(100)
	,reciever_name VARCHAR2(100)
	,type NUMBER(10,0)
	,title VARCHAR2(100)
	,body VARCHAR2(1000)
	,data VARCHAR2(500)
	,time  NUMBER(20,0)
	,file_id VARCHAR2(100)
	,statue NUMBER(10,0)
	,class VARCHAR2(100)
	);

create table rd4_task_report(
	id VARCHAR2(50) primary key
	,task_id VARCHAR2(50) references rd4_task(id) on delete cascade
	,report VARCHAR2(1000)
	,file_id VARCHAR2(50)
	);

create table rd4_dependent_system(
	id VARCHAR2(50) primary key
	,type NUMBER(10,0)
	,target_id VARCHAR2(50)
	,system_name VARCHAR2(100) references rd4_system(system_name) on delete cascade
	);
create table rd4_system_function(
	id VARCHAR2(50) primary key
	,system_name VARCHAR2(100) references rd4_system(system_name) on delete cascade
	,function_name VARCHAR2(100)
	,description VARCHAR2(500)
	);

create table rd4_system_authority(
	id VARCHAR2(50) primary key
	,role_name VARCHAR2(100) references rd4_role(role_name) on delete cascade
	,system_function_id VARCHAR2(100) references rd4_system_function(id) on delete cascade
	);

create table rd4_dependent_function(
	id VARCHAR2(50) primary key
	,type VARCHAR2(100)
	,target_id VARCHAR2(50)
	,function_id VARCHAR2(50) references rd4_system_function(id) on delete cascade
	);
create table rd4_bug_report(
	id VARCHAR2(50) primary key
	,system_name VARCHAR2(100) references rd4_system(system_name) on delete cascade
	,founder_name VARCHAR2(100)
	,report VARCHAR2(1000)
	,fix_report VARCHAR2(1000)
	,isfixed NUMBER(10,0)
	,file_id VARCHAR2(50)
	);

create table rd4_partition_dependency(
 	id VARCHAR2(50) primary key,
 	belonged_mission_id VARCHAR2(50) references rd4_mission(id) on delete cascade,
 	pre_partition_id VARCHAR2(50) references rd4_mission_partition(id) on delete cascade,
 	post_partition_id VARCHAR2(50) references rd4_mission_partition(id) on delete cascade
 	);

create table rd4_unit_dependency(
	id VARCHAR2(50) primary key,
	belonged_partition_id VARCHAR2(50) references rd4_mission_partition(id) on delete cascade,
	pre_unit_id VARCHAR2(50) references rd4_mission_unit(id) on delete cascade,
	post_unit_id VARCHAR2(50) references rd4_mission_unit(id) on delete cascade
	);



create table rd4_temporary_authority(
	id VARCHAR2(50) primary key
	,user_name VARCHAR2(100) references rd4_user(user_name) on delete cascade
	,system_function_id VARCHAR2(50) references rd4_system_function(id) on delete cascade
	,description VARCHAR2(1000)
	,expire_time  NUMBER(20,0)
	);

alter table rd4_bug_report add constraint rd4_bug_report_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_temporary_authority add constraint rd4_tempo_author_fk_func foreign key(system_function_id)
references rd4_system_function(id);

alter table rd4_system_function add constraint rd4_system_function_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_system_authority add constraint rd4_system_authority_fk_role foreign key(role_name)
references rd4_role(role_name);
alter table rd4_system_authority add constraint rd4_sys_autho_fk_system_func foreign key(system_function_id)
references rd4_system_function(id);

alter table rd4_dependent_system add constraint rd4_dependent_system_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_mission_partition add constraint rd4_miss_parti_fk_miss foreign key(mission_id)
references rd4_mission(id);


alter table rd4_mission_unit add constraint rd4_miss_parti_fk_parti foreign key(partition_id)
references rd4_mission_partition(id);


alter table rd4_message add constraint rd4_message_fk_sender foreign key(sender_name)
references rd4_user(user_name);
alter table rd4_message add constraint rd4_message_fk_reciever foreign key(reciever_name)
references rd4_user(user_name);
alter table rd4_notification add constraint rd4_notification_fk_reciever foreign key(reciever_name)
references rd4_user(user_name);

alter table rd4_application add constraint rd4_application_fk_sender foreign key(sender_name)
references rd4_user(user_name);
alter table rd4_application add constraint rd4_application_fk_reciever foreign key(reciever_name)
references rd4_user(user_name);
alter table rd4_mission add constraint rd4_mission_fk_creator foreign key(creator_name)
references rd4_user(user_name);
alter table rd4_mission add constraint rd4_mission_fk_leader foreign key(leader_name)
references rd4_user(user_name);
alter table rd4_upload_file add constraint rd4_upload_file_fk_user foreign key(uploader_name)
references rd4_user(user_name);

alter table rd4_partition_dependency add constraint rd4_parti_depend_fk_belong foreign key(belonged_mission_id)
references rd4_mission(id) on delete cascade;
alter table rd4_partition_dependency add constraint rd4_parti_depend_fk_pre foreign key(pre_partition_id)
references rd4_mission_partition(id) on delete cascade;
alter table rd4_partition_dependency add constraint rd4_parti_depend_fk_post foreign key(post_partition_id)
references rd4_mission_partition(id) on delete cascade;

alter table rd4_unit_dependency add constraint rd4_unit_depend_fk_belong foreign key(belonged_partition_id)
references rd4_mission_partition(id) on delete cascade;
alter table rd4_unit_dependency add constraint rd4_unit_depend_fk_pre foreign key(pre_unit_id)
references rd4_mission_unit(id) on delete cascade;
alter table rd4_unit_dependency add constraint rd4_unit_depend_fk_post foreign key(post_unit_id)
references rd4_mission_unit(id) on delete cascade;

alter table rd4_system_admin add constraint rd4_sys_admin_fk_user foreign key(user_name)
references rd4_user(user_name);
alter table rd4_system_admin add constraint rd4_sys_admin_fk_system foreign key(system_name)
references rd4_system(system_name);
alter table rd4_main_authority add constraint rd4_main_autho_fk_autho foreign key(authority_name) references rd4_authority(authority_name);
alter table rd4_user add constraint rd4_user_fk_role foreign key(role_name)
references rd4_role(role_name);
alter table rd4_main_authority add constraint rd4_main_autho_fk_user foreign key(user_name)
references rd4_user(user_name) on delete cascade;
alter table rd4_managed_authority add constraint rd4_managed_autho_fk_role foreign key(role_name)
references rd4_role(role_name);

alter table rd4_temporary_authority add constraint rd4_tempo_autho_fk_user foreign key(user_name)
references rd4_user(user_name);






alter table rd4_task add constraint rd4_task_fk_user foreign key(owner_name)
references rd4_user(user_name);
alter table rd4_task add constraint rd4_task_fk_creator foreign key(creator_name)
references rd4_user(user_name);


insert into rd4_role values('employee','asd');
insert into rd4_role values('manager','asd');
insert into rd4_role values('accountant','asd');
insert into rd4_role values('auditor','asd');

insert into rd4_authority values('user');
insert into rd4_authority values('admin');

insert into rd4_user values('user','user','employee',99999999,'','','','');
insert into rd4_user values('admin','admin','manager',99999999,'','','','');
insert into rd4_user values('auditor','auditor','manager',99999999,'','','','');
insert into rd4_user values('user2','user','manager',99999999,'','','','');
insert into rd4_user values('user3','user','manager',99999999,'','','','');
insert into rd4_system values('google','www.google.com','google','none','');
insert into rd4_main_authority values('1','user','user');
insert into rd4_main_authority values('2','admin','admin');
insert into rd4_main_authority values('3','auditor','user');
insert into rd4_main_authority values('4','user2','user');
insert into rd4_main_authority values('5','user3','user');