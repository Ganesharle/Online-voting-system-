create database Voting;
create table voters( voter_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL);
create table candidates( candidate_id SERIAL PRIMARY KEY,name VARCHAR(100) NOT NULL);
create table votes( vote_id SERIAL PRIMARY KEY,voter_id VARCHAR(50) REFERENCES voters(voter_id),
candidate_id INT REFERENCES candidates(candidate_id));
SELECT datname FROM pg_database;
SELECT * FROM candidates;
insert into candidates(name) values('Ganesh'),('Sai'),('Vikram');
SELECT * FROM candidates;
select * from votes;