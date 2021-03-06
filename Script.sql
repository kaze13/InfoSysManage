

CREATE TABLE RD4_APPLICATION (
		ID VARCHAR2(100) NOT NULL,
		SENDER_NAME VARCHAR2(100),
		RECIEVER_NAME VARCHAR2(100),
		TYPE NUMBER(25),
		TITLE VARCHAR2(100),
		BODY VARCHAR2(1000),
		DATA VARCHAR2(200),
		TIME NUMBER(25),
		FILE_ID VARCHAR2(100),
		STATUE NUMBER(25),
		CLASS VARCHAR2(100),
		STAGE NUMBER(25)
	);



CREATE TABLE RD4_NOTIFICATION (
		ID VARCHAR2(100) NOT NULL,
		RECIEVER_NAME VARCHAR2(100),
		TYPE NUMBER(25),
		TITLE VARCHAR2(100),
		BODY VARCHAR2(1000),
		DATA VARCHAR2(200),
		TIME NUMBER(25),
		FILE_ID VARCHAR2(100),
		STATUE NUMBER(25),
		LINK VARCHAR2(200)
	);

CREATE TABLE RD4_MISSION_UNIT (
		ID VARCHAR2(100) NOT NULL,
		PARTITION_ID VARCHAR2(100),
		LEADER_NAME VARCHAR2(100),
		TITLE VARCHAR2(100),
		DESCRIPTION VARCHAR2(300),
		FILE_ID VARCHAR2(100),
		PROGRESS NUMBER(25)
	);



CREATE TABLE RD4_DEPENDENT_FUNCTION (
		ID VARCHAR2(100) NOT NULL,
		TYPE VARCHAR2(100),
		TARGET_ID VARCHAR2(100),
		FUNCTION_ID VARCHAR2(100)
	);

CREATE TABLE RD4_UPLOAD_FILE (
		ID VARCHAR2(100) NOT NULL,
		UPLOADER_NAME VARCHAR2(100),
		FILE_PATH VARCHAR2(500),
		FILE_NAME VARCHAR2(500)
	);

CREATE TABLE RD4_MESSAGE (
		ID VARCHAR2(100) NOT NULL,
		SENDER_NAME VARCHAR2(100),
		RECIEVER_NAME VARCHAR2(100),
		TITLE VARCHAR2(100),
		BODY VARCHAR2(2000),
		TIME NUMBER(25),
		FILE_ID VARCHAR2(100),
		STATUE NUMBER(25)
	);

CREATE TABLE RD4_ROLE (
		ROLE_NAME VARCHAR2(100) NOT NULL,
		DESCRIPTION VARCHAR2(200)
	);

CREATE TABLE RD4_BUG_REPORT (
		ID VARCHAR2(100) NOT NULL,
		SYSTEM_NAME VARCHAR2(100),
		FOUNDER_NAME VARCHAR2(100),
		REPORT VARCHAR2(500),
		FIX_REPORT VARCHAR2(500),
		ISFIXED NUMBER(25),
		FILE_ID VARCHAR2(100)
	);

CREATE TABLE RD4_UNIT_DEPENDENCY (
		ID VARCHAR2(100) NOT NULL,
		BELONGED_PARTITION_ID VARCHAR2(100),
		PRE_UNIT_ID VARCHAR2(100),
		POST_UNIT_ID VARCHAR2(100)
	);

CREATE TABLE RD4_SYSTEM_FUNCTION (
		ID VARCHAR2(100) NOT NULL,
		SYSTEM_NAME VARCHAR2(100),
		FUNCTION_NAME VARCHAR2(100),
		DESCRIPTION VARCHAR2(200)
	);

CREATE TABLE RD4_TASK (
		ID VARCHAR2(100) NOT NULL,
		CREATOR_NAME VARCHAR2(100),
		OWNER_NAME VARCHAR2(100),
		TYPE VARCHAR2(20),
		TITLE VARCHAR2(100),
		DESCRIPTION VARCHAR2(300),
		START_TIME NUMBER(25),
		END_TIME NUMBER(25),
		STATUE NUMBER(25),
		DATA VARCHAR2(100),
		FILE_ID VARCHAR2(100)
	);

CREATE TABLE RD4_DEPENDENT_SYSTEM (
		ID VARCHAR2(100) NOT NULL,
		TYPE NUMBER(25),
		TARGET_ID VARCHAR2(100),
		SYSTEM_NAME VARCHAR2(100)
	);

CREATE TABLE RD4_TEMPORARY_AUTHORITY (
		ID VARCHAR2(100) NOT NULL,
		USER_NAME VARCHAR2(100),
		SYSTEM_FUNCTION_ID VARCHAR2(100),
		DESCRIPTION VARCHAR2(200),
		EXPIRE_TIME NUMBER(25)
	);

CREATE TABLE RD4_PARTITION_DEPENDENCY (
		ID VARCHAR2(100) NOT NULL,
		BELONGED_MISSION_ID VARCHAR2(100),
		PRE_PARTITION_ID VARCHAR2(100),
		POST_PARTITION_ID VARCHAR2(100)
	);

CREATE TABLE RD4_SYSTEM (
		SYSTEM_NAME VARCHAR2(100) NOT NULL,
		URL VARCHAR2(100),
		DESCRIPTION VARCHAR2(80),
		WAR_PATH VARCHAR2(100),
		DURATION VARCHAR2(100),
		STATUS NUMBER(25)
	);

CREATE TABLE RD4_APPLICATION_STAGE (
		ID VARCHAR2(100) NOT NULL,
		NAME VARCHAR2(200),
		APPLICATION_CLASS_ID VARCHAR2(100),
		DEALER_NAME VARCHAR2(100),
		COMMENTS VARCHAR2(2000),
		DATA VARCHAR2(2000),
		FILE_ID VARCHAR2(100),
		STATUS NUMBER(25)
	);

CREATE TABLE RD4_SYSTEM_AUTHORITY (
		ID VARCHAR2(100) NOT NULL,
		ROLE_NAME VARCHAR2(100),
		SYSTEM_FUNCTION_ID VARCHAR2(100)
	);

CREATE TABLE RD4_TASK_REPORT (
		ID VARCHAR2(100) NOT NULL,
		TASK_ID VARCHAR2(100),
		REPORT VARCHAR2(2000),
		FILE_ID VARCHAR2(100)
	);

CREATE TABLE RD4_AUTHORITY (
		AUTHORITY_NAME VARCHAR2(100) NOT NULL
	);


CREATE TABLE RD4_SYSTEM_ADMIN (
		ID VARCHAR2(100),
		USER_NAME VARCHAR2(100),
		SYSTEM_NAME VARCHAR2(100)
	);

CREATE TABLE RD4_MAIN_AUTHORITY (
		ID VARCHAR2(100) NOT NULL,
		USER_NAME VARCHAR2(100) NOT NULL,
		AUTHORITY_NAME VARCHAR2(100) NOT NULL
	);

CREATE TABLE RD4_MISSION_PARTITION (
		ID VARCHAR2(100) NOT NULL,
		MISSION_ID VARCHAR2(100),
		LEADER_NAME VARCHAR2(100),
		TITLE VARCHAR2(100),
		DESCRIPTION VARCHAR2(300),
		FILE_ID VARCHAR2(100),
		PROGRESS NUMBER(25)
	);

CREATE TABLE RD4_APPLICATION_RESULT (
		ID VARCHAR2(100) NOT NULL,
		APPLICATION_CLASS_ID VARCHAR2(100),
		TYPE NUMBER(25),
		TITLE VARCHAR2(500),
		BODY VARCHAR2(2000),
		TIME NUMBER(25),
		FILE_ID VARCHAR2(100),
		DATA1 VARCHAR2(1000),
		DATA2 VARCHAR2(1000),
		DATA3 VARCHAR2(1000),
		STATUE NUMBER(25)
	);

CREATE TABLE RD4_USER (
		USER_NAME VARCHAR2(100) NOT NULL,
		PASSWORD VARCHAR2(100),
		ROLE_NAME VARCHAR2(100),
		EXPIRE_TIME NUMBER(25),
		REAL_NAME VARCHAR2(100),
		IDENTITY_NUMBER VARCHAR2(100),
		DESCRIPTION VARCHAR2(200),
		DEPARTMENT VARCHAR2(100)
	);


CREATE TABLE RD4_MISSION (
		ID VARCHAR2(100) NOT NULL,
		CREATOR_NAME VARCHAR2(100),
		LEADER_NAME VARCHAR2(100),
		TITLE VARCHAR2(100),
		DESCRIPTION VARCHAR2(300),
		FILE_ID VARCHAR2(100),
		PROGRESS NUMBER(25)
	);

CREATE TABLE RD4_DICTIONARY (
		DICKEY VARCHAR2(200) PRIMARY KEY NOT NULL,
		VALUE VARCHAR2(200)

	);

CREATE UNIQUE INDEX SQL131025122148530 ON RD4_UPLOAD_FILE (ID ASC);

CREATE UNIQUE INDEX SQL131025122148430 ON RD4_SYSTEM (SYSTEM_NAME ASC);

CREATE INDEX SQL131025122148800 ON RD4_BUG_REPORT (FOUNDER_NAME ASC);

CREATE UNIQUE INDEX SQL131025122148740 ON RD4_DEPENDENT_FUNCTION (ID ASC);

CREATE INDEX SQL131025122148860 ON RD4_SYSTEM_AUTHORITY (SYSTEM_FUNCTION_ID ASC);

CREATE UNIQUE INDEX SQL131106123707840 ON RD4_APPLICATION_RESULT (ID ASC);

CREATE UNIQUE INDEX SQL131025122148480 ON RD4_AUTHORITY (AUTHORITY_NAME ASC);

CREATE INDEX SQL131025122148780 ON RD4_BUG_REPORT (SYSTEM_NAME ASC);

CREATE UNIQUE INDEX SQL131025122148620 ON RD4_MESSAGE (ID ASC);

CREATE UNIQUE INDEX SQL131105152436960 ON RD4_APPLICATION (ID ASC);

CREATE INDEX SQL131025122148840 ON RD4_SYSTEM_FUNCTION (SYSTEM_NAME ASC);


CREATE UNIQUE INDEX SQL131108125627930 ON RD4_MISSION_PARTITION (ID ASC);

CREATE INDEX SQL131025122148910 ON RD4_MESSAGE (SENDER_NAME ASC);

CREATE UNIQUE INDEX SQL131108125647670 ON RD4_PARTITION_DEPENDENCY (ID ASC);

CREATE UNIQUE INDEX SQL131025122148690 ON RD4_DEPENDENT_SYSTEM (ID ASC);

CREATE UNIQUE INDEX SQL131106171836220 ON RD4_NOTIFICATION (ID ASC);


CREATE INDEX SQL131025122148820 ON RD4_TEMPORARY_AUTHORITY (SYSTEM_FUNCTION_ID ASC);

CREATE UNIQUE INDEX SQL131108125626870 ON RD4_MISSION (ID ASC);

CREATE INDEX SQL131025122149080 ON RD4_SYSTEM_ADMIN (USER_NAME ASC);

CREATE INDEX SQL131025122149180 ON RD4_TEMPORARY_AUTHORITY (USER_NAME ASC);

CREATE INDEX SQL131025122148850 ON RD4_SYSTEM_AUTHORITY (ROLE_NAME ASC);

CREATE INDEX SQL131025122148880 ON RD4_DEPENDENT_SYSTEM (SYSTEM_NAME ASC);

CREATE UNIQUE INDEX SQL131025122148450 ON RD4_ROLE (ROLE_NAME ASC);

CREATE UNIQUE INDEX SQL131108125648690 ON RD4_UNIT_DEPENDENCY (ID ASC);

CREATE INDEX SQL131025122149110 ON RD4_USER (ROLE_NAME ASC);

CREATE UNIQUE INDEX SQL131025122148750 ON RD4_BUG_REPORT (ID ASC);

CREATE INDEX SQL131025122149100 ON RD4_MAIN_AUTHORITY (AUTHORITY_NAME ASC);

CREATE INDEX SQL131025122148980 ON RD4_UPLOAD_FILE (UPLOADER_NAME ASC);

CREATE INDEX SQL131025122148920 ON RD4_MESSAGE (RECIEVER_NAME ASC);

CREATE INDEX SQL131025122149200 ON RD4_TASK (OWNER_NAME ASC);


CREATE UNIQUE INDEX SQL131025122148460 ON RD4_MAIN_AUTHORITY (ID ASC);

CREATE UNIQUE INDEX SQL131025122148720 ON RD4_SYSTEM_AUTHORITY (ID ASC);

CREATE UNIQUE INDEX SQL131025122148550 ON RD4_TEMPORARY_AUTHORITY (ID ASC);

CREATE UNIQUE INDEX SQL131025122148360 ON RD4_USER (USER_NAME ASC);

CREATE UNIQUE INDEX SQL131108125628950 ON RD4_MISSION_UNIT (ID ASC);

CREATE UNIQUE INDEX SQL131025122148710 ON RD4_SYSTEM_FUNCTION (ID ASC);

CREATE INDEX SQL131025122149170 ON RD4_MAIN_AUTHORITY (USER_NAME ASC);

CREATE INDEX SQL131025122149240 ON RD4_TASK (CREATOR_NAME ASC);

CREATE INDEX SQL131025122149090 ON RD4_SYSTEM_ADMIN (SYSTEM_NAME ASC);

CREATE UNIQUE INDEX SQL131025122148570 ON RD4_TASK (ID ASC);

CREATE UNIQUE INDEX SQL131106113620160 ON RD4_APPLICATION_STAGE (ID ASC);

CREATE UNIQUE INDEX SQL131025122148680 ON RD4_TASK_REPORT (ID ASC);

ALTER TABLE RD4_UPLOAD_FILE ADD CONSTRAINT SQL131025122148530 PRIMARY KEY (ID);

ALTER TABLE RD4_SYSTEM ADD CONSTRAINT SQL131025122148430 PRIMARY KEY (SYSTEM_NAME);

ALTER TABLE RD4_MISSION ADD CONSTRAINT SQL131108125626870 PRIMARY KEY (ID);

ALTER TABLE RD4_BUG_REPORT ADD CONSTRAINT SQL131025122148750 PRIMARY KEY (ID);

ALTER TABLE RD4_AUTHORITY ADD CONSTRAINT SQL131025122148480 PRIMARY KEY (AUTHORITY_NAME);

ALTER TABLE RD4_TEMPORARY_AUTHORITY ADD CONSTRAINT SQL131025122148550 PRIMARY KEY (ID);


ALTER TABLE RD4_SYSTEM_AUTHORITY ADD CONSTRAINT SQL131025122148720 PRIMARY KEY (ID);


ALTER TABLE RD4_ROLE ADD CONSTRAINT SQL131025122148450 PRIMARY KEY (ROLE_NAME);

ALTER TABLE RD4_APPLICATION ADD CONSTRAINT SQL131105152436960 PRIMARY KEY (ID);

ALTER TABLE RD4_APPLICATION_STAGE ADD CONSTRAINT SQL131106113620160 PRIMARY KEY (ID);

ALTER TABLE RD4_SYSTEM_FUNCTION ADD CONSTRAINT SQL131025122148710 PRIMARY KEY (ID);

ALTER TABLE RD4_DEPENDENT_SYSTEM ADD CONSTRAINT SQL131025122148690 PRIMARY KEY (ID);

ALTER TABLE RD4_NOTIFICATION ADD CONSTRAINT SQL131106171836220 PRIMARY KEY (ID);

ALTER TABLE RD4_MISSION_UNIT ADD CONSTRAINT SQL131108125628950 PRIMARY KEY (ID);

ALTER TABLE RD4_TASK ADD CONSTRAINT SQL131025122148570 PRIMARY KEY (ID);

ALTER TABLE RD4_PARTITION_DEPENDENCY ADD CONSTRAINT SQL131108125647670 PRIMARY KEY (ID);

ALTER TABLE RD4_APPLICATION_RESULT ADD CONSTRAINT SQL131106123707840 PRIMARY KEY (ID);

ALTER TABLE RD4_DEPENDENT_FUNCTION ADD CONSTRAINT SQL131025122148740 PRIMARY KEY (ID);

ALTER TABLE RD4_MAIN_AUTHORITY ADD CONSTRAINT SQL131025122148460 PRIMARY KEY (ID);

ALTER TABLE RD4_MESSAGE ADD CONSTRAINT SQL131025122148620 PRIMARY KEY (ID);

ALTER TABLE RD4_TASK_REPORT ADD CONSTRAINT SQL131025122148680 PRIMARY KEY (ID);

ALTER TABLE RD4_UNIT_DEPENDENCY ADD CONSTRAINT SQL131108125648690 PRIMARY KEY (ID);


ALTER TABLE RD4_USER ADD CONSTRAINT SQL131025122148360 PRIMARY KEY (USER_NAME);

ALTER TABLE RD4_MISSION_PARTITION ADD CONSTRAINT SQL131108125627930 PRIMARY KEY (ID);

ALTER TABLE RD4_SYSTEM_ADMIN ADD CONSTRAINT RD4_SYSTEM_ADMIN_FK_USER FOREIGN KEY (USER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;




ALTER TABLE RD4_MESSAGE ADD CONSTRAINT RD4_MESSAGE_FK_RECIEVER FOREIGN KEY (RECIEVER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;



ALTER TABLE RD4_SYSTEM_AUTHORITY ADD CONSTRAINT RD4_SYSTEM_AU_FK_SYSTEM_FU FOREIGN KEY (SYSTEM_FUNCTION_ID)
	REFERENCES RD4_SYSTEM_FUNCTION (ID) ON DELETE CASCADE;

ALTER TABLE RD4_USER ADD CONSTRAINT RD4_USER_FK_ROLE FOREIGN KEY (ROLE_NAME)
	REFERENCES RD4_ROLE (ROLE_NAME) ON DELETE SET NULL;



ALTER TABLE RD4_BUG_REPORT ADD CONSTRAINT RD4_BUG_RET_FK_USER FOREIGN KEY (FOUNDER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_TASK ADD CONSTRAINT RD4_TASK_FK_USER FOREIGN KEY (OWNER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_TEMPORARY_AUTHORITY ADD CONSTRAINT RD4_TEMPOR_AUITY_FK_USER FOREIGN KEY (USER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_SYSTEM_ADMIN ADD CONSTRAINT RD4_SEM_AIN_FK_SYSTEM FOREIGN KEY (SYSTEM_NAME)
	REFERENCES RD4_SYSTEM (SYSTEM_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_DEPENDENT_SYSTEM ADD CONSTRAINT RD4_DEPENT_YSTEM_FK_YSTEM FOREIGN KEY (SYSTEM_NAME)
	REFERENCES RD4_SYSTEM (SYSTEM_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_UPLOAD_FILE ADD CONSTRAINT RD4_UPLD_FIE_FK_USER FOREIGN KEY (UPLOADER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE SET NULL;

ALTER TABLE RD4_BUG_REPORT ADD CONSTRAINT RD4_BUG_RORT_FK_YSTEM FOREIGN KEY (SYSTEM_NAME)
	REFERENCES RD4_SYSTEM (SYSTEM_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_MAIN_AUTHORITY ADD CONSTRAINT RD4_MAIN_AUOTY_FKORITY FOREIGN KEY (AUTHORITY_NAME)
	REFERENCES RD4_AUTHORITY (AUTHORITY_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_MESSAGE ADD CONSTRAINT RD4_MESGE_FK_SENDER FOREIGN KEY (SENDER_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_TASK ADD CONSTRAINT RD4_TASK_FK_CREATOR FOREIGN KEY (CREATOR_NAME)
	REFERENCES RD4_USER (USER_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_TEMPORARY_AUTHORITY ADD CONSTRAINT RD4_TMPOR_AUORITY_FUTION FOREIGN KEY (SYSTEM_FUNCTION_ID)
	REFERENCES RD4_SYSTEM_FUNCTION (ID) ON DELETE CASCADE;

ALTER TABLE RD4_SYSTEM_FUNCTION ADD CONSTRAINT RD4_SYSTEMCTION_FK_SYSTEM FOREIGN KEY (SYSTEM_NAME)
	REFERENCES RD4_SYSTEM (SYSTEM_NAME) ON DELETE CASCADE;

ALTER TABLE RD4_MAIN_AUTHORITY ADD CONSTRAINT RD4_MAIN__FK_USER FOREIGN KEY (USER_NAME)
	REFERENCES RD4_USER (USER_NAME)
	ON DELETE CASCADE;

ALTER TABLE RD4_SYSTEM_AUTHORITY ADD CONSTRAINT RD4_SYSTEM_Y_FK_ROLE FOREIGN KEY (ROLE_NAME)
	REFERENCES RD4_ROLE (ROLE_NAME) ON DELETE CASCADE;
ALTER TABLE RD4_PARTITION_DEPENDENCY ADD CONSTRAINT RD4_PARTI_DEPEN_PRE FOREIGN KEY (PRE_PARTITION_ID)
	REFERENCES RD4_MISSION_PARTITION (ID) ON DELETE CASCADE;
ALTER TABLE RD4_PARTITION_DEPENDENCY ADD CONSTRAINT RD4_PARTI_DEPEN_POST FOREIGN KEY (POST_PARTITION_ID)
	REFERENCES RD4_MISSION_PARTITION (ID) ON DELETE CASCADE;

	ALTER TABLE RD4_UNIT_DEPENDENCY ADD CONSTRAINT RD4_UNIT_DEPEN_POST FOREIGN KEY (PRE_UNIT_ID)
	REFERENCES RD4_MISSION_UNIT (ID) ON DELETE CASCADE;
	ALTER TABLE RD4_UNIT_DEPENDENCY ADD CONSTRAINT RD4_UNIT_DEPEN_RP FOREIGN KEY (POST_UNIT_ID)
	REFERENCES RD4_MISSION_UNIT (ID) ON DELETE CASCADE;