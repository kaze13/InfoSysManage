

drop table rd4_temporary_authority;
drop table rd4_partition_dependency;
drop table rd4_unit_dependency;
drop table rd4_mission_unit;
drop table rd4_mission_partition;


drop table rd4_task;
drop table rd4_message;
drop table rd4_apply_authority;
drop table rd4_system_admin;
DROP
    TABLE rd4_main_authority;
DROP
    TABLE rd4_authority;
DROP
    TABLE rd4_managed_authority;
drop table rd4_upload_file;
drop table rd4_mission;
drop table rd4_notification;
drop table rd4_application;
drop table rd4_task_report;
drop table rd4_system_function;
drop table rd4_system_authority;
DROP
    TABLE rd4_user;
DROP
    TABLE rd4_role;
DROP
    TABLE rd4_system;
drop table rd4_dependent_system;
drop table rd4_dependent_function;
drop table rd4_task_report;
drop table rd4_bug_report;
drop table rd4_user_profile;
drop table rd4_application_stage;



CREATE
    TABLE rd4_user(
        user_name VARCHAR(30) PRIMARY KEY
        ,password VARCHAR(50)
        ,role_name VARCHAR(30)
        ,expire_time bigint
        ,real_name varchar(30)
		,identity_number varchar(30)
		,description varchar(200)
		,department varchar(30)
    );

CREATE
    TABLE rd4_system(
        system_name VARCHAR(30) PRIMARY KEY
        ,url VARCHAR(30)
        ,description VARCHAR(80)
        ,war_path VARCHAR(50)
        ,duration VARCHAR(30)
    );
CREATE
	TABLE rd4_system_admin(
		id varchar(50)
		,user_name VARCHAR(30)
		,system_name VARCHAR(30)
		);
CREATE
    TABLE rd4_role(
        role_name VARCHAR(30) PRIMARY KEY
        ,description varchar(100)
    );
CREATE
    TABLE rd4_main_authority(
        id VARCHAR(50) PRIMARY KEY
        ,user_name VARCHAR(30) NOT null
        ,authority_name VARCHAR(30) NOT null
    );
create table rd4_authority(
	authority_name varchar(30) primary key
	);


create table rd4_partition_dependency(
 	id varchar(50) primary key,
 	belonged_mission_id varchar(50),
 	pre_partition_id varchar(50),
 	post_partition_id varchar(50)
 	);

create table rd4_unit_dependency(
	id varchar(50) primary key,
	belonged_partition_id varchar(50),
	pre_unit_id varchar(50),
	post_unit_id varchar(50)
	);

 create table rd4_upload_file(
	id varchar(50) primary key
	,uploader_name varchar(30)
	,file_path varchar(250)
	,file_name varchar(250)
	);


create table rd4_temporary_authority(
	id varchar(50) primary key
	,user_name varchar(30)
	,system_function_id varchar(50)
	,description varchar(100)
	,expire_time bigint
	);

create table rd4_task(
	id varchar(50) primary key
	,creator_name varchar(30)
	,owner_name varchar(30)
	,type varchar(20)
	,title varchar(50)
	,description varchar(300)
	,start_time bigint
	,end_time bigint
	,statue int
	,data varchar(50)
	,file_id varchar(50)
	);


create table rd4_mission(
	id varchar(50) primary key
	,creator_name varchar(30)
	,leader_name varchar(30)
	,title varchar(50)
	,description varchar(300)
	,file_id varchar(50)
	,progress int
	);


create table rd4_mission_partition(
	id varchar(50) primary key
	,mission_id varchar(50)
	,leader_name varchar(30)
	,title varchar(50)
	,description varchar(300)
	,file_id varchar(50)
	,progress int
	);

create table rd4_mission_unit(
	id varchar(50) primary key
	,partition_id varchar(50)
	,leader_name varchar(30)
	,title varchar(50)
	,description varchar(300)
	,file_id varchar(50)
	,progress int
	);



create table rd4_message(
	id varchar(50) primary key
	,sender_name varchar(30)
	,reciever_name varchar(30)
	,title varchar(50)
	,body varchar(10000)
	,time bigint
	,file_id varchar(50)
	,statue int
	);



create table rd4_notification(
	id varchar(50) primary key
	,reciever_name varchar(30)
	,type int
	,title varchar(50)
	,body varchar(500)
	,data varchar(200)
	,time bigint
	,file_id varchar(50)
	,statue int
	,link varchar(100)
	);



create table rd4_application(
	id varchar(50) primary key
	,sender_name varchar(30)
	,reciever_name varchar(30)
	,type int
	,title varchar(50)
	,body varchar(500)
	,data varchar(200)
	,time bigint
	,file_id varchar(50)
	,statue int
	,class varchar(50)
	,stage int
	);

create table rd4_task_report(
	id varchar(50) primary key
	,task_id varchar(50)
	,report varchar(1000)
	,file_id varchar(50)
	);

create table rd4_dependent_system(
	id varchar(50) primary key
	,type int
	,target_id varchar(50)
	,system_name varchar(30)
	);
create table rd4_system_function(
	id varchar(50) primary key
	,system_name varchar(30)
	,function_name varchar(30)
	,description varchar(100)
	);

create table rd4_system_authority(
	id varchar(50) primary key
	,role_name varchar(30)
	,system_function_id varchar(50)
	);

create table rd4_dependent_function(
	id varchar(50) primary key
	,type varchar(30)
	,target_id varchar(50)
	,function_id varchar(50)
	);
create table rd4_bug_report(
	id varchar(50) primary key
	,system_name varchar(30)
	,founder_name varchar(30)
	,report varchar(250)
	,fix_report varchar(250)
	,isfixed int
	,file_id varchar(50)
	);
create table rd4_application_stage(
	id varchar(50) primary key
	,name varchar(200)
	,serial int
	,application_class_id varchar(50)
	,dealer_name varchar(30)
	,comment varchar(1000)
	,data varchar(1000)
	,file_id varchar(50)
	)
create table rd4_application_result(
	id varchar(50) primary key
	,application_class_id varchar(50)
	,type int
	,title varchar(250)
	,body varchar(2000)
	,time bigint
	,file_id varchar(50)
	,data1 varchar(500)
	,data2 varchar(500)
	,data3 varchar(500)
	,statue int
	);
	
create table rd4_dictionary(
	dickey varchar(250) primary key
	,value varchar(250)
	);
alter table rd4_user_profile add constraint rd4_user_profile_fk_user foreign key(user_id)
references rd4_user(user_name);

alter table rd4_bug_report add constraint rd4_bug_report_fk_system foreign key(system_name)
references rd4_system(system_name);
alter table rd4_bug_report add constraint rd4_bug_report_fk_user foreign key(founder_name)
references rd4_user(user_name);


alter table rd4_temporary_authority add constraint rd4_temporary_authority_fk_function foreign key(system_function_id)
references rd4_system_function(id);

alter table rd4_system_function add constraint rd4_system_function_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_system_authority add constraint rd4_system_authority_fk_role foreign key(role_name)
references rd4_role(role_name);
alter table rd4_system_authority add constraint rd4_system_authority_fk_system_function foreign key(system_function_id)
references rd4_system_function(id);

alter table rd4_dependent_system add constraint rd4_dependent_system_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_mission_partition add constraint rd4_mission_partition_fk_mission foreign key(mission_id)
references rd4_mission(id);


alter table rd4_mission_unit add constraint rd4_mission_partition_fk_partition foreign key(partition_id)
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

alter table rd4_partition_dependency add constraint rd4_partition_dependency_fk_belong foreign key(belonged_mission_id)
references rd4_mission(id) on delete cascade;
alter table rd4_partition_dependency add constraint rd4_partition_dependency_fk_pre foreign key(pre_partition_id)
references rd4_mission_partition(id) on delete cascade;
alter table rd4_partition_dependency add constraint rd4_partition_dependency_fk_post foreign key(post_partition_id)
references rd4_mission_partition(id) on delete cascade;

alter table rd4_unit_dependency add constraint rd4_unit_dependency_fk_belong foreign key(belonged_partition_id)
references rd4_mission_partition(id) on delete cascade;
alter table rd4_unit_dependency add constraint rd4_unit_dependency_fk_pre foreign key(pre_unit_id)
references rd4_mission_unit(id) on delete cascade;
alter table rd4_unit_dependency add constraint rd4_unit_dependency_fk_post foreign key(post_unit_id)
references rd4_mission_unit(id) on delete cascade;

alter table rd4_system_admin add constraint rd4_system_admin_fk_user foreign key(user_name)
references rd4_user(user_name);
alter table rd4_system_admin add constraint rd4_system_admin_fk_system foreign key(system_name)
references rd4_system(system_name);
alter table rd4_main_authority add constraint rd4_main_authority_fk_authority foreign key(authority_name) references rd4_authority(authority_name);
alter table rd4_user add constraint rd4_user_fk_role foreign key(role_name)
references rd4_role(role_name);
alter table rd4_main_authority add constraint rd4_main_authority_fk_user foreign key(user_name)
references rd4_user(user_name) on delete cascade;
alter table rd4_managed_authority add constraint rd4_managed_authority_fk_role foreign key(role_name)
references rd4_role(role_name);

alter table rd4_temporary_authority add constraint rd4_temporary_authority_fk_user foreign key(user_name)
references rd4_user(user_name);
alter table rd4_temporary_authority add constraint rd4_temporary_authority_fk_system foreign key(system_name)
references rd4_system(system_name);

alter table rd4_apply_authority add constraint rd4_apply_authority_fk_user foreign key(user_name)
references rd4_user(user_name);
alter table rd4_apply_authority add constraint rd4_apply_authority_fk_system foreign key(system_name)
references rd4_system(system_name);



alter table rd4_mission_partition_relation add constraint rd4_mission_partition_relation_fk_mission foreign key(mission_id)
references rd4_mission(id) on delete cascade;
alter table rd4_mission_partition_relation add constraint rd4_mission_partition_relation_fk_partition foreign key(partition_id)
references rd4_mission_partition(id) on delete cascade;

alter table rd4_partition_unit_relation add constraint rd4_partition_unit_relation_fk_partition foreign key(partition_id)
references rd4_mission_partition(id) on delete cascade;
alter table rd4_partition_unit_relation add constraint rd4_partition_unit_relation_fk_unit foreign key(unit_id)
references rd4_mission_unit(id) on delete cascade;
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
insert into rd4_system values('google','www.google.com','google','none','');
insert into rd4_main_authority values('1','user','user');
insert into rd4_main_authority values('2','admin','admin');
insert into rd4_main_authority values('3','auditor','auditor');
insert into rd4_main_authority values('4','user2','user2');
insert into rd4_main_authority values('5','user3','user3');


I can't login your system.(shows "WARNING|glassfish3.0.1|javax.enterprise.system.container.web.com.sun.web.security|_ThreadID=30;_ThreadName=Thread-1;|Web login failed: Login failed: javax.security.auth.login.LoginException: Security Exception|#]
"

The priority is strange in your catalog.
How new employee can start project without "New incoming employee setting" feature?
System administrators should not make new account directly.