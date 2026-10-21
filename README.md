# ECOSTACK - Full-Stack Code Auditing Platform

An enterprise-grade code auditing application built with Angular, Spring Boot, and MySQL.

## Project Structure
* fullstack/ - Spring Boot REST API backend (Java 17, Maven, MySQL)
* eco-frontend/ - Angular Single Page Application frontend

## Key Features
* Automated AST code analysis & security report generation
* Real-time metrics calculation and summary analytics
* Relational database persistence using MySQL (rlnt_fullstack_db)

## How to Run Locally

### 1. Database Setup
Execute the MySQL schema script in your MySQL client:
```sql
CREATE DATABASE IF NOT EXISTS rlnt_fullstack_db;
USE rlnt_fullstack_db;
