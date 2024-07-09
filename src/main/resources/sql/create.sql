CREATE DATABASE gestcursos;
USE gestcursos;
CREATE TABLE course (
                            Id int NOT NULL AUTO_INCREMENT,
                            Name varchar(60) DEFAULT NULL,
                            Semester float DEFAULT NULL,
                            PRIMARY KEY (Id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE student (
                        Id int NOT NULL AUTO_INCREMENT,
                        Name varchar(60) NOT NULL,
                        Email varchar(100) NOT NULL,
                        BirthDate date NOT NULL,
                        JoinDate date NOT NULL,
                        Cpf varchar(60) NOT NULL,
                        CourseId int NOT NULL,
                        PRIMARY KEY (Id),
                        KEY CourseId (CourseId),
                        CONSTRAINT student_ibfk_1 FOREIGN KEY (CourseId) REFERENCES course (Id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;